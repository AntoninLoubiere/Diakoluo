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
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.save_test.FileManager;
import fr.pyjacpp.diakoluo.save_test.XmlLoader;
import fr.pyjacpp.diakoluo.save_test.XmlSaver;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;
import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.tests.data.DataCell;

/**
* A column class that hold parameters for cells, like type, and column specific settings
 * @see DataCell
 */
public abstract class Column {
    /**
     * When a edit text with a layout is clicked send focus change to the parent so it can be
     * receive from other class
     */
    private static final View.OnFocusChangeListener editTextListener =
            new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    ViewParent parent = v.getParent();
                    if (parent instanceof View) {
                        View parent1 = (View) parent;
                        View.OnFocusChangeListener onFocusChangeListener =
                                parent1.getOnFocusChangeListener();
                        if (onFocusChangeListener != null)
                            onFocusChangeListener.onFocusChange(parent1, true);
                    }
                }
            };
    @NonNull protected ColumnInputType inputType;
    @Nullable private String name;
    @Nullable private String description;
    protected int settings = -1;

    /**
     * Default constructor that initialize a non-valid column.
     * @param inputType the input type of the column
     */
    protected Column(@NonNull ColumnInputType inputType) {
        initialize();
        this.inputType = inputType;
    }

    /**
     * Default constructor that initialize a valid column with empty (not null !) name and
     * description.
     * @param columnInputType the input type of the column
     */
    public static Column newColumn(ColumnInputType columnInputType) {
        return newColumn(columnInputType, "", "");
    }

    /**
     * Default constructor that initialize a valid column (or not if name and description are null)
     * @param columnInputType the input type of the column
     * @param name the name of the column
     * @param description the description of the column
     */
    public static Column newColumn(ColumnInputType columnInputType, String name,
                                   String description) {
        Column column;
        switch (columnInputType) {
            case String:
                column = new ColumnString();
                break;

            case List:
                column = new ColumnList();
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + columnInputType);
        }
        if (name != null || description != null) column.initialize(name, description);
        return column;
    }

    /**
     * Create a new column from a xml file.
     * @param parser the XmlPullParser of the file
     * @return the new column
     * @throws XmlPullParserException if while reading the file an exception occur
     * @throws IOException if while reading the file an exception occur
     */
    public static Column readColumnXml(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, FileManager.TAG_COLUMN);
        String attributeValue = parser.getAttributeValue(null,
                FileManager.ATTRIBUTE_INPUT_TYPE);
        if (attributeValue == null) {
            // old version
            return XmlLoader.readColumnXml(parser);
        } else {
            ColumnInputType columnInputType = ColumnInputType.get(attributeValue);
            if (columnInputType == null) {
                throw new XmlPullParserException("Column input type error");
            } else {
                Column column = Column.newColumn(columnInputType, null, null);

                column.loopXmlTags(parser);
                column.setDefaultValueBackWardCompatibility();
                if (column.isValid()) {
                    return column;
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Get the default value of a column, type depend of column.
     * @return the default value
     */
    public abstract Object getDefaultValue();

    /**
     * Set the default value of a column, type depend of column.
     * @param defaultValue the default value to set
     */
    public abstract void setDefaultValue(Object defaultValue);

    /**
     * Set the settings view of the column.
     * Override method must add to root the view.
     * @param layoutInflater a layout inflater to inflate the layout
     * @param parent the parent which receive the inflated layout
     * @see #getEditColumnSettings(LayoutInflater, ViewGroup)
     * @see #setEditColumnSettings(View)
     */
    public abstract void getViewColumnSettings(LayoutInflater layoutInflater, ViewGroup parent);

    /**
     * Set and return the edit settings view of the column
     * Override method must add to root the view.
     * Params are updated by {@link #setEditColumnSettings}.
     * @param layoutInflater a layout inflater to inflate the layout
     * @param parent the parent which receive the inflated layout
     * @return the view that was generated
     * @see #getViewColumnSettings(LayoutInflater, ViewGroup)
     * @see #setEditColumnSettings(View)
     */
    @NonNull
    public abstract View getEditColumnSettings(LayoutInflater layoutInflater, ViewGroup parent);

    /**
     * Set column params from view. Params inputted by the user are update there.
     * @param columnSettingsView the view which contain edit view (generated from
     * {@link #getEditColumnSettings(LayoutInflater, ViewGroup)}).
     * @see #getViewColumnSettings(LayoutInflater, ViewGroup)
     * @see #getEditColumnSettings(LayoutInflater, ViewGroup)
     */
    public abstract void setEditColumnSettings(View columnSettingsView);

    /**
     * Verify if a answer inputted by the user is the same that the value stored.
     * @param dataCell the DataCell that hold the value
     * @param answer the value inputted by the user
     * @return if the a
     */
    public abstract boolean verifyAnswer(DataCell dataCell, Object answer);

    /**
     * Copy the column.
     * @return the column copied
     */
    public abstract Column copyColumn();

    /**
     * Copy column fields of all levels.
     * @param newColumn the column instance
     */
    protected void copyColumn(Column newColumn) {
        newColumn.name = name;
        newColumn.description = description;
        newColumn.inputType = inputType;
        newColumn.settings = settings;
    }

    /**
     * Initialize a non-valid column. Inverse of {@link #initialize(String, String)}
     * @see #initialize(String, String)
     */
    public void initialize() {
        this.name = null;
        this.description = null;
        settings = -1;
    }

    /**
     * Initialize a valid column. Inverse of {@link #initialize()}.
     * @see #initialize()
     */
    protected void initialize(String name, String description) {
        this.name = name;
        this.description = description;
        settings = 0;
    }

    /**
     * Get the name of the column.
     * @return the name of the column
     */
    @Nullable
    public String getName() {
        return name;
    }

    /**
     * Set the name of the column.
     * @param name the new name of the column
     */
    public void setName(@Nullable String name) {
        this.name = name;
    }

    /**
     * Get the description of the column.
     * @return the description of the file
     */
    @Nullable
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the file.
     * @param description the description of the file
     */
    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    /**
     * Get the input type of the file
     * @return the input type of the file
     */
    @NonNull
    public ColumnInputType getInputType() {
        return inputType;
    }

    /**
     * Get if the column is a valid column.
     * @return if the column is valid
     */
    public boolean isValid() {
        return !(name == null || description == null) && settings >= 0;
    }

    /**
     * Get the view who show the column name.
     * @param context the context to create widgets
     * @return the view to add to show the column name
     */
    public View showColumnName(Context context) {
        TextView columnNameTextView = new TextView(context);
        columnNameTextView.setTextAppearance(context, R.style.BoldHeadline5);
        columnNameTextView.setText(context.getString(R.string.column_name_format, name));
        return columnNameTextView;
    }

    /**
     * Get the view to answer the data cell (type depend on the column).
     * If override, {@link #getValueFromView(View)} may need to be override.
     * @param context the context to create widgets
     * @param defaultValue the default value of the input, can be null
     * @return the view to add to show the edit input
     */
    public View showEditValueView(Context context, @Nullable Object defaultValue) {
        TextInputLayout inputLayout = new TextInputLayout(context, null,
                R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox);
        TextInputEditText inputField = new TextInputEditText(context);
        inputLayout.setHint(name);
        inputLayout.addView(inputField);
        inputField.setOnFocusChangeListener(editTextListener);

        if (defaultValue != null)
            inputField.setText((String) defaultValue);

        return inputLayout;
    }

    /**
     * Get the value of the data cell from a view (type depend on the column).
     * If override, {@link #showEditValueView(Context, Object)} may need to be override too
     * @param view the view which contain the value of the cell
     * @return the value in the view (type variable)
     * @see #setValueFromView(DataCell, View)
     * @see #showEditValueView(Context, Object)
     */
    public Object getValueFromView(View view) {
        TextInputLayout inputLayout = (TextInputLayout) view;
        EditText editText = inputLayout.getEditText();
        if (editText != null)
            return editText.getText().toString();
        else
            return null;
    }

    /**
     * Set the value from a view.
     * @param view the view which contain the value
     * @see #getValueFromView(View)
     * @see #showEditValueView(Context, Object)
     */
    public void setValueFromView(DataCell dataCell, View view) {
        dataCell.setValue(getValueFromView(view));
    }

    /**
     * If a settings is true
     * @param parameter the settings wanted
     * @return if the settings is true
     */
    protected boolean isInSettings(int parameter) {
        return (settings & parameter) == parameter;
    }

    /**
     * Set the settings to a value
     * @param parameter the settings to set
     * @param value the value of the settings
     */
    protected void setSettings(int parameter, boolean value) {
        if (value) {
            settings = settings | parameter;
        } else {
            settings = settings & ~parameter;
        }
    }

    /**
     * Write the column into a xml file.
     * @param fileOutputStream the FileOutputStream of the xml file
     * @throws IOException if while writing the file an error occur
     */
    public void writeXml(OutputStream fileOutputStream) throws IOException {
        fileOutputStream.write(XmlSaver.getCoupleBeacon(FileManager.TAG_SETTINGS,
                String.valueOf(settings)).getBytes());
    }

    /**
     * Get columns params from a xml file.
     * @param parser the parser of the xml file
     * @throws IOException if an error occur while the file reading the file
     * @throws XmlPullParserException if an error occur while reading the file
     */
    protected void readColumnXmlTag(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        switch (parser.getName()) {
            case FileManager.TAG_NAME:
                name = XmlLoader.readName(parser);
                break;

            case FileManager.TAG_DESCRIPTION:
                description = XmlLoader.readDescription(parser);
                break;

            case FileManager.TAG_SETTINGS:
                settings = XmlLoader.readInt(parser);

            default:
                XmlLoader.skip(parser);
                break;
        }
    }

    /**
     * Set default values for backward compatibility.
     */
    protected void setDefaultValueBackWardCompatibility() {
    }

    /**
     * Loop over all xml tag while reading an xml file that represent the column.
     * @param parser the parser that represents the xml file
     * @throws IOException if an error occur while the file is reading
     * @throws XmlPullParserException if an error occur while the file is reading
     */
    private void loopXmlTags(XmlPullParser parser) throws IOException, XmlPullParserException {
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                // continue until it is a start tag
                continue;
            }

            readColumnXmlTag(parser);
        }
    }

    /**
     *
     * Update all cells if the column need to change
     * @param currentTest the current test which is updated
     * @param previousColumn the previous column which will change into this column
     */
    public void updateCells(Test currentTest, Column previousColumn) {
        ArrayList<DataRow> listRow = currentTest.getListRow();
        for (int i = 0, listRowSize = listRow.size(); i < listRowSize; i++) {
            DataRow row = listRow.get(i);
            HashMap<Column, DataCell> listCells = row.getListCells();
            DataCell dataCell = listCells.get(previousColumn);
            if (dataCell == null) {
                listCells.put(this, DataCell.newCellWithDefaultValue(this));
            } else {
                listCells.remove(previousColumn); // should be remove before put in case of
                // previousColumn == this
                listCells.put(this, DataCell.newCellMigrate(this, previousColumn,
                        dataCell));
            }
        }
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Column) {
            Column c = (Column) obj;
            return Objects.equals(c.name, name) && Objects.equals(c.description, description) &&
                    c.settings == settings;
        }
        return false;
    }
}
