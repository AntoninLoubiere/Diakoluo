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

package fr.pyjacpp.diakoluo.tests.column;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.OutputStream;

import fr.pyjacpp.diakoluo.test_tests.TestTestContext;
import fr.pyjacpp.diakoluo.tests.data.DataCell;


/**
 * A example of column
 * @see fr.pyjacpp.diakoluo.tests.data.DummyDataCell
 */

public class DummyColumn extends Column {

    // TODO create a ColumnInputType, do not forget to implement ColumnInputType.get(String)
    // TODO create the corresponding DataCell
    // TODO implement required functions
    // TODO launch unit test and instrumented test to verify that all is working
    // TODO implement the switch in Column#newColumn()
    // TODO if the column contain data like ColumnList implement in test/DefaultTest and
    //  android/DefaultTest #setTestValue(Column) and #setTestValueEmpty(Column)

    /**
     * Default constructor that initialize a non-valid column.
     */
    protected DummyColumn() {
        // super(ColumnInputType.DummyType); // TODO call the super with the appropriate type
        super(null);
        throw new RuntimeException("Do not use DummyColumn !"); // TODO remove this
    }

    /**
     * Get the default value of a column, type depend of column.
     * @return the default value
     */
    @Override
    public Object getDefaultValue() {
        return null; // TODO implement the get default value
    }

    /**
     * Set the default value of a column, type depend of column.
     * @param defaultValue the default value to set
     */
    @Override
    public void setDefaultValue(Object defaultValue) {
        // TODO set the default value
    }

    /**
     * Verify if a answer inputted by the user is the same that the value stored.
     * @param dataCell the DataCell that hold the value
     * @param answer   the value inputted by the user
     * @return if the a
     */
    @Override
    public boolean verifyAnswer(DataCell dataCell, Object answer) {
        return false; // TODO verify answer depending of the data cell and the answer
    }

    /**
     * Copy the column.
     * @return the column copied
     */
    @Override
    public Column copyColumn() {
        // TODO change with the appropriate constructor
        DummyColumn column = new DummyColumn();
        copyColumn(column);
        return column;
    }

    /**
     * Copy column fields of all levels.
     * @param newColumn the column instance
     */
    @Override
    protected void copyColumn(Column newColumn) {
        super.copyColumn(newColumn);
        // TODO copy ALL fields (super call required)
    }

    /**
     * Initialize a non-valid column. Inverse of {@link #initialize(String, String)}
     * @see #initialize(String, String)
     */
    @Override
    public void initialize() {
        super.initialize();
        // TODO initialize all fields in a non-valid way (super call required)
    }

    /**
     * Initialize a valid column. Inverse of {@link #initialize()}.
     * @param name the name of the column
     * @param description the description of the column
     * @see #initialize()
     */
    @Override
    protected void initialize(String name, String description) {
        super.initialize(name, description);
        // TODO initialize all filed in a valid way (super call required)
    }

    /**
     * Get if the column is a valid column.
     * @return if the column is valid
     */
    @Override
    public boolean isValid() {
        return super.isValid(); // TODO add verification of column fields (super call required)
    }

    /**
     * Verify if the answer is correct and give score depending
     * @param testTestContext the test context
     * @param dataCell        the dataCell to verify
     * @param answer          the answer given by the user
     */
    @Override
    public void verifyAndScoreAnswer(TestTestContext testTestContext, DataCell dataCell, Object answer) {
        // TODO implement to create a non binary score (0/all) or delete this method
        //  (super call not recommended)
    }

    /**
     * Get the view to answer the data cell (type depend on the column).
     * If override, {@link #getValueFromView(View)} may need to be override.
     * @param context      the context to create widgets
     * @param defaultValue the default value of the input, can be null
     * @return the view to add to show the edit input
     */
    @Override
    public View showEditValueView(Context context, @Nullable Object defaultValue) {
        // TODO implement to create a custom widget to choose the value of a data cell
        //  or remove to leave the default which use a string input (super call not recommended)
        return null;
    }

    /**
     * Get the value of the data cell from a view (type depend on the column).
     * If override, {@link #showEditValueView(Context, Object)} may need to be override too
     * @param view the view which contain the value of the cell
     * @return the value in the view (type variable)
     * @see #setValueFromView(DataCell, View)
     * @see #showEditValueView(Context, Object)
     */
    @Override
    public Object getValueFromView(View view) {
        return super.getValueFromView(view);
    }

    /**
     * Set the settings view of the column.
     * Override method must add to root the view and must call the super method
     * @param layoutInflater a layout inflater to inflate the layout
     * @param parent         the parent which receive the inflated layout
     * @see #getEditColumnSettings(LayoutInflater, ViewGroup)
     * @see #setEditColumnSettings(ViewGroup)
     */
    @Override
    public void getViewColumnSettings(LayoutInflater layoutInflater, ViewGroup parent) {
        super.getViewColumnSettings(layoutInflater, parent);
        // TODO implement the creation of the settings of column (super call required)
    }

    /**
     * Set and return the edit settings view of the column
     * Override method must add to root the view.
     * Params are updated by {@link #setEditColumnSettings}.
     * @param layoutInflater a layout inflater to inflate the layout
     * @param parent         the parent which receive the inflated layout
     * @see #getViewColumnSettings(LayoutInflater, ViewGroup)
     * @see #setEditColumnSettings(ViewGroup)
     */
    @Override
    public void getEditColumnSettings(LayoutInflater layoutInflater, ViewGroup parent) {
        super.getEditColumnSettings(layoutInflater, parent);
        // TODO implement the creation of the settings of column (super call required)
    }

    /**
     * Set column params from view. Params inputted by the user are update there.
     * @param parent the parent which contain inflated layouts (generated from
     *               {@link #getEditColumnSettings(LayoutInflater, ViewGroup)}).
     * @see #getViewColumnSettings(LayoutInflater, ViewGroup)
     * @see #getEditColumnSettings(LayoutInflater, ViewGroup)
     */
    @Override
    public void setEditColumnSettings(ViewGroup parent) {
        super.setEditColumnSettings(parent);
        // TODO implement the creation of the settings of column (super call required)
    }

    /**
     * Write the column into a xml file.
     * This method should be call by {@link #writeXml(OutputStream)} only.
     * If override you should call the super.
     * @param fileOutputStream the FileOutputStream of the xml file
     * @throws IOException if while writing the file an error occur
     * @see #writeXml(OutputStream)
     */
    @Override
    protected void writeXmlInternal(OutputStream fileOutputStream) throws IOException {
        super.writeXmlInternal(fileOutputStream);
        // TODO write fields in the xml file (super call required)
    }

    /**
     * Get columns params from a xml file.
     * @param parser the parser of the xml file
     * @throws IOException            if an error occur while the file reading the file
     * @throws XmlPullParserException if an error occur while reading the file
     */
    @Override
    protected void readColumnXmlTag(XmlPullParser parser) throws IOException, XmlPullParserException {
        // TODO read tags of a file (super call required)
        super.readColumnXmlTag(parser);
    }

    /**
     * Set default values for backward compatibility.
     * @param fileVersion the version of the file
     */
    @Override
    protected void setDefaultValueBackWardCompatibility(int fileVersion) {
        super.setDefaultValueBackWardCompatibility(fileVersion);
        // TODO implement back ward compatibility
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj); // TODO must be implemented
    }
}
