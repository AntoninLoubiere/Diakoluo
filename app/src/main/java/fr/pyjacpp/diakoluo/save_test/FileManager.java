package fr.pyjacpp.diakoluo.save_test;

import android.content.Context;

import org.xmlpull.v1.XmlPullParserException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import fr.pyjacpp.diakoluo.tests.Test;

@SuppressWarnings("TryFinallyCanBeTryWithResources") // because not enought API
public class FileManager {
    private static final String TEST_PREFIX = "test_";
    
    // tag constants
    static final String TAG_TEST              = "test";
    static final String TAG_NAME              = "name";
    static final String TAG_DESCRIPTION       = "description";
    static final String TAG_NUMBER_TEST_DID   = "numberTestDid";
    static final String TAG_CREATED_DATE      = "createdDate";
    static final String TAG_LAST_MODIFICATION = "lastModification";
    static final String TAG_COLUMNS           = "columns";
    static final String TAG_COLUMN            = "column";
    static final String TAG_ROWS              = "rows";
    static final String TAG_ROW               = "row";
    static final String TAG_CELL              = "cell";
    static final String TAG_INPUT_TYPE        = "inputType";
    static final String TAG_DEFAULT_VALUE     = "defaultValue";

    public static void save(Context context, Test test) throws IOException {
        FileOutputStream fileOutputStream = context.openFileOutput(TEST_PREFIX + test.getFilename(),
                Context.MODE_PRIVATE);
        try {
            XmlSaver.save(fileOutputStream, test);
        } finally {
            fileOutputStream.close();
        }
    }

    public static Test load(Context context, String filename) throws IOException, XmlPullParserException {
        FileInputStream fileInputStream = context.openFileInput(TEST_PREFIX + filename);

        try {
            Test loadedTest = XmlLoader.load(fileInputStream);
            if (loadedTest != null) {
                loadedTest.setFilename(filename);
            }
            return loadedTest;
        } finally {
            fileInputStream.close();
        }
    }

    public static Test loadFromAsset(Context context, String filename) throws IOException, XmlPullParserException {
        InputStream fileInputStream = context.getAssets().open(filename);

        try {
            Test loadedTest = XmlLoader.load(fileInputStream);
            if (loadedTest != null) {
                loadedTest.setFilename(filename);
            }
            return loadedTest;
        } finally {
            fileInputStream.close();
        }
    }

    public static void getAvailableFilename(Context context, Test test) {
        String extension = ".dkl";
        String name = test.getName()
                .replace(' ', '_')
                .replace('/', '_')
                .replace('.', '_');
        int index = -1;

        String currentFileName;

        do {
            index++;

            if (index > 0) {
                extension = "_" + index + ".dkl";
            }

            currentFileName = name + extension;
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
}
