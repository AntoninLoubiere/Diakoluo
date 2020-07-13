package fr.pyjacpp.diakoluo;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import fr.pyjacpp.diakoluo.test_tests.TestTestContext;

import static org.junit.Assert.assertEquals;

public class ScoreUtilsTest {

    private DefaultTest defaultTest;

    private TestTestContext testTestContextMin;
    private TestTestContext testTestContextMax;
    private TestTestContext testTestContextRandom;

    @Before
    public void setUp(){
        defaultTest = new DefaultTest();

        testTestContextMin = new TestTestTestContext(10, 1);
        testTestContextMin.addScore(0);
        testTestContextMax = new TestTestTestContext(10, 1);
        testTestContextMax.addScore(testTestContextMax.getMaxScore());
        testTestContextRandom = new TestTestTestContext(10, 1);
        testTestContextRandom.addScore(new Random().nextInt(testTestContextRandom.getMaxScore() - 2) + 1);
    }

    @Test
    public void getScore20() {
        assertEquals("GetScore20Min", 0, ScoreUtils.getScore20(testTestContextMin), 0f);
        assertEquals("GetScore20Max", 20, ScoreUtils.getScore20(testTestContextMax), 0f);
        assertEquals("GetScore20Random", (testTestContextRandom.getScore() / (float) testTestContextRandom.getMaxScore()) * 20, ScoreUtils.getScore20(testTestContextRandom), 0f);
    }

    @Test
    public void getScore10() {
        assertEquals("GetScore10Min", 0, ScoreUtils.getScore10(testTestContextMin), 0f);
        assertEquals("GetScore10Max", 10, ScoreUtils.getScore10(testTestContextMax), 0f);
        assertEquals("GetScore10Random", (testTestContextRandom.getScore() / (float) testTestContextRandom.getMaxScore()) * 10, ScoreUtils.getScore10(testTestContextRandom), 0f);
    }

    @Test
    public void getScoreFrac() {
        assertEquals("GetScoreFracMin", 0, ScoreUtils.getScoreFrac(testTestContextMin), 0f);
        assertEquals("GetScoreFracMax", 1, ScoreUtils.getScoreFrac(testTestContextMax), 0f);
        assertEquals("GetScoreFracRandom", (testTestContextRandom.getScore() / (float) testTestContextRandom.getMaxScore()), ScoreUtils.getScoreFrac(testTestContextRandom), 0f);
    }

    @Test
    public void getScorePercent() {
        assertEquals("GetScorePercentMin", 0, ScoreUtils.getScorePercent(testTestContextMin), 0f);
        assertEquals("GetScorePercentMax", 100, ScoreUtils.getScorePercent(testTestContextMax), 0f);
        assertEquals("GetScorePercentRandom", (testTestContextRandom.getScore() / (float) testTestContextRandom.getMaxScore() * 100), ScoreUtils.getScorePercent(testTestContextRandom), 0f);
    }

    @Test
    public void getScoreCustom() {
        int n = new Random().nextInt(1000);
        assertEquals("GetScoreCustomMin", 0, ScoreUtils.getScoreCustom(testTestContextMin, n), 0f);
        assertEquals("GetScoreCustomMax", n, ScoreUtils.getScoreCustom(testTestContextMax, n), 0f);
        assertEquals("GetScoreCustomRandom", (testTestContextRandom.getScore() / (float) testTestContextRandom.getMaxScore() * n), ScoreUtils.getScoreCustom(testTestContextRandom, n), 0f);
    }

    /* Test of TestTestContext */
    private class TestTestTestContext extends TestTestContext {
        TestTestTestContext(int numberQuestionToAsk, int numberColumnToShow) {
            super(defaultTest, numberQuestionToAsk, numberColumnToShow);
        }
    }
}