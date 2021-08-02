package gui;


import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import object.HasOpenMore;
import tool.FATService;
import tool.FileSystem;

/**
 *
 */
public class FileTable extends AbstractTableModel {

    private Vector<String> columnNames;
    private Vector<Vector<String>> rowDatas;
    private FATService fatService;

    public FileTable() {
        fatService = new FATService();
        initData();
    }

    public void initData() {
        columnNames = new Vector<>();
        columnNames.add("�ļ���");
        columnNames.add("·��");
        columnNames.add("��ʼ�̿��");
        columnNames.add("��С");
        columnNames.add("��������");


        Vector<String> vc;
        rowDatas = new Vector<>();
        HasOpenMore hasOpenMore = FATService.getHasOpenMore();
        //����Ϊ���ļ�����
        for (int i = 0; i < FileSystem.num; i++) {
            vc = new Vector<>();
            if (i < hasOpenMore.getFiles().size()) {
                vc.add(" " + hasOpenMore.getFiles().get(i).getFile().getFileName());
                vc.add(" " + hasOpenMore.getFiles().get(i).getFile().getLocation());
                vc.add(" " + hasOpenMore.getFiles().get(i).getFile().getDiskNum() + "����ʼ");
                vc.add(" " + hasOpenMore.getFiles().get(i).getFile().getSize());
                vc.add(hasOpenMore.getFiles().get(i).getFile().isReadOnly() ? " �ļ�ֻ��" : " �ļ���д");
            } else {
                vc.add("");
                vc.add("");
                vc.add("");
                vc.add("");
                vc.add("");
            }
            rowDatas.add(vc);
        }
    }

    @Override
    public int getRowCount() {
        return 5;
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames.get(column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return rowDatas.get(rowIndex).get(columnIndex);
    }

}
