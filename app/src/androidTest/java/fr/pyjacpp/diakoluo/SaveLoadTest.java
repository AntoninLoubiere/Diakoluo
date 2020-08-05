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

import android.content.Context;
import android.util.Log;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import fr.pyjacpp.diakoluo.save_test.CsvLoader;
import fr.pyjacpp.diakoluo.save_test.CsvSaver;
import fr.pyjacpp.diakoluo.save_test.XmlLoader;
import fr.pyjacpp.diakoluo.save_test.XmlSaver;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;
import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.column.Column;
import fr.pyjacpp.diakoluo.tests.data.DataCell;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4ClassRunner.class)
public class SaveLoadTest {

    private static final String TAG = "SaveLoadTest";
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void saveLoadTest() {
        boolean failed = false;
        FileOutputStream outputStream = null;
        fr.pyjacpp.diakoluo.tests.Test test = new DefaultTest();
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        File file;
        try {
            file = temporaryFolder.newFile();
            outputStream = new FileOutputStream(file);
            XmlSaver.save(outputStream, test);
            fr.pyjacpp.diakoluo.tests.Test loadedTest = XmlLoader.load(new FileInputStream(file));

            assertEquals(test, loadedTest);
            assertNotSame(test, loadedTest);

            loadedTest = XmlLoader.load(context.getResources().getAssets()
                    .open(DiakoluoApplication.DEFAULT_TEST));
            assertEquals(test, loadedTest);
            assertNotSame(test, loadedTest);

            test = new fr.pyjacpp.diakoluo.tests.Test("Test", "");
            assertTrue(test.isValid());
            DataRow dataRow = new DataRow();
            test.addRow(dataRow);

            for (ColumnInputType inputType : ColumnInputType.values()) {
                Column column = Column.newColumn(inputType);
                test.addColumn(column);
                dataRow.getListCells().put(column,
                        DefaultTest.setTestValue(DataCell.getDefaultValueCell(column)));
            }

            outputStream.close();
            outputStream = new FileOutputStream(file);

            XmlSaver.save(outputStream, test);
            loadedTest = XmlLoader.load(new FileInputStream(file));

            assertEquals(test, loadedTest);
            assertNotSame(test, loadedTest);

            assertEquals(1, CsvSaver.DEFAULT_LINE_SEPARATOR.length());

            for (String separator :  CsvSaver.SEPARATORS) {
                assertEquals(1, separator.length());
                outputStream.close();
                outputStream = new FileOutputStream(file);

                CsvSaver.save(outputStream, test, true, true,
                        CsvSaver.DEFAULT_LINE_SEPARATOR, separator);
                loadedTest = CsvLoader.load(context, new FileInputStream(file), separator.charAt(0),
                        true, true, test.getName());

                assertNotNull(loadedTest);

                loadedTest.setCreatedDate(test.getCreatedDate()); // not supported by csv
                loadedTest.setLastModificationDate(test.getLastModificationDate());

                assertEquals(test, loadedTest);
                assertNotSame(test, loadedTest);
            }

        } catch (IOException | XmlPullParserException e) {
            Log.e(TAG, "xml: error during test", e);
            failed = true;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "xml: error during test", e);
                failed = true;
            }
        }

        if (failed) {
            fail("An error occur, available in the logcat");
        }
    }
}
