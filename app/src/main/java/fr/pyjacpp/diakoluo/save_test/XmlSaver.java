package fr.pyjacpp.diakoluo.save_test;

import java.io.IOException;
import java.io.OutputStream;

import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.tests.column.Column;

public class XmlSaver {
    private static final String TEST_START = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    static void save(OutputStream fileOutputStream, Test test) throws IOException {
        save(fileOutputStream, test, true);
    }
    static void save(OutputStream fileOutputStream, Test test, boolean writePrivateStats) throws IOException {
        fileOutputStream.write(TEST_START.getBytes());

        fileOutputStream.write(getStartBalise(FileManager.TAG_TEST).getBytes());

        fileOutputStream.write(getCoupleBalise(FileManager.TAG_NAME, test.getName()).getBytes());

        fileOutputStream.write(getCoupleBalise(FileManager.TAG_DESCRIPTION,
                test.getDescription()).getBytes());

        fileOutputStream.write(getCoupleBalise(FileManager.TAG_CREATED_DATE,
                String.valueOf(test.getCreatedDate().getTime())).getBytes());

        fileOutputStream.write(getCoupleBalise(FileManager.TAG_LAST_MODIFICATION,
                String.valueOf(test.getLastModificationDate().getTime())).getBytes());

        if (writePrivateStats)
            fileOutputStream.write(getCoupleBalise(FileManager.TAG_NUMBER_TEST_DID,
                    String.valueOf(test.getNumberTestDid())).getBytes());

        fileOutputStream.write(getStartBalise(FileManager.TAG_COLUMNS).getBytes());

        for (Column column : test.getListColumn()) {
            writeColumn(fileOutputStream, column);
        }

        fileOutputStream.write(getEndBalise(FileManager.TAG_COLUMNS).getBytes());

        fileOutputStream.write(getStartBalise(FileManager.TAG_ROWS).getBytes());

        for (DataRow row : test.getListRow()) {
            writeRow(fileOutputStream, test, row);
        }

        fileOutputStream.write(getEndBalise(FileManager.TAG_ROWS).getBytes());

        fileOutputStream.write(getEndBalise(FileManager.TAG_TEST).getBytes());

    }

    private static void writeColumn(OutputStream fileOutputStream, Column column) throws IOException {
        fileOutputStream.write(getStartBalise(FileManager.TAG_COLUMN).getBytes());

        fileOutputStream.write(getCoupleBalise(FileManager.TAG_NAME, column.getName()).getBytes());

        fileOutputStream.write(getCoupleBalise(FileManager.TAG_DESCRIPTION,
                column.getDescription()).getBytes());

        fileOutputStream.write(getCoupleBalise(FileManager.TAG_INPUT_TYPE,
                column.getInputType().name()).getBytes());

        column.writeXmlHeader(fileOutputStream);

        fileOutputStream.write(getEndBalise(FileManager.TAG_COLUMN).getBytes());
    }

    private static void writeRow(OutputStream fileOutputStream, Test test, DataRow dataRow) throws IOException {
        fileOutputStream.write(getStartBalise(FileManager.TAG_ROW).getBytes());

        dataRow.writeXml(fileOutputStream, test);

        fileOutputStream.write(getEndBalise(FileManager.TAG_ROW).getBytes());
    }

    private static String getStartBalise(String baliseName) {
        return "<" + baliseName + ">";
    }

    private static String getEndBalise(String baliseName) {
        return "</" + baliseName + ">";
    }

    public static String getCoupleBalise(String baliseName, String data) {
        return getStartBalise(baliseName) + data + getEndBalise(baliseName);
    }
}
