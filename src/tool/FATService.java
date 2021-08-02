package tool;


import java.awt.Component;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

import object.*;

/**
 * �ļ�����������
 */

public class FATService {

    private static FAT[] myFAT; //����һ���ļ����������
    private static HasOpenMore hasOpenMore; //�Ѵ��ļ���

    public FATService() {
        hasOpenMore = new HasOpenMore();
    }

    public void addOpenFile(FAT fat, int flag) {  //����Ѵ��ļ�
        HasOpen hasOpen = new HasOpen();
        hasOpen.setFile((File) fat.getObject());
        hasOpen.setFlag(flag);
        hasOpenMore.addFile(hasOpen);
    }

    public void removeOpenFile(FAT fat) { //ɾ���Ѵ��ļ�
        for (int i = 0; i < hasOpenMore.getFiles().size(); i++) {
            if (hasOpenMore.getFiles().get(i).getFile() == fat.getObject()) {
                hasOpenMore.getFiles().remove(i);
            }
        }
    }

    public boolean checkOpenFile(FAT fat) { //����Ѵ򿪵��ļ�
        for (int i = 0; i < hasOpenMore.getFiles().size(); i++) {
            if (hasOpenMore.getFiles().get(i).getFile() == fat.getObject()) {
                return true;
            }
        }
        return false;
    }

    public void initFAT() {  //�½��ļ������
        myFAT = new FAT[128];   //128
        myFAT[0] = new FAT(FileSystem.END, FileSystem.DISK, null);  //myFAT[0]�ѱ�ռ��
        myFAT[1] = new FAT(FileSystem.END, FileSystem.DISK, new Disk("C"));  //myFAT[1]�ѱ�ռ��
    }

    /**
     * �½��ļ���
     */
    public int createFolder(String path) {
        String folderName;
        boolean canName;
        int index = 1; //
        //�õ��½��ļ�������
        do {
            folderName = "�½��ļ���";
            canName = true;
            folderName += index; //�½��ļ�������ʹ������
            for (FAT fat : myFAT) {   //��0��ʼ����
                if (fat != null) {   //���ļ������Ϊ��
                    if (fat.getType() == FileSystem.FOLDER) {  //���ļ�����������Ϊ�ļ���
                        Folder folder = (Folder) fat.getObject();
                        if (path.equals(folder.getLocation())) { //????
                            if (folderName.equals(folder.getFolderName())) {  //�������ͬ��Ŀ¼
                                canName = false;
                            }
                        }
                    }
                }
            }
            index++;
        } while (!canName);
        //��myFAT������ļ���
        int index2 = searchEmptyFromMyFAT();   //�ҵ���һ��Ϊ�յ�fat�±� index2Ϊ���ļ��е���ʼ�̿��
        if (index2 == FileSystem.ERROR) {  //����Ҳ���
            return FileSystem.ERROR;  //���ش�����ʾ
        } else {
            Folder folder = new Folder(folderName, path, index2);  //�½��ļ���
            myFAT[index2] = new FAT(FileSystem.END, FileSystem.FOLDER, folder); //255���ļ��У�folder����
        }
        return index2;
    }

    /**
     * �½��ļ�
     */
    public int createFile(String path) {
        String fileName;
        boolean canName;
        int index = 1;
        //�õ��½��ļ�����
        do {
            fileName = "�½��ļ�";
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
        //��myFAT������ļ�
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
     * �õ�myFAT�е�һ��Ϊ�յĴ��̿�����
     */
    public int searchEmptyFromMyFAT() {
        for (int i = 2; i < myFAT.length; i++) { //�ӵڶ�����̿�ʼ����
            if (myFAT[i] == null) {
                return i;
            }
        }
        return FileSystem.ERROR;
    }

    /**
     * �õ��յĴ��̿�����
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
     * �������ʱ�����������
     */
    public boolean saveToModifyFATS2(Component parent, int num, FAT fat) {
        //����Ƭ���̿�ʼ
        int begin = ((File) fat.getObject()).getDiskNum();   //�ҵ���ʼ�̿��
        int index = myFAT[begin].getIndex(); //�����һ���̿�ŵ�λ��
        int oldNum = 1; //ͳ�ƾɵ��ļ���ռ�õ��̿�����
        while (index != FileSystem.END) {  //���±겻Ϊ255��˵����δ����
            oldNum++;
            if (myFAT[index].getIndex() == FileSystem.END) { //�����һ������Ϊ255��˵���Ѿ�����
                begin = index; //
            }
            index = myFAT[index].getIndex();
        }

        //
        if (num > oldNum) {
            //��Ҫ��Ӵ��̿�
            int n = num - oldNum;
            if (this.getSpaceOfFAT() < n) {
                Message.showErrorMgs(parent, "���ݳ���Ӳ�̿��пռ�");
                return false;
            }
            int space = this.searchEmptyFromMyFAT(); //�õ�myFAT�е�һ��Ϊ�յĴ��̿�����
            myFAT[begin].setIndex(space);
            for (int i = 1; i <= n; i++) {
                space = this.searchEmptyFromMyFAT();//�õ�myFAT�е�һ��Ϊ�յĴ��̿�����
                if (i == n) {
                    myFAT[space] = new FAT(255, FileSystem.FILE, fat.getObject());
                } else {
                    myFAT[space] = new FAT(100, FileSystem.FILE, fat.getObject());//��ʱ���100��fat
                    int space2 = this.searchEmptyFromMyFAT();
                    myFAT[space].setIndex(space2);
                }
            }
        }
        return true;
    }

    /**
     * ɾ������ʱ�����̵ķ���
     */
    public boolean saveToModifyFATS3(int num, FAT fat) {
        //����Ƭ���̿�ʼ
        int begin = ((File) fat.getObject()).getDiskNum();   //�ҵ���ʼ�̿��
        int index = myFAT[begin].getIndex(); //�����һ���̿�ŵ�λ��

        if (index != FileSystem.END) {
            for (int i = 1; i < num - 1; i++) {
                index = myFAT[index].getIndex();
            }
        }

        int begin1 = ((File) fat.getObject()).getDiskNum();   //�ҵ���ʼ�̿��
        int index3 = myFAT[begin1].getIndex(); //�����һ���̿�ŵ�λ��
        int oldNum = 1; //ͳ�ƾɵ��ļ���ռ�õ��̿�����
        while (index3 != FileSystem.END) {  //���±겻Ϊ255��˵����δ����
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
     * ������·���µ��ļ��к��ļ�
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
     * �޸�·��
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
     * ɾ��
     */
    public void delete(JPanel jp1, FAT fat, Map<String, DefaultMutableTreeNode> map) {
        if (fat.getType() == FileSystem.FILE) {
            //�ж��Ƿ��ļ����ڴ򿪣����������ɾ��
            for (int i = 0; i < hasOpenMore.getFiles().size(); i++) {
                if (hasOpenMore.getFiles().get(i).getFile().equals(fat.getObject())) {
                    Message.showErrorMgs(jp1, "�ļ������ţ�����ɾ��");
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
            System.out.println("·����" + folderPath);
            int index = 0;
            for (int i = 2; i < myFAT.length; i++) {
                if (myFAT[i] != null) {
                    Object obj = myFAT[i].getObject();
                    if (myFAT[i].getType() == FileSystem.FOLDER) {
                        if (((Folder) obj).getLocation().equals(folderPath)) {
                            Message.showErrorMgs(jp1, "�޷�����ɾ���������ļ����к����ļ�");
                            return;
                        }
                    } else {
                        if (((File) obj).getLocation().equals(folderPath)) {
                            Message.showErrorMgs(jp1, "�޷�����ɾ���������ļ����к����ļ�");
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
