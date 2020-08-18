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

package fr.pyjacpp.diakoluo.tests.data;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.OutputStream;

import fr.pyjacpp.diakoluo.save_test.FileManager;
import fr.pyjacpp.diakoluo.save_test.XmlSaver;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;
import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.column.Column;

/**
 * Cell that contain a value of the test.
 * Can have different type (String, list, int)...
 * @see Column
 */
public abstract class DataCell {
    /**
     * Create a new instance from a DataCell.
     * @param dataCell the dataCell to copy
     */
    protected DataCell(DataCell dataCell) {
    }

    /**
     * Load a DataCell form xml.
     * @param parser The xml parser
     */
    protected DataCell(XmlPullParser parser) {
    }


    /**
     * Default initializer.
     */
    protected DataCell() {
    }

    /**
     * Copy a DataCell.
     * @param dataCell the DataCell to copy
     * @return the DataCell copied
     */
    public static DataCell copyDataCell(DataCell dataCell) {
        if (dataCell == null) {
            return null;
        } else {
            if (dataCell instanceof DataCellString) {
                return new DataCellString((DataCellString) dataCell);
            } else if (dataCell instanceof DataCellList) {
                return new DataCellList((DataCellList) dataCell);
            } else {
                throw new IllegalStateException("Unexpected value: " + dataCell.getClass());
            }
        }
    }

    /**
     * Create a new cell with default value of the parent column.
     * The DataCell returned is valid
     * @param currentColumn the column attached with the DataCell
     * @return the DataCell created (is valid)
     */
    public static DataCell newCellWithDefaultValue(Column currentColumn) {
        switch (currentColumn.getInputType()) {
            case String:
                return new DataCellString((String) currentColumn.getDefaultValue());

            case List:
                return new DataCellList((int) currentColumn.getDefaultValue());

            default:
                throw new IllegalStateException("Unexpected value: " +
                        currentColumn.getInputType());
        }
    }

    /**
     * Create a new cell from another cell (different type).
     * @param newColumn the new column where the cell will be attached
     * @param previousColumn the previous cell where the cell was attached
     * @param previousDataCell the previous data cell
     * @return the new DataCell
     */
    public static DataCell newCellMigrate(Column newColumn, Column previousColumn,
                                          @NonNull DataCell previousDataCell) {
        switch (newColumn.getInputType()) {
            case String:
                return new DataCellString(newColumn, previousDataCell.getMigrationString(
                        previousColumn));

            case List:
                return new DataCellList(newColumn, previousDataCell.getMigrationString(
                        previousColumn));

            default:
                throw new IllegalStateException("Unexpected value: " + newColumn.getInputType());
        }
    }

    /**
     * Create a new cell from a xml file.
     * @param parser the XmlPullParser of the file
     * @param inputType the input type of the column
     * @return the new DataCell
     * @throws IOException if when reading an exception occur
     * @throws XmlPullParserException if when reading the xml file an exception occur
     */
    private static DataCell newCell(XmlPullParser parser, ColumnInputType inputType)
            throws IOException, XmlPullParserException {
        switch (inputType) {
            case String:
                return new DataCellString(parser);

            case List:
                return new DataCellList(parser);

            default:
                throw new IllegalStateException("Unexpected value: " + inputType);
        }
    }

    /**
     * Create a new cell and initialize it's value from a view.
     * @param view the view which contains the value
     * @param row the row where the cell will be added
     * @param column the column attached to the new cell
     */
    public static void setDefaultCellFromView(View view, DataRow row, Column column) {
        DataCell cell = newCellWithDefaultValue(column);
        row.getListCells().put(column, cell);
        column.setValueFromView(cell, view);
    }

    /**
     * Get the class from the column type.
     * @param inputType the column input type
     * @return the class referring to the column type
     */
    public static Class<? extends DataCell> getClassByColumnType(ColumnInputType inputType) {
        switch (inputType) {
            case String:
                return DataCellString.class;

            case List:
                return DataCellList.class;

            default:
                throw new RuntimeException("Unknown inputType !");
        }
    }

    /**
     * Read a cell from a xml file
     * @param parser the parser of the xml file
     * @param inputType the column input type
     * @return the new DataCell
     * @throws IOException if while reading the file an exception occur
     * @throws XmlPullParserException if while reading an exception occur
     */
    @NonNull
    public static DataCell readCell(XmlPullParser parser, ColumnInputType inputType)
            throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, FileManager.TAG_CELL);
        return DataCell.newCell(parser, inputType);
    }

    /**
     * Get the value of the cell (type depend on the column).
     * @return the value (type variable)
     */
    public abstract Object getValue();

    /**
     * Set the value of the cell (type depend on the column).
     * @param value the value (type variable)
     */
    public abstract void setValue(Object value);

    /**
     * Get the migration string to migrate from different cell types (ex: String -> List).
     * @param column the column attached with the cell
     * @return the migration string
     */
    @NonNull
    protected abstract String getMigrationString(Column column);

    /**
     * Get the string representation of the value.
     * @param context the context to get a user response (if error ex: list empty) or if null get
     *                a empty string
     * @param column the column attached with the cell
     * @return the string representation of the value
     */
    @NonNull
    public abstract String getStringValue(@Nullable Context context, Column column);

    /**
     * Get the string representation of the answer. Different from
     * {@link #getStringValue(Context, Column)} because it does not use the value of the cell but
     * the answer instead.
     * @param context the context to get a user response (if error ex: list empty) or if null get a
     *                empty string
     * @param column the column attached with the cell
     * @param answer a value object
     * @return the string representation of the answer
     */
    @NonNull
    public abstract String getStringValue(@Nullable Context context, Column column,
                                             Object answer);

    /**
     * Get the value to write in csv file.
     * @param column the column attached to file
     * @return the value to write in csv file
     */
    @NonNull
    public abstract String getCsvValue(Column column);

    /**
     * Set the value from the value get in csv file.
     * @param lineCell the value write in csv file
     * @param column the column attached to the cell
     */
    public abstract void setValueFromCsv(String lineCell, Column column);

    /**
     * Write the cell in the xml file. This method should be call by {@link #writeXml(OutputStream)}
     * only.
     * @param fileOutputStream the xml file output stream
     * @throws IOException if an error occur while reading the file
     * @see #writeXml(OutputStream)
     */
    protected abstract void writeXmlInternal(OutputStream fileOutputStream) throws IOException;

    /**
     * Write the cell in the xml file.
     * This method should not be override. Override {@link #writeXmlInternal(OutputStream)} instead.
     * @param fileOutputStream the xml file output stream
     * @throws IOException if an error occur while reading the file
     * @see #writeXmlInternal(OutputStream)
     */
    public void writeXml(OutputStream fileOutputStream) throws IOException {
        XmlSaver.writeStartBeacon(fileOutputStream, FileManager.TAG_CELL);
        writeXmlInternal(fileOutputStream);
        XmlSaver.writeEndBeacon(fileOutputStream, FileManager.TAG_CELL);
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof DataCell;
    }
}