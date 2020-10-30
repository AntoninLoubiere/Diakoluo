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

/**
 * A class that will hold the current score when processing different columns.
 */
public class ScoreActionContext {
    private float currentScore;
    private boolean addMode;

    /**
     * Create a score action context with a currentScore of 0.
     */
    public ScoreActionContext() {
        currentScore = 0f;
        addMode = false;
    }

    /**
     * Get the current score.
     *
     * @return the current score
     */
    public float getCurrentScore() {
        return currentScore;
    }

    /**
     * Set the current score.
     *
     * @param currentScore the current score to add
     */
    public void setCurrentScore(float currentScore) {
        if (!addMode) this.currentScore = currentScore;
    }

    /**
     * Add to the current score.
     *
     * @param currentScore the current score to add
     */
    public void addCurrentScore(float currentScore) {
        addMode = true;
        this.currentScore += currentScore;
    }
}
