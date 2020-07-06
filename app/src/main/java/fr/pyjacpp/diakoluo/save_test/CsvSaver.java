package fr.pyjacpp.diakoluo.save_test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.tests.column.Column;

public class CsvSaver {

    public static final String DEFAULT_LINE_SEPARATOR = "\n"; // must be only one char
    public static final String[] SEPARATORS = {",", ";"}; // must be only one char

    static void save(FileOutputStream fileOutputStream, Test test, boolean columnHeader, boolean columnTypeHeader, String lineSeparator, String separator) throws IOException {
        ArrayList<Column> listColumn = test.getListColumn();
        ArrayList<DataRow> listRow = test.getListRow();

        CsvContext csvContext = new CsvContext(fileOutputStream, separator, lineSeparator);

        if (columnHeader) {
            for (int i = 0, listColumnSize = listColumn.size(); i < listColumnSize; i++) {
                Column column = listColumn.get(i);
                writeCell(csvContext, i, column.getName());
            }
            fileOutputStream.write(csvContext.lineSeparator.getBytes());
        }

        if (columnTypeHeader) {
            for (int i = 0, listColumnSize = listColumn.size(); i < listColumnSize; i++) {
                Column column = listColumn.get(i);
                writeCell(csvContext, i, column.getInputType().name());
            }
            fileOutputStream.write(csvContext.lineSeparator.getBytes());
        }

        for (int i = 0, listRowSize = listRow.size(); i < listRowSize; i++) {
            DataRow row = listRow.get(i);
            row.writeCsv(csvContext, test);
            fileOutputStream.write(csvContext.lineSeparator.getBytes());
        }
    }

    public static void writeCell(CsvContext csvContext, int index, String value) throws IOException {
        if (index > 0)
            csvContext.fileOutputStream.write(csvContext.separator.getBytes());
        boolean addQuotes = false;

        if (value.equals("")) {
            addQuotes = true;
        }

        if (value.contains(csvContext.separator) || value.contains("\n") || value.startsWith(" ") || value.endsWith(" ")) {
            addQuotes = true;
        }

        if (value.contains("\"")) {
            value = value.replace("\"", "\"\"");
        }

        if (addQuotes) {
            value = "\"" + value + "\"";
        }

        csvContext.fileOutputStream.write(value.getBytes());

    }

    public static class CsvContext {
        private FileOutputStream fileOutputStream;
        private String separator;
        private String lineSeparator;

        CsvContext(FileOutputStream fileOutputStream, String separator, String lineSeparator) {
            this.fileOutputStream = fileOutputStream;
            this.separator = separator;
            this.lineSeparator = lineSeparator;
        }
    }
}
