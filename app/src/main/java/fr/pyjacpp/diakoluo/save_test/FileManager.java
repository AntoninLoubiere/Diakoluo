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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.Utils;
import fr.pyjacpp.diakoluo.tests.CompactTest;
import fr.pyjacpp.diakoluo.tests.Test;

/**
 * A class that hold all useful constant to write and load test (without specific column tag that
 * are defined by the respective class).
 */
public class FileManager {
    // version constants
    public static final int VER_V_0_3_0 = 1;
    public static final int VER_ACTUAL = VER_V_0_3_0;

    // tag constants
    public static final String TAG_TEST = "test";
    public static final String TAG_NAME = "name";
    public static final String TAG_DESCRIPTION = "description";
    public static final String TAG_NUMBER_TEST_DID = "numberTestDid";
    public static final String TAG_CREATED_DATE = "createdDate";
    public static final String TAG_LAST_MODIFICATION = "lastModification";
    public static final String TAG_COLUMNS = "columns";
    public static final String TAG_COLUMN = "column";
    public static final String TAG_ROWS = "rows";
    public static final String TAG_ROW = "row";
    public static final String TAG_CELL = "cell";
    public static final String TAG_DEFAULT_VALUE = "defaultValue";
    public static final String TAG_SETTINGS = "settings";
    public static final String TAG_SCORE_RIGHT = "scoreRight";
    public static final String TAG_SCORE_WRONG = "scoreWrong";
    public static final String TAG_SCORE_SKIPPED = "scoreSkipped";
    public static final String TAG_SCORE_METHOD = "scoreMethod";
    public static final String ATTRIBUTE_VERSION = "version";
    public static final String ATTRIBUTE_INPUT_TYPE = "inputType";

    private static final String MIME_TYPE = "*/*";

    private static final String TEST_PREFIX = "test_";
    private static final String EDIT_TEST_FILE = "edit_test";

    private static final int CREATE_DOCUMENT_REQUEST_CODE = 1;
    private static final int OPEN_DOCUMENT_REQUEST_CODE = 2;
    private static final String DKL_EXTENSION = ".dkl";
    private static final String CSV_EXTENSION = ".csv";
    private static final Boolean BOOLEAN_DIAKOLUO_TYPE = true;

    private static final int NUMBER_LINE_SHOW_CSV = 4;

    private static FileCreateContext fileCreateContext = null;


    /**
     * Save a test in a private file.
     *
     * @param context the context of the application
     * @param test    the test to save
     * @throws IOException if an IOException occur while writing the file
     */
    public static void saveInPrivateFile(Context context, Test test) throws IOException {
        try (FileOutputStream fileOutputStream = context.openFileOutput(TEST_PREFIX + test.getFilename(),
                Context.MODE_PRIVATE)) {
            XmlSaver.save(fileOutputStream, test);
        }
    }

    /**
     * Save the edit test in a private file.
     *
     * @param context  the context of the application
     * @param editTest the test to save
     * @throws IOException if an IOException occur while writing the file
     */
    public static void saveCurrentEditTest(Context context, Test editTest) throws IOException {
        try (FileOutputStream fileOutputStream = context.openFileOutput(EDIT_TEST_FILE,
                Context.MODE_PRIVATE)) {
            XmlSaver.save(fileOutputStream, editTest);
        }
    }

    /**
     * Load a test from a private file.
     *
     * @param context  the context of the application
     * @param filename the name of the file to load
     * @return the test loaded or null if an error occur
     */
    @Nullable
    public static Test loadFromPrivateFile(Context context, String filename) {
        try (FileInputStream fileInputStream = context.openFileInput(TEST_PREFIX + filename)) {
            Test loadedTest = XmlLoader.load(fileInputStream);
            if (loadedTest != null) {
                loadedTest.setFilename(filename);
            }
            return loadedTest;
        } catch (IOException | XmlPullParserException e) {
            Log.e("DiakoluoApplication", "Can't load test " + filename, e);
            return null;
        }
    }

    /**
     * Load the current edit test.
     *
     * @param context the context of the application
     * @return the test loaded or null if an error occur
     */
    @Nullable
    public static Test loadCurrentEditTest(Context context) {
        try (FileInputStream fileInputStream = context.openFileInput(EDIT_TEST_FILE)) {
            return XmlLoader.load(fileInputStream);
        } catch (IOException | XmlPullParserException e) {
            Log.e("DiakoluoApplication", "Can't load the edit test", e);
            return null;
        }
    }

    /**
     * Load a test from an asset.
     *
     * @param context  the context of the application
     * @param filename the name of the file to load
     * @return the test loaded
     * @throws IOException            if an exception occur while reading the file
     * @throws XmlPullParserException if an exception occur while reading the file
     */
    public static Test loadFromAsset(Context context, String filename) throws IOException, XmlPullParserException {

        try (InputStream fileInputStream = context.getAssets().open(filename)) {
            Test loadedTest = XmlLoader.load(fileInputStream);
            if (loadedTest != null) {
                loadedTest.setFilename(filename);
            }
            return loadedTest;
        }
    }

    /**
     * Remove the file that hold the edit test.
     * @param context the context
     */
    public static void deleteCurrentEditTest(Context context) {
        context.deleteFile(EDIT_TEST_FILE);
    }

    /**
     * Get an available filename in private files.
     *
     * @param context the context of the application
     * @param test    the test to find a filename available
     */
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

    /**
     * Verify if a filename exist in privates files.
     *
     * @param context  the context of the application
     * @param filename the name to verify
     * @return if the filename already exist or not
     */
    private static boolean filenameExist(Context context, String filename) {
        filename = TEST_PREFIX + filename;
        for (String currentFilename : context.fileList()) {
            if (filename.equals(currentFilename)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get all filename that represent a test.
     *
     * @param context the context
     * @return all filename
     */
    public static ArrayList<String> getListFilenameTest(Context context) {
        ArrayList<String> strings = new ArrayList<>();
        for (String f : context.fileList()) {
            if (f.startsWith(TEST_PREFIX)) strings.add(f.substring(TEST_PREFIX.length()));
        }
        return strings;
    }

    /**
     * Delete a private file.
     *
     * @param context     the context of the application
     * @param testRemoved the test removed
     */
    public static void delete(Context context, CompactTest testRemoved) {
        context.deleteFile(TEST_PREFIX + testRemoved.getFilename());
    }

    /**
     * Delete a private file. And cache the file deleted to recover if needed.
     *
     * @param context     the context of the application
     * @param testRemoved the test removed
     * @return get the cache file that hold the deleted test
     */
    public static File deleteAndCache(Context context, CompactTest testRemoved) {
        File tempFile = null;
        FileOutputStream fileOutputStream = null;
        try {
            tempFile = File.createTempFile(TEST_PREFIX, DKL_EXTENSION, context.getCacheDir());
            fileOutputStream = new FileOutputStream(tempFile);
            FileInputStream testInputStream = null;
            try {
                testInputStream = context.openFileInput(TEST_PREFIX + testRemoved.getFilename());
                Utils.copyStream(testInputStream, fileOutputStream);
            } catch (IOException e) {
                Log.e("FileManager", "Can't read/copy test file");
            } finally {
                if (testInputStream != null) {
                    try {
                        testInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            Log.e("FileManager", "Can't create a temp file");
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        delete(context, testRemoved);
        return tempFile;
    }

    /**
     * Export the current test as xml file.
     *
     * @param activity           the activity that export the test
     * @param saveNumberTestDone if save the number of test done
     */
    public static void exportXmlTest(final AppCompatActivity activity, final boolean saveNumberTestDone) {
        if (fileCreateContext == null) {
            DiakoluoApplication diakoluoApplication = DiakoluoApplication.get(activity);
            diakoluoApplication.getCurrentTest(
                    new DiakoluoApplication.GetTest(true, activity,
                            new DiakoluoApplication.GetTestRunnable() {
                                @Override
                                public void loadingInProgress() {

                                }

                                @Override
                                public void error(boolean canceled) {
                                    if (!canceled)
                                        showError(activity, R.string.dialog_export_error_title,
                                                R.string.dialog_error_cant_load_test);
                                }

                                @Override
                                public void success(@NonNull Test test) {
                                    fileCreateContext = new XmlCreateContext(saveNumberTestDone);

                                    Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                                    intent.setType(MIME_TYPE);
                                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                                    intent.putExtra(Intent.EXTRA_TITLE, test.getDefaultFilename() + DKL_EXTENSION);

                                    activity.startActivityForResult(intent, CREATE_DOCUMENT_REQUEST_CODE);
                                }
                            }));
        } else {
            Log.w("FileManager", "Can't export test, already waiting result");
        }
    }

    /**
     * Export the current test as csv file.
     *
     * @param activity         the activity that export the test
     * @param columnHeader     if columns name need to be saved
     * @param columnTypeHeader if column type need to be save
     * @param separator        the separator of cell to use
     * @param lineSeparator    the line separator to use
     */
    public static void exportCsvTest(final AppCompatActivity activity, final boolean columnHeader, final boolean columnTypeHeader, final String separator, final String lineSeparator) {
        if (fileCreateContext == null) {
            DiakoluoApplication diakoluoApplication = DiakoluoApplication.get(activity);
            diakoluoApplication.getCurrentTest(
                    new DiakoluoApplication.GetTest(false, activity,
                            new DiakoluoApplication.GetTestRunnable() {

                                @Override
                                public void loadingInProgress() {
                                }

                                @Override
                                public void error(boolean canceled) {
                                    if (!canceled)
                                        showError(activity, R.string.dialog_export_error_title,
                                                R.string.dialog_error_cant_load_test);
                                }

                                @Override
                                public void success(@NonNull Test test) {
                                    fileCreateContext = new CsvCreateContext(columnHeader, columnTypeHeader, separator, lineSeparator);

                                    Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                                    intent.setType(MIME_TYPE);
                                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                                    intent.putExtra(Intent.EXTRA_TITLE,
                                            test.getDefaultFilename() + CSV_EXTENSION);

                                    activity.startActivityForResult(intent, CREATE_DOCUMENT_REQUEST_CODE);
                                }
                            }));

        } else {
            Log.w("FileManager", "Can't export test, already waiting result");
        }
    }

    /**
     * Import a test.
     *
     * @param activity the activity which import the test
     */
    public static void importTest(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType(MIME_TYPE);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        activity.startActivityForResult(intent, OPEN_DOCUMENT_REQUEST_CODE);
    }

    /**
     * Activities that export and import test must call this method
     *
     * @param activity           the activity which import and/or export test
     * @param requestCode        the request code of the activity result
     * @param resultCode         the result code of the activity result function
     * @param data               the data given by the activity result
     * @param snackbarAnchorView if the snackbar need to be anchored
     * @param resultListener     the result listener to receive results
     */
    public static void exportTestResult(final AppCompatActivity activity, int requestCode, int resultCode,
                                        @Nullable Intent data, @Nullable final View snackbarAnchorView,
                                        ResultListener resultListener) { // activity must call this in activity on result
        if (requestCode == CREATE_DOCUMENT_REQUEST_CODE) {
            // process create document
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    final Uri uri = data.getData();

                    if (uri != null) {
                        DiakoluoApplication.get(activity).getCurrentTest(
                                new DiakoluoApplication.GetTest(false, activity,
                                        new DiakoluoApplication.GetTestRunnable() {
                                            @Override
                                            public void loadingInProgress() {

                                            }

                                            @Override
                                            public void error(boolean canceled) {
                                                if (!canceled)
                                                    showError(activity, R.string.dialog_export_error_title,
                                                            R.string.dialog_error_cant_load_test);
                                            }

                                            @Override
                                            public void success(@NonNull Test test) {
                                                onExportTest(activity, snackbarAnchorView, uri, test);
                                            }
                                        }));
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
                        onImportTest(activity, resultListener, uri);
                    }
                }
            }
        }
    }

    /**
     * When a import occur.
     *
     * @param activity       the activity that create the import
     * @param resultListener the result callback
     * @param uri            the uri where the file is imported
     */
    private static void onImportTest(Activity activity, ResultListener resultListener, Uri uri) {
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
                if (testLoaded != null && testLoaded.isValid()) {
                    resultListener.showXmlImportDialog(new ImportXmlContext(testLoaded));
                } else {
                    throw new IOException("Test not imported");
                }
            } else {
                // Process .csv
                String[] firstsLines = new String[NUMBER_LINE_SHOW_CSV + 1];
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
            showError(activity, R.string.dialog_import_error_title, R.string.dialog_import_error_message);
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

    /**
     * When an export is in progress.
     *
     * @param activity           the activity that create the export
     * @param snackbarAnchorView a optional anchor for the respond snack bar
     * @param uri                the uri of the file where export
     * @param testToSave         the test to export
     */
    private static void onExportTest(Activity activity, @Nullable View snackbarAnchorView, Uri uri,
                                     Test testToSave) {
        try {
            ParcelFileDescriptor pfd = activity.getContentResolver().openFileDescriptor(uri, "w");

            if (pfd != null) {
                FileOutputStream fos = new FileOutputStream(pfd.getFileDescriptor());
                try {
                    if (fileCreateContext instanceof XmlCreateContext) {
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
            showError(activity, R.string.dialog_export_error_title, R.string.dialog_export_error_message);
        }
    }

    /**
     * Show an error dialog.
     *
     * @param activity       the activity that is show
     * @param titleRes       the title of the error
     * @param descriptionRes the description of the error
     */
    private static void showError(Activity activity,
                                  @StringRes int titleRes, @StringRes int descriptionRes) {
        new MaterialAlertDialogBuilder(activity)
                .setTitle(titleRes)
                .setMessage(descriptionRes)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setIcon(R.drawable.ic_error_red_24dp)
                .show();
    }

    /**
     * Detect the file type by starting reading the file
     *
     * @param reader the reader that represent the file to detect type
     * @return the file detected
     * @throws IOException if an exception occur while detecting the file type
     */
    private static Boolean detectFileType(InputStream reader) throws IOException {
        int i_char;
        char c;
        Boolean result = null;
        while ((i_char = reader.read()) != -1) {
            c = (char) i_char;
            if (c == '<') {
                result = BOOLEAN_DIAKOLUO_TYPE;
                break;
            } else if (c != '\n' && c != ' ' && c != '\t') {
                result = !BOOLEAN_DIAKOLUO_TYPE;
                break;
            }
        }
        return result; // file empty
    }

    /**
     * Copy a test from a file.
     *
     * @param context  the context
     * @param testFile the file to copy
     * @param filename the test to save
     * @throws IOException if an exception occur
     */
    public static void copyTestFromFile(Context context, File testFile, String filename) throws IOException {
        Utils.copyStream(new FileInputStream(testFile),
                context.openFileOutput(
                        TEST_PREFIX + filename, Context.MODE_PRIVATE));
    }

    /**
     * The result listener.
     */
    public interface ResultListener {
        /**
         * Show the xml import dialog
         *
         * @param importXmlContext the context of the import
         */
        void showXmlImportDialog(ImportXmlContext importXmlContext);

        /**
         * Show the xml import dialog
         *
         * @param importCsvContext the context of the import
         */
        void showCsvImportDialog(ImportCsvContext importCsvContext);
    }

    /**
     * The file create context that hold the position of the test to save.
     */
    private static class FileCreateContext {

        /**
         * Default constructor.
         */
        FileCreateContext() {
        }
    }

    /**
     * The file create context of a xml file.
     */
    private static class XmlCreateContext extends FileCreateContext {
        private final boolean saveNumberTestDone;

        /**
         * Default constructor.
         *
         * @param saveNumberTestDone if number of test done need to be save
         */
        XmlCreateContext(boolean saveNumberTestDone) {
            super();
            this.saveNumberTestDone = saveNumberTestDone;
        }
    }

    /**
     * The file create context of a csv file.
     */
    private static class CsvCreateContext extends FileCreateContext {
        private final boolean columnHeader;
        private final boolean columnTypeHeader;
        private final String separator;
        private final String lineSeparator;


        /**
         * Default constructor.
         *
         * @param columnHeader     if the name of columns need to be save
         * @param columnTypeHeader if the type of column need to be save
         * @param separator        the separator of cell in a csv line
         * @param lineSeparator    the separator of lines
         */
        CsvCreateContext(boolean columnHeader, boolean columnTypeHeader, String separator, String lineSeparator) {
            super();
            this.columnHeader = columnHeader;
            this.columnTypeHeader = columnTypeHeader;
            this.separator = separator;
            this.lineSeparator = lineSeparator;
        }
    }

    /**
     * A context to import a file.
     */
    public static class ImportContext {
    }

    /**
     * A import xml context.
     */
    public static class ImportXmlContext extends ImportContext {
        public final Test importTest;

        /**
         * Default constructor.
         *
         * @param importTest the test loaded
         */
        ImportXmlContext(Test importTest) {
            this.importTest = importTest;
        }
    }

    /**
     * The csv import context.
     */
    public static class ImportCsvContext extends ImportContext {
        public final String[] firstLines;
        public final Uri fileUri;

        /**
         * Default constructor
         *
         * @param firstLines first lines of the files. The size is {@link #NUMBER_LINE_SHOW_CSV}
         * @param fileUri    the file uri to load
         * @see #NUMBER_LINE_SHOW_CSV
         */
        ImportCsvContext(String[] firstLines, Uri fileUri) {
            this.firstLines = firstLines;
            this.fileUri = fileUri;
        }
    }
}
