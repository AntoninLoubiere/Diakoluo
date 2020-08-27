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

import androidx.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Date;

import fr.pyjacpp.diakoluo.tests.ColumnInputType;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.tests.column.Column;

/**
 * The class that hold load of xml files.
 */
public class XmlLoader {

    public static final String TAG = "XmlLoader";

    /**
     * Load a test from a xml file.
     * @param fileInputStream the file input stream of the file to load
     * @return the file loaded
     * @throws IOException if while loading the file an error occur
     * @throws XmlPullParserException if while loading the file an exception occur
     */
    @Nullable
    public static Test load(InputStream fileInputStream) throws IOException, XmlPullParserException {
            // configure parser
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(fileInputStream, null);
        parser.nextTag();

        Test test = Test.readXmlTest(parser);

        if (test.isValid()) {
            return test;
        } else {
            Log.w(TAG, "Can't load this test");
            return null;
        }
    }

    /**
     * Read a text in xml file.
     * @param parser the parser of the file
     * @return the value read
     * @throws IOException if while loading the file an error occur
     * @throws XmlPullParserException if while loading the file an exception occur
     */
    public static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, null);
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, XmlPullParser.NO_NAMESPACE, null);
        return result;
    }

    /**
     * Read a int in xml file with a default value of -1
     * @param parser the parser of the file
     * @return the value read
     * @throws IOException if while loading the file an error occur
     * @throws XmlPullParserException if while loading the file an exception occur
     * @see #readInt(XmlPullParser, int)
     */
    public static int readInt(XmlPullParser parser) throws IOException, XmlPullParserException {
        return readInt(parser, -1);
    }

    /**
     * Read a int in xml file with a default value.
     * @param parser       the parser of the file
     * @param defaultValue the default value
     * @return the value read
     * @throws IOException if while loading the file an error occur
     * @throws XmlPullParserException if while loading the file an exception occur
     * @see #readInt(XmlPullParser)
     */
    public static int readInt(XmlPullParser parser, int defaultValue) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, null);
        int result = defaultValue;

        if (parser.next() == XmlPullParser.TEXT) {
            try {
                result = Integer.parseInt(parser.getText());
            } catch (NumberFormatException ignored) {
                Log.w(TAG, "Read int not a number !");
            }
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, XmlPullParser.NO_NAMESPACE, null);
        return result;
    }

    /**
     * Read a float in xml file with a default value of -1
     * @param parser the parser of the file
     * @return the value read
     * @throws IOException if while loading the file an error occur
     * @throws XmlPullParserException if while loading the file an exception occur
     * @see #readFloat(XmlPullParser, float)
     */
    public static float readFloat(XmlPullParser parser) throws IOException, XmlPullParserException {
        return readFloat(parser, -1);
    }

    /**
     * Read a float in xml file with a default value.
     * @param parser       the parser of the file
     * @param defaultValue the default value
     * @return the value read
     * @throws IOException if while loading the file an error occur
     * @throws XmlPullParserException if while loading the file an exception occur
     * @see #readFloat(XmlPullParser)
     */
    public static float readFloat(XmlPullParser parser, float defaultValue) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, null);
        float result = defaultValue;

        if (parser.next() == XmlPullParser.TEXT) {
            try {
                result = Float.parseFloat(parser.getText());
            } catch (NumberFormatException ignored) {
                Log.w(TAG, "Read int not a number !");
            }
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, XmlPullParser.NO_NAMESPACE, null);
        return result;
    }

    /**
     * Read a boolean
     * @param parser the parser of the file
     * @param defaultValue the default value to return
     * @return the value read
     * @throws IOException if while loading the file an error occur
     * @throws XmlPullParserException if while loading the file an exception occur
     */
    public static boolean readBoolean(XmlPullParser parser, boolean defaultValue) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, null);
        boolean result = defaultValue;

        if (parser.next() == XmlPullParser.TEXT) {
            String parserText = parser.getText();
            if (parserText.equalsIgnoreCase("true") || parserText.equalsIgnoreCase("1")) {
                result = true;
            } else if (parserText.equalsIgnoreCase("false") || parserText.equalsIgnoreCase("0")) {
                result = false;
            } else {
                Log.w(TAG, "Read boolean not valid !");
            }
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, XmlPullParser.NO_NAMESPACE, null);
        return result;
    }

    /**
     * Read a date
     * @param parser the parser of the file
     * @return the value read
     * @throws IOException if while loading the file an error occur
     * @throws XmlPullParserException if while loading the file an exception occur
     */
    public static Date readDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        Date result = null;

        if (parser.next() == XmlPullParser.TEXT) {
            try {
                result =  new Date(Long.parseLong(parser.getText()));
            } catch (NumberFormatException ignored) {
                Log.w(TAG, "Read date not valid !");
            }
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, XmlPullParser.NO_NAMESPACE, null);
        return result;
    }

    /**
     * Skip a sub tree of the xml file
     * @param parser the parser of the file
     * @throws IOException if while loading the file an error occur
     * @throws XmlPullParserException if while loading the file an exception occur
     */
    public static void skip(XmlPullParser parser) throws IOException, XmlPullParserException {
        Log.w(TAG, parser.getName() + " is not handle");
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
                    name = readText(parser);
                    break;

                case FileManager.TAG_DESCRIPTION:
                    description = readText(parser);
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

        return Column.readColumnXml(parser2, 0);
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

    @Deprecated
    public static String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, FileManager.TAG_NAME);

        return readText(parser);
    }

    @Deprecated
    public static String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, FileManager.TAG_DESCRIPTION);

        return readText(parser);
    }

}
