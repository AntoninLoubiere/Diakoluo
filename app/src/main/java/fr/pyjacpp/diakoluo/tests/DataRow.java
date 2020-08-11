/*
 * Copyright (c) 2020 LOUBIERE Antonin <https://www.github.com/AntoninLoubiere/>
 *
 * This file is part of Diakôluô project <https://www.github.com/AntoninLoubiere/Diakoluo/>.
 *
 *     Diakôluô is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Diakôluô is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     A copy of the license is available in the root folder of Diakôluô, under the
 *     name of LICENSE.md. You could find it also at <https://www.gnu.org/licenses/gpl-3.0.html>.
 */

package fr.pyjacpp.diakoluo.tests;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import fr.pyjacpp.diakoluo.save_test.CsvSaver;
import fr.pyjacpp.diakoluo.save_test.FileManager;
import fr.pyjacpp.diakoluo.save_test.XmlSaver;
import fr.pyjacpp.diakoluo.tests.column.Column;
import fr.pyjacpp.diakoluo.tests.data.DataCell;

public class DataRow {
    private HashMap<Column, DataCell> listCells;
    private boolean selected;

    /**
     * Default constructor.
     */
    public DataRow() {
        selected = true;
        listCells = new HashMap<>();
    }

    public DataRow(boolean selected) {
        this.selected = selected;
    }

    /**
     * Copy a data row need the new list of column and the previous list of column.
     * @param dataRow the dataRow to copy
     * @param newListColumn the new list of columns
     * @param previousListColumn the previous list of columns
     */
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

    /**
     * Get list of cells.
     * @return the list of cells
     */
    public HashMap<Column, DataCell> getListCells() {
        return listCells;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Write the data row in an xml file.
     * @param fileOutputStream the file output stream of the file
     * @param test the test to save
     * @throws IOException if an exception occur while writing the file
     */
    public void writeXml(OutputStream fileOutputStream, Test test) throws IOException {
        XmlSaver.writeStartBeacon(fileOutputStream, FileManager.TAG_ROW);

        ArrayList<Column> listColumn = test.getListColumn();
        for (int i = 0, listColumnSize = listColumn.size(); i < listColumnSize; i++) {
            Column column = listColumn.get(i);
            DataCell dataCell = listCells.get(column);
            if (dataCell != null) {
                dataCell.writeXml(fileOutputStream);
            }
        }

        XmlSaver.writeEndBeacon(fileOutputStream, FileManager.TAG_ROW);
    }

    /**
     * Write the row in a csv file.
     * @param csvContext the CsvContext to write
     * @param test the test to save
     * @throws IOException if an exception occur while writing the file
     */
    public void writeCsv(CsvSaver.CsvContext csvContext, Test test) throws IOException {
        ArrayList<Column> listColumn = test.getListColumn();
        for (int i = 0, listColumnSize = listColumn.size(); i < listColumnSize; i++) {
            Column column = listColumn.get(i);
            DataCell dataCell = listCells.get(column);
            if (dataCell == null) CsvSaver.writeCell(csvContext, i, "");
            else {
                String csvValue = dataCell.getCsvValue(column);
                CsvSaver.writeCell(csvContext, i, csvValue);
            }
        }
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        throw new RuntimeException("Equals(@Nullable Object obj, ArrayList<Column> objColumns, " +
                "ArrayList<Column> dataRowColumns) should be used instead");
    }

    public boolean equals(@Nullable Object obj, ArrayList<Column> objColumns,
                          ArrayList<Column> dataRowColumns) {
        if (obj instanceof DataRow) {
            DataRow row = (DataRow) obj;
            int size = row.listCells.keySet().size();
            if (row.selected == selected && size == listCells.keySet().size() &&
                    size == objColumns.size() && size == dataRowColumns.size()) {

                for (int i = 0; i < size; i++) {
                    Column objColumn = objColumns.get(i);
                    Column dataRowColumn = dataRowColumns.get(i);

                    DataCell dataCell1 = row.listCells.get(objColumn);
                    DataCell dataCell2 = listCells.get(dataRowColumn);
                    if (dataCell1 == null) {
                        if (dataCell2 != null) {
                            return false;
                        }
                    } else if (!dataCell1.equals(dataCell2)) {
                        return false;
                    }
                }

                return true;
            }
        }
        return false;
    }
}
