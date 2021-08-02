package gui;

/**
 * 打开文件后的界面
 */

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.JTextArea;

import object.*;
import tool.FATService;
import tool.*;


public class FileEditPanel extends JFrame {

    private JTextArea jta1;
    private JMenuBar jmb;
    private JMenu jm;
    private JMenuItem jmi1, jmi2;

    private FAT fat;
    private File file;
    private String oldContent;  //内容
    private int length;
    private FATService fatService;
    private FileTable ft;
    private JTable jt;
    private JFrame jf;

    private DiskTable tm;
    private JTable jta;

    private boolean canClose = true;

    public FileEditPanel(FAT fat, FATService fatService, FileTable ft, JTable jt, DiskTable tm, JTable jta) {
        this.jf = this;
        this.fat = fat;
        this.fatService = fatService;
        this.ft = ft;
        this.jt = jt;
        this.tm = tm;
        this.jta = jta;
        this.file = (File) fat.getObject();
        jta1 = new JTextArea();
        jta1.setLineWrap(true);
        jmb = new JMenuBar();
        jm = new JMenu("文件");
        jmi1 = new JMenuItem("保存");
        jmi2 = new JMenuItem("退出");
        jmb.add(jm);
        jm.add(jmi1);
        jm.add(jmi2);

        oldContent = file.getContent();

        jta1.setText(oldContent);

        init();
        menuItemAddListener();
    }

    private void init() {
        this.setResizable(false);
        this.setSize(750, 600);
        this.setTitle("编辑界面");
        this.setLocation(200, 150);
        this.add(jmb, BorderLayout.NORTH);
        this.add(jta1);
        this.addWindowListener(new WindowClosingListener());

        this.setVisible(true);
    }

    private void menuItemAddListener() {
        //保存
        jmi1.addActionListener(e -> {
            if (file.isReadOnly()) {
                Message.showErrorMgs(jf, "文件为只读");
            } else {
                save();
            }
        });

        //退出
        jmi2.addActionListener(e -> {
            jf.setVisible(false);
            fatService.removeOpenFile(fat);
            ft.initData();   //更新已打开列表的数据
            jt.updateUI();
        });
    }

    /**
     * 保存数据
     */
    private void save() { //此方法应只考虑到添加数据的情况，并没考虑删除数据的情况
        length = jta1.getText().length();
        int num = FileSystem.getNumOfFAT(length);//返回需要分配多少块磁盘
        if (length > ((File) fat.getObject()).getLength() - 8) {
            //添加内容的
            if (num > 1) {
                boolean boo = fatService.saveToModifyFATS2(this, num, fat); //判断是否需要添加磁盘块
                if (boo) {
                    file.setSize(length + "B"); //此处需验证
                    file.setLength(length);
                    file.setContent(jta1.getText());
                }
            } else {
                file.setSize(length + "B"); //此处需验证
                file.setLength(length);
                file.setContent(jta1.getText());
            }

        } else {
            //删除内容
            boolean boo = fatService.saveToModifyFATS3(num, fat); //判断是否需要添加磁盘块
            file.setSize(length + "B"); //此处需验证
            file.setLength(length);
            file.setContent(jta1.getText());
        }
        tm.initData();
        jta.updateUI();

    }

    class WindowClosingListener extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent e) {

            if (!jta1.getText().equals(file.getContent())) {
                if (!(file.isReadOnly())) {
                    int ret = Message.showConfirmMgs(jf, "是否保存修改内容?");
                    if (ret == 0) {
                        save();
                    }
                }
                fatService.removeOpenFile(fat);
                ft.initData();
                jt.updateUI();
            }
            fatService.removeOpenFile(fat);
            ft.initData();
            jt.updateUI();
        }

    }

}
