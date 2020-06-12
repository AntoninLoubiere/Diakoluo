package fr.pyjacpp.diakoluo.save_test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import androidx.annotation.Nullable;

import org.xmlpull.v1.XmlPullParserException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.tests.Test;

public class FileManager {
    private static final String TEST_PREFIX = "test_";
    
    // tag constants
    static final String TAG_TEST                 = "test";
    static final String TAG_NAME                 = "name";
    static final String TAG_DESCRIPTION          = "description";
    static final String TAG_NUMBER_TEST_DID      = "numberTestDid";
    static final String TAG_CREATED_DATE         = "createdDate";
    static final String TAG_LAST_MODIFICATION    = "lastModification";
    static final String TAG_COLUMNS              = "columns";
    static final String TAG_COLUMN               = "column";
    static final String TAG_ROWS                 = "rows";
    static final String TAG_ROW                  = "row";
    public static final String TAG_CELL          = "cell";
    static final String TAG_INPUT_TYPE           = "inputType";
    public static final String TAG_DEFAULT_VALUE = "defaultValue";

    private static final String MIME_TYPE = "application/dkl";

    private static final int CREATE_DOCUMENT_REQUEST_CODE = 1;
    public static final String extension = ".dkl";
    private static int currentTestPosition = -1;


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
        String extention = extension;
        String name = test.getDefaultFilename(false);
        int index = -1;

        String currentFileName;

        do {
            index++;

            if (index > 0) {
                extention = "_" + index + ".dkl";
            }

            currentFileName = name + extention;
        } while (filenameExist(context, currentFileName) && !test.getFilename().equals(currentFileName));
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

    public static void exportTest(Activity activity, int position) {
        if (currentTestPosition < 0) {
            Test testToSave = DiakoluoApplication.getListTest(activity).get(position);
            currentTestPosition = position;

            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            // TODO: intent.setType(MIME_TYPE);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_TITLE, testToSave.getDefaultFilename(true));

            activity.startActivityForResult(intent, CREATE_DOCUMENT_REQUEST_CODE);
        } else {
            Log.w("FileManager", "Can't export test, already wainting result");
        }
    }

    public static void exportTestResult(Activity activity, int requestCode, int resultCode,
                                        @Nullable Intent data) { // activity must call this in activity on result
        if (requestCode == CREATE_DOCUMENT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri uri = data.getData();

                    if (uri != null) {
                        try {
                            Test testToSave = DiakoluoApplication.getListTest(activity).get(currentTestPosition);
                            ParcelFileDescriptor pfd = activity.getContentResolver().openFileDescriptor(uri, "w");

                            if (pfd != null) {
                                FileOutputStream fos = new FileOutputStream(pfd.getFileDescriptor());
                                try {
                                    XmlSaver.save(fos, testToSave, false);
                                } finally {
                                    fos.close();
                                    pfd.close();
                                }
                            }
                        } catch (IOException e) {
                            // TODO: error
                        }
                    }
                }
            }
            currentTestPosition = -1; // reset test position to allow another export
        }
    }
}
