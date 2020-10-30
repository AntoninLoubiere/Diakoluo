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

package fr.pyjacpp.diakoluo.tests.score.view_creator.parameters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import fr.pyjacpp.diakoluo.tests.score.condition.BaseCondition;
import fr.pyjacpp.diakoluo.tests.score.view_creator.ViewCreator;

/**
 * A condition parameter.
 */
public class ConditionParameter extends BaseParameter {
    private final BaseCondition condition;
    private final ViewCreator conditionViewCreator;

    /**
     * Default constructor.
     *
     * @param name        the name of the parameter
     * @param description the description of the parameter
     */
    public ConditionParameter(int name, int description, BaseCondition condition) {
        super(name, description);
        this.condition = condition;
        this.conditionViewCreator = condition.getViewCreator();
    }

    /**
     * Generate the view to view the value of this parameter.
     *
     * @param context the context of the application
     * @return the view generated
     * @see BaseParameter#getEditView(Context)
     */
    @NonNull
    @Override
    public View getViewView(@NonNull Context context) {
        return conditionViewCreator.getViewView(context);
    }

    /**
     * Generate the view to view the value of this parameter.
     *
     * @param context the context of the application
     * @return the view generated
     * @see #getEditValue()
     * @see BaseParameter#getViewView(Context)
     */
    @NonNull
    @Override
    public View getEditView(@NonNull Context context) {
        return conditionViewCreator.getEditView(context);
    }

    /**
     * Get the value inputted by the user.
     *
     * @return the value inputted by the user
     * @see BaseParameter#getEditView(Context)
     */
    @Override
    public Object getEditValue() {
        condition.setFromViewCreator(conditionViewCreator);
        return condition;
    }
}
