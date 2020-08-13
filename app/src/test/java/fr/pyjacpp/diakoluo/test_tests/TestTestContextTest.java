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

package fr.pyjacpp.diakoluo.test_tests;

import org.junit.Before;
import org.junit.Test;

import fr.pyjacpp.diakoluo.DefaultTest;
import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.column.Column;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TestTestContextTest {

    private static final int NUMBER_QUESTION_TO_ASK = 10;
    private static final int NUMBER_COLUMN_TO_SHOW = 1;
    private DefaultTest defaultTest;

    @Before
    public void setUp() {
        defaultTest = new DefaultTest();
    }

    @Test
    public void getProgressScore() {
        TestTestContext testTestContext = new TestTestContext(defaultTest, NUMBER_QUESTION_TO_ASK, NUMBER_COLUMN_TO_SHOW,
                false);
        testTestContext.addScore(10, 10);
        assertEquals(10 * TestTestContext.PROGRESS_BAR_PRECISION, testTestContext.getProgressScore());
        assertEquals((int) testTestContext.getMaxScore() * TestTestContext.PROGRESS_BAR_PRECISION, testTestContext.getMaxProgressScore());
    }

    @Test
    public void addScore() {
        TestTestContext testTestContext = new TestTestContext(defaultTest, NUMBER_QUESTION_TO_ASK, NUMBER_COLUMN_TO_SHOW,
                false);
        assertEquals(0, testTestContext.getScore(), 0);
        testTestContext.addScore(10, 10);
        assertEquals(10, testTestContext.getScore(), 0);
    }

    @Test
    public void incrementCurrentIndex() {
        int numberQuestionToAsk = NUMBER_QUESTION_TO_ASK;
        TestTestContext testTestContext = new TestTestContext(defaultTest, numberQuestionToAsk, NUMBER_COLUMN_TO_SHOW,
                false);
        for (int i = 0; i < numberQuestionToAsk - 1; i++) {
            assertEquals(i, testTestContext.getCurrentIndex());
            assertTrue(testTestContext.incrementCurrentIndex());
        }
        assertEquals(numberQuestionToAsk - 1, testTestContext.getCurrentIndex());
        assertFalse(testTestContext.incrementCurrentIndex());
        assertEquals(numberQuestionToAsk - 1, testTestContext.getCurrentIndex());
    }

    @Test
    public void answerGive() {
        TestTestContext testTestContext = new TestTestContext(defaultTest, NUMBER_QUESTION_TO_ASK, NUMBER_COLUMN_TO_SHOW, false);
        assertFalse(testTestContext.isAnswerGive());
        testTestContext.setAnswerGive(true);
        assertTrue(testTestContext.isAnswerGive());
    }

    @Test
    public void getTest() {
        TestTestContext testTestContext = new TestTestContext(defaultTest, NUMBER_QUESTION_TO_ASK, NUMBER_COLUMN_TO_SHOW, false);
        assertSame(defaultTest, testTestContext.getTest());
    }

    @Test
    public void selectShowColumn() {
        int numberColumnToShow = 2;
        TestTestContext testTestContext = new TestTestContext(defaultTest, NUMBER_QUESTION_TO_ASK, numberColumnToShow, false);
        for (int i = 0; i < 10; i++) {
            testTestContext.selectShowColumn();
            assertEquals(numberColumnToShow, countShowColumn(testTestContext));
        }
        testTestContext.reset();
        testTestContext.selectShowColumn();
        assertEquals(numberColumnToShow, countShowColumn(testTestContext));
    }

    @Test
    public void getCurrentRow() {
        TestTestContext testTestContext = new TestTestContext(defaultTest, NUMBER_QUESTION_TO_ASK, NUMBER_COLUMN_TO_SHOW, false);
        DataRow currentRow = testTestContext.getCurrentRow();
        testTestContext.incrementCurrentIndex();
        assertNotSame(currentRow, testTestContext.getCurrentRow());

    }

    @Test
    public void removeUserInput() {
        TestTestContext testTestContext = new TestTestContext(defaultTest, NUMBER_QUESTION_TO_ASK, NUMBER_COLUMN_TO_SHOW, false);
        String test = "Test";
        testTestContext.getUserAnswer().put(testTestContext.getTest().getListColumn().get(0), test);
        assertEquals(test, testTestContext.getUserAnswer().get(testTestContext.getTest().getListColumn().get(0)));
        testTestContext.removeUserInput();
        assertNull(testTestContext.getUserAnswer().get(testTestContext.getTest().getListColumn().get(0)));
    }

    @Test
    public void reset() {
        TestTestContext testTestContext = new TestTestContext(defaultTest, NUMBER_QUESTION_TO_ASK, NUMBER_COLUMN_TO_SHOW, false);
        testTestContext.addScore(10, 10);
        assertEquals(10, testTestContext.getScore(), 0);
        testTestContext.setAnswerGive(true);
        assertTrue(testTestContext.isAnswerGive());
        testTestContext.reset();
        assertEquals(0, testTestContext.getScore(), 0);
        assertFalse(testTestContext.isAnswerGive());
        assertEquals(0, testTestContext.getMaxScore(), 0);
    }

    @Test
    public void progress() {
        TestTestContext testTestContext = new TestTestContext(defaultTest, NUMBER_QUESTION_TO_ASK, NUMBER_COLUMN_TO_SHOW, false);
        do {
            assertEquals(testTestContext.getCurrentIndex() * TestTestContext.PROGRESS_BAR_PRECISION, testTestContext.getProgress());
        } while (testTestContext.incrementCurrentIndex());
        assertEquals(testTestContext.getMaxProgress(), testTestContext.getProgress() + TestTestContext.PROGRESS_BAR_PRECISION);
    }

    private int countShowColumn(TestTestContext testTestContext) {
        int count = 0;
        for (Column column : testTestContext.getTest().getListColumn()) {
            Boolean aBoolean = testTestContext.getShowColumn().get(column);
            if (aBoolean != null && aBoolean) {
                count++;
            }
        }
        return count;
    }
}