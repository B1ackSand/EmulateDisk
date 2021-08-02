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
        columnNames.add("文件名");
        columnNames.add("路径");
        columnNames.add("起始盘块号");
        columnNames.add("大小");
        columnNames.add("操作类型");


        Vector<String> vc;
        rowDatas = new Vector<>();
        HasOpenMore hasOpenMore = FATService.getHasOpenMore();
        //下面为打开文件属性
        for (int i = 0; i < FileSystem.num; i++) {
            vc = new Vector<>();
            if (i < hasOpenMore.getFiles().size()) {
                vc.add(" " + hasOpenMore.getFiles().get(i).getFile().getFileName());
                vc.add(" " + hasOpenMore.getFiles().get(i).getFile().getLocation());
                vc.add(" " + hasOpenMore.getFiles().get(i).getFile().getDiskNum() + "号起始");
                vc.add(" " + hasOpenMore.getFiles().get(i).getFile().getSize());
                vc.add(hasOpenMore.getFiles().get(i).getFile().isReadOnly() ? " 文件只读" : " 文件可写");
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
