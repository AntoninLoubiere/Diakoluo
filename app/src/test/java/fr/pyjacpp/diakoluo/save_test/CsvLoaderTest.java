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

package fr.pyjacpp.diakoluo.save_test;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class CsvLoaderTest {

    @Test
    public void readLine() {
        for (String separator : CsvSaver.SEPARATORS) {
            char separator1 = separator.charAt(0);

            ArrayList<String> values = new ArrayList<>();
            String lineToRead = "testSEPARATOR\"\"SEPARATOR\"test test\"SEPARATORNull".replace("SEPARATOR", separator);
            values.add("test");
            values.add("");
            values.add("test test");
            values.add("Null");

            CsvLoader.CsvContext csvContext = new CsvLoader.CsvContext(
                    null,
                    new BufferedReader(new StringReader(lineToRead)),
                    separator1,
                    '\n');

            try {
                assertEquals(values, CsvLoader.readLine(csvContext));
            } catch (IOException e) {
                fail("An IOException occur");
            }
        }
    }

}