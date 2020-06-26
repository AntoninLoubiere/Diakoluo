package fr.pyjacpp.diakoluo.save_test;

import java.io.IOException;
import java.io.OutputStream;

import fr.pyjacpp.diakoluo.tests.Column;
import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.tests.data.DataCellString;

class XmlSaver {
    private static final String TEST_START = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    static void save(OutputStream fileOutputStream, Test test) throws IOException {
        fileOutputStream.write(TEST_START.getBytes());

        fileOutputStream.write(getStartBeacon(FileManager.TAG_TEST).getBytes());

        fileOutputStream.write(getCoupleBeacon(FileManager.TAG_NAME, test.getName()).getBytes());

        fileOutputStream.write(getCoupleBeacon(FileManager.TAG_DESCRIPTION,
                test.getDescription()).getBytes());

        fileOutputStream.write(getCoupleBeacon(FileManager.TAG_CREATED_DATE,
                String.valueOf(test.getCreatedDate().getTime())).getBytes());

        fileOutputStream.write(getCoupleBeacon(FileManager.TAG_LAST_MODIFICATION,
                String.valueOf(test.getLastModificationDate().getTime())).getBytes());

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
        fileOutputStream.write(getStartBeacon(FileManager.TAG_COLUMN).getBytes());

        fileOutputStream.write(getCoupleBeacon(FileManager.TAG_NAME, column.getName()).getBytes());

        fileOutputStream.write(getCoupleBeacon(FileManager.TAG_DESCRIPTION,
                column.getDescription()).getBytes());

        fileOutputStream.write(getCoupleBeacon(FileManager.TAG_INPUT_TYPE,
                column.getInputType().name()).getBytes());

        switch (column.getInputType()) {
            case String:
                fileOutputStream.write(getCoupleBeacon(FileManager.TAG_DEFAULT_VALUE,
                        (String) column.getDefaultValue()).getBytes());
                break;

            default:
                throw new IllegalStateException("Unexpected value " + column.getInputType());
        }

        fileOutputStream.write(getEndBeacon(FileManager.TAG_COLUMN).getBytes());
    }

    private static void writeRow(OutputStream fileOutputStream, Test test, DataRow dataRow) throws IOException {
        fileOutputStream.write(getStartBeacon(FileManager.TAG_ROW).getBytes());

        for (Column column : test.getListColumn()) {
            switch (column.getInputType()) {
                case String:
                    DataCellString dataCell = (DataCellString) dataRow.getListCells().get(column);
                    if (dataCell != null) {
                        fileOutputStream.write(getCoupleBeacon(FileManager.TAG_CELL,
                                dataCell.getValue()).getBytes());
                        break;
                    }

                default:
                    throw new IllegalStateException("Unexpected value " + column.getInputType());
            }
        }

        fileOutputStream.write(getEndBeacon(FileManager.TAG_ROW).getBytes());
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
