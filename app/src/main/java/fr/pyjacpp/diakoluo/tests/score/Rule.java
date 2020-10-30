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

    public BaseCondition getCondition() {
        return condition;
    }

    public BaseAction getAction() {
        return action;
    }
}
