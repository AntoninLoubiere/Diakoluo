package fr.pyjacpp.diakoluo;

import fr.pyjacpp.diakoluo.test_tests.TestTestContext;

public class ScoreUtils {
    public static float getScore20(float score, float max) {
        return score / max * 20;
    }

    public static float getScore10(float score, float max) {
        return score / max * 10;
    }

    public static float getScoreFrac(float score, float max) {
        return score / max;
    }

    public static float getScorePercent(float score, float max) {
        return score / max * 100;
    }

    public static float getScoreCustom(float score, float max, int n) {
        return score / max * n;
    }

    public static float getScore20(TestTestContext context) {
        return getScore20(context.getScore(), context.getMaxScore());
    }

    public static float getScore10(TestTestContext context) {
        return getScore10(context.getScore(), context.getMaxScore());
    }

    public static float getScoreFrac(TestTestContext context) {
        return getScoreFrac(context.getScore(), context.getMaxScore());
    }

    public static float getScorePercent(TestTestContext context) {
        return getScorePercent(context.getScore(), context.getMaxScore());
    }

    public static float getScoreCustom(TestTestContext context, int n) {
        return getScoreCustom(context.getScore(), context.getMaxScore(), n);
    }
}
