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

import fr.pyjacpp.diakoluo.tests.column.Column;
import fr.pyjacpp.diakoluo.tests.score.view_creator.ViewCreator;

/**
 * A base action that will give the appropriate score to the column.
 */
public abstract class BaseAction {
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
