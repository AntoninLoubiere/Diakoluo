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

package fr.pyjacpp.diakoluo;

import fr.pyjacpp.diakoluo.test_tests.TestTestContext;

public class ScoreUtils {
    private static float getScore20(float score, float max) {
        return score / max * 20;
    }

    private static float getScore10(float score, float max) {
        return score / max * 10;
    }

    private static float getScoreFrac(float score, float max) {
        return score / max;
    }

    private static float getScorePercent(float score, float max) {
        return score / max * 100;
    }

    private static float getScoreCustom(float score, float max, int n) {
        return score / max * n;
    }

    public static float getScore20(TestTestContext context) {
        return getScore20(context.getScore(), context.getMaxScore());
    }

    static float getScore10(TestTestContext context) {
        return getScore10(context.getScore(), context.getMaxScore());
    }

    static float getScoreFrac(TestTestContext context) {
        return getScoreFrac(context.getScore(), context.getMaxScore());
    }

    static float getScorePercent(TestTestContext context) {
        return getScorePercent(context.getScore(), context.getMaxScore());
    }

    static float getScoreCustom(TestTestContext context, int n) {
        return getScoreCustom(context.getScore(), context.getMaxScore(), n);
    }
}
