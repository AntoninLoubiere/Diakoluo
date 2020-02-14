package fr.pyjacpp.diakoluo.save_test;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import fr.pyjacpp.diakoluo.tests.Column;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;
import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.tests.data.DataCell;
import fr.pyjacpp.diakoluo.tests.data.DataCellString;

public class XmlLoader {

    private static final String TAG_TEST              = "test";
    private static final String TAG_NAME              = "name";
    private static final String TAG_DESCRIPTION       = "description";
    private static final String TAG_NUMBER_TEST_DID   = "numberTestDid";
    private static final String TAG_CREATED_DATE      = "createdDate";
    private static final String TAG_LAST_MODIFICATION = "lastModification";
    private static final String TAG_COLUMNS           = "columns";
    private static final String TAG_COLUMN            = "column";
    private static final String TAG_ROWS              = "rows";
    private static final String TAG_ROW               = "row";
    private static final String TAG_CELL              = "cell";
    private static final String TAG_INPUT_TYPE        = "inputType";
    private static final String TAG_DEFAULT_VALUE     = "defaultValue";


    public static Test parse(Context context, String assetName) throws IOException, XmlPullParserException {
        InputStream inputStream = context.getAssets().open(assetName);
        //noinspection TryFinallyCanBeTryWithResources (because not enought API
        try {
            // configure parser
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();

            return readFeed(parser);
        } finally {
            inputStream.close();
        }
    }

    private static Test readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {
        Test test = new Test();

        // start of the document
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, TAG_TEST);

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                // continue until it is a start tag
                continue;
            }

            switch (parser.getName()) {
                case TAG_NAME:
                    test.setName(readName(parser));
                    break;

                case TAG_DESCRIPTION:
                    test.setDescription(readDescription(parser));
                    break;

                case TAG_CREATED_DATE:
                    test.setCreatedDate(readCreatedDate(parser));
                    break;

                case TAG_LAST_MODIFICATION:
                    test.setLastModificationDate(readLastModificationDate(parser));
                    break;

                case TAG_NUMBER_TEST_DID:
                    test.setNumberTestDid(readNumberTestDid(parser));
                    break;

                case TAG_COLUMNS:
                    test.setListColumn(readColumns(parser));
                    break;

                case TAG_ROWS:
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

    private static String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, TAG_NAME);

        return readText(parser);
    }

    private static String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, TAG_DESCRIPTION);

        return readText(parser);
    }

    private static Date readCreatedDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, TAG_CREATED_DATE);
        try {
            return new Date(Long.parseLong(readText(parser)));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Date readLastModificationDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, TAG_LAST_MODIFICATION);
        try {
            return new Date(Long.parseLong(readText(parser)));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static int readNumberTestDid(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, TAG_NUMBER_TEST_DID);

        try {
            return Integer.parseInt(readText(parser));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static ArrayList<Column> readColumns(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, TAG_COLUMNS);

        ArrayList<Column> columnsList = new ArrayList<>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                // continue until it is a start tag
                continue;
            }

            if (TAG_COLUMN.equals(parser.getName())) {
                Column column = readColumn(parser);
                if (column != null) {
                    columnsList.add(column);
                }
            } else {
                skip(parser);
            }
        }

        return  columnsList;
    }

    private static Column readColumn(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, TAG_COLUMN);

        Column column = new Column();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                // continue until it is a start tag
                continue;
            }

            switch (parser.getName()) {
                case TAG_NAME:
                    column.setName(readName(parser));
                    break;

                case TAG_DESCRIPTION:
                    column.setDescription(readDescription(parser));
                    break;

                case TAG_INPUT_TYPE:
                    ColumnInputType inputType = readInputType(parser);
                    if (inputType != null) {
                        column.setInputType(inputType);
                    }
                    break;

                case TAG_DEFAULT_VALUE:
                    column.setDefaultValue(readDefaultValue(parser));
                    break;

                default:
                    skip(parser);
                    break;
            }
        }

        if (column.isValid()) {
            return column;
        } else {
            return null;
        }
    }

    private static ColumnInputType readInputType(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, TAG_INPUT_TYPE);
        try {
            return ColumnInputType.valueOf(readText(parser));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static String readDefaultValue(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, TAG_DEFAULT_VALUE);
        return readText(parser);
    }

    private static ArrayList<DataRow> readRows(XmlPullParser parser, Test test) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, TAG_ROWS);

        if (test.getListColumn() == null) {
            throw new XmlPullParserException("Columns must be defined before");
        }

        ArrayList<DataRow> rowsList = new ArrayList<>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                // continue until it is a start tag
                continue;
            }

            if (TAG_ROW.equals(parser.getName())) {
                DataRow row = readRow(parser, test);
                if (row != null) {
                    rowsList.add(row);
                }
            } else {
                skip(parser);
            }
        }

        return rowsList;
    }

    private static DataRow readRow(XmlPullParser parser, Test test) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, TAG_ROW);

        DataRow dataRow = new DataRow();
        int indexColumn = 0;


        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                // continue until it is a start tag
                continue;
            }

            if (TAG_CELL.equals(parser.getName())) {
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
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, TAG_CELL);
        switch (inputType) {
            case String:
                return new DataCellString(readText(parser));

            default:
                throw new IllegalStateException("Unexpected value: " + inputType);
        }
    }

    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }


    private static void skip(XmlPullParser parser) throws IOException, XmlPullParserException {
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
}
