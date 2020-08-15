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

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.Utils;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;
import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.tests.column.Column;
import fr.pyjacpp.diakoluo.tests.data.DataCell;

public class CsvLoader {

    /**
     * Load a test from a inputStream of a csv file. Return the test loaded or null if the test
     * can't be loaded.
     * @param context the context of the application (or the activity)
     * @param inputStream the input stream of the csv file to load
     * @param separator the separator used in the csv file
     * @param loadColumnName if the first line is the name of columns
     * @param loadColumnType if the second or the first line is the type of column
     * @param testName the name of the test
     * @return the test loaded or null if can't load
     * @throws IOException if while reading the file an exception occur
     */
    @Nullable
    public static Test load(Context context,  FileInputStream inputStream, char separator, boolean loadColumnName, boolean loadColumnType, String testName) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        CsvContext csvContext = new CsvContext(context, bufferedReader, separator, CsvSaver.DEFAULT_LINE_SEPARATOR.charAt(0));
        Test currentTest = new Test();
        currentTest.setName(testName);
        currentTest.setDescription("");

        int numberColumns = -1;

        ArrayList<Column> columns = new ArrayList<>();
        ArrayList<DataRow> rows = new ArrayList<>();

        ArrayList<String> columnNames = null;
        ArrayList<String> columnType = null;
        ArrayList<String> line;

        if (loadColumnName) {
            line = readLine(csvContext);
            numberColumns = line.size();
            if (numberColumns > 0) {
                columnNames = line;
            } else {
                throw new CsvException("Line missing !");
            }
        }

        if (loadColumnType) {
            line = readLine(csvContext);
            int currentSize = line.size();
            if ((numberColumns < 0 && currentSize > 0) || currentSize == numberColumns) {
                columnType = line;
                if (numberColumns < 0) {
                    numberColumns = currentSize;
                }
            } else {
                throw new CsvException("Line missing !");
            }
        }

        // create columns
        if (loadColumnName || loadColumnType)
            createColumns(csvContext, loadColumnName, loadColumnType, numberColumns, columns, columnNames, columnType);

        // load data
        int currentSize;
        while ((currentSize = (line = readLine(csvContext)).size()) > 0) {
            if (numberColumns < 0 || currentSize == numberColumns) {
                if (numberColumns < 0) {
                    numberColumns = currentSize;
                    createColumns(csvContext, false, false,
                            numberColumns, columns, null, null);
                }

                DataRow row = new DataRow();
                HashMap<Column, DataCell> listCells = row.getListCells();

                for (int i = 0; i < numberColumns; i++) {
                    Column currentColumn = columns.get(i);
                    DataCell cell = DataCell.newCellWithDefaultValue(currentColumn);
                    cell.setValueFromCsv(line.get(i), currentColumn);
                    listCells.put(currentColumn, cell);
                }
                rows.add(row);
            } else {
                throw new CsvException("Csv format error");
            }
        }

        currentTest.setListColumn(columns);
        currentTest.setListRow(rows);
        Date now = new Date();
        currentTest.setCreatedDate(now);
        currentTest.setLastModificationDate(now);

        if (currentTest.isValid()) {
            return currentTest;
        } else {
            return null;
        }
    }

    /**
     * Create columns to load the test.
     * @param csvContext the context of the loader
     * @param loadColumnName if the first line is the name of columns
     * @param loadColumnType if the second or the first line is the type of column
     * @param numberColumns the number of columns to create
     * @param columns the list where to add columns
     * @param columnNames the list of names of columns
     * @param columnType the list of columns types
     */
    private static void createColumns(CsvContext csvContext, boolean loadColumnName, boolean loadColumnType, int numberColumns, ArrayList<Column> columns, ArrayList<String> columnNames, ArrayList<String> columnType) {
        for (int i = 0; i < numberColumns; i++) {
            Column column;
            String columnName = loadColumnName ?
                    columnNames.get(i) :
                    csvContext.context.getString(R.string.default_column_name, i + 1);

            if (loadColumnType) {
                ColumnInputType cit = ColumnInputType.get(columnType.get(i));
                column = Column.newColumn(cit == null ? ColumnInputType.DEFAULT_INPUT_TYPE : cit, columnName, "");
            } else {
                column = Column.newColumn(ColumnInputType.DEFAULT_INPUT_TYPE, columnName, "");
            }

            columns.add(column);
        }
    }

    /**
     * Read a line of csv
     * @param context the csv context
     * @return the list of cell in the csv document
     * @throws IOException if an exception occur while reading the file
     */
    @VisibleForTesting()
    public static ArrayList<String> readLine(CsvContext context) throws IOException {
        ArrayList<String> list = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();

        int i;
        char c;
        char previousC = ' ';

        boolean valueStarted = false;
        boolean quoted = false;
        boolean hasBeenQuoted = false;


        while ((i = context.bufferedReader.read()) != -1) {
            c = (char) i;
            if (quoted) {
                if (previousC == '"') {
                    if (c == '"') {
                        currentValue.append('"');
                        previousC = ' ';
                    } else if (c == context.separator) {
                        list.add(currentValue.toString());
                        currentValue = new StringBuilder();
                        valueStarted = false;
                        previousC = ' ';
                        quoted = false;
                    } else if (c == context.lineSeparator) {
                        break;
                    } else {
                        // not often because need "test" ,
                        hasBeenQuoted = true;
                        quoted = false;
                        previousC = ' ';
                        currentValue.append(c);
                    }
                } else if (c == '"') {
                    previousC = c;
                } else {
                    currentValue.append(c);
                }
            } else {
                if (valueStarted) {
                    if (c == context.separator) {
                        if (hasBeenQuoted) {
                            list.add(currentValue.toString());
                            hasBeenQuoted = false;
                        } else {
                            list.add(Utils.removeUselessSpacesEnd(currentValue));
                        }
                        currentValue = new StringBuilder();
                        valueStarted = false;
                    } else if (c == context.lineSeparator) {
                        break;
                    } else {
                        currentValue.append(c);
                    }
                } else {
                    if (c == '"') {
                        quoted = true;
                        valueStarted = true;
                    } else if (c == context.separator) {
                        list.add("");
                    } else if (c == context.lineSeparator && list.size() > 0) {
                        break;
                    } else if (c != ' ' && c != '\t') {
                        valueStarted = true;
                        currentValue.append(c);
                    }
                }
            }
        }

        if (valueStarted) {
            if (hasBeenQuoted) {
                list.add(currentValue.toString());
            } else {
                list.add(Utils.removeUselessSpacesEnd(currentValue));
            }
        }

        return list;
    }

    /**
     * A csv context that hold the context of the application the buffered reader, the separator
     * and the line separator.
     */
    public static class CsvContext {
        private final Context context;
        private final BufferedReader bufferedReader;
        private final char separator;
        private final char lineSeparator;

        /**
         * The default constructor
         * @param context the context of the application
         * @param bufferedReader the buffered reader of the csv file
         * @param separator the separator of the csv file
         * @param lineSeparator the line separator of the csv file
         */
        CsvContext(Context context, BufferedReader bufferedReader, char separator, char lineSeparator) {
            this.context = context;
            this.bufferedReader = bufferedReader;
            this.separator = separator;
            this.lineSeparator = lineSeparator;
        }
    }

    /**
     * A csv exception.
     */
    public static class CsvException extends IOException {
        CsvException(String csv_format_error) {
            super(csv_format_error);
        }
    }
}
