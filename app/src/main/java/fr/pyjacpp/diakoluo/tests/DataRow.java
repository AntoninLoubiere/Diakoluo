package fr.pyjacpp.diakoluo.tests;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import fr.pyjacpp.diakoluo.tests.column.Column;
import fr.pyjacpp.diakoluo.tests.data.DataCell;

public class DataRow {
    private HashMap<Column, DataCell> listCells;
    private boolean selected;

    public DataRow() {
        selected = true;
        listCells = new HashMap<>();
    }

    public DataRow(boolean selected) {
        this.selected = selected;
    }

    public HashMap<Column, DataCell> getListCells() {
        return listCells;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void writeXml(OutputStream fileOutputStream, Test test) throws IOException {
        for (Column column : test.getListColumn()) {
            DataCell dataCell = listCells.get(column);
            if (dataCell != null)
                dataCell.writeXml(fileOutputStream);
        }
    }
}
