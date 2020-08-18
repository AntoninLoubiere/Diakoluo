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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.OutputStream;

import fr.pyjacpp.diakoluo.save_test.XmlLoader;
import fr.pyjacpp.diakoluo.save_test.XmlSaver;
import fr.pyjacpp.diakoluo.tests.column.Column;

/**
 * A example of cell
 * @see fr.pyjacpp.diakoluo.tests.column.DummyColumn
 */
public class DummyDataCell extends DataCell {

    // TODO implement all required functions
    // TODO implement in test/DefaultTest and android/DefaultTest, setTestValue(DataCell)
    // TODO implement switch in DataCell#copyDataCell(ColumnInputType)
    // TODO implement switch in DataCell#newCellWithDefaultValue(ColumnInputType)
    // TODO implement switch in DataCell#newCellMigrate(Column, Column, DataCell)
    // TODO implement switch in DataCell#newCell(ColumnInputType)
    // TODO implement switch in DataCell#getClassByColumnType(ColumnInputType)

    /**
     * Create a new instance from a DataCell.
     * @param dataCell the dataCell to copy
     */
    protected DummyDataCell(DataCell dataCell) {
        super(dataCell);
        // TODO copy all fields
        throw new RuntimeException("Do not use DummyCell !"); // TODO remove this
    }

    /**
     * Load a DataCell form xml.
     * @param parser The xml parser
     */
    protected DummyDataCell(XmlPullParser parser) throws IOException, XmlPullParserException {
        super(parser);
        // TODO load dataCell from parser
        int d = XmlLoader.readInt(parser);
        throw new RuntimeException("Do not use DummyCell !"); // TODO remove this
    }

    /**
     * Default initializer.
     */
    protected DummyDataCell(Object param) {
        super();
        // TODO initialize with an object (or not, as you want)
        throw new RuntimeException("Do not use DummyCell !"); // TODO remove this
    }

    /**
     * Get the value of the cell (type depend on the column).
     *
     * @return the value (type variable)
     */
    @Override
    public Object getValue() {
        return null; // TODO get the value stored by the cell
    }

    /**
     * Set the value of the cell (type depend on the column).
     *
     * @param value the value (type variable)
     */
    @Override
    public void setValue(Object value) {
        // TODO set the value stored by the cell
    }

    /**
     * Get the migration string to migrate from different cell types (ex: String -> List).
     *
     * @param column the column attached with the cell
     * @return the migration string
     */
    @NonNull
    @Override
    protected String getMigrationString(Column column) {
        return null; // TODO get a string to migrate the cell
    }

    /**
     * Get the string representation of the value.
     *
     * @param context the context to get a user response (if error ex: list empty) or if null get
     *                a empty string
     * @param column  the column attached with the cell
     * @return the string representation of the value
     */
    @NonNull
    @Override
    public String getStringValue(@Nullable Context context, Column column) {
        return null; // TODO get a string representation of the value
    }

    /**
     * Get the string representation of the answer. Different from
     * {@link #getStringValue(Context, Column)} because it does not use the value of the cell but
     * the answer instead.
     *
     * @param context the context to get a user response (if error ex: list empty) or if null get a
     *                empty string
     * @param column  the column attached with the cell
     * @param answer  a value object
     * @return the string representation of the answer
     */
    @NonNull
    @Override
    public String getStringValue(@Nullable Context context, Column column, Object answer) {
        return null; // TODO get the string representation of the answer
    }

    /**
     * Get the value to write in csv file.
     *
     * @param column the column attached to file
     * @return the value to write in csv file
     */
    @NonNull
    @Override
    public String getCsvValue(Column column) {
        return null; // TODO get the csvValue of this cell
    }

    /**
     * Set the value from the value get in csv file.
     *
     * @param lineCell the value write in csv file
     * @param column   the column attached to the cell
     */
    @Override
    public void setValueFromCsv(String lineCell, Column column) {
        // TODO set the value from the csv value
    }

    /**
     * Write the cell in the xml file. This method should be call by {@link #writeXml(OutputStream)}
     * only.
     *
     * @param fileOutputStream the xml file output stream
     * @throws IOException if an error occur while reading the file
     * @see #writeXml(OutputStream)
     */
    @Override
    protected void writeXmlInternal(OutputStream fileOutputStream) throws IOException {
        // TODO write all fields in a xml file
        XmlSaver.writeNotSafeData(fileOutputStream, 3);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj); // TODO verify all fields
    }
}
