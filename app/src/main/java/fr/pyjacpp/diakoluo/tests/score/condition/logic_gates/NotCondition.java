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

import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.column.Column;
import fr.pyjacpp.diakoluo.tests.data.DataCell;
import fr.pyjacpp.diakoluo.tests.score.condition.BaseCondition;
import fr.pyjacpp.diakoluo.tests.score.view_creator.ViewCreator;
import fr.pyjacpp.diakoluo.tests.score.view_creator.parameters.BaseParameter;
import fr.pyjacpp.diakoluo.tests.score.view_creator.parameters.ConditionParameter;

/**
 * Inverse the result of a condition
 */
public class NotCondition extends BaseCondition {

    private BaseCondition condition;

    public NotCondition(BaseCondition condition) {
        this.condition = condition;
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
        return !condition.get(column, cell, answer);
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
                R.string.condition_not_name,
                R.string.condition_not_description,
                new BaseParameter[]{
                        new ConditionParameter(
                                R.string.condition_not_condition_name,
                                R.string.condition_not_condition_description,
                                condition
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
        this.condition = (BaseCondition) viewCreator.getParameters()[0].getEditValue();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof NotCondition) {
            NotCondition c = (NotCondition) obj;
            return c.condition.equals(condition);
        }
        return false;
    }
}
