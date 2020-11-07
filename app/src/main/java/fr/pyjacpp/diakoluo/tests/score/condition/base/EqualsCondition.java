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

package fr.pyjacpp.diakoluo.tests.score.condition.base;

import androidx.annotation.NonNull;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;
import fr.pyjacpp.diakoluo.tests.column.Column;
import fr.pyjacpp.diakoluo.tests.data.DataCell;
import fr.pyjacpp.diakoluo.tests.score.condition.BaseCondition;
import fr.pyjacpp.diakoluo.tests.score.view_creator.ViewCreator;

/**
 * Test if an answer is equals to the value excepted.
 */
public class EqualsCondition extends BaseCondition {
    public static final String ATTRIBUTE_TYPE_VALUE = "equals";

    /**
     * Create a new empty base condition.
     */
    public EqualsCondition() {
        super();
    }

    /**
     * Read a condition from a xml file.
     *
     * @param parser    the parser to load
     * @param inputType the inputType of the column that hold the condition
     * @throws IOException            if an error occur while reading the file
     * @throws XmlPullParserException if an error occur while reading the file
     */
    public EqualsCondition(XmlPullParser parser, ColumnInputType inputType) throws IOException, XmlPullParserException {
        super(parser, inputType);
    }

    /**
     * Get the type of the class (to write in xml file).
     *
     * @return the type of the class
     */
    @Override
    protected String getType() {
        return ATTRIBUTE_TYPE_VALUE;
    }

    /**
     * Get the value of the condition
     *
     * @param column the column attached to the cell
     * @param cell   the cell with contain the right value
     * @param answer the answer from the user
     * @return if the condition is respected
     */
    @Override
    public boolean get(Column column, DataCell cell, Object answer) {
        return column.isAnswerEquals(cell, answer);
    }

    /**
     * Get the view creator of the condition, the name and the description.
     *
     * @return the descriptor of the condition
     * @see #setFromViewCreator(ViewCreator)
     */
    @NonNull
    @Override
    public ViewCreator getViewCreator() {
        return new ViewCreator(
                R.string.condition_equals_name,
                R.string.condition_equals_description
        );
    }

    /**
     * Set value from the edited view.
     *
     * @param viewCreator the view creator that create views
     * @see #getViewCreator()
     */
    @Override
    public void setFromViewCreator(@NonNull ViewCreator viewCreator) {
        // nothing to set
    }
}
