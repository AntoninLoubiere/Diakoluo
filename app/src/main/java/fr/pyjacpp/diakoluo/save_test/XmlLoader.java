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

package fr.pyjacpp.diakoluo.save_test;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;

import fr.pyjacpp.diakoluo.tests.column.Column;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;
import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.tests.data.DataCell;
import fr.pyjacpp.diakoluo.tests.data.DataCellString;

public class XmlLoader {

    static Test load(InputStream fileInputStream) throws IOException, XmlPullParserException {
            // configure parser
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(fileInputStream, null);
        parser.nextTag();

        return readFeed(parser);
    }

    private static Test readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {
        Test test = new Test();

        // start of the document
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, FileManager.TAG_TEST);

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                // continue until it is a start tag
                continue;
            }

            switch (parser.getName()) {
                case FileManager.TAG_NAME:
                    test.setName(readName(parser));
                    break;

                case FileManager.TAG_DESCRIPTION:
                    test.setDescription(readDescription(parser));
                    break;

                case FileManager.TAG_CREATED_DATE:
                    test.setCreatedDate(readCreatedDate(parser));
                    break;

                case FileManager.TAG_LAST_MODIFICATION:
                    test.setLastModificationDate(readLastModificationDate(parser));
                    break;

                case FileManager.TAG_NUMBER_TEST_DID:
                    test.setNumberTestDid(readNumberTestDid(parser));
                    break;

                case FileManager.TAG_COLUMNS:
                    test.setListColumn(readColumns(parser));
                    break;

                case FileManager.TAG_ROWS:
                    test.setListRow(readRows(parser, test));
                    break;

                default:
                    skip(parser);
                    break;
            }
        }
        if (test.isValid()) {
            return test;
        } else {
            Log.w("XmlLoader", "Can't load this test");
            return null;
        }
    }

    public static String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, FileManager.TAG_NAME);

        return readText(parser);
    }

    public static String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, FileManager.TAG_DESCRIPTION);

        return readText(parser);
    }

    private static Date readCreatedDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, FileManager.TAG_CREATED_DATE);
        try {
            return new Date(Long.parseLong(readText(parser)));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Date readLastModificationDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, FileManager.TAG_LAST_MODIFICATION);
        try {
            return new Date(Long.parseLong(readText(parser)));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static int readNumberTestDid(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, FileManager.TAG_NUMBER_TEST_DID);

        try {
            return Integer.parseInt(readText(parser));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static ArrayList<Column> readColumns(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, FileManager.TAG_COLUMNS);

        ArrayList<Column> columnsList = new ArrayList<>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                // continue until it is a start tag
                continue;
            }

            if (FileManager.TAG_COLUMN.equals(parser.getName())) {
                Column column = Column.readColumnXml(parser);
                if (column != null) {
                    columnsList.add(column);
                }
            } else {
                skip(parser);
            }
        }

        return  columnsList;
    }

    private static ArrayList<DataRow> readRows(XmlPullParser parser, Test test) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, FileManager.TAG_ROWS);

        if (test.getListColumn() == null) {
            throw new XmlPullParserException("Columns must be defined before");
        }

        ArrayList<DataRow> rowsList = new ArrayList<>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                // continue until it is a start tag
                continue;
            }

            if (FileManager.TAG_ROW.equals(parser.getName())) {
                DataRow row = readRow(parser, test);
                rowsList.add(row);
            } else {
                skip(parser);
            }
        }

        return rowsList;
    }

    private static DataRow readRow(XmlPullParser parser, Test test) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, FileManager.TAG_ROW);

        DataRow dataRow = new DataRow();
        int indexColumn = 0;


        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                // continue until it is a start tag
                continue;
            }

            if (FileManager.TAG_CELL.equals(parser.getName())) {
                if (indexColumn >= test.getNumberColumn()) {
                    Log.w("XmlLoader", "Too many columns");
                    continue;
                }
                Column currentColumn = test.getListColumn().get(indexColumn);
                DataCell cell = readCell(parser, currentColumn.getInputType());
                dataRow.getListCells().put(currentColumn, cell);

                indexColumn++;

            } else {
                skip(parser);
            }
        }

        if (indexColumn < test.getNumberColumn()) {
            Log.w("XmlLoader", "Too few cells");
            for (int i = indexColumn; i < test.getNumberColumn(); i++) {
                Column currentColumn = test.getListColumn().get(indexColumn);
                DataCell cell = DataCell.getDefaultValueCell(currentColumn);
                dataRow.getListCells().put(currentColumn, cell);
            }
        }

        return dataRow;
    }

    private static DataCell readCell(XmlPullParser parser, ColumnInputType inputType) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, FileManager.TAG_CELL);
        switch (inputType) {
            case String:
                return new DataCellString(readText(parser));

            default:
                throw new IllegalStateException("Unexpected value: " + inputType);
        }
    }

    public static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }


    public static void skip(XmlPullParser parser) throws IOException, XmlPullParserException {
        Log.w("XmlLoader", parser.getName() + " is not handle");
        int depth = 1;
        while (depth > 0) {
            parser.next();
            switch (parser.getEventType()) {
                case XmlPullParser.START_TAG:
                    depth++;
                    break;

                case XmlPullParser.END_TAG:
                    depth--;
                    break;
            }
        }
    }

    // Back Support Zone

    @Deprecated
    public static Column readColumnXml(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, FileManager.TAG_COLUMN);

        String name = null;
        String description = null;
        ColumnInputType inputType = null;
        String defaultValue = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                // continue until it is a start tag
                continue;
            }

            switch (parser.getName()) {
                case FileManager.TAG_NAME:
                    name = readName(parser);
                    break;

                case FileManager.TAG_DESCRIPTION:
                    description = readDescription(parser);
                    break;

                case FileManager.ATTRIBUTE_INPUT_TYPE:
                    inputType = readInputType(parser);
                    break;

                case FileManager.TAG_DEFAULT_VALUE:
                    defaultValue = readDefaultValue(parser);
                    break;

                default:
                    skip(parser);
                    break;
            }
        }

        if (name == null || description == null || inputType == null || defaultValue == null) {
            return null;
        }

        String tag = "<column inputType=\"" +
                inputType.name() +
                "\">" +
                "<name>" +
                name +
                "</name>" +
                "<description>" +
                description +
                "</description>" +
                "<defaultValue>" +
                defaultValue +
                "</defaultValue>" +
                "</column>";

        XmlPullParser parser2 = Xml.newPullParser();
        parser2.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser2.setInput(new StringReader(tag));
        parser2.nextTag();

        return Column.readColumnXml(parser2);
    }

    @Deprecated
    private static ColumnInputType readInputType(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, FileManager.ATTRIBUTE_INPUT_TYPE);
        try {
            return ColumnInputType.valueOf(XmlLoader.readText(parser));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Deprecated
    private static String readDefaultValue(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, FileManager.TAG_DEFAULT_VALUE);
        return XmlLoader.readText(parser);
    }
}
