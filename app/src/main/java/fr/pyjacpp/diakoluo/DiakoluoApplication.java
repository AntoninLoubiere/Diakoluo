package fr.pyjacpp.diakoluo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import fr.pyjacpp.diakoluo.save_test.FileManager;
import fr.pyjacpp.diakoluo.test_tests.TestTestContext;
import fr.pyjacpp.diakoluo.tests.Test;

public class DiakoluoApplication extends Application {
    private static final String GLOBAL_SHARED_PREFERENCES = "diakoluo";
    private static final String PREFERENCES_LIST_TEST_FILENAMES = "testFilenames";
    private static final String DEFAULT_TEST = "default.dkl";

    private ArrayList<Test> listTest;
    private Test currentTest;
    private TestTestContext testTestContext;
    private Test currentEditTest;
    private Integer currentIndexEditTest;
    
    private RecyclerViewChange testListChanged;
    private RecyclerViewChange answerListChanged;
    private RecyclerViewChange columnListChanged;

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences = getSharedPreferences(GLOBAL_SHARED_PREFERENCES, Context.MODE_PRIVATE);

        listTest = new ArrayList<>();

        Set<String> listTestFilename = sharedPreferences.getStringSet(PREFERENCES_LIST_TEST_FILENAMES,
                null);
        if (listTestFilename == null) {
            try {
                listTest.add(FileManager.loadFromAsset(this, DEFAULT_TEST));
                saveTest();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }

        loadTest();
    }

    private void saveTest() {
        Set<String> listTestFilename = sharedPreferences.getStringSet(PREFERENCES_LIST_TEST_FILENAMES,
                null);

        if (listTestFilename == null) {
            listTestFilename = new HashSet<>();
        }

        for (Test test : listTest) {
            if (test.getFilename() == null) {
                FileManager.getAvailableFilename(this, test);
            }
            try {
                FileManager.save(this, test);
                listTestFilename.add(test.getFilename());
            } catch (IOException e) {
                Log.e("DiakoluoApplication", "Can't save test " + test.getName());
                e.printStackTrace();
            }
        }

        sharedPreferences
                .edit()
                .putStringSet(PREFERENCES_LIST_TEST_FILENAMES, listTestFilename)
                .apply();
        Log.i("DiakoluoApplication", "Test saved");
    }

    private void loadTest() {
        Set<String> listTestFilename = sharedPreferences.getStringSet(PREFERENCES_LIST_TEST_FILENAMES,
                new HashSet<String>());

        listTest.clear();

        if (listTestFilename == null) {
            listTestFilename = new HashSet<>();
        }

        for (String filename : listTestFilename) {
            try {
                listTest.add(FileManager.load(this, filename));
            } catch (IOException | XmlPullParserException e){
                Log.e("DiakoluoApplication", "Can't load test " + filename);
                e.printStackTrace();
            }
        }

        Log.i("DiakoluoApplication", "Test loaded");
    }

    private void setCurrentTest(Test currentTest) {
        this.currentTest = currentTest;
    }

    /*private void setCurrentTest(int currentTest) {
        this.currentTest = getListTest().get(currentTest);
    }*/

    private Test getCurrentTest() {
        return currentTest;
    }

    private ArrayList<Test> getListTest() {
        return listTest;
    }

    private TestTestContext getTestTestContext() {
        return testTestContext;
    }

    private void setTestTestContext(TestTestContext testTestContext) {
        this.testTestContext = testTestContext;
    }

    private Test getCurrentEditTest() {
        return currentEditTest;
    }

    private void setCurrentEditTest(Test currentEditTest) {
        this.currentEditTest = currentEditTest;
        currentIndexEditTest = null;
    }

    private Integer getCurrentIndexEditTest() {
        return currentIndexEditTest;
    }

    private void setCurrentIndexEditTest(Integer currentIndexEditTest) {
        if (currentIndexEditTest == null) {
            currentEditTest = null;
            this.currentIndexEditTest = null;
        } else {
            currentEditTest = new Test(listTest.get(currentIndexEditTest));
            this.currentIndexEditTest = currentIndexEditTest;
        }
    }

    private RecyclerViewChange getTestListChanged() {
        return testListChanged;
    }

    private void setTestListChanged(RecyclerViewChange testListChanged) {
        this.testListChanged = testListChanged;
    }
    
    private RecyclerViewChange getAnswerListChanged() {
        return answerListChanged;
    }

    private void setAnswerListChanged(RecyclerViewChange answerListChanged) {
        this.answerListChanged = answerListChanged;
    }

    private RecyclerViewChange getColumnListChanged() {
        return columnListChanged;
    }

    private void setColumnListChanged(RecyclerViewChange columnListChanged) {
        this.columnListChanged = columnListChanged;
    }

    // static

    public static void setCurrentTest(Context context, Test currentTest) {
        ((DiakoluoApplication) context.getApplicationContext()).setCurrentTest(currentTest);
    }

    public static Test getCurrentTest(Context context) {
        return ((DiakoluoApplication) context.getApplicationContext()).getCurrentTest();
    }

    public static ArrayList<Test> getListTest(Context context) {
        return ((DiakoluoApplication) context.getApplicationContext()).getListTest();
    }

    public static TestTestContext getTestTestContext(Context context) {
        return ((DiakoluoApplication) context.getApplicationContext()).getTestTestContext();
    }

    public static void setTestTestContext(Context context,TestTestContext testTestContext) {
        ((DiakoluoApplication) context.getApplicationContext()).setTestTestContext(testTestContext);
    }

    public static Test getCurrentEditTest(Context context) {
        return ((DiakoluoApplication) context.getApplicationContext()).getCurrentEditTest();
    }

    public static void setCurrentEditTest(Context context, Test currentEditTest) {
        ((DiakoluoApplication) context.getApplicationContext()).setCurrentEditTest(currentEditTest);
    }

    public static Integer getCurrentIndexEditTest(Context context) {
        return ((DiakoluoApplication) context.getApplicationContext()).getCurrentIndexEditTest();
    }

    public static void setCurrentIndexEditTest(Context context, Integer currentIndexEditTest) {
        ((DiakoluoApplication) context.getApplicationContext()).setCurrentIndexEditTest(currentIndexEditTest);
    }

    public static void setTestListChanged(Context context, RecyclerViewChange setTestListChanged) {
        ((DiakoluoApplication) context.getApplicationContext()).setTestListChanged(setTestListChanged);
    }

    public static RecyclerViewChange getTestListChanged(Context context) {
        return ((DiakoluoApplication) context.getApplicationContext()).getTestListChanged();
    }

    public static void setAnswerListChanged(Context context, RecyclerViewChange setAnswerListChanged) {
        ((DiakoluoApplication) context.getApplicationContext()).setAnswerListChanged(setAnswerListChanged);
    }

    public static RecyclerViewChange getAnswerListChanged(Context context) {
        return ((DiakoluoApplication) context.getApplicationContext()).getAnswerListChanged();
    }

    public static void setColumnListChanged(Context context, RecyclerViewChange setColumnListChanged) {
        ((DiakoluoApplication) context.getApplicationContext()).setColumnListChanged(setColumnListChanged);
    }

    public static RecyclerViewChange getColumnListChanged(Context context) {
        return ((DiakoluoApplication) context.getApplicationContext()).getColumnListChanged();
    }

    public static void saveTest(Context context) {
        ((DiakoluoApplication) context.getApplicationContext()).saveTest();
    }
    
}
