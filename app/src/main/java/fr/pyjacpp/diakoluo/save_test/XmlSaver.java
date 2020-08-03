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

import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.tests.column.Column;

public class XmlSaver {
    private static final String TEST_START = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    public static void save(OutputStream fileOutputStream, Test test) throws IOException {
        save(fileOutputStream, test, true);
    }
    static void save(OutputStream fileOutputStream, Test test, boolean saveNumberTestDone) throws IOException {
        fileOutputStream.write(TEST_START.getBytes());

        fileOutputStream.write(getStartBeacon(FileManager.TAG_TEST).getBytes());

        fileOutputStream.write(getCoupleBeacon(FileManager.TAG_NAME, test.getName()).getBytes());

        fileOutputStream.write(getCoupleBeacon(FileManager.TAG_DESCRIPTION,
                test.getDescription()).getBytes());

        fileOutputStream.write(getCoupleBeacon(FileManager.TAG_CREATED_DATE,
                String.valueOf(test.getCreatedDate().getTime())).getBytes());

        fileOutputStream.write(getCoupleBeacon(FileManager.TAG_LAST_MODIFICATION,
                String.valueOf(test.getLastModificationDate().getTime())).getBytes());

        if (saveNumberTestDone)
            fileOutputStream.write(getCoupleBeacon(FileManager.TAG_NUMBER_TEST_DID,
                    String.valueOf(test.getNumberTestDid())).getBytes());

        fileOutputStream.write(getStartBeacon(FileManager.TAG_COLUMNS).getBytes());

        for (Column column : test.getListColumn()) {
            writeColumn(fileOutputStream, column);
        }

        fileOutputStream.write(getEndBeacon(FileManager.TAG_COLUMNS).getBytes());

        fileOutputStream.write(getStartBeacon(FileManager.TAG_ROWS).getBytes());

        for (DataRow row : test.getListRow()) {
            writeRow(fileOutputStream, test, row);
        }

        fileOutputStream.write(getEndBeacon(FileManager.TAG_ROWS).getBytes());

        fileOutputStream.write(getEndBeacon(FileManager.TAG_TEST).getBytes());

    }

    private static void writeColumn(OutputStream fileOutputStream, Column column) throws IOException {
        fileOutputStream.write(getStartBeacon(FileManager.TAG_COLUMN, FileManager.ATTRIBUTE_INPUT_TYPE, column.getInputType().name()).getBytes());

        fileOutputStream.write(getCoupleBeacon(FileManager.TAG_NAME, column.getName()).getBytes());

        fileOutputStream.write(getCoupleBeacon(FileManager.TAG_DESCRIPTION,
                column.getDescription()).getBytes());

        column.writeXmlHeader(fileOutputStream);

        fileOutputStream.write(getEndBeacon(FileManager.TAG_COLUMN).getBytes());
    }

    private static void writeRow(OutputStream fileOutputStream, Test test, DataRow dataRow) throws IOException {
        fileOutputStream.write(getStartBeacon(FileManager.TAG_ROW).getBytes());

        dataRow.writeXml(fileOutputStream, test);

        fileOutputStream.write(getEndBeacon(FileManager.TAG_ROW).getBytes());
    }

    public static String getStartBeacon(String beaconName) {
        return "<" + beaconName + ">";
    }

    public static String getStartBeacon(String beaconName, String attributeName, String attributeValue) {
        return "<" + beaconName + " " + attributeName + "=\"" + attributeValue + "\">";
    }

    public static String getEndBeacon(String beaconName) {
        return "</" + beaconName + ">";
    }

    public static String getCoupleBeacon(String beaconName, String data) {
        return getStartBeacon(beaconName) + data + getEndBeacon(beaconName);
    }
}
