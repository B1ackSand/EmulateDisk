package gui;


import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.tree.*;

import object.Disk;
import object.FAT;
import object.File;
import object.Folder;
import tool.FATService;
import tool.FileSystem;
import tool.Message;

/**
 *
 */
public class MainPanel extends JFrame {

    /**
     * ���涨�����
     */
    private JPanel jp1, jp2, jp3, jp4;
    private JTextField jtfpath;
    private Tree jtr;
    private JTable jtadisktable, jta2;
    private JScrollPane jsp1, jsp2;
    private JMenuBar jmb;
    private JMenu newmenu;
    private JMenu helpMenu;
    private JMenuItem jmiNewFile, jmiHelp, jmiNewFolder;
    private JLabel jl1, jl2, jl3;
    private DiskTable dt;
    private int n;
    private FileTable ft;//�ļ��������

    private Map<String, DefaultMutableTreeNode> map;
    private FATService fatService;
    private List<FAT> fatList;
    private int fatIndex = 0;

    public MainPanel() {
        ft = new FileTable();
        fatList = new ArrayList<>();
        map = new HashMap<>();
        initService();
        //��ʼ��
        initMainFrame();


        //�˵����Ͳ˵�����Ŀ
        newmenu.add(jmiNewFile);
        newmenu.add(jmiNewFolder);
        jmb.add(newmenu);
        helpMenu.add(jmiHelp);
        jmb.add(helpMenu);

        //JTable �ļ������
        dt = new DiskTable();
        jtadisktable = new JTable(dt);
        jsp1 = new JScrollPane(jtadisktable);//������
        jsp1.setPreferredSize(new Dimension(300, 490));

        //JTable �ļ�����
        jta2 = new JTable(ft);
        jsp2 = new JScrollPane(jta2);//������
        jsp2.setPreferredSize(new Dimension(1000, 200));
        jsp2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jsp2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);


        //�Ϸ���·����ʾ
        jtfpath.setPreferredSize(new Dimension(960, 25));
        jtfpath.setText("C:");
        jp2.add(jl2);
        jp2.add(jtfpath);

        jp3.add(jl3);
        jp3.add(jsp2);

        jp4.add(jl1);
        jp4.add(jsp1, BorderLayout.CENTER);

        //tree
        jp2.setLayout(new FlowLayout(FlowLayout.LEFT));
        jp2.setPreferredSize(new Dimension(900, 30));
        //��ǰ���ļ�����
        jp3.setPreferredSize(new Dimension(900, 150));
        //
        jp4.setPreferredSize(new Dimension(300, 500));

        jp1.setLayout(new BorderLayout());
        jp1.add(jp2, BorderLayout.NORTH);//·��

        jp1.add(jtr, BorderLayout.WEST);//�ļ���

        jp1.add(jp3, BorderLayout.SOUTH);//��ǰ���ļ����Ա�

        jp1.add(jp4, BorderLayout.EAST);//�ļ������


        this.jmiAddListener();

        //����MainJFrame����
        this.setTitle("ģ������ļ�ϵͳ");
        this.setSize(1010, 750);
        this.setLocation(380,170);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(new BorderLayout());

        this.add(jmb, BorderLayout.NORTH);
        this.add(jp1, BorderLayout.CENTER);

        this.setVisible(true);
    }

    /**
     * �½��ļ�/�ļ��� �˵�
     */
    private void jmiAddListener() {
        jmiNewFile.addActionListener(e -> {
            int index;
            index = fatService.createFile(jtfpath.getText());
            if (index == FileSystem.ERROR) {
                Message.showErrorMgs(jp1, "��ʣ��ռ䴴���ļ�");
            } else {
                jtr.tree.updateUI();
                dt.initData();
                jtadisktable.updateUI();

                jtr.jp1.removeAll();
                jtr.addJLabel(fatService.getFATs(jtfpath.getText()), jtfpath.getText());
                jtr.jp1.updateUI();
            }
        });

        jmiNewFolder.addActionListener(e -> {
            int index;
            index = fatService.createFolder(jtfpath.getText());
            if (index == FileSystem.ERROR) {
                Message.showErrorMgs(jp1, "��ʣ��ռ䴴���ļ���");
            } else {
                FAT fat = FATService.getFAT(index);
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(fat.getObject());
                map.put(jtfpath.getText() + "\\" + ((Folder) (fat.getObject())).getFolderName(), node);
                DefaultMutableTreeNode nodeParent = map.get(jtfpath.getText());
                nodeParent.add(node);
                jtr.tree.updateUI();
                dt.initData();
                jtadisktable.updateUI();

                jtr.jp1.removeAll();
                jtr.addJLabel(fatService.getFATs(jtfpath.getText()), jtfpath.getText());
                jtr.jp1.updateUI();
            }
        });

        jmiHelp.addActionListener(e -> new Help(jp1));
    }


    /**
     * ��ʼ����̨����
     */
    private void initService() {
        fatService = new FATService();
        fatService.initFAT();
    }


    //��ʼ��ʾ���
    public void initMainFrame() {
        jp1 = new JPanel();
        jp2 = new JPanel();
        jp3 = new JPanel();
        jp4 = new JPanel();
        jp4 = new JPanel();
        jtfpath = new JTextField();
        jtfpath.setEditable(false);
        jtr = new Tree();
        jl1 = new JLabel("�ļ������");
        jl3 = new JLabel("��ǰ�ļ�����");
        jl2 = new JLabel("·��:");
        jmb = new JMenuBar();
        newmenu = new JMenu("�½�...");
        helpMenu = new JMenu("����...");

        jmiNewFile = new JMenuItem("�½��ļ�");
        jmiNewFolder = new JMenuItem("�½��ļ���");
        jmiHelp = new JMenuItem("����");


    }

    /**
     * ��
     */
    public class Tree extends JPanel {

        private JTree tree;
        private JScrollPane jsp1, jsp2;
        private JSplitPane jsp;
        private JPanel jp1;
        private IconSet[] jLabel;
        private JPopupMenu pm, pm2;
        private JMenuItem mi1, mi2, mi3, mi4, mi5, mi6;
        private DefaultMutableTreeNode node1;

        public Tree() {

            this.initMenuItem();
            this.initMenuItemByJLabel();
            this.menuItemAddListener();

            this.initTree();
            this.treeAddListener();
            this.jPanelAddListener();

            jp1.setLayout(new FlowLayout(FlowLayout.LEFT));
            jp1.setBackground(Color.white);
            jp1.add(pm);

            jsp2 = new JScrollPane(jp1);
            jsp2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            jsp2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            jsp2.setPreferredSize(new Dimension(482, 510));//�ļ�����ϵͳ
            jsp2.setBackground(Color.white);
            jsp2.setViewportView(jp1);
            jsp1.setPreferredSize(new Dimension(200, 510));//�ļ���ϵͳ
            jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jsp1, jsp2);
            jsp.setDividerSize(0);
            jsp.setDividerLocation(210);
            jsp.setEnabled(false);

            this.add(jsp);
        }

        /**
         * ��ʼ����
         */
        private void initTree() {
            node1 = new DefaultMutableTreeNode(new Disk("C"));
            map.put("C:", node1);
            jp1 = new JPanel();
            tree = new JTree(node1);//ָ�����ڵ�
            jsp1 = new JScrollPane(tree);

        }

        private void treeAddListener() {
            tree.addMouseListener(new MouseListener() {

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    //��ȡ��ǰѡ�еĽڵ� �ı��ַ��·��
                    TreePath path = tree.getSelectionPath();
                    if (path != null) {
                        String pathStr = path.toString().replace("[", "").replace("]", "").replace(",", "\\").replace(" ", "").replaceFirst("C", "C:");
                        jtfpath.setText(pathStr);
                        //����jp1
                        jp1.removeAll();
                        addJLabel(fatService.getFATs(pathStr), pathStr);
                        jp1.updateUI();
                    }
                }
            });
        }

        /**
         * �����������ļ�����
         */
        private void addJLabel(List<FAT> fats, String path) {
            fatList = fats;
            n = fats.size();
            jp1.setPreferredSize(new Dimension(482, FileSystem.getHeight(n)));
            jLabel = new IconSet[n];
            for (int i = 0; i < n; i++) {
                if (fats.get(i).getIndex() == FileSystem.END) {
                    if (fats.get(i).getType() == FileSystem.FOLDER) {
                        jLabel[i] = new IconSet(false, ((Folder) fats.get(i).getObject()).getFolderName());
                    } else {
                        jLabel[i] = new IconSet(true, ((File) fats.get(i).getObject()).getFileName());
                    }
                    jp1.add(jLabel[i]);
                    jLabel[i].add(pm2);
                    jLabel[i].addMouseListener(new MouseListener() {

                        @Override
                        public void mouseReleased(MouseEvent e) {

                        }

                        @Override
                        public void mousePressed(MouseEvent e) {
                            for (int j = 0; j < n; j++) {
                                if (e.getSource() == jLabel[j] && ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0)) {
                                    pm2.show(jLabel[j], e.getX(), e.getY());
                                }
                            }
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            for (int j = 0; j < n; j++) {
                                if (e.getSource() == jLabel[j]) {
                                    fatIndex = j;
                                    if (jLabel[j].type) {
                                        jLabel[j].setIcon(new ImageIcon(FileSystem.filePath));
                                    } else {
                                        jLabel[j].setIcon(new ImageIcon(FileSystem.folderPath));
                                    }
                                }
                            }
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            for (int j = 0; j < n; j++) {
                                if (e.getSource() == jLabel[j]) {
                                    fatIndex = j;
                                    if (jLabel[j].type) {
                                        jLabel[j].setIcon(new ImageIcon(FileSystem.fileselectPath));
                                    } else {
                                        jLabel[j].setIcon(new ImageIcon(FileSystem.folderselectPath));
                                    }
                                }
                            }
                        }

                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (e.getClickCount() == 2) {
                                if (fatList.get(fatIndex).getType() == FileSystem.FILE) {
                                    //�ļ�
                                    if (FATService.getHasOpenMore().getFiles().size() < FileSystem.num) {
                                        if (fatService.checkOpenFile(fatList.get(fatIndex))) {
                                            Message.showErrorMgs(jp1, "�ļ��Ѵ�");
                                            return;
                                        }
                                        fatService.addOpenFile(fatList.get(fatIndex), FileSystem.flagWrite);
                                        ft.initData();
                                        jta2.updateUI();
                                        new FileEditPanel(fatList.get(fatIndex), fatService, ft, jta2, dt, jtadisktable);
                                    } else {
                                        Message.showErrorMgs(jp1, "�Ѿ���5���ļ�");
                                    }

                                } else {
                                    //�ļ���
                                    Folder folder = (Folder) fatList.get(fatIndex).getObject();
                                    String path = folder.getLocation() + "\\" + folder.getFolderName();

                                    jp1.removeAll();
                                    addJLabel(fatService.getFATs(path), path);
                                    jp1.updateUI();
                                    jtfpath.setText(path);
                                }
                            }
                        }
                    });
                }
            }
        }

        /**
         * �������Ӽ�����
         */
        private void jPanelAddListener() {
            //����Ҽ�ʱ���¼�
            jp1.addMouseListener(new MouseListener() {

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        pm.show(jp1, e.getX(), e.getY());
                    }

                }
            });
        }

        /**
         * ��ʼ���Ҽ��˵�
         */
        public void initMenuItem() {
            pm = new JPopupMenu();
            mi1 = new JMenuItem("�½��ļ�");
            mi2 = new JMenuItem("�½��ļ���");
            pm.add(mi1);
            pm.add(mi2);
        }

        public void initMenuItemByJLabel() {
            pm2 = new JPopupMenu();
            mi3 = new JMenuItem("��");
            mi4 = new JMenuItem("������");
            mi5 = new JMenuItem("ɾ��");
            mi6 = new JMenuItem("����");
            pm2.add(mi3);
            pm2.add(mi4);
            pm2.add(mi5);
            pm2.add(mi6);
        }

        /**
         * ����Ҽ�ѡ����Ӽ�����
         */
        public void menuItemAddListener() {
            mi1.addActionListener(e -> {
                int index = fatService.createFile(jtfpath.getText());
                if (index == FileSystem.ERROR) {
                    Message.showErrorMgs(jp1, "��ʣ��ռ䴴���ļ�");
                } else {
                    tree.updateUI();
                    dt.initData();
                    jtadisktable.updateUI();

                    jp1.removeAll();
                    addJLabel(fatService.getFATs(jtfpath.getText()), jtfpath.getText());
                    jp1.updateUI();
                }
            });
            mi2.addActionListener(e -> {
                int index = fatService.createFolder(jtfpath.getText());
                if (index == FileSystem.ERROR) {
                    Message.showErrorMgs(jp1, "��ʣ��ռ䴴���ļ���");
                } else {
                    FAT fat = FATService.getFAT(index);
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(fat.getObject());
                    map.put(jtfpath.getText() + "\\" + ((Folder) (fat.getObject())).getFolderName(), node);
                    DefaultMutableTreeNode nodeParent = map.get(jtfpath.getText());
                    nodeParent.add(node);
                    tree.updateUI();
                    dt.initData();
                    jtadisktable.updateUI();

                    jp1.removeAll();
                    addJLabel(fatService.getFATs(jtfpath.getText()), jtfpath.getText());
                    jp1.updateUI();
                }
            });
            //��
            mi3.addActionListener(e -> {
                if (fatList.get(fatIndex).getType() == FileSystem.FILE) {
                    //�ļ�
                    if (FATService.getHasOpenMore().getFiles().size() < FileSystem.num) {
                        if (fatService.checkOpenFile(fatList.get(fatIndex))) {
                            Message.showErrorMgs(jp1, "�ļ��Ѵ�");
                            return;
                        }
                        fatService.addOpenFile(fatList.get(fatIndex), FileSystem.flagWrite);
                        ft.initData();
                        jta2.updateUI();
                        new FileEditPanel(fatList.get(fatIndex), fatService, ft, jta2, dt, jtadisktable);
                    } else {
                        Message.showErrorMgs(jp1, "�Ѿ���5���ļ��ˣ��ﵽ����");
                    }

                } else {
                    //�ļ���
                    Folder folder = (Folder) fatList.get(fatIndex).getObject();
                    String path = folder.getLocation() + "\\" + folder.getFolderName();

                    jp1.removeAll();
                    addJLabel(fatService.getFATs(path), path);
                    jp1.updateUI();
                    jtfpath.setText(path);
                }
            });
            mi4.addActionListener(e -> {
                new RenamePanel(jp1, fatList.get(fatIndex), map, fatService);
                tree.updateUI();
                dt.initData();
                jtadisktable.updateUI();

                jp1.removeAll();
                addJLabel(fatService.getFATs(jtfpath.getText()), jtfpath.getText());
                jp1.updateUI();
            });
            mi5.addActionListener(e -> {
                int i = Message.showConfirmMgs(jp1, "�Ƿ�ȷʵ�Ѵ��ļ�ɾ����");
                if (i == 0) {
                    fatService.delete(jp1, fatList.get(fatIndex), map);

                    tree.updateUI();
                    dt.initData();
                    jtadisktable.updateUI();

                    jp1.removeAll();
                    addJLabel(fatService.getFATs(jtfpath.getText()), jtfpath.getText());
                    jp1.updateUI();
                }
            });
            mi6.addActionListener(e -> {
                System.out.println("����");
                new PropPanel(jp1, fatList.get(fatIndex));
            });
        }

    }
}
