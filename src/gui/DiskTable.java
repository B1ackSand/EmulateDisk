package gui;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import object.FAT;
import tool.FATService;

/**
 * ¥≈≈Ã∑÷Œˆ
 *
 */
public class DiskTable extends AbstractTableModel {

    private Vector<String> columnNames;
    private Vector<Vector<Integer>> rowData;
    private FATService fatService;
    private int index = 0;

    public DiskTable() {
        fatService = new FATService();
        initData();
    }

    public void initData() {
        columnNames = new Vector<>();
        columnNames.add("¥≈≈ÃøÈ∫≈");
        columnNames.add("ƒ⁄»›");
        rowData = new Vector<>();
        Vector<Integer> vs;
        FAT[] list = FATService.getMyFAT();
        for (int i = 0; i < 128; i++) {
            vs = new Vector<>();
            if (list[i] != null) {
                vs.add(i);
                vs.add(list[i].getIndex());
            } else {
                vs.add(i);
                vs.add(0);
            }
            rowData.add(vs);
        }

    }

    @Override
    public int getRowCount() {
        return 128;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames.get(column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return rowData.get(rowIndex).get(columnIndex);
    }

}
