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

import android.view.View;

import androidx.annotation.StringRes;

/**
 * A base parameter that will create a view to edit the parameter.
 */
public abstract class BaseParameter {
    @StringRes
    private final int name;
    @StringRes
    private final int description;

    /**
     * Default constructor.
     *
     * @param name        the name of the parameter
     * @param description the description of the parameter
     */
    public BaseParameter(@StringRes int name, @StringRes int description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Generate the view to view the value of this parameter.
     *
     * @return the view generated
     * @see #getEditView()
     */
    public abstract View getViewView();

    /**
     * Generate the view to view the value of this parameter.
     *
     * @return the view generated
     * @see #getEditValue()
     * @see #getViewView()
     */
    public abstract View getEditView();

    /**
     * Get the value inputted by the user.
     *
     * @return the value inputted by the user
     * @see #getEditView()
     */
    public abstract Object getEditValue();

    /**
     * Get the name of the field.
     *
     * @return the name of the field
     */
    @StringRes
    public int getName() {
        return name;
    }

    /**
     * Get the description of the field.
     *
     * @return the description of the field
     */
    @StringRes
    public int getDescription() {
        return description;
    }
}
