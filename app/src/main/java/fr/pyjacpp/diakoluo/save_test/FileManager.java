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

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.Test;

public class FileManager {
    private static final String TEST_PREFIX = "test_";
    
    // tag constants
    static final String TAG_TEST                    = "test";
    public static final String TAG_NAME             = "name";
    public static final String TAG_DESCRIPTION      = "description";
    static final String TAG_NUMBER_TEST_DID         = "numberTestDid";
    static final String TAG_CREATED_DATE            = "createdDate";
    static final String TAG_LAST_MODIFICATION       = "lastModification";
    static final String TAG_COLUMNS                 = "columns";
    public static final String TAG_COLUMN           = "column";
    static final String TAG_ROWS                    = "rows";
    static final String TAG_ROW                     = "row";
    public static final String TAG_CELL             = "cell";
    public static final String ATTRIBUTE_INPUT_TYPE = "inputType";
    public static final String TAG_DEFAULT_VALUE    = "defaultValue";
    public static final String TAG_SETTINGS         = "settings";

    private static final String MIME_TYPE = "*/*";

    private static final int CREATE_DOCUMENT_REQUEST_CODE = 1;
    private static final int OPEN_DOCUMENT_REQUEST_CODE = 2;
    private static final String DKL_EXTENSION = ".dkl";
    private static final String CSV_EXTENSION = ".csv";
    private static final Boolean BOOLEAN_DIAKOLUO_TYPE = true;

    private static final int NUMBER_LINE_SHOW_CSV = 4;

    private static FileCreateContext fileCreateContext = null;


    public static void saveFromPrivateFile(Context context, Test test) throws IOException {
        try (FileOutputStream fileOutputStream = context.openFileOutput(TEST_PREFIX + test.getFilename(),
                Context.MODE_PRIVATE)) {
            XmlSaver.save(fileOutputStream, test);
        }
    }

    public static Test loadFromPrivateFile(Context context, String filename) throws IOException, XmlPullParserException {

        try (FileInputStream fileInputStream = context.openFileInput(TEST_PREFIX + filename)) {
            Test loadedTest = XmlLoader.load(fileInputStream);
            if (loadedTest != null) {
                loadedTest.setFilename(filename);
            }
            return loadedTest;
        }
    }

    public static Test loadFromAsset(Context context, String filename) throws IOException, XmlPullParserException {

        try (InputStream fileInputStream = context.getAssets().open(filename)) {
            Test loadedTest = XmlLoader.load(fileInputStream);
            if (loadedTest != null) {
                loadedTest.setFilename(filename);
            }
            return loadedTest;
        }
    }

    public static void getAvailableFilename(Context context, Test test) {
        String fileExtension = DKL_EXTENSION;
        String name = test.getDefaultFilename();
        int index = -1;

        String currentFileName;

        do {
            index++;

            if (index > 0) {
                fileExtension = "_" + index + DKL_EXTENSION;
            }

            currentFileName = name + fileExtension;
        } while (filenameExist(context, currentFileName) && (test.getFilename() == null || !test.getFilename().equals(currentFileName)));
        test.setFilename(currentFileName);
    }

    private static boolean filenameExist(Context context, String filename) {
        filename = TEST_PREFIX + filename;
        for (String currentFilename : context.fileList()) {
            if (filename.equals(currentFilename)) {
                return true;
            }
        }

        return false;
    }

    public static void delete(Context context, Test testRemoved) {
        context.deleteFile(TEST_PREFIX + testRemoved.getFilename());
    }

    public static void exportXmlTest(Activity activity, int position, boolean saveNumberTestDone) {
        if (fileCreateContext == null) {
            Test testToSave = DiakoluoApplication.getListTest(activity).get(position);
            fileCreateContext = new XmlCreateContext(position, saveNumberTestDone);

            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.setType(MIME_TYPE);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_TITLE, testToSave.getDefaultFilename() + DKL_EXTENSION);

            activity.startActivityForResult(intent, CREATE_DOCUMENT_REQUEST_CODE);
        } else {
            Log.w("FileManager", "Can't export test, already waiting result");
        }
    }

    public static void exportCsvTest(Activity activity, int position, boolean columnHeader, boolean columnTypeHeader, String separator, String lineSeparator) {
        if (fileCreateContext == null) {
            Test testToSave = DiakoluoApplication.getListTest(activity).get(position);
            fileCreateContext = new CsvCreateContext(position, columnHeader, columnTypeHeader, separator, lineSeparator);

            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.setType(MIME_TYPE);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_TITLE, testToSave.getDefaultFilename() + CSV_EXTENSION);

            activity.startActivityForResult(intent, CREATE_DOCUMENT_REQUEST_CODE);
        } else {
            Log.w("FileManager", "Can't export test, already waiting result");
        }
    }

    public static void importTest(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType(MIME_TYPE);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        activity.startActivityForResult(intent, OPEN_DOCUMENT_REQUEST_CODE);
    }

    public static void exportTestResult(Activity activity, int requestCode, int resultCode,
                                        @Nullable Intent data, @Nullable View snackbarAnchorView, ResultListener resultListener) { // activity must call this in activity on result
        if (requestCode == CREATE_DOCUMENT_REQUEST_CODE) {
            // process create document
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri uri = data.getData();

                    if (uri != null) {
                        try {
                            Test testToSave = DiakoluoApplication.getListTest(activity).get(fileCreateContext.position);
                            ParcelFileDescriptor pfd = activity.getContentResolver().openFileDescriptor(uri, "w");

                            if (pfd != null) {
                                FileOutputStream fos = new FileOutputStream(pfd.getFileDescriptor());
                                try {
                                    if (fileCreateContext instanceof  XmlCreateContext) {
                                        XmlSaver.save(fos, testToSave, ((XmlCreateContext) fileCreateContext).saveNumberTestDone);
                                    } else if (fileCreateContext instanceof CsvCreateContext) {
                                        CsvCreateContext fileCreateContext = (CsvCreateContext) FileManager.fileCreateContext;
                                        CsvSaver.save(
                                                fos,
                                                testToSave,
                                                fileCreateContext.columnHeader,
                                                fileCreateContext.columnTypeHeader,
                                                fileCreateContext.lineSeparator,
                                                fileCreateContext.separator);
                                    } else {
                                        throw new IllegalStateException("File create context has a incorrect type");
                                    }
                                } finally {
                                    fos.close();
                                    pfd.close();
                                }
                                Snackbar.make(activity.findViewById(android.R.id.content), R.string.test_exported, BaseTransientBottomBar.LENGTH_LONG)
                                        .setAnchorView(snackbarAnchorView).show();
                            }
                        } catch (IOException e) {
                            new MaterialAlertDialogBuilder(activity)
                                    .setTitle(R.string.dialog_export_error_title)
                                    .setMessage(R.string.dialog_export_error_message)
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .setIcon(R.drawable.ic_error_red_24dp)
                                    .show();
                        }
                    }
                }
            }
            fileCreateContext = null; // reset test position to allow another export
        } else if (requestCode == OPEN_DOCUMENT_REQUEST_CODE) {
            // process open document
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri uri = data.getData();

                    if (uri != null) {
                        InputStream inputStream = null;
                        try {
                            inputStream = activity.getContentResolver().openInputStream(uri);

                            if (inputStream == null) {
                                throw new IOException("Error");
                            }

                            Boolean b = detectFileType(inputStream);

                            inputStream.close();
                            // reset inputStream
                            inputStream =
                                    activity.getContentResolver().openInputStream(uri);

                            if (inputStream == null) {
                                throw new IOException("Error");
                            }

                            if (b == null) {
                                throw new IOException("Type not detected");
                            } else if (b == BOOLEAN_DIAKOLUO_TYPE) {
                                // Process .dkl
                                Test testLoaded = XmlLoader.load(inputStream);
                                if (testLoaded.isValid()) {
                                    resultListener.showXmlImportDialog(new ImportXmlContext(testLoaded));
                                } else {
                                    throw new IOException("Test not imported");
                                }
                            } else {
                                // Process .csv
                                String[] firstsLines= new String[NUMBER_LINE_SHOW_CSV + 1];
                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                                String line;
                                int lineCount = -1;
                                while ((line = bufferedReader.readLine()) != null) {
                                    lineCount += 1;
                                    if (lineCount < NUMBER_LINE_SHOW_CSV) {
                                        firstsLines[lineCount] = line;
                                    } else {
                                        firstsLines[NUMBER_LINE_SHOW_CSV] = "...";
                                        break;
                                    }
                                }

                                resultListener.showCsvImportDialog(new ImportCsvContext(firstsLines, uri));
                            }
                        } catch (IOException | ClassCastException | XmlPullParserException e) {
                            new MaterialAlertDialogBuilder(activity)
                                    .setTitle(R.string.dialog_import_error_title)
                                    .setMessage(R.string.dialog_import_error_message)
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .setIcon(R.drawable.ic_error_red_24dp)
                                    .show();
                            e.printStackTrace();
                        } finally {
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (IOException ignored) {
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static Boolean detectFileType(InputStream reader) throws IOException {
        int i_char;
        char c;
        Boolean result = null;
        while ((i_char = reader.read()) != -1) {
            c = (char) i_char;
            if (c == '<') {
                result = BOOLEAN_DIAKOLUO_TYPE;
                break;
            }
            else if (c != '\n' && c != ' ' && c != '\t') {
                result = !BOOLEAN_DIAKOLUO_TYPE;
                break;
            }
        }
        return result; // file empty
    }

    private static class FileCreateContext {
        private final int position;

        FileCreateContext(int position) {
            this.position = position;
        }
    }

    private static class XmlCreateContext extends FileCreateContext{
        private final boolean saveNumberTestDone;

        XmlCreateContext(int position, boolean saveNumberTestDone) {
            super(position);
            this.saveNumberTestDone = saveNumberTestDone;
        }
    }

    private static class CsvCreateContext extends FileCreateContext{
        private final boolean columnHeader;
        private final boolean columnTypeHeader;
        private final String separator;
        private final String lineSeparator;


        CsvCreateContext(int position, boolean columnHeader, boolean columnTypeHeader, String separator, String lineSeparator) {
            super(position);
            this.columnHeader = columnHeader;
            this.columnTypeHeader = columnTypeHeader;
            this.separator = separator;
            this.lineSeparator = lineSeparator;
        }
    }

    public static class ImportContext {
    }

    public static class ImportXmlContext extends ImportContext {
        public final Test importTest;

        ImportXmlContext(Test importTest) {
            this.importTest = importTest;
        }
    }

    public static class ImportCsvContext extends ImportContext {
        public final String[] firstLines;
        public final Uri fileUri;

        ImportCsvContext(String[] firstLines, Uri fileUri) {
            this.firstLines = firstLines;
            this.fileUri = fileUri;
        }
    }

    public interface  ResultListener {
        void showXmlImportDialog(ImportXmlContext importXmlContext);
        void showCsvImportDialog(ImportCsvContext importCsvContext);
    }
}
