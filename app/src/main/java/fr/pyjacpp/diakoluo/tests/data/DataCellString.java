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
 * A cell that contain a string value.
 * @see fr.pyjacpp.diakoluo.tests.column.ColumnString
 */
public class DataCellString extends DataCell {
    private String value;

    DataCellString(DataCellString dataCellString) {
        super(dataCellString);
        value = dataCellString.value;
    }

    public DataCellString(String value) {
        super();
        this.value = value;
    }

    public DataCellString(XmlPullParser parser) throws IOException, XmlPullParserException {
        super(parser);
        value = XmlLoader.readText(parser);
    }

    public DataCellString(@SuppressWarnings("unused") Column newColumn,
                          @NonNull String migrationString) {
        super();
        value = migrationString;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (String) value;
    }

    @NonNull
    @Override
    protected String getMigrationString(Column column) {
        return value;
    }

    @NonNull
    @Override
    public String getStringValue(Context context, Column column) {
        return value;
    }

    @NonNull
    @Override
    public String getStringValue(Context context, Column column, Object answer) {
        return (String) answer;
    }

    @NonNull
    public String getCsvValue(Column column) {
        return value;
    }

    @Override
    public void setValueFromCsv(String lineCell, Column column) {
        value = lineCell;
    }

    @Override
    public void writeXmlInternal(OutputStream fileOutputStream) throws IOException {
        XmlSaver.writeNotSafeData(fileOutputStream, value);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof DataCellString) {
            DataCellString dataCellString = (DataCellString) obj;
            return super.equals(obj) && value.equals(dataCellString.value);
        }
        return false;
    }
}
