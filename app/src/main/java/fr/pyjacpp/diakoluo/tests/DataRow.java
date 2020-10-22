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

import android.util.Log;

import androidx.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import fr.pyjacpp.diakoluo.save_test.CsvSaver;
import fr.pyjacpp.diakoluo.save_test.FileManager;
import fr.pyjacpp.diakoluo.save_test.XmlLoader;
import fr.pyjacpp.diakoluo.save_test.XmlSaver;
import fr.pyjacpp.diakoluo.tests.column.Column;
import fr.pyjacpp.diakoluo.tests.data.DataCell;

import static fr.pyjacpp.diakoluo.save_test.XmlLoader.TAG;

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
     *
     * @param dataRow            the dataRow to copy
     * @param newListColumn      the new list of columns
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

    public static DataRow readXmlRow(XmlPullParser parser, ArrayList<Column> columns)
            throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, FileManager.TAG_ROW);

        DataRow dataRow = new DataRow();
        int indexColumn = 0;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                // continue until it is a start tag
                continue;
            }

            if (FileManager.TAG_CELL.equals(parser.getName())) {
                if (indexColumn >= columns.size()) {
                    Log.w(TAG, "Too many cells !");
                    continue;
                }
                Column currentColumn = columns.get(indexColumn);
                DataCell cell = DataCell.readCell(parser, currentColumn.getInputType());
                dataRow.getListCells().put(currentColumn, cell);

                indexColumn++;

            } else {
                XmlLoader.skip(parser);
            }
        }

        if (indexColumn < columns.size()) {
            Log.w(TAG, "Too few cells !");
            for (int i = indexColumn; i < columns.size(); i++) {
                Column currentColumn = columns.get(indexColumn);
                DataCell cell = DataCell.newCellWithDefaultValue(currentColumn);
                dataRow.getListCells().put(currentColumn, cell);
            }
        }

        return dataRow;
    }

    /**
     * Read rows from xml file.
     *
     * @return the list of rows loaded
     */
    public static ArrayList<DataRow> readXmlRows(XmlPullParser parser, ArrayList<Column> columns)
            throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, FileManager.TAG_ROWS);
        if (columns == null)
            throw new XmlPullParserException("Columns must be defined before rows");
        ArrayList<DataRow> rows = new ArrayList<>();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                // continue until it is a start tag
                continue;
            }

            if (parser.getName().equals(FileManager.TAG_ROW)) {
                rows.add(DataRow.readXmlRow(parser, columns));
            } else {
                XmlLoader.skip(parser);
            }
        }
        return rows;
    }

    /**
     * Get list of cells.
     *
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
     *
     * @param fileOutputStream the file output stream of the file
     * @param test             the test to save
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
     *
     * @param csvContext the CsvContext to write
     * @param test       the test to save
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

    /**
     * Don't use this in test !
     * To scan the the hashList, use{@link #equals(Object, ArrayList, ArrayList)}
     *
     * @param obj the obj to test
     * @return an exception
     * @see #equals(Object, ArrayList, ArrayList)
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }

    /**
     * Test if this DataRow is equal to another DataRow
     *
     * @param obj            the obj to test
     * @param objColumns     the columns of the row
     * @param dataRowColumns the columns of this DataRow
     * @return if this DataRow is equal to the obj
     */
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
