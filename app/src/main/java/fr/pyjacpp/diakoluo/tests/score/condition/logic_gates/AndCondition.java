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

package fr.pyjacpp.diakoluo.tests.score.condition.logic_gates;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.OutputStream;

import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.save_test.FileManager;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;
import fr.pyjacpp.diakoluo.tests.column.Column;
import fr.pyjacpp.diakoluo.tests.data.DataCell;
import fr.pyjacpp.diakoluo.tests.score.condition.BaseCondition;
import fr.pyjacpp.diakoluo.tests.score.view_creator.ViewCreator;
import fr.pyjacpp.diakoluo.tests.score.view_creator.parameters.BaseParameter;
import fr.pyjacpp.diakoluo.tests.score.view_creator.parameters.ConditionParameter;

/**
 * And gate
 */
public class AndCondition extends BaseCondition {
    public static final String ATTRIBUTE_TYPE_VALUE = "and";

    private BaseCondition condition1 = null;
    private BaseCondition condition2 = null;

    /**
     * Create a new empty base condition.
     */
    public AndCondition(BaseCondition condition1, BaseCondition condition2) {
        this.condition1 = condition1;
        this.condition2 = condition2;
    }

    /**
     * Read a condition from a xml file.
     *
     * @param parser    the parser to load
     * @param inputType the inputType of the column that hold the condition
     * @throws IOException            if an error occur while reading the file
     * @throws XmlPullParserException if an error occur while reading the file
     */
    public AndCondition(XmlPullParser parser, ColumnInputType inputType) throws IOException, XmlPullParserException {
        super(parser, inputType);
    }

    /**
     * Get if the condition is valid after loaded it.
     *
     * @param inputType the input type of the column that hold the action
     * @return if the column is valid
     */
    @Override
    protected boolean isValid(ColumnInputType inputType) {
        // accept all input type
        return condition1 != null && condition2 != null;
    }

    /**
     * Read xml tag of the condition.
     *
     * @param parser    the parser that read the file
     * @param inputType the inputType of the column that hold the condition
     * @throws IOException            if an exception occur while reading the file
     * @throws XmlPullParserException if an exception occur while reading the file
     */
    @Override
    protected void readXmlTag(XmlPullParser parser, ColumnInputType inputType) throws IOException, XmlPullParserException {
        if (parser.getName().equals(FileManager.TAG_CONDITION)) {
            if (condition1 == null) condition1 = BaseCondition.readXmlCondition(parser, inputType);
            else condition2 = BaseCondition.readXmlCondition(parser, inputType);
        } else {
            super.readXmlTag(parser, inputType);
        }
    }

    @Override
    public void writeXmlFields(OutputStream fileOutputStream) throws IOException {
        super.writeXmlFields(fileOutputStream);
        condition1.writeXml(fileOutputStream);
        condition2.writeXml(fileOutputStream);
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
        return condition1.get(column, cell, answer) && condition2.get(column, cell, answer);
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
                R.string.condition_and_name,
                R.string.condition_and_description,
                new BaseParameter[]{
                        new ConditionParameter(
                                R.string.condition_and_condition_name,
                                R.string.condition_and_condition_description,
                                condition1
                        ),
                        new ConditionParameter(
                                R.string.condition_and_condition_name,
                                R.string.condition_and_condition_description,
                                condition2
                        )
                }
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
        this.condition1 = (BaseCondition) viewCreator.getParameters()[0].getEditValue();
        this.condition2 = (BaseCondition) viewCreator.getParameters()[1].getEditValue();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof AndCondition) {
            AndCondition c = (AndCondition) obj;
            return c.condition1.equals(condition1) && c.condition2.equals(condition2);
        }
        return false;
    }
}
