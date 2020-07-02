package fr.pyjacpp.diakoluo.tests;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import fr.pyjacpp.diakoluo.save_test.CsvSaver;
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

    public DataRow(DataRow dataRow, ArrayList<Column> newListColumn,
                   ArrayList<Column> previousListColumn) {
        // ASSERT if (newListColumn.size() != previousListColumn.size()) throw new AssertionError();
        selected = dataRow.selected;
        listCells = new HashMap<>();
        for (int i = 0; i < newListColumn.size(); i++) {
            listCells.put(newListColumn.get(i),
                    DataCell.copyDataCell(dataRow.listCells.get(previousListColumn.get(i))));
        }
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
        ArrayList<Column> listColumn = test.getListColumn();
        for (int i = 0, listColumnSize = listColumn.size(); i < listColumnSize; i++) {
            Column column = listColumn.get(i);
            DataCell dataCell = listCells.get(column);
            if (dataCell != null)
                dataCell.writeXml(fileOutputStream);
        }
    }

    public void writeCsv(CsvSaver.CsvContext csvContext, Test test) throws IOException {
        ArrayList<Column> listColumn = test.getListColumn();
        for (int i = 0, listColumnSize = listColumn.size(); i < listColumnSize; i++) {
            Column column = listColumn.get(i);
            DataCell dataCell = listCells.get(column);
            CsvSaver.writeCell(csvContext, i, dataCell == null ? "" : dataCell.getStringValue());
        }
    }
}
