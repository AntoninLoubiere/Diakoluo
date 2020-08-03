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
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.save_test.XmlLoader;
import fr.pyjacpp.diakoluo.save_test.XmlSaver;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;
import fr.pyjacpp.diakoluo.tests.data.DataCell;

public class ColumnList extends IntSettingsColumn {
    private static final int DEFAULT_SETTINGS = 0;
    private ArrayList<String> values;
    private int defaultValue = 0;

    private static final String TAG_VALUES = "values";
    private static final String TAG_VALUE = "value";

    protected ColumnList() {
        super();
    }

    @Override
    void initialize() {
        super.initialize();
        defaultValue = -1;
        values = null;
        settings = -1;
        inputType = ColumnInputType.List;
    }

    @Override
    public void initializeChildValue() {
        defaultValue = 0;
        values = new ArrayList<>();
        settings = DEFAULT_SETTINGS;
    }

    @Override
    public Object getDefaultValue() {
        return defaultValue;
    }

    @Override
    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = (int) defaultValue;
    }

    public String getStringValue(Context context, int valueIndex) {
        if (valueIndex < 0 || valueIndex >= values.size()) {
            if (context == null)
                return "";
            return context.getString(R.string.default_string_list);
        }
        return values.get(valueIndex);
    }

    public int setValueFromCsvGetIndex(String lineCell) {
        int valuesSize = values.size();
        for (int i = 0; i < valuesSize; i++) {
            if (lineCell.equals(values.get(i))) {
                return i;
            }
        }
        values.add(lineCell);
        return valuesSize;
    }

    public int getMigrationIndex(String migration) {
        int valuesSize = values.size();
        for (int i = 0; i < valuesSize; i++) {
            if (migration.equals(values.get(i))) {
                return i;
            }
        }
        values.add(migration);
        return valuesSize;
    }

    @Override
    public boolean verifyAnswer(DataCell dataCell, Object answer) {
        return dataCell.getValue().equals(answer);
    }

    @Override
    public void getViewColumnSettings(LayoutInflater layoutInflater, ViewGroup parent) {
        // TODO
    }

    @NonNull
    @Override
    public View getEditColumnSettings(LayoutInflater layoutInflater, ViewGroup parent) {
        return new View(parent.getContext()); // TODO
    }

    @Override
    public void setEditColumnSettings(View columnSettingsView) {
        // TODO
    }

    @Override
    public View showColumnEditValue(Context context, @Nullable Object defaultValue) {
        Spinner spinner = new Spinner(context);
        spinner.setAdapter(new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, values));
        if (defaultValue != null)
            spinner.setSelection((int) defaultValue);
        return spinner;
    }

    @Override
    public void writeXmlHeader(OutputStream fileOutputStream) throws IOException {
        fileOutputStream.write(XmlSaver.getStartBeacon(TAG_VALUES).getBytes());
        for (int i = 0, valuesSize = values.size(); i < valuesSize; i++) {
            fileOutputStream.write(XmlSaver.getCoupleBeacon(TAG_VALUE,
                    String.valueOf(values.get(i))).getBytes());
        }
        fileOutputStream.write(XmlSaver.getEndBeacon(TAG_VALUES).getBytes());
    }

    @Override
    void readColumnXmlTag(XmlPullParser parser) throws IOException, XmlPullParserException {
        if (TAG_VALUES.equals(parser.getName())) {
            readXmlValuesTag(parser);
        } else {
            super.readColumnXmlTag(parser);
        }
    }

    private void readXmlValuesTag(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, TAG_VALUES);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                // continue until it is a start tag
                continue;
            }

            if (parser.getName().equals(TAG_VALUE)) {
                values.add(XmlLoader.readText(parser));
            } else {
                XmlLoader.skip(parser);
            }
        }
    }

    static ColumnList privateCopyColumn(ColumnList baseColumn) {
        ColumnList newColumn = new ColumnList();
        newColumn.defaultValue = baseColumn.defaultValue;
        newColumn.values = new ArrayList<>(baseColumn.values);
        IntSettingsColumn.privateCopyColumn(baseColumn, newColumn);
        return newColumn;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof ColumnList && super.equals(obj)) {
            ColumnList cL = (ColumnList) obj;
            return cL.defaultValue == defaultValue &&
                    cL.values.equals(values);
        }
        return false;
    }

    public ArrayList<String> getValues() {
        return values;
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }
}
