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
