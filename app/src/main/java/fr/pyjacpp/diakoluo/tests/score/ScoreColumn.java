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
import androidx.annotation.Nullable;

import java.util.ArrayList;

import fr.pyjacpp.diakoluo.test_tests.TestTestContext;
import fr.pyjacpp.diakoluo.tests.column.Column;
import fr.pyjacpp.diakoluo.tests.data.DataCell;
import fr.pyjacpp.diakoluo.tests.score.action.ScoreActionContext;
import fr.pyjacpp.diakoluo.tests.score.view_creator.ViewCreator;

/**
 * A class that save all informations of the score of a column.
 */
public class ScoreColumn {
    public ArrayList<Rule> rules;
    public float maxScore;

    /**
     * Create a new instance.
     *
     * @param rules    the list of rules
     * @param maxScore the max score of the column
     */
    public ScoreColumn(ArrayList<Rule> rules, float maxScore) {
        this.rules = rules;
        this.maxScore = maxScore;
    }

    /**
     * Apply the score of the column.
     *
     * @param testTestContext the context of the test
     * @param column          the column that hold the dataCell
     * @param dataCell        the dataCell that hold the right value
     * @param answer          the answer inputted by the user
     */
    public void apply(TestTestContext testTestContext, Column column, DataCell dataCell, Object answer) {
        ScoreActionContext context = new ScoreActionContext();
        for (Rule r : rules) {
            if (r.getCondition().get(column, dataCell, answer) &&
                    r.getAction().apply(context, column)) {
                // if the condition is true, apply, AND if the apply return that we can break, break.
                break;
            }
        }
        testTestContext.addScore(context.getCurrentScore(), maxScore);
    }

    /**
     * Get the view creator of the condition, the name and the description.
     *
     * @return the descriptor of the condition
     * @see #setFromViewCreator(ViewCreator)
     */
    public ViewCreator getViewCreator() {
        ViewCreator viewCreator = new ViewCreator(
                0, 0
        );// TODO
        viewCreator.setUseAccordion(false);
        return viewCreator;
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

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof ScoreColumn) {
            ScoreColumn sc = ((ScoreColumn) obj);
            return sc.maxScore == maxScore && sc.rules.equals(rules);
        }
        return false;
    }
}
