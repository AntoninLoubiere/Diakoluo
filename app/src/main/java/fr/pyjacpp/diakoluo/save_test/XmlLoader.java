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
import java.util.Date;

import fr.pyjacpp.diakoluo.tests.ColumnInputType;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.tests.column.Column;

public class XmlLoader {

    public static final String TAG = "XmlLoader";

    public static Test load(InputStream fileInputStream) throws IOException, XmlPullParserException {
            // configure parser
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(fileInputStream, null);
        parser.nextTag();

        return readFeed(parser);
    }

    private static Test readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {
        // start of the document
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, FileManager.TAG_TEST);

        Test test = Test.readXmlTest(parser);

        if (test.isValid()) {
            return test;
        } else {
            Log.w(TAG, "Can't load this test");
            return null;
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

    public static int readInt(XmlPullParser parser) throws IOException, XmlPullParserException {
        int result = -1;

        if (parser.next() == XmlPullParser.TEXT) {
            try {
                result = Integer.parseInt(parser.getText());
            } catch (NumberFormatException ignored) {
                Log.w(TAG, "Read int not a number !");
            }
            parser.nextTag();
        }
        return result;
    }

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
        return result;
    }

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
