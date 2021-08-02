package gui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import object.FAT;
import object.File;
import object.Folder;
import tool.FATService;
import tool.FileSystem;
import tool.Message;

/**
 *
 */
public class RenamePanel {

    private FAT fat;
    private boolean isFile = false;
    private Component com;
    String oldName = "";
    String rename = "";
    String oldPath = "";
    String path = "";
    Map<String, DefaultMutableTreeNode> map;
    private FATService fatService;

    public RenamePanel(Component c, FAT f, Map<String, DefaultMutableTreeNode> m, FATService fatService) {
        this.fatService = fatService;
        this.map = m;
        this.fat = f;
        this.com = c;
        this.init();
    }

    private void init() {
        if (fat.getType() == FileSystem.FILE) {
            isFile = true;
            File file = (File) fat.getObject();
            oldName = file.getFileName();
            oldPath = file.getLocation() + "\\" + oldName;
            path = file.getLocation();
            rename = JOptionPane.showInputDialog(com, "请输入名称", oldName);
            if (rename != null && !"".equals(rename) && !rename.equals(oldName)) {
                String path1 = ((File) fat.getObject()).getLocation() + "\\" + rename;
                if (this.checkHasName(path1, isFile)) {
                    Message.showErrorMgs(com, "已有该名字的文件了");
                    return;
                }
                ((File) fat.getObject()).setFileName(rename);
            }
        } else {
            isFile = false;
            Folder folder = (Folder) fat.getObject();
            oldName = folder.getFolderName();
            oldPath = folder.getLocation() + "\\" + oldName;
            path = folder.getLocation();
            rename = JOptionPane.showInputDialog(com, "请输入名称", oldName);
            if (rename != null && (!"".equals(rename) && !rename.equals(oldName))) {
                String path1 = ((Folder) fat.getObject()).getLocation() + "\\" + rename;
                if (this.checkHasName(path1, isFile)) {
                    Message.showErrorMgs(com, "已有该名字的文件夹了");
                    return;
                }
                ((Folder) fat.getObject()).setFolderName(rename);
            }
        }

        //更改map中的路径
        String newPath = path + "\\" + rename;
        fatService.modifyLocation(oldPath, newPath);
        Set<String> set = map.keySet();
        List<String> setStr = new ArrayList<>(set);
        for (String s : setStr) {
            if (s.contains(oldPath)) {
                DefaultMutableTreeNode n = map.get(s);
                String newPaths = s.replace(oldPath, newPath);
                map.remove(s);
                map.put(newPaths, n);
            }
        }
    }

    //解决重命名时的冲突情况
    public boolean checkHasName(String path1, boolean isFile1) {
        for (int i = 2; i < FATService.getMyFAT().length; i++) {
            if (FATService.getMyFAT()[i] != null) {
                if (isFile1) {
                    //文件
                    if (FATService.getMyFAT()[i].getType() == FileSystem.FILE) {
                        String path2 = ((File) (FATService.getMyFAT()[i].getObject())).getLocation() + "\\" + ((File) (FATService.getMyFAT()[i].getObject())).getFileName();
                        if (path2.equals(path1)) {
                            return true;
                        }
                    }
                } else {
                    //文件夹
                    if (FATService.getMyFAT()[i].getType() == FileSystem.FOLDER) {
                        String path2 = ((Folder) (FATService.getMyFAT()[i].getObject())).getLocation() + "\\" + ((Folder) (FATService.getMyFAT()[i].getObject())).getFolderName();
                        if (path2.equals(path1)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
