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

package fr.pyjacpp.diakoluo.tests.score;

import androidx.annotation.NonNull;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import fr.pyjacpp.diakoluo.save_test.FileManager;
import fr.pyjacpp.diakoluo.save_test.XmlLoader;
import fr.pyjacpp.diakoluo.save_test.XmlSaver;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;
import fr.pyjacpp.diakoluo.tests.score.action.BaseAction;
import fr.pyjacpp.diakoluo.tests.score.condition.BaseCondition;
import fr.pyjacpp.diakoluo.tests.score.view_creator.ViewCreator;

/**
 * A Rule class that hold a condition and an action to do
 */
public class Rule {

    private BaseCondition condition;
    private BaseAction action;

    /**
     * Create a Rule from a condition and an action
     *
     * @param condition the condition
     * @param action    the action
     */
    public Rule(BaseCondition condition, BaseAction action) {
        this.condition = condition;
        this.action = action;
    }

    /**
     * Create a rule from a xml file.
     *
     * @param parser    the parser to load
     * @param inputType the input type of the column that hold this rule
     * @throws IOException            if an error occur while reading the file
     * @throws XmlPullParserException if an error occur while reading the file
     */
    private Rule(XmlPullParser parser, ColumnInputType inputType) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, FileManager.TAG_RULE);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                // continue until it is a start tag
                continue;
            }

            switch (parser.getName()) {
                case FileManager.TAG_ACTION:
                    action = BaseAction.readXmlAction(parser, inputType);
                    break;

                case FileManager.TAG_CONDITION:
                    condition = BaseCondition.readXmlCondition(parser, inputType);
                    break;

                default:
                    XmlLoader.skip(parser);
                    break;
            }
        }
    }

    /**
     * Read rules from a xml document.
     *
     * @param parser    the parser to load
     * @param inputType the InputType of the column
     * @return the list of rules
     * @throws IOException            if an error occur while reading the file
     * @throws XmlPullParserException if an error occur while reading the file
     */
    public static ArrayList<Rule> readXmlRules(XmlPullParser parser, ColumnInputType inputType) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, FileManager.TAG_RULES);
        ArrayList<Rule> rules = new ArrayList<>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                // continue until it is a start tag
                continue;
            }

            if (parser.getName().equals(FileManager.TAG_RULE)) {
                Rule rule = new Rule(parser, inputType);
                if (rule.isValid()) {
                    rules.add(rule);
                }
            } else {
                XmlLoader.skip(parser);
            }
        }

        return rules;
    }

    /**
     * Write a list of rules in a xml file.
     *
     * @param fileOutputStream the file to write
     * @param rules            the list of rules to write
     * @throws IOException if an exception occur while writing the file
     */
    public static void writeXmlRules(OutputStream fileOutputStream, ArrayList<Rule> rules) throws IOException {
        XmlSaver.writeStartBeacon(fileOutputStream, FileManager.TAG_RULES);
        for (Rule r : rules) {
            r.writeXml(fileOutputStream);
        }
        XmlSaver.writeEndBeacon(fileOutputStream, FileManager.TAG_RULES);
    }

    /**
     * Write the rule in a xml file.
     *
     * @param fileOutputStream the file to write
     * @throws IOException if an error occur while writing the file
     */
    private void writeXml(OutputStream fileOutputStream) throws IOException {
        XmlSaver.writeStartBeacon(fileOutputStream, FileManager.TAG_RULE);
        action.writeXml(fileOutputStream);
        condition.writeXml(fileOutputStream);
        XmlSaver.writeEndBeacon(fileOutputStream, FileManager.TAG_RULE);
    }

    /**
     * Get if it is a valid column after load.
     *
     * @see #readXmlRules(XmlPullParser, ColumnInputType)
     */
    private boolean isValid() {
        return condition != null && action != null;
    }

    /**
     * Get the view creator of the condition, the name and the description.
     *
     * @return the descriptor of the condition
     * @see #setFromViewCreator(ViewCreator)
     */
    public ViewCreator getViewCreator() {
        return new ViewCreator(0, 0); // TODO
    }

    /**
     * Set value from the edited view.
     *
     * @param viewCreator the view creator that create views
     * @see #getViewCreator()
     */
    public void setFromViewCreator(@NonNull ViewCreator viewCreator) {
        // TODO
    }

    /**
     * Get the condition of the rule.
     *
     * @return the condition of the rule
     */
    public BaseCondition getCondition() {
        return condition;
    }

    /**
     * Get the action of the rule.
     *
     * @return the action of the rule
     */
    public BaseAction getAction() {
        return action;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Rule) {
            Rule rule = (Rule) obj;
            return condition.equals(rule.condition) &&
                    action.equals(rule.action);
        }
        return false;
    }
}
