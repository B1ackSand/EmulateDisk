package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import object.Folder;
import object.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import object.FAT;
import tool.FileSystem;

/**
 *
 */
public class PropPanel extends JDialog {

    private JPanel jp1, jp2;
    private JLabel jl1, jl2, jl3, jl4, jl5, jl6;
    private FAT fat;
    private JButton jb1, jb2;
    private JRadioButton jrb1;
    private boolean isFile = false;
    private JDialog jd1;

    public PropPanel(Component c, FAT f) {
        jd1 = this;
        this.fat = f;
        this.init();
        this.setTitle("����");
        this.setSize(280, 330);
        this.setLayout(new GridLayout(7, 1));
        this.setModal(true);
        this.setLocationRelativeTo(c);
        this.setVisible(true);
    }

    private void init() {
        if (fat.getType() == FileSystem.FOLDER) {
            Folder folder = (Folder) (fat.getObject());
            jl1 = new JLabel(" ���� :       " + folder.getFolderName());
            jl2 = new JLabel(" ���� :       " + folder.getType());
            jl3 = new JLabel(" ·�� :       " + folder.getLocation());
            jl5 = new JLabel(" ��������: " + folder.getCreateTime());
            jp2 = new JPanel();


        } else if (fat.getType() == FileSystem.FILE) {
            isFile = true;
            File file = (File) (fat.getObject());
            jl1 = new JLabel(" ���� :    " + file.getFileName());
            jl2 = new JLabel(" ���� :    " + file.getType());
            jl3 = new JLabel(" ·�� :    " + file.getLocation());
            jl4 = new JLabel(" ��С:  " + file.getSize());
            jl4.setPreferredSize(new Dimension(230, 40));
            jl5 = new JLabel(" �������ڣ�" + file.getCreateTime());
            jp2 = new JPanel();
            jl6 = new JLabel(" ����");
            jrb1 = new JRadioButton(" ֻ��");
            jp2.add(jl6);
            jp2.add(jrb1);
            if (file.isReadOnly()) {
                jrb1.setSelected(true);
            }
        }

        jl1.setPreferredSize(new Dimension(230, 40));
        jl2.setPreferredSize(new Dimension(230, 40));
        jl3.setPreferredSize(new Dimension(230, 40));
        jl5.setPreferredSize(new Dimension(230, 40));
        this.add(jl1);
        this.add(jl2);
        this.add(jl3);
        if (fat.getType() == FileSystem.FILE) {
            this.add(jl4);
        }
        this.add(jl5);

        this.add(jp2);

        jp1 = new JPanel();
        jb1 = new JButton("ȷ��");
        jb2 = new JButton("ȡ��");
        jp1.add(jb1);
        jp1.add(jb2);
        this.add(jp1);

        jb1.addActionListener(e -> {
            if (jrb1.isSelected()) {
                if (isFile) {
                    ((File) (fat.getObject())).setReadOnly(true);
                }
            } else {
                if (isFile) {
                    ((File) (fat.getObject())).setReadOnly(false);
                }
            }
            jd1.setVisible(false);
        });


        jb2.addActionListener(e -> jd1.setVisible(false));

    }

}
