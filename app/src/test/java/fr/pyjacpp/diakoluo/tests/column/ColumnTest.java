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

import fr.pyjacpp.diakoluo.tests.ColumnInputType;

import static org.junit.Assert.*;

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
        columnString.initializeDefaultValue();
        assertNotNull(columnString.getDefaultValue());

        for (ColumnInputType inputType : ColumnInputType.values()) {
            Column newColumn = Column.newColumn(inputType);
            newColumn.initializeDefaultValue();
            assertNotNull(newColumn.getDefaultValue());
        }
    }

    @Test
    public void isValid() {
        Column column = new ColumnString();
        assertFalse(column.isValid());
        column.setName("Test");
        column.setDescription("Test");
        column.initializeDefaultValue();
        assertTrue(column.isValid());

    }

    @Test
    public void newColumn() {
        String name = "Name";
        String description = "Description";
        for (ColumnInputType inputType : ColumnInputType.values()) {
            Column column = Column.newColumn(inputType);
            assertTrue(column.isValid());
            column = Column.newColumn(inputType, name, description);
            assertTrue(column.isValid());
            assertEquals(name, column.getName());
            assertEquals(description, column.getDescription());
        }
    }

    @Test
    public void copyColumn() {
        Column column = new ColumnString();
        Column column1 = Column.copyColumn(column);

        assertNotSame(column, column1);
    }
}