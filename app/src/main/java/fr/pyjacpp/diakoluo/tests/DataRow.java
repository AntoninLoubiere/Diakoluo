package fr.pyjacpp.diakoluo.tests;

import java.util.HashMap;

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
}
