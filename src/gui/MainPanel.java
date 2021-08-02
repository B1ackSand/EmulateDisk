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
     * 界面定义变量
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
    private FileTable ft;//文件属性面板

    private Map<String, DefaultMutableTreeNode> map;
    private FATService fatService;
    private List<FAT> fatList;
    private int fatIndex = 0;

    public MainPanel() {
        ft = new FileTable();
        fatList = new ArrayList<>();
        map = new HashMap<>();
        initService();
        //初始化
        initMainFrame();


        //菜单条和菜单条项目
        newmenu.add(jmiNewFile);
        newmenu.add(jmiNewFolder);
        jmb.add(newmenu);
        helpMenu.add(jmiHelp);
        jmb.add(helpMenu);

        //JTable 文件分配表
        dt = new DiskTable();
        jtadisktable = new JTable(dt);
        jsp1 = new JScrollPane(jtadisktable);//滚轮条
        jsp1.setPreferredSize(new Dimension(300, 490));

        //JTable 文件管理
        jta2 = new JTable(ft);
        jsp2 = new JScrollPane(jta2);//滚轮条
        jsp2.setPreferredSize(new Dimension(1000, 200));
        jsp2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jsp2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);


        //上方的路径显示
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
        //当前打开文件属性
        jp3.setPreferredSize(new Dimension(900, 150));
        //
        jp4.setPreferredSize(new Dimension(300, 500));

        jp1.setLayout(new BorderLayout());
        jp1.add(jp2, BorderLayout.NORTH);//路径

        jp1.add(jtr, BorderLayout.WEST);//文件树

        jp1.add(jp3, BorderLayout.SOUTH);//当前打开文件属性表

        jp1.add(jp4, BorderLayout.EAST);//文件分配表


        this.jmiAddListener();

        //设置MainJFrame属性
        this.setTitle("模拟磁盘文件系统");
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
     * 新建文件/文件夹 菜单
     */
    private void jmiAddListener() {
        jmiNewFile.addActionListener(e -> {
            int index;
            index = fatService.createFile(jtfpath.getText());
            if (index == FileSystem.ERROR) {
                Message.showErrorMgs(jp1, "无剩余空间创建文件");
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
                Message.showErrorMgs(jp1, "无剩余空间创建文件夹");
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
     * 初始化后台数据
     */
    private void initService() {
        fatService = new FATService();
        fatService.initFAT();
    }


    //开始显示面板
    public void initMainFrame() {
        jp1 = new JPanel();
        jp2 = new JPanel();
        jp3 = new JPanel();
        jp4 = new JPanel();
        jp4 = new JPanel();
        jtfpath = new JTextField();
        jtfpath.setEditable(false);
        jtr = new Tree();
        jl1 = new JLabel("文件分配表");
        jl3 = new JLabel("当前文件属性");
        jl2 = new JLabel("路径:");
        jmb = new JMenuBar();
        newmenu = new JMenu("新建...");
        helpMenu = new JMenu("帮助...");

        jmiNewFile = new JMenuItem("新建文件");
        jmiNewFolder = new JMenuItem("新建文件夹");
        jmiHelp = new JMenuItem("帮助");


    }

    /**
     * 树
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
            jsp2.setPreferredSize(new Dimension(482, 510));//文件管理系统
            jsp2.setBackground(Color.white);
            jsp2.setViewportView(jp1);
            jsp1.setPreferredSize(new Dimension(200, 510));//文件树系统
            jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jsp1, jsp2);
            jsp.setDividerSize(0);
            jsp.setDividerLocation(210);
            jsp.setEnabled(false);

            this.add(jsp);
        }

        /**
         * 初始化树
         */
        private void initTree() {
            node1 = new DefaultMutableTreeNode(new Disk("C"));
            map.put("C:", node1);
            jp1 = new JPanel();
            tree = new JTree(node1);//指定根节点
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
                    //获取当前选中的节点 改变地址栏路径
                    TreePath path = tree.getSelectionPath();
                    if (path != null) {
                        String pathStr = path.toString().replace("[", "").replace("]", "").replace(",", "\\").replace(" ", "").replaceFirst("C", "C:");
                        jtfpath.setText(pathStr);
                        //更新jp1
                        jp1.removeAll();
                        addJLabel(fatService.getFATs(pathStr), pathStr);
                        jp1.updateUI();
                    }
                }
            });
        }

        /**
         * 在面板中添加文件可视
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
                                    //文件
                                    if (FATService.getHasOpenMore().getFiles().size() < FileSystem.num) {
                                        if (fatService.checkOpenFile(fatList.get(fatIndex))) {
                                            Message.showErrorMgs(jp1, "文件已打开");
                                            return;
                                        }
                                        fatService.addOpenFile(fatList.get(fatIndex), FileSystem.flagWrite);
                                        ft.initData();
                                        jta2.updateUI();
                                        new FileEditPanel(fatList.get(fatIndex), fatService, ft, jta2, dt, jtadisktable);
                                    } else {
                                        Message.showErrorMgs(jp1, "已经打开5个文件");
                                    }

                                } else {
                                    //文件夹
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
         * 面板中添加监听器
         */
        private void jPanelAddListener() {
            //点击右键时的事件
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
         * 初始化右键菜单
         */
        public void initMenuItem() {
            pm = new JPopupMenu();
            mi1 = new JMenuItem("新建文件");
            mi2 = new JMenuItem("新建文件夹");
            pm.add(mi1);
            pm.add(mi2);
        }

        public void initMenuItemByJLabel() {
            pm2 = new JPopupMenu();
            mi3 = new JMenuItem("打开");
            mi4 = new JMenuItem("重命名");
            mi5 = new JMenuItem("删除");
            mi6 = new JMenuItem("属性");
            pm2.add(mi3);
            pm2.add(mi4);
            pm2.add(mi5);
            pm2.add(mi6);
        }

        /**
         * 点击右键选项添加监听器
         */
        public void menuItemAddListener() {
            mi1.addActionListener(e -> {
                int index = fatService.createFile(jtfpath.getText());
                if (index == FileSystem.ERROR) {
                    Message.showErrorMgs(jp1, "无剩余空间创建文件");
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
                    Message.showErrorMgs(jp1, "无剩余空间创建文件夹");
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
            //打开
            mi3.addActionListener(e -> {
                if (fatList.get(fatIndex).getType() == FileSystem.FILE) {
                    //文件
                    if (FATService.getHasOpenMore().getFiles().size() < FileSystem.num) {
                        if (fatService.checkOpenFile(fatList.get(fatIndex))) {
                            Message.showErrorMgs(jp1, "文件已打开");
                            return;
                        }
                        fatService.addOpenFile(fatList.get(fatIndex), FileSystem.flagWrite);
                        ft.initData();
                        jta2.updateUI();
                        new FileEditPanel(fatList.get(fatIndex), fatService, ft, jta2, dt, jtadisktable);
                    } else {
                        Message.showErrorMgs(jp1, "已经打开5个文件了，达到上限");
                    }

                } else {
                    //文件夹
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
                int i = Message.showConfirmMgs(jp1, "是否确实把此文件删除？");
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
                System.out.println("属性");
                new PropPanel(jp1, fatList.get(fatIndex));
            });
        }

    }
}
