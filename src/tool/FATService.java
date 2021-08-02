package tool;


import java.awt.Component;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

import object.*;

/**
 * 文件分配表服务处理
 */

public class FATService {

    private static FAT[] myFAT; //定义一个文件分配表数组
    private static HasOpenMore hasOpenMore; //已打开文件表

    public FATService() {
        hasOpenMore = new HasOpenMore();
    }

    public void addOpenFile(FAT fat, int flag) {  //添加已打开文件
        HasOpen hasOpen = new HasOpen();
        hasOpen.setFile((File) fat.getObject());
        hasOpen.setFlag(flag);
        hasOpenMore.addFile(hasOpen);
    }

    public void removeOpenFile(FAT fat) { //删除已打开文件
        for (int i = 0; i < hasOpenMore.getFiles().size(); i++) {
            if (hasOpenMore.getFiles().get(i).getFile() == fat.getObject()) {
                hasOpenMore.getFiles().remove(i);
            }
        }
    }

    public boolean checkOpenFile(FAT fat) { //检查已打开的文件
        for (int i = 0; i < hasOpenMore.getFiles().size(); i++) {
            if (hasOpenMore.getFiles().get(i).getFile() == fat.getObject()) {
                return true;
            }
        }
        return false;
    }

    public void initFAT() {  //新建文件分配表
        myFAT = new FAT[128];   //128
        myFAT[0] = new FAT(FileSystem.END, FileSystem.DISK, null);  //myFAT[0]已被占用
        myFAT[1] = new FAT(FileSystem.END, FileSystem.DISK, new Disk("C"));  //myFAT[1]已被占用
    }

    /**
     * 新建文件夹
     */
    public int createFolder(String path) {
        String folderName;
        boolean canName;
        int index = 1; //
        //得到新建文件夹名字
        do {
            folderName = "新建文件夹";
            canName = true;
            folderName += index; //新建文件夹命名使用数字
            for (FAT fat : myFAT) {   //从0开始搜索
                if (fat != null) {   //若文件分配表不为空
                    if (fat.getType() == FileSystem.FOLDER) {  //若文件分配表的类型为文件夹
                        Folder folder = (Folder) fat.getObject();
                        if (path.equals(folder.getLocation())) { //????
                            if (folderName.equals(folder.getFolderName())) {  //如果存在同名目录
                                canName = false;
                            }
                        }
                    }
                }
            }
            index++;
        } while (!canName);
        //在myFAT中添加文件夹
        int index2 = searchEmptyFromMyFAT();   //找到第一个为空的fat下标 index2为该文件夹的起始盘块号
        if (index2 == FileSystem.ERROR) {  //如果找不到
            return FileSystem.ERROR;  //返回错误提示
        } else {
            Folder folder = new Folder(folderName, path, index2);  //新建文件夹
            myFAT[index2] = new FAT(FileSystem.END, FileSystem.FOLDER, folder); //255，文件夹，folder对象
        }
        return index2;
    }

    /**
     * 新建文件
     */
    public int createFile(String path) {
        String fileName;
        boolean canName;
        int index = 1;
        //得到新建文件名字
        do {
            fileName = "新建文件";
            canName = true;
            fileName += index;
            for (FAT fat : myFAT) {
                if (fat != null) {
                    if (fat.getType() == FileSystem.FILE) {
                        File file = (File) fat.getObject();
                        if (path.equals(file.getLocation())) {
                            if (fileName.equals(file.getFileName())) {
                                canName = false;
                            }
                        }
                    }
                }
            }
            index++;
        } while (!canName);
        //在myFAT中添加文件
        int index2 = searchEmptyFromMyFAT();
        if (index2 == FileSystem.ERROR) {
            return FileSystem.ERROR;
        } else {
            File file = new File(fileName, path, index2);
            myFAT[index2] = new FAT(FileSystem.END, FileSystem.FILE, file);
        }
        return index2;
    }

    /**
     * 得到myFAT中第一个为空的磁盘块索引
     */
    public int searchEmptyFromMyFAT() {
        for (int i = 2; i < myFAT.length; i++) { //从第二块磁盘开始搜索
            if (myFAT[i] == null) {
                return i;
            }
        }
        return FileSystem.ERROR;
    }

    /**
     * 得到空的磁盘块数量
     */
    public int getSpaceOfFAT() {
        int n = 0;
        for (int i = 2; i < myFAT.length; i++) {
            if (myFAT[i] == null) {
                n++;
            }
        }
        return n;
    }

    /**
     * 添加数据时，磁盘需分配
     */
    public boolean saveToModifyFATS2(Component parent, int num, FAT fat) {
        //从哪片磁盘开始
        int begin = ((File) fat.getObject()).getDiskNum();   //找到起始盘块号
        int index = myFAT[begin].getIndex(); //获得下一个盘块号的位置
        int oldNum = 1; //统计旧的文件所占用的盘块数量
        while (index != FileSystem.END) {  //当下标不为255，说明还未结束
            oldNum++;
            if (myFAT[index].getIndex() == FileSystem.END) { //如果下一个磁盘为255，说明已经结束
                begin = index; //
            }
            index = myFAT[index].getIndex();
        }

        //
        if (num > oldNum) {
            //需要添加磁盘块
            int n = num - oldNum;
            if (this.getSpaceOfFAT() < n) {
                Message.showErrorMgs(parent, "内容超出硬盘空闲空间");
                return false;
            }
            int space = this.searchEmptyFromMyFAT(); //得到myFAT中第一个为空的磁盘块索引
            myFAT[begin].setIndex(space);
            for (int i = 1; i <= n; i++) {
                space = this.searchEmptyFromMyFAT();//得到myFAT中第一个为空的磁盘块索引
                if (i == n) {
                    myFAT[space] = new FAT(255, FileSystem.FILE, fat.getObject());
                } else {
                    myFAT[space] = new FAT(100, FileSystem.FILE, fat.getObject());//暂时存放100号fat
                    int space2 = this.searchEmptyFromMyFAT();
                    myFAT[space].setIndex(space2);
                }
            }
        }
        return true;
    }

    /**
     * 删除数据时，磁盘的分配
     */
    public boolean saveToModifyFATS3(int num, FAT fat) {
        //从哪片磁盘开始
        int begin = ((File) fat.getObject()).getDiskNum();   //找到起始盘块号
        int index = myFAT[begin].getIndex(); //获得下一个盘块号的位置

        if (index != FileSystem.END) {
            for (int i = 1; i < num - 1; i++) {
                index = myFAT[index].getIndex();
            }
        }

        int begin1 = ((File) fat.getObject()).getDiskNum();   //找到起始盘块号
        int index3 = myFAT[begin1].getIndex(); //获得下一个盘块号的位置
        int oldNum = 1; //统计旧的文件所占用的盘块数量
        while (index3 != FileSystem.END) {  //当下标不为255，说明还未结束
            oldNum++;
            index3 = myFAT[index3].getIndex();
        }
        if (oldNum > 1) {
            int index1 = myFAT[index].getIndex();
            int index2;
            while (index1 != FileSystem.END) {
                index2 = index1;
                index1 = myFAT[index1].getIndex();
                myFAT[index2] = null;
            }
        }
        if (num > 1) {
            myFAT[index] = new FAT(255, FileSystem.FILE, fat.getObject());
        } else {
            if (oldNum > 1) {
                myFAT[index] = null;
            }
            myFAT[begin] = new FAT(255, FileSystem.FILE, fat.getObject());
        }
        return true;
    }

    /**
     * 遍历该路径下的文件夹和文件
     */
    public List<FAT> getFATs(String path) {
        List<FAT> fats = new ArrayList<>();
        for (FAT value : myFAT) {
            if (value != null) {
                if (value.getObject() instanceof Folder) {
                    if (((Folder) (value.getObject())).getLocation().equals(path)) {
                        fats.add(value);
                    }
                }
            }
        }
        for (FAT fat : myFAT) {
            if (fat != null) {
                if (fat.getObject() instanceof File) {
                    if (((File) (fat.getObject())).getLocation().equals(path)) {
                        fats.add(fat);
                    }
                }
            }
        }
        return fats;

    }


    /**
     * 修改路径
     */
    public void modifyLocation(String oldPath, String newPath) {
        for (FAT fat : myFAT) {
            if (fat != null) {
                if (fat.getType() == FileSystem.FILE) {
                    if (((File) fat.getObject()).getLocation().contains(oldPath)) {
                        ((File) fat.getObject()).setLocation(((File) fat.getObject()).getLocation().replace(oldPath, newPath));
                    }
                } else if (fat.getType() == FileSystem.FOLDER) {
                    if (((Folder) fat.getObject()).getLocation().contains(oldPath)) {
                        ((Folder) fat.getObject()).setLocation(((Folder) fat.getObject()).getLocation().replace(oldPath, newPath));
                    }
                }
            }
        }

    }

    /**
     * 删除
     */
    public void delete(JPanel jp1, FAT fat, Map<String, DefaultMutableTreeNode> map) {
        if (fat.getType() == FileSystem.FILE) {
            //判断是否文件正在打开，如果打开则不能删除
            for (int i = 0; i < hasOpenMore.getFiles().size(); i++) {
                if (hasOpenMore.getFiles().get(i).getFile().equals(fat.getObject())) {
                    Message.showErrorMgs(jp1, "文件正打开着，不能删除");
                    return;
                }
            }

            for (int i = 0; i < myFAT.length; i++) {
                if (myFAT[i] != null && myFAT[i].getType() == FileSystem.FILE) {
                    if (myFAT[i].getObject().equals(fat.getObject())) {
                        myFAT[i] = null;
                    }
                }
            }

        } else {
            String path = ((Folder) fat.getObject()).getLocation();
            String folderPath = ((Folder) fat.getObject()).getLocation() + "\\" + ((Folder) fat.getObject()).getFolderName();
            System.out.println("路径：" + folderPath);
            int index = 0;
            for (int i = 2; i < myFAT.length; i++) {
                if (myFAT[i] != null) {
                    Object obj = myFAT[i].getObject();
                    if (myFAT[i].getType() == FileSystem.FOLDER) {
                        if (((Folder) obj).getLocation().equals(folderPath)) {
                            Message.showErrorMgs(jp1, "无法进行删除操作，文件夹中含有文件");
                            return;
                        }
                    } else {
                        if (((File) obj).getLocation().equals(folderPath)) {
                            Message.showErrorMgs(jp1, "无法进行删除操作，文件夹中含有文件");
                            return;
                        }
                    }
                    if (myFAT[i].getType() == FileSystem.FOLDER) {
                        if (myFAT[i].getObject().equals(fat.getObject())) {
                            index = i;
                        }
                    }
                }
            }

            myFAT[index] = null;
            DefaultMutableTreeNode parentNode = map.get(path);
            parentNode.remove(map.get(folderPath));
            map.remove(folderPath);
        }
    }

    public static FAT[] getMyFAT() {
        return myFAT;
    }

    public static FAT getFAT(int index) {
        return myFAT[index];
    }

    public static HasOpenMore getHasOpenMore() {
        return hasOpenMore;
    }
}
