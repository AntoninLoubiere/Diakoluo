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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.OutputStream;

import fr.pyjacpp.diakoluo.save_test.FileManager;
import fr.pyjacpp.diakoluo.save_test.XmlLoader;
import fr.pyjacpp.diakoluo.save_test.XmlSaver;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;

class ColumnString extends Column {
    private String defaultValue;

    ColumnString() {
        super();
    }

    @Override
    void initialize() {
        super.initialize();
        defaultValue = null;
        inputType = ColumnInputType.String;
    }

    ColumnString(XmlPullParser parser) throws IOException, XmlPullParserException {
        super(parser);
    }

    @Override
    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = (String) defaultValue;
    }

    @Override
    public void initializeDefaultValue() {
        defaultValue = "";
    }

    @Override
    public Object getDefaultValue() {
        return defaultValue;
    }

    @Override
    public boolean isValid() {
        return super.isValid() && defaultValue != null;
    }

    @Override
    public void writeXmlHeader(OutputStream fileOutputStream) throws IOException {
        fileOutputStream.write(XmlSaver.getCoupleBeacon(FileManager.TAG_DEFAULT_VALUE,
                defaultValue).getBytes());
    }

    @Override
    void readColumnXmlTag(XmlPullParser parser) throws IOException, XmlPullParserException {
        if (FileManager.TAG_DEFAULT_VALUE.equals(parser.getName())) {
            defaultValue = XmlLoader.readText(parser);
        } else {
            super.readColumnXmlTag(parser);
        }
    }

    static ColumnString privateCopyColumn(ColumnString baseColumn) {
        ColumnString newColumn = new ColumnString();
        newColumn .defaultValue = baseColumn.defaultValue;
        Column.privateCopyColumn(baseColumn, newColumn);
        return newColumn;
    }
}
