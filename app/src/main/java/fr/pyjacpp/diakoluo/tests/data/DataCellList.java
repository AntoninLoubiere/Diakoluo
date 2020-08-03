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
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.OutputStream;

import fr.pyjacpp.diakoluo.save_test.FileManager;
import fr.pyjacpp.diakoluo.save_test.XmlLoader;
import fr.pyjacpp.diakoluo.save_test.XmlSaver;
import fr.pyjacpp.diakoluo.tests.column.Column;
import fr.pyjacpp.diakoluo.tests.column.ColumnList;

/**
 * A cell that contain a index of value in list. The list is own by {@link ColumnList}
 * @see ColumnList
 */
public class DataCellList extends DataCell {
    private int valueIndex;

    public DataCellList(int valueIndex) {
        super();
        this.valueIndex = valueIndex;
    }

    DataCellList(DataCellList dataCellList) {
        super(dataCellList);
        valueIndex = dataCellList.valueIndex;
    }

    public DataCellList(XmlPullParser parser) throws IOException, XmlPullParserException {
        super(parser);
        valueIndex = XmlLoader.readInt(parser);
    }

    public DataCellList(Column newColumn, @NonNull String migration) {
        super();
        valueIndex = ((ColumnList) newColumn).getMigrationIndex(migration);
    }

    @Override
    public Object getValue() {
        return valueIndex;
    }

    @Override
    public void setValue(Object value) {
        valueIndex = (int) value;
    }

    @NonNull
    @Override
    protected String getMigrationString(Column column) {
        return ((ColumnList) column).getStringValue(null, valueIndex);
    }

    @NonNull
    @Override
    public String getStringValue(Context context, Column column) {
        ColumnList columnList = (ColumnList) column;
        return columnList.getStringValue(context, valueIndex);
    }

    @NonNull
    @Override
    protected String getStringValue(Context context, Column column, Object answer) {
        ColumnList columnList = (ColumnList) column;
        return columnList.getStringValue(context, (int) answer);
    }

    public Object getValueFromView(View view) {
        Spinner spinner = (Spinner) view;
        return spinner.getSelectedItemPosition();
    }

    @NonNull
    @Override
    public String getCsvValue(Column column) {
        ColumnList columnList = (ColumnList) column;
        return columnList.getStringValue(null, valueIndex);
    }

    @Override
    public void setValueFromCsv(String lineCell, Column column) {
        ColumnList columnList = (ColumnList) column;
        valueIndex = columnList.setValueFromCsvGetIndex(lineCell);
    }

    @Override
    public void writeXml(OutputStream fileOutputStream) throws IOException {
        fileOutputStream.write(XmlSaver.getCoupleBeacon(FileManager.TAG_CELL,
                String.valueOf(valueIndex)).getBytes());
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof DataCellList) {
            DataCellList dataCellList = (DataCellList) obj;
            return super.equals(obj) && valueIndex == dataCellList.valueIndex;
        }
        return false;
    }
}
