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

package fr.pyjacpp.diakoluo.tests.score.action.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.OutputStream;

import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.save_test.XmlLoader;
import fr.pyjacpp.diakoluo.save_test.XmlSaver;
import fr.pyjacpp.diakoluo.tests.column.Column;
import fr.pyjacpp.diakoluo.tests.score.action.BaseAction;
import fr.pyjacpp.diakoluo.tests.score.action.ScoreActionContext;
import fr.pyjacpp.diakoluo.tests.score.view_creator.ViewCreator;
import fr.pyjacpp.diakoluo.tests.score.view_creator.parameters.BaseParameter;
import fr.pyjacpp.diakoluo.tests.score.view_creator.parameters.FloatParameter;

/**
 * An action that will add to the score an amount.
 */
public class AddAction extends BaseAction {
    public static final String ATTRIBUTE_TYPE_VALUE = "add";
    private static final String TAG_SCORE = "score";
    private static final float DEFAULT_SCORE = 0f;

    private float score = DEFAULT_SCORE;

    /**
     * Initialise with a score
     *
     * @param score the score to add
     */
    public AddAction(float score) {
        super();
        this.score = score;
    }

    /**
     * Read an action from a xml file.
     *
     * @param parser the parser to load
     * @throws IOException            if an error occur while reading the file
     * @throws XmlPullParserException if an error occur while reading the file
     */
    public AddAction(XmlPullParser parser) throws IOException, XmlPullParserException {
        super(parser);
    }

    /**
     * Read xml tag of the action.
     *
     * @param parser the parser that read the file
     * @throws IOException            if an exception occur while reading the file
     * @throws XmlPullParserException if an exception occur while reading the file
     */
    @Override
    protected void readXmlTag(XmlPullParser parser) throws IOException, XmlPullParserException {
        if (parser.getName().equals(TAG_SCORE)) {
            score = XmlLoader.readFloat(parser, DEFAULT_SCORE);
        } else {
            super.readXmlTag(parser);
        }
    }

    /**
     * Write the action fields in a xml file. Override method should call the super.
     *
     * @param fileOutputStream the file stream to write
     * @throws IOException if an error occur while writing the file
     */
    @Override
    protected void writeXmlFields(OutputStream fileOutputStream) throws IOException {
        super.writeXmlFields(fileOutputStream);
        XmlSaver.writeData(fileOutputStream, TAG_SCORE, score);
    }

    /**
     * Get the type of the class.
     *
     * @return the type of the class (to be write in the xml file)
     */
    @Override
    protected String getType() {
        return ATTRIBUTE_TYPE_VALUE;
    }

    /**
     * Apply the action, set the score to the context.
     *
     * @param context the context of actions
     * @param column  the column attached to the cell
     * @return true if the column should stop reading rules
     */
    @Override
    public boolean apply(ScoreActionContext context, Column column) {
        context.addCurrentScore(score);
        return false;
    }

    /**
     * Get the view creator of the condition, the name and the description.
     *
     * @return the descriptor of the condition
     * @see #setFromViewCreator(ViewCreator)
     */
    @Override
    public ViewCreator getViewCreator() {
        return new ViewCreator(
                R.string.action_add_name,
                R.string.action_add_description,
                new BaseParameter[]{
                        new FloatParameter(
                                R.string.action_add_score_name,
                                R.string.action_add_score_description,
                                score
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
        score = (float) viewCreator.getParameters()[0].getEditValue();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof AddAction) {
            AddAction a = (AddAction) obj;
            return score == a.score;
        }
        return false;
    }
}
