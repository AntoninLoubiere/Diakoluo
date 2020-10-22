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

import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;

import fr.pyjacpp.diakoluo.DefaultTest;

import static org.junit.Assert.assertEquals;

public class TestTestContextTest {

    private static final int NUMBER_QUESTION_TO_ASK = 10;
    private static final int NUMBER_COLUMN_TO_SHOW = 1;
    private DefaultTest defaultTest;

    @Before
    public void setUp() {
        defaultTest = new DefaultTest();
    }

    @Test
    public void bundleSave() {
        TestTestContext testTestContext = new TestTestContext(defaultTest, NUMBER_QUESTION_TO_ASK, NUMBER_COLUMN_TO_SHOW, false);

        do {
            Bundle testContextSaved = testTestContext.getBundle();
            TestTestContext context = new TestTestContext(defaultTest, testContextSaved);
            assertEquals("CurrentIndex = " + testTestContext.getCurrentIndex(), testTestContext, context);
        } while (testTestContext.incrementCurrentIndex());
        
        Bundle testContextSaved = testTestContext.getBundle();
        TestTestContext context = new TestTestContext(defaultTest, testContextSaved);
        assertEquals(testTestContext, context);
    }
}