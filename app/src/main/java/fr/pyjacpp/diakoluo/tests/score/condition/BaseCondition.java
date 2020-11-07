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

package fr.pyjacpp.diakoluo.tests.score.condition;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.OutputStream;

import fr.pyjacpp.diakoluo.save_test.FileManager;
import fr.pyjacpp.diakoluo.save_test.XmlLoader;
import fr.pyjacpp.diakoluo.save_test.XmlSaver;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;
import fr.pyjacpp.diakoluo.tests.column.Column;
import fr.pyjacpp.diakoluo.tests.data.DataCell;
import fr.pyjacpp.diakoluo.tests.score.condition.base.EqualsCondition;
import fr.pyjacpp.diakoluo.tests.score.condition.base.SkippedCondition;
import fr.pyjacpp.diakoluo.tests.score.condition.base.TrueCondition;
import fr.pyjacpp.diakoluo.tests.score.condition.logic_gates.AndCondition;
import fr.pyjacpp.diakoluo.tests.score.condition.logic_gates.NotCondition;
import fr.pyjacpp.diakoluo.tests.score.condition.logic_gates.OrCondition;
import fr.pyjacpp.diakoluo.tests.score.condition.logic_gates.XorCondition;
import fr.pyjacpp.diakoluo.tests.score.view_creator.ViewCreator;

/**
 * A base condition class
 */
public abstract class BaseCondition {
    /**
     * Create a new empty base condition.
     */
    protected BaseCondition() {
    }

    /**
     * Read a condition from a xml file.
     *
     * @param parser    the parser to load
     * @param inputType the inputType of the column that hold the condition
     * @throws IOException            if an error occur while reading the file
     * @throws XmlPullParserException if an error occur while reading the file
     */
    protected BaseCondition(XmlPullParser parser, ColumnInputType inputType) throws IOException, XmlPullParserException {
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                // continue until it is a start tag
                continue;
            }

            readXmlTag(parser, inputType);
        }
    }

    /**
     * Read a condition from a xml file.
     *
     * @param parser    the parser to load
     * @param inputType the inputType of the column that hold this action
     * @return the action loaded
     * @throws IOException            if an error occur while reading the file
     * @throws XmlPullParserException if an error occur while reading the file
     */
    @Nullable
    public static BaseCondition readXmlCondition(XmlPullParser parser, ColumnInputType inputType) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, FileManager.TAG_CONDITION);

        String type = parser.getAttributeValue(null, FileManager.ATTRIBUTE_TYPE);
        BaseCondition condition;

        // Register the condition class here
        switch (type) {
            case TrueCondition.ATTRIBUTE_TYPE_VALUE:
                condition = new TrueCondition(parser, inputType);
                break;

            case NotCondition.ATTRIBUTE_TYPE_VALUE:
                condition = new NotCondition(parser, inputType);
                break;

            case EqualsCondition.ATTRIBUTE_TYPE_VALUE:
                condition = new EqualsCondition(parser, inputType);
                break;

            case SkippedCondition.ATTRIBUTE_TYPE_VALUE:
                condition = new SkippedCondition(parser, inputType);
                break;

            case AndCondition.ATTRIBUTE_TYPE_VALUE:
                condition = new AndCondition(parser, inputType);
                break;

            case OrCondition.ATTRIBUTE_TYPE_VALUE:
                condition = new OrCondition(parser, inputType);
                break;

            case XorCondition.ATTRIBUTE_TYPE_VALUE:
                condition = new XorCondition(parser, inputType);
                break;

            default:
                condition = null;
                break;
        }

        if (condition == null || !condition.isValid(inputType)) return null;
        else return condition;
    }

    /**
     * Get if the condition is valid after loaded it.
     *
     * @param inputType the input type of the column that hold the action
     * @return if the column is valid
     */
    protected boolean isValid(ColumnInputType inputType) {
        return true; // by default, accept all input type
    }

    /**
     * Read xml tag of the condition.
     *
     * @param parser    the parser that read the file
     * @param inputType the inputType of the column that hold the condition
     * @throws IOException            if an exception occur while reading the file
     * @throws XmlPullParserException if an exception occur while reading the file
     */
    protected void readXmlTag(XmlPullParser parser, ColumnInputType inputType) throws IOException, XmlPullParserException {
        XmlLoader.skip(parser);
    }

    /**
     * Write the condition in a xml file. Should not be override, override
     * {@link #writeXmlFields(OutputStream)} instead.
     *
     * @param fileOutputStream the file stream to write
     * @throws IOException if an error occur while writing the file
     */
    public void writeXml(OutputStream fileOutputStream) throws IOException {
        XmlSaver.writeStartBeacon(fileOutputStream, FileManager.TAG_CONDITION, FileManager.ATTRIBUTE_TYPE, getType());
        writeXmlFields(fileOutputStream);
        XmlSaver.writeEndBeacon(fileOutputStream, FileManager.TAG_CONDITION);
    }

    /**
     * Write the xml fields in a xml file. If override, it should call the the super.
     *
     * @param fileOutputStream the file stream to write
     * @throws IOException if an error occur while writing the file
     */
    protected void writeXmlFields(OutputStream fileOutputStream) throws IOException {
        // Nothing to write in Base condition
    }

    /**
     * Get the type of the class (to write in xml file).
     *
     * @return the type of the class
     */
    protected abstract String getType();

    /**
     * Get the value of the condition
     *
     * @param column the column attached to the cell
     * @param cell   the cell with contain the right value
     * @param answer the answer from the user
     * @return if the condition is respected
     */
    public abstract boolean get(Column column, DataCell cell, Object answer);

    /**
     * Get the view creator of the condition, the name and the description.
     *
     * @return the descriptor of the condition
     * @see #setFromViewCreator(ViewCreator)
     */
    @NonNull
    public abstract ViewCreator getViewCreator();

    /**
     * Set value from the edited view.
     *
     * @param viewCreator the view creator that create views
     * @see #getViewCreator()
     */
    public abstract void setFromViewCreator(@NonNull ViewCreator viewCreator);

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
