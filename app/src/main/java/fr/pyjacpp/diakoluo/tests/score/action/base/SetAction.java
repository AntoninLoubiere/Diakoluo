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

import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.column.Column;
import fr.pyjacpp.diakoluo.tests.score.action.BaseAction;
import fr.pyjacpp.diakoluo.tests.score.action.ScoreActionContext;
import fr.pyjacpp.diakoluo.tests.score.view_creator.ViewCreator;
import fr.pyjacpp.diakoluo.tests.score.view_creator.parameters.BaseParameter;
import fr.pyjacpp.diakoluo.tests.score.view_creator.parameters.FloatParameter;

/**
 * An action that will set the score of an amount
 */
public class SetAction extends BaseAction {

    private float score;

    /**
     * Initialise with a score
     *
     * @param score the score to set
     */
    public SetAction(float score) {
        this.score = score;
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
        return true;
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
                R.string.action_set_name,
                R.string.action_set_description,
                new BaseParameter[]{
                        new FloatParameter(
                                R.string.action_set_score_name,
                                R.string.action_set_score_description,
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
}
