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

package fr.pyjacpp.diakoluo.tests.score.descriptor;

import androidx.annotation.StringRes;

/**
 * A descriptor that describe a {@link fr.pyjacpp.diakoluo.tests.score.condition.BaseCondition} or
 * a {@link fr.pyjacpp.diakoluo.tests.score.action.BaseAction}
 */
public final class Descriptor {
    @StringRes
    private final int name;
    @StringRes
    private final int description;

    /**
     * Default constructor.
     *
     * @param name        the name of the object
     * @param description the description of the object
     */
    public Descriptor(@StringRes int name, @StringRes int description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Get the name of the object.
     *
     * @return the name
     */
    @StringRes
    public int getName() {
        return name;
    }

    /**
     * Get the description of the object.
     *
     * @return the description
     */
    @StringRes
    public int getDescription() {
        return description;
    }
}
