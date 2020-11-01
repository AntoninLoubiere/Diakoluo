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

package fr.pyjacpp.diakoluo.tests.score.action;

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
import fr.pyjacpp.diakoluo.tests.score.action.base.AddAction;
import fr.pyjacpp.diakoluo.tests.score.action.base.SetAction;
import fr.pyjacpp.diakoluo.tests.score.view_creator.ViewCreator;

/**
 * A base action that will give the appropriate score to the column.
 */
public abstract class BaseAction {
    /**
     * Create a new empty base action .
     */
    protected BaseAction() {
    }

    /**
     * Read an action from a xml file.
     *
     * @param parser the parser to load
     * @throws IOException            if an error occur while reading the file
     * @throws XmlPullParserException if an error occur while reading the file
     */
    protected BaseAction(XmlPullParser parser) throws IOException, XmlPullParserException {
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                // continue until it is a start tag
                continue;
            }

            readXmlTag(parser);
        }
    }

    /**
     * Read an action from a xml file.
     *
     * @param parser    the parser to load
     * @param inputType the inputType of the column that hold this action
     * @return the action loaded
     * @throws IOException            if an error occur while reading the file
     * @throws XmlPullParserException if an error occur while reading the file
     */
    @Nullable
    public static BaseAction readXmlAction(XmlPullParser parser, ColumnInputType inputType) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, FileManager.TAG_ACTION);

        String type = parser.getAttributeValue(null, FileManager.ATTRIBUTE_TYPE);
        BaseAction action;

        // Register the action class here
        switch (type) {
            case AddAction.ATTRIBUTE_TYPE_VALUE:
                action = new AddAction(parser);
                break;

            case SetAction.ATTRIBUTE_TYPE_VALUE:
                action = new SetAction(parser);
                break;

            default:
                action = null;
                break;
        }

        if (action == null || !action.isValid(inputType)) return null;
        else return action;
    }

    /**
     * Get if the action is valid after loaded it.
     *
     * @param inputType the input type of the column that hold the action
     * @return if the column is valid
     */
    protected boolean isValid(ColumnInputType inputType) {
        return true; // by default, accept all input type
    }

    /**
     * Read xml a tag of the action (without looping).
     *
     * @param parser the parser that read the file
     * @throws IOException            if an exception occur while reading the file
     * @throws XmlPullParserException if an exception occur while reading the file
     */
    protected void readXmlTag(XmlPullParser parser) throws IOException, XmlPullParserException {
        XmlLoader.skip(parser);
    }

    /**
     * Write the action in a xml file. Should not be override, override
     * {@link #writeXmlFields(OutputStream)}.
     *
     * @param fileOutputStream the file stream to write
     * @throws IOException if an error occur while writing the file
     */
    public void writeXml(OutputStream fileOutputStream) throws IOException {
        XmlSaver.writeStartBeacon(fileOutputStream, FileManager.TAG_ACTION, FileManager.ATTRIBUTE_TYPE,
                getType());

        writeXmlFields(fileOutputStream);

        XmlSaver.writeEndBeacon(fileOutputStream, FileManager.TAG_ACTION);
    }

    /**
     * Write the action fields in a xml file. Override method should call the super.
     *
     * @param fileOutputStream the file stream to write
     * @throws IOException if an error occur while writing the file
     */
    protected void writeXmlFields(OutputStream fileOutputStream) throws IOException {
        // No BaseAction field to write
    }

    /**
     * Get the type of the class.
     *
     * @return the type of the class (to be write in the xml file)
     */
    protected abstract String getType();

    /**
     * Apply the action, set the score to the context.
     *
     * @param context the context of actions
     * @param column  the column attached to the cell
     * @return true if the column should stop reading rules
     */
    public abstract boolean apply(ScoreActionContext context, Column column);

    /**
     * Get the view creator of the condition, the name and the description.
     *
     * @return the descriptor of the condition
     * @see #setFromViewCreator(ViewCreator)
     */
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
