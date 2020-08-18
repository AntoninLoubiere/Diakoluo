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

/**
 * A class that save test in xml file.
 */
public class XmlSaver {
    private static final String TEST_START = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    /**
     * Save a test in a xml file.
     * @param fileOutputStream the output stream of the file
     * @param test the test to save
     * @throws IOException if an exception occur while writing the file
     */
    public static void save(OutputStream fileOutputStream, Test test) throws IOException {
        save(fileOutputStream, test, true);
    }

    /**
     * Save a test in a xml file.
     * @param fileOutputStream the output stream of the file
     * @param test the test to save
     * @param saveNumberTestDone if numberOfTestDone need to be saved
     * @throws IOException if an exception occur while writing the file
     */
    static void save(OutputStream fileOutputStream, Test test, boolean saveNumberTestDone) throws IOException {
        writeSafeData(fileOutputStream, TEST_START);
        test.writeXml(fileOutputStream, saveNumberTestDone);
    }

    /**
     * Write a data between beacon.
     * @param outputStream the output stream of the file
     * @param beacons the beacons that hold the data
     * @param data the data to save
     * @throws IOException if an exception occur while writing the file
     */
    public static void writeData(OutputStream outputStream, String beacons, String data) throws IOException {
        outputStream.write(getCoupleBeacon(beacons, unSafeToSafe(data)).getBytes());
    }

    /**
     * Write a data between beacon.
     * @param outputStream the output stream of the file
     * @param beacons the beacons that hold the data
     * @param data the data to save
     * @throws IOException if an exception occur while writing the file
     */
    public static void writeData(OutputStream outputStream, String beacons, Date data) throws IOException {
        writeData(outputStream, beacons, String.valueOf(data.getTime()));
    }

    /**
     * Write a data between beacon.
     * @param outputStream the output stream of the file
     * @param beacons the beacons that hold the data
     * @param data the data to save
     * @throws IOException if an exception occur while writing the file
     */
    public static void writeData(OutputStream outputStream, String beacons, int data) throws IOException {
        writeData(outputStream, beacons, String.valueOf(data));
    }

    /**
     * Write a data between beacon.
     * @param outputStream the output stream of the file
     * @param beacons the beacons that hold the data
     * @param data the data to save
     * @throws IOException if an exception occur while writing the file
     */
    public static void writeData(OutputStream outputStream, String beacons, float data) throws IOException {
        writeData(outputStream, beacons, String.valueOf(data));
    }

    /**
     * Write a data between beacon.
     * @param outputStream the output stream of the file
     * @param beacons the beacons that hold the data
     * @param data the data to save
     * @throws IOException if an exception occur while writing the file
     */
    public static void writeData(OutputStream outputStream, String beacons, boolean data) throws IOException {
        writeData(outputStream, beacons, data ? "true" : "false");
    }

    /**
     * Write a raw data but replace not allowed character.
     * @param outputStream the output stream of the file
     * @param data the data to save
     * @throws IOException if an exception occur while writing the file
     */
    public static void writeNotSafeData(OutputStream outputStream, String data) throws IOException {
        outputStream.write(unSafeToSafe(data).getBytes());
    }

    /**
     * Write a raw data but replace not allowed character.
     * @param outputStream the output stream of the file
     * @param data the data to save
     * @throws IOException if an exception occur while writing the file
     */
    public static void writeNotSafeData(OutputStream outputStream, int data) throws IOException {
       writeNotSafeData(outputStream, String.valueOf(data));
    }

    /**
     * Write a safe data like a beacon start...
     * @param outputStream the output stream of the file
     * @param data the data to save
     * @throws IOException if an exception occur while writing the file
     */
    private static void writeSafeData(OutputStream outputStream, String data) throws IOException {
        outputStream.write(data.getBytes());
    }

    /**
     * Write a start of beacon.
     * @param outputStream the output stream of the file
     * @param beacon start beacon to write
     * @throws IOException if an exception occur while writing the file
     */
    public static void writeStartBeacon(OutputStream outputStream, String beacon) throws IOException {
        writeSafeData(outputStream, "<" + beacon + ">");
    }

    /**
     * Write a start of beacon with an attribute.
     * @param outputStream the output stream of the file
     * @param beacon start beacon to write
     * @param attributeName the name of the attribute
     * @param attributeValue the value of the attribute
     * @throws IOException if an exception occur while writing the file
     */
    public static void writeStartBeacon(OutputStream outputStream, String beacon,
                                        String attributeName, String attributeValue)
            throws IOException {
        writeSafeData(outputStream, "<" + beacon + " " + unSafeToSafe(attributeName) + "=\"" + unSafeToSafe(attributeValue) + "\">");
    }

    /**
     * Write a start of beacon with an attribute.
     * @param outputStream the output stream of the file
     * @param beacon start beacon to write
     * @param attributeName the name of the attribute
     * @param attributeValue the value of the attribute
     * @throws IOException if an exception occur while writing the file
     */
    public static void writeStartBeacon(OutputStream outputStream, String beacon,
                                        String attributeName, int attributeValue)
            throws IOException {
        writeStartBeacon(outputStream, beacon, attributeName, String.valueOf(attributeValue));
    }

    /**
     * Write a end of beacon.
     * @param outputStream the output stream of the file
     * @param beacon end beacon to write
     * @throws IOException if an exception occur while writing the file
     */
    public static void writeEndBeacon(OutputStream outputStream, String beacon) throws IOException {
        writeSafeData(outputStream, "</" + beacon + ">");
    }

    /**
     * Replace not allowed characters.
     * @param data the data to process
     * @return the processed data
     */
    private static String unSafeToSafe(String data) {
        return data.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    /**
     * Get the start of a beacon.
     * @param beaconName the name of the beacon
     * @return the xml beacon start
     */
    private static String getStartBeacon(String beaconName) {
        return "<" + beaconName + ">";
    }

    /**
     * Get the end of a beacon.
     * @param beaconName the name of the beacon
     * @return the xml beacon end
     */
    private static String getEndBeacon(String beaconName) {
        return "</" + beaconName + ">";
    }

    /**
     * Get a data between two beacons.
     * The data must be safe !
     * @param beaconName the name of beacons
     * @return the xml beacon with the data
     */
    private static String getCoupleBeacon(String beaconName, String data) {
        return getStartBeacon(beaconName) + data + getEndBeacon(beaconName);
    }
}
