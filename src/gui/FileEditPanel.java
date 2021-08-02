package gui;

/**
 * ���ļ���Ľ���
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
    private String oldContent;  //����
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
        jm = new JMenu("�ļ�");
        jmi1 = new JMenuItem("����");
        jmi2 = new JMenuItem("�˳�");
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
        this.setTitle("�༭����");
        this.setLocation(200, 150);
        this.add(jmb, BorderLayout.NORTH);
        this.add(jta1);
        this.addWindowListener(new WindowClosingListener());

        this.setVisible(true);
    }

    private void menuItemAddListener() {
        //����
        jmi1.addActionListener(e -> {
            if (file.isReadOnly()) {
                Message.showErrorMgs(jf, "�ļ�Ϊֻ��");
            } else {
                save();
            }
        });

        //�˳�
        jmi2.addActionListener(e -> {
            jf.setVisible(false);
            fatService.removeOpenFile(fat);
            ft.initData();   //�����Ѵ��б������
            jt.updateUI();
        });
    }

    /**
     * ��������
     */
    private void save() { //�˷���Ӧֻ���ǵ�������ݵ��������û����ɾ�����ݵ����
        length = jta1.getText().length();
        int num = FileSystem.getNumOfFAT(length);//������Ҫ������ٿ����
        if (length > ((File) fat.getObject()).getLength() - 8) {
            //������ݵ�
            if (num > 1) {
                boolean boo = fatService.saveToModifyFATS2(this, num, fat); //�ж��Ƿ���Ҫ��Ӵ��̿�
                if (boo) {
                    file.setSize(length + "B"); //�˴�����֤
                    file.setLength(length);
                    file.setContent(jta1.getText());
                }
            } else {
                file.setSize(length + "B"); //�˴�����֤
                file.setLength(length);
                file.setContent(jta1.getText());
            }

        } else {
            //ɾ������
            boolean boo = fatService.saveToModifyFATS3(num, fat); //�ж��Ƿ���Ҫ��Ӵ��̿�
            file.setSize(length + "B"); //�˴�����֤
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
                    int ret = Message.showConfirmMgs(jf, "�Ƿ񱣴��޸�����?");
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
