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

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import fr.pyjacpp.diakoluo.DefaultTest;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;

import static org.junit.Assert.assertEquals;

public class ColumnTest {

    private Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    @Ignore("Not working error in inflater ?")
    public void getColumnSettings() {
        LinearLayout linearLayout = new LinearLayout(context);
        LayoutInflater layoutInflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (ColumnInputType inputType : ColumnInputType.values()) {
            Column column = DefaultTest.setTestValue(Column.newColumn(inputType));
            Column column1 = DefaultTest.setTestValueEmpty(Column.newColumn(inputType));

            column.getViewColumnSettings(layoutInflater, linearLayout);
            column1.getViewColumnSettings(layoutInflater, linearLayout);

            column.setEditColumnSettings(column.getEditColumnSettings(layoutInflater, linearLayout));
            column1.setEditColumnSettings(column1.getEditColumnSettings(layoutInflater, linearLayout));
            assertEquals(inputType.name(), DefaultTest.setTestValue(Column.newColumn(inputType)), column);
            assertEquals(inputType.name(), DefaultTest.setTestValueEmpty(Column.newColumn(inputType)), column1);
        }
    }
}