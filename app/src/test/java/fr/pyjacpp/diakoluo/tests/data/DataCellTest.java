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

package fr.pyjacpp.diakoluo.tests.data;

import org.junit.Test;

import fr.pyjacpp.diakoluo.tests.ColumnInputType;
import fr.pyjacpp.diakoluo.tests.column.Column;

import static org.junit.Assert.*;

public class DataCellTest {

    @Test
    public void copyDataCell() {
        for (ColumnInputType inputType : ColumnInputType.values()) {
            DataCell dataCell = DataCell.getDefaultValueCell(Column.newColumn(inputType));
            DataCell dataCell1 = DataCell.copyDataCell(dataCell);
            assertNotSame(dataCell, dataCell1);
        }
    }

    @Test
    public void value() {
        for (ColumnInputType inputType : ColumnInputType.values()) {
            Column currentColumn = Column.newColumn(inputType);
            DataCell dataCell = DataCell.getDefaultValueCell(currentColumn);
            assertEquals(currentColumn.getDefaultValue(), dataCell.getValue());
        }
    }

    @Test
    public void getStringValue() {
        for (ColumnInputType inputType : ColumnInputType.values()) {
            Column currentColumn = Column.newColumn(inputType);
            DataCell dataCell = DataCell.getDefaultValueCell(currentColumn);
            assertNotNull(dataCell.getStringValue());
        }
    }

    @Test
    public void verifyAnswer() {
        for (ColumnInputType inputType : ColumnInputType.values()) {
            Column currentColumn = Column.newColumn(inputType);
            DataCell dataCell = DataCell.getDefaultValueCell(currentColumn);
            assertTrue(currentColumn.verifyAnswer(dataCell, dataCell.getValue()));
        }
    }
}