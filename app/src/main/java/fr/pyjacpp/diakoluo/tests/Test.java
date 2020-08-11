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

package fr.pyjacpp.diakoluo.tests;

import android.content.Context;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

import fr.pyjacpp.diakoluo.save_test.FileManager;
import fr.pyjacpp.diakoluo.save_test.XmlSaver;
import fr.pyjacpp.diakoluo.test_tests.ColumnToShow;
import fr.pyjacpp.diakoluo.tests.column.Column;
import fr.pyjacpp.diakoluo.tests.data.DataCell;

/**
 * A test class that save data in a table.
 * @see Column
 * @see DataRow
 * @see DataCell
 */
public class Test {
    private String name;
    private String description;

    private String filename;
    private Date createdDate;
    private Date lastModification;

    private int numberTestDid;

    private ArrayList<Column> listColumn;
    private ArrayList<DataRow> listRow;

    /**
     * Default constructor.
     * Create a non valid test
     */
    public Test() {
        // new test
        this.name = null;
        this.description = null;

        createdDate = null;
        lastModification = null;
        numberTestDid = 0;

        listColumn = null;
        listRow = null;
    }

    /**
     * Copy a test.
     * @param test the base test
     */
    public Test(Test test) {
        name = test.name;
        description = test.description;
        createdDate = new Date(test.createdDate.getTime());
        lastModification = new Date(test.lastModification.getTime());
        numberTestDid = test.numberTestDid;
        listColumn = new ArrayList<>();
        for (Column column : test.listColumn) {
            listColumn.add(column.copyColumn());
        }
        listRow = new ArrayList<>();
        for (DataRow dataRow : test.listRow) {
            listRow.add(new DataRow(dataRow, listColumn, test.listColumn));
        }
        filename = test.filename;
    }

    /**
     * Create a valid test with name and description.
     * @param name the name of the test
     * @param description the description of the test
     */
    public Test(String name, String description) {
        init(name, description);
    }


    /**
     * Create a valid test from all fields.
     * @param name the name of the test
     * @param description the description of the test
     * @param createdDate the date of creation of the test
     * @param lastModification the date of the last modification
     * @param numberTestDid the number of test did
     * @param listColumn the list of column
     * @param listRow the list of row
     */
    public Test(String name, String description, Date createdDate, Date lastModification, int numberTestDid,
                ArrayList<Column> listColumn, ArrayList<DataRow> listRow) {
        // previous test
        this.name = name;
        this.description = description;

        this.createdDate = createdDate;
        this.lastModification = lastModification;
        this.numberTestDid = numberTestDid;

        this.listColumn = listColumn;
        this.listRow = listRow;
    }

    /**
     * Initialize for a valid test.
     * @param name the name of the test
     * @param description the description of the test
     */
    private void init(String name, String description) {
        // new test
        this.name = name;
        this.description = description;

        createdDate = new Date();
        lastModification = new Date();
        numberTestDid = 0;

        listColumn = new ArrayList<>();
        listRow = new ArrayList<>();
    }

    /**
     * Get the name of the test.
     * @return the name of the test
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the test.
     * @param name the name of the test
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the description of the test.
     * @return the description of the test
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the test.
     * @param description the description of the test
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the date of creation of the test.
     * @return the date of creation of the test
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * Set the date of creation of the test.
     * @param createdDate the date of creation of the test
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Get the date of last modification.
     * @return the date of the last modification
     */
    public Date getLastModificationDate() {
        return lastModification;
    }

    /**
     * Set the last modification date.
     * @param lastModification the last modification date
     * @see #registerModificationDate()
     */
    public void setLastModificationDate(Date lastModification) {
        this.lastModification = lastModification;
    }

    /**
     * Set the last modification date to now.
     * @see #setLastModificationDate(Date)
     */
    public void registerModificationDate() {
        lastModification = new Date();
    }

    /**
     * Get the number of test did.
     * @return The number of test did
     */
    public int getNumberTestDid() {
        return numberTestDid;
    }

    /**
     * Set the number of test did.
     * @param numberTestDid the number of test did
     */
    public void setNumberTestDid(int numberTestDid) {
        this.numberTestDid = numberTestDid;
    }

    /**
     * Add 1 test did
     */
    public void addNumberTestDid() {
        numberTestDid++;
    }

    /**
     * Get the list of column.
     * @return the list of column
     */
    public ArrayList<Column> getListColumn() {
        return listColumn;
    }

    /**
     * Set the list of column.
     * @param listColumn the list of column
     */
    public void setListColumn(ArrayList<Column> listColumn) {
        this.listColumn = listColumn;
    }

    /**
     * Get the list of columns that can be asked or show randomly.
     * @return the list of column that can be asked or show randomly
     */
    public ArrayList<Column> getTestListColumn() {
        ArrayList<Column> columns = new ArrayList<>();
        for (Column c : listColumn) {
            if (c.isInSettings(Column.SET_CAN_BE_HIDE) || c.isInSettings(Column.SET_CAN_BE_SHOW)) {
                columns.add(c);
            }
        }
        return columns;
    }

    /**
     * Get the number of column.
     * @return the number of column
     */
    public int getNumberColumn() {
        return listColumn.size();
    }

    /**
     * Add a column to the list.
     * @param column the column to add
     */
    public void addColumn(Column column) {
        listColumn.add(column);
    }

    /**
     * Get the list of row.
     * @return the list of row
     */
    public ArrayList<DataRow> getListRow() {
        return listRow;
    }

    /**
     * Set the list of row.
     * @param listRow the list of row
     */
    public void setListRow(ArrayList<DataRow> listRow) {
        this.listRow = listRow;
    }

    /**
     * Get the number of row.
     * @return the number of row
     */
    public int getNumberRow() {
        return listRow.size();
    }

    /**
     * Add a row.
     * @param row the row to add
     */
    public void addRow(DataRow row) {
        listRow.add(row);
    }

    /**
     * Get if the test can be play.
     * @return if the test can be play
     */
    public boolean canBePlay() {
        ColumnToShow r = getNumberColumnToAsk();
        return r.numberColumnToShowMin <= r.numberColumnToShowMax &&
                r.numberColumnTotal > 1 && r.numberColumnToShowMax > 0 && listRow.size() > 0;
    }

    /**
     * Get if the test is valid.
     * @return if the test is valid
     */
    public boolean isValid() {
        return !(name == null ||
                description == null ||
                createdDate == null ||
                lastModification == null ||
                listColumn == null ||
                listRow == null
        );
    }

    /**
     * Setthe filename of the test.
     * @param filename the filename of the test
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Get the filename of the test.
     * @return the filename of the test
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Get the first cell of the row depending on the position given.
     * @param rowPosition the position of the row
     * @return the first cell of the row or null if the row doesn't have cell
     * @see #getRowFirstCellString(Context, int)
     */
    @Nullable
    public DataCell getRowFirstCell(int rowPosition) {
        if (listColumn.size() > 0) {
            return listRow.get(rowPosition).getListCells().get(listColumn.get(0));
        } else {
            return null;
        }
    }

    /**
     * Get the first cell string value of the row depending on the position given.
     * @param rowPosition the position of the row
     * @param context the context
     * @return string value of the first cell of the row or null if the row doesn't have cell
     * @see #getRowFirstCellString(Context, int)
     */
    public String getRowFirstCellString(Context context, int rowPosition) {
        DataCell firstCell = getRowFirstCell(rowPosition);
        if (firstCell == null) {
            return String.valueOf(rowPosition);
        } else {
            return firstCell.getStringValue(context, listColumn.get(0));
        }
    }

    /**
     * Get the default filename of the file.
     * @return the default filename of the file
     */
    public String getDefaultFilename() {
        return name
                .replace(' ', '_')
                .replace('/', '_')
                .replace('.', '_').toLowerCase();
    }

    /**
     * Reset all stats of the test like number of test did
     */
    public void reset() {
        this.numberTestDid = 0;
    }

    /**
     * Get the number of column to ask (min and max) and the number of column total (without column
     * that can't be show or ask)
     * @return an object that contain the number of column to ask (min and max) and the number of
     * column total
     */
    public ColumnToShow getNumberColumnToAsk() {
        int numberColumnToShowMin = 0;
        int numberColumnToShowMax = 0;
        int numberColumnTotal = 0;
        boolean randomColumns = false;

        for (Column c : listColumn) {
            boolean canHide = c.isInSettings(Column.SET_CAN_BE_HIDE);
            boolean canShow = c.isInSettings(Column.SET_CAN_BE_SHOW);

            if (canHide && canShow) {
                numberColumnToShowMax++;
                numberColumnTotal++;
                randomColumns = true;
            } else if (canHide) {
                numberColumnTotal++;
            } else if (canShow) {
                numberColumnToShowMin++;
                numberColumnToShowMax++;
                numberColumnTotal++;
            }
        }

        if (numberColumnToShowMax >= numberColumnTotal) numberColumnToShowMax = numberColumnTotal - 1;
        if (randomColumns && numberColumnToShowMin <= 0) {
            numberColumnToShowMin = 1;
            if (numberColumnToShowMax <= 0) numberColumnToShowMax = 1;
        }
        return new ColumnToShow(numberColumnToShowMin, numberColumnToShowMax, numberColumnTotal);
    }

    public void writeXml(OutputStream fileOutputStream, boolean saveNumberTestDone) throws IOException {
        XmlSaver.writeStartBeacon(fileOutputStream, FileManager.TAG_TEST);

        XmlSaver.writeData(fileOutputStream, FileManager.TAG_NAME, name);
        XmlSaver.writeData(fileOutputStream, FileManager.TAG_DESCRIPTION, description);
        XmlSaver.writeData(fileOutputStream, FileManager.TAG_CREATED_DATE, createdDate);
        XmlSaver.writeData(fileOutputStream, FileManager.TAG_LAST_MODIFICATION, lastModification);
        XmlSaver.writeData(fileOutputStream, FileManager.TAG_NAME, name);

        if (saveNumberTestDone)
            XmlSaver.writeData(fileOutputStream, FileManager.TAG_NUMBER_TEST_DID, numberTestDid);

        XmlSaver.writeStartBeacon(fileOutputStream, FileManager.TAG_COLUMNS);
        for (Column column : listColumn) {
            column.writeXml(fileOutputStream);
        }
        XmlSaver.writeEndBeacon(fileOutputStream, FileManager.TAG_COLUMNS);

        XmlSaver.writeStartBeacon(fileOutputStream, FileManager.TAG_ROWS);
        for (DataRow row : listRow) {
            row.writeXml(fileOutputStream, this);
        }
        XmlSaver.writeEndBeacon(fileOutputStream, FileManager.TAG_ROWS);

        XmlSaver.writeEndBeacon(fileOutputStream, FileManager.TAG_TEST);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Test) {
            Test t = (Test) obj;
            if (t.name.equals(name) &&
                    t.description.equals(description) &&
                    t.createdDate.equals(createdDate) &&
                    t.lastModification.equals(lastModification) &&
                    t.listColumn.size() == listColumn.size() && t.listRow.size() == listRow.size()) {
                ArrayList<Column> columns = t.listColumn;
                for (int i = 0; i < columns.size(); i++) {
                    Column testC = columns.get(i);
                    Column thisC = listColumn.get(i);
                    if (!testC.equals(thisC)) {
                        return false;
                    }
                }

                ArrayList<DataRow> rows = t.listRow;
                for (int i = 0; i < rows.size(); i++) {
                    DataRow testR = rows.get(i);
                    DataRow thisR = listRow.get(i);
                    if (!testR.equals(thisR, t.listColumn, listColumn)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
