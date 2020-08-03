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

package fr.pyjacpp.diakoluo.tests.column;

import org.junit.Test;

import fr.pyjacpp.diakoluo.DefaultTest;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;
import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.data.DataCell;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ColumnTest {

    @Test
    public void name() {
        Column column = Column.newColumn(ColumnInputType.DEFAULT_INPUT_TYPE);
        String name = "Name";
        column.setName(name);
        assertEquals(name, column.getName());
    }

    @Test
    public void description() {
        Column column = Column.newColumn(ColumnInputType.DEFAULT_INPUT_TYPE);
        String description = "Description";
        column.setDescription(description);
        assertEquals(description, column.getDescription());
    }

    @Test
    public void getInputType() {
        Column column = Column.newColumn(ColumnInputType.DEFAULT_INPUT_TYPE);
        assertEquals(ColumnInputType.DEFAULT_INPUT_TYPE, column.getInputType());

        for (ColumnInputType inputType : ColumnInputType.values()) {
            Column newColumn = Column.newColumn(inputType);
            assertEquals(inputType, newColumn.getInputType());
        }
    }

    @Test
    public void defaultValue() {
        Column column = Column.newColumn(ColumnInputType.String);
        String defaultValue = "DefaultValue";
        column.setDefaultValue(defaultValue);
        assertEquals(defaultValue, column.getDefaultValue());
    }

    @Test
    public void initializeDefaultValue() {
        ColumnString columnString = new ColumnString();
        assertNull(columnString.getDefaultValue());
        columnString.initializeChildValue();
        assertNotNull(columnString.getDefaultValue());

        for (ColumnInputType inputType : ColumnInputType.values()) {
            Column newColumn = Column.newColumn(inputType);
            newColumn.initializeChildValue();
            assertNotNull(newColumn.getDefaultValue());
        }
    }

    @Test
    public void isValid() {
        Column column = new ColumnString();
        assertFalse(column.isValid());
        column.setName("Test");
        column.setDescription("Test");
        column.initializeChildValue();
        assertTrue(column.isValid());

    }

    @Test
    public void newColumn() {
        String name = "Name";
        String description = "Description";
        for (ColumnInputType inputType : ColumnInputType.values()) {
            Column column = Column.newColumn(inputType);
            assertTrue(inputType.name(), column.isValid());
            column = Column.newColumn(inputType, name, description);
            assertTrue(inputType.name(), column.isValid());
            assertEquals(inputType.name(), name, column.getName());
            assertEquals(inputType.name(), description, column.getDescription());
        }
    }

    @Test
    public void copyColumn() {
        for (ColumnInputType inputType : ColumnInputType.values()) {
            Column column = Column.newColumn(inputType);
            Column column1 = Column.copyColumn(column);

            assertNotSame(inputType.name(), column, column1);
            assertEquals(inputType.name(), column, column1);
        }
    }

    @Test
    public void updateCells() {
        fr.pyjacpp.diakoluo.tests.Test excepted = new fr.pyjacpp.diakoluo.tests.Test("Test", "Test");
        fr.pyjacpp.diakoluo.tests.Test test = new fr.pyjacpp.diakoluo.tests.Test("Test", "Test");
        DataRow exceptedRow = new DataRow();
        DataRow row = new DataRow();
        excepted.addRow(exceptedRow);
        test.addRow(row);
        for (ColumnInputType inputType : ColumnInputType.values()) {
            Column column = DefaultTest.setTestValue(Column.newColumn(inputType));
            Column column1 = DefaultTest.setTestValueEmpty(Column.newColumn(inputType));
            test.addColumn(column);
            excepted.addColumn(column);
            test.addColumn(column1);
            excepted.addColumn(column1);
            row.getListCells().put(column, DefaultTest.setTestValue(DataCell.newCellWithDefaultValue(column)));
            exceptedRow.getListCells().put(column, DefaultTest.setTestValue(DataCell.newCellWithDefaultValue(column)));
            row.getListCells().put(column1, DefaultTest.setTestValue(DataCell.newCellWithDefaultValue(column1)));
            exceptedRow.getListCells().put(column1, DefaultTest.setTestValue(DataCell.newCellWithDefaultValue(column1)));

            column.updateCells(test, column);
            column1.updateCells(test, column1);

            assertEquals(inputType.name(), excepted, test); // assert that no data is lost when migrate is use
        }
    }
}