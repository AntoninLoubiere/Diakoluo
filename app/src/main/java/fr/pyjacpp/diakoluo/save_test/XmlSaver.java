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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import fr.pyjacpp.diakoluo.tests.Test;

public class XmlSaver {
    private static final String TEST_START = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    public static void save(OutputStream fileOutputStream, Test test) throws IOException {
        save(fileOutputStream, test, true);
    }
    static void save(OutputStream fileOutputStream, Test test, boolean saveNumberTestDone) throws IOException {
        writeSafeData(fileOutputStream, TEST_START);
        test.writeXml(fileOutputStream, saveNumberTestDone);
    }

    public static void writeData(OutputStream outputStream, String beacons, String data) throws IOException {
        outputStream.write(getCoupleBeacon(beacons, unSafeToSafe(data)).getBytes());
    }

    public static void writeData(OutputStream outputStream, String beacons, Date date) throws IOException {
        writeData(outputStream, beacons, String.valueOf(date.getTime()));
    }

    public static void writeData(OutputStream outputStream, String beacons, int nb) throws IOException {
        writeData(outputStream, beacons, String.valueOf(nb));
    }

    public static void writeNotSafeData(OutputStream outputStream, String data) throws IOException {
        outputStream.write(unSafeToSafe(data).getBytes());
    }

    public static void writeNotSafeData(OutputStream outputStream, int data) throws IOException {
       writeNotSafeData(outputStream, String.valueOf(data));
    }

    private static void writeSafeData(OutputStream outputStream, String data) throws IOException {
        outputStream.write(data.getBytes());
    }

    public static void writeStartBeacon(OutputStream outputStream, String beacon) throws IOException {
        writeSafeData(outputStream, "<" + beacon + ">");
    }

    public static void writeStartBeacon(OutputStream outputStream, String beacon,
                                        String attributeName, String attributeValue)
            throws IOException {
        writeSafeData(outputStream, "<" + beacon + " " + unSafeToSafe(attributeName) + "=\"" + unSafeToSafe(attributeValue) + "\">");
    }

    public static void writeStartBeacon(OutputStream outputStream, String beacon,
                                        String attributeName, int attributeValue)
            throws IOException {
        writeStartBeacon(outputStream, beacon, attributeName, String.valueOf(attributeValue));
    }

    public static void writeEndBeacon(OutputStream outputStream, String beacon) throws IOException {
        writeSafeData(outputStream, "</" + beacon + ">");
    }

    private static String unSafeToSafe(String data) {
        return data.replace("&", "&amp;")
                .replace("<", "lt")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    private static String getStartBeacon(String beaconName) {
        return "<" + beaconName + ">";
    }

    private static String getEndBeacon(String beaconName) {
        return "</" + beaconName + ">";
    }

    private static String getCoupleBeacon(String beaconName, String data) {
        return getStartBeacon(beaconName) + data + getEndBeacon(beaconName);
    }
}
