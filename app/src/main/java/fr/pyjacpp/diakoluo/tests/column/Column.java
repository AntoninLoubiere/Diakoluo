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

import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.save_test.FileManager;
import fr.pyjacpp.diakoluo.save_test.XmlLoader;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;
import fr.pyjacpp.diakoluo.tests.data.DataCell;

public abstract class Column {
    private String name;
    private String description;

    protected ColumnInputType inputType;
    protected Column() {
        initialize();
    }

    void initialize() {
        this.name = null;
        this.description = null;
        this.inputType = null;
    }

    public abstract void initializeChildValue();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ColumnInputType getInputType() {
        return inputType;
    }

    public void setInputType(ColumnInputType inputType) {
        this.inputType = inputType;
    }

    public abstract Object getDefaultValue();

    public abstract void setDefaultValue(Object defaultValue);

    public boolean isValid() {
        return !(name == null ||
                description == null ||
                inputType == null);
    }

    public abstract boolean verifyAnswer(DataCell dataCell, Object answer);

    public View showColumnName(Context context) {
        TextView columnNameTextView = new TextView(context);
        columnNameTextView.setTextAppearance(context, R.style.BoldHeadline5);
        columnNameTextView.setText(context.getString(R.string.column_name_format, name));
        return columnNameTextView;
    }

    private TextInputLayout showColumnEditValue(Context context) {
        TextInputLayout inputLayout = new TextInputLayout(context, null, R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox);
        TextInputEditText inputField = new TextInputEditText(context);
        inputLayout.setHint(name);

        inputLayout.addView(inputField);

        return inputLayout;
    }


    public TextInputLayout showColumnEditValue(Context context, Object defaultValue) {
        TextInputLayout inputLayout = showColumnEditValue(context);
        EditText inputField = inputLayout.getEditText();
        if (inputField != null && defaultValue != null)
            inputField.setText((String) defaultValue);

        return inputLayout;
    }

    public abstract void getViewColumnSettings(LayoutInflater layoutInflater, ViewGroup parent);

    @NonNull
    public abstract View getEditColumnSettings(LayoutInflater layoutInflater, ViewGroup parent);

    public abstract void setEditColumnSettings(View columnSettingsView);

    public static Column newColumn(ColumnInputType columnInputType) {
        return newColumn(columnInputType, "", "");
    }

    public static Column newColumn(ColumnInputType columnInputType, String name, String description) {
        switch (columnInputType) {
            case String:
                ColumnString columnString = new ColumnString();
                columnString.setName(name);
                columnString.setDescription(description);
                columnString.initializeChildValue();
                return columnString;

            default:
                throw new IllegalStateException("Unexpected value: " + columnInputType);
        }
    }

    static void privateCopyColumn(Column baseColumn, Column newColumn) {
        newColumn.name = baseColumn.name;
        newColumn.description = baseColumn.description;
        newColumn.inputType = baseColumn.inputType;
    }

    public abstract void writeXmlHeader(OutputStream fileOutputStream) throws IOException;

    private void loopXmlTags(XmlPullParser parser) throws IOException, XmlPullParserException {
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                // continue until it is a start tag
                continue;
            }

            readColumnXmlTag(parser);
        }
    }

    void readColumnXmlTag(XmlPullParser parser) throws IOException, XmlPullParserException {
        switch (parser.getName()) {
            case FileManager.TAG_NAME:
                name = XmlLoader.readName(parser);
                break;

            case FileManager.TAG_DESCRIPTION:
                description = XmlLoader.readDescription(parser);
                break;

            default:
                XmlLoader.skip(parser);
                break;
        }
    }

    public static Column readColumnXml(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, FileManager.TAG_COLUMN);
        String attributeValue = parser.getAttributeValue(null, FileManager.ATTRIBUTE_INPUT_TYPE);
        if (attributeValue == null) {
            // old version
            return XmlLoader.readColumnXml(parser);
        } else {
            ColumnInputType columnInputType = ColumnInputType.get(attributeValue);
            if (columnInputType == null) {
                throw new XmlPullParserException("Column input type error");
            } else {
                Column column;
                switch (columnInputType) {
                    case String:
                        column = new ColumnString();
                        column.loopXmlTags(parser);
                        break;

                    default:
                        throw new IllegalStateException("InputType " + columnInputType.name() + " not implemented");
                }
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
     * Set default values for backward compatibility
     */
    protected void setDefaultValueBackWardCompatibility() {
    }

    public static Column copyColumn(Column column) {
        if (column instanceof ColumnString) {
            return ColumnString.privateCopyColumn((ColumnString) column);
        } else {
            throw new IllegalStateException("Column type not detected");
        }
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Column) {
            Column c = (Column) obj;
            return c.name.equals(name) && c.description.equals(description);
        }
        return false;
    }
}
