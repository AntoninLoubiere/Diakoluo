package fr.pyjacpp.diakoluo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

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
    private static final String PREFERENCES_NUMBER_TEST_CREATED_FILENAMES = "numberTestCreated";
    private static final String DEFAULT_TEST = "default.dkl";
    private static final String USER_PROPERTY_NUMBER_TEST_CREATED = "number_test";

    private static final String ANALYTICS_ENABLE = "analytics";
    private static final int ANALYTIC = 1 << 1;
    private static final int CRASHLYTICS = 1 << 2;
    private static final int ANALYTICS_SET = 1;

    private ArrayList<Test> listTest;
    private Test currentTest = null;
    private Test currentEditTest = null;
    private Test currentImportTest = null;
    private TestTestContext testTestContext;
    private Integer currentIndexEditTest;
    
    private RecyclerViewChange testListChanged;

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
                Test test = FileManager.loadFromAsset(this, DEFAULT_TEST);
                test.setFilename(null);
                listTest.add(test);
                saveTest();
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
        }

        FirebaseCrashlytics firebaseCrashlytics = FirebaseCrashlytics.getInstance();
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseAnalytics.setAnalyticsCollectionEnabled(getAnalyticsEnable());
        firebaseCrashlytics.setCrashlyticsCollectionEnabled(getCrashlyticsEnable());

        loadTest();
    }

    private void saveTest() {
        int numberTestCreated = sharedPreferences.getInt(PREFERENCES_NUMBER_TEST_CREATED_FILENAMES, -1);

        Set<String> listTestFilename = new HashSet<>();

        for (Test test : listTest) {
            if (test.getFilename() == null) {
                FileManager.getAvailableFilename(this, test);
            }
            try {
                FileManager.saveFromPrivateFile(this, test);
                listTestFilename.add(test.getFilename());
            } catch (IOException e) {
                Log.e("DiakoluoApplication", "Can't saveFromPrivateFile test " + test.getName());
                e.printStackTrace();
            }
        }

        if (listTestFilename.size() != numberTestCreated) {
            FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);
            firebaseAnalytics.setUserProperty(USER_PROPERTY_NUMBER_TEST_CREATED,
                    String.valueOf(listTestFilename.size()));
        }

        sharedPreferences
                .edit()
                .putStringSet(PREFERENCES_LIST_TEST_FILENAMES, listTestFilename)
                .putInt(PREFERENCES_NUMBER_TEST_CREATED_FILENAMES, listTestFilename.size())
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
                listTest.add(FileManager.loadFromPrivateFile(this, filename));
            } catch (IOException | XmlPullParserException e){
                Log.e("DiakoluoApplication", "Can't load test " + filename);
                e.printStackTrace();
            }
        }

        Log.i("DiakoluoApplication", "Test loaded");
    }

    private void removeTest(int position) {
        Test testRemoved = listTest.remove(position);
        FileManager.delete(this, testRemoved);

        saveTest();
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

    public ArrayList<Test> getListTest() {
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

    public Test getCurrentImportTest() {
        return currentImportTest;
    }

    public void setCurrentImportTest(Test currentImportTest) {
        this.currentImportTest = currentImportTest;
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

    private boolean getAnalyticsEnable() {
        int i = sharedPreferences.getInt(ANALYTICS_ENABLE, 0);  // -1: not enable, 0 not set, 1: enable

        if ((i & ANALYTICS_SET) == 0) {
            return false;
        } else {
            return (i & ANALYTIC) == ANALYTIC;
        }
    }

    private boolean getCrashlyticsEnable() {
        int i = sharedPreferences.getInt(ANALYTICS_ENABLE, 0);  // -1: not enable, 0 not set, 1: enable

        if ((i & ANALYTICS_SET) == 0) {
            return false;
        } else {
            return (i & CRASHLYTICS) == CRASHLYTICS;
        }
    }

    private boolean getAnalyticsSet() {
        int i = sharedPreferences.getInt(ANALYTICS_ENABLE, 0);  // -1: not enable, 0 not set, 1: enable

        return (i & ANALYTICS_SET) == ANALYTICS_SET;
    }

    private void setAnalyticsEnable(boolean b) {
        int i = sharedPreferences.getInt(ANALYTICS_ENABLE, 0);  // -1: not enable, 0 not set, 1: enable

        int set_i = b ? i | ANALYTIC : i & (~ANALYTIC);

        sharedPreferences.edit().putInt(ANALYTICS_ENABLE, set_i).apply();

        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseAnalytics.setAnalyticsCollectionEnabled(b);
    }

    private void setCrashlyticsEnable(boolean b) {
        int i = sharedPreferences.getInt(ANALYTICS_ENABLE, 0);  // -1: not enable, 0 not set, 1: enable

        int set_i = b ? i | CRASHLYTICS : i & (~CRASHLYTICS);

        sharedPreferences.edit().putInt(ANALYTICS_ENABLE, set_i).apply();

        FirebaseCrashlytics firebaseCrashlytics = FirebaseCrashlytics.getInstance();
        firebaseCrashlytics.setCrashlyticsCollectionEnabled(b);
    }

    private void setAnalyticsSet(boolean b) {
        int i = sharedPreferences.getInt(ANALYTICS_ENABLE, 0);  // -1: not enable, 0 not set, 1: enable

        int set_i = b ? i | ANALYTICS_SET : i & (~ANALYTICS_SET);

        sharedPreferences.edit().putInt(ANALYTICS_ENABLE, set_i).apply();
    }

    // static

    public static DiakoluoApplication getDiakoluoApplication(Context context) {
        return  (DiakoluoApplication) context.getApplicationContext();
    }

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

    public static Test getCurrentImportTest(Context context) {
        return ((DiakoluoApplication) context.getApplicationContext()).getCurrentImportTest();
    }

    public static void setCurrentImportTest(Context context, Test currentEditTest) {
        ((DiakoluoApplication) context.getApplicationContext()).setCurrentImportTest(currentEditTest);
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

    public static void saveTest(Context context) {
        ((DiakoluoApplication) context.getApplicationContext()).saveTest();
    }

    public static void removeTest(Context context, int position) {
        ((DiakoluoApplication) context.getApplicationContext()).removeTest(position);
    }

    public static boolean getAnalyticsEnable(Context context) {
        return ((DiakoluoApplication) context.getApplicationContext()).getAnalyticsEnable();
    }

    public static boolean getCrashlyticsEnable(Context context) {
        return ((DiakoluoApplication) context.getApplicationContext()).getCrashlyticsEnable();
    }

    public static boolean getAnalyticsSet(Context context) {
        return ((DiakoluoApplication) context.getApplicationContext()).getAnalyticsSet();
    }

    public static void setAnalyticsEnable(Context context, boolean b) {
        ((DiakoluoApplication) context.getApplicationContext()).setAnalyticsEnable(b);
    }

    public static void setCrashlyticsEnable(Context context, boolean b) {
        ((DiakoluoApplication) context.getApplicationContext()).setCrashlyticsEnable(b);
    }

    public static void setAnalyticsSet(Context context, boolean b) {
        ((DiakoluoApplication) context.getApplicationContext()).setAnalyticsSet(b);
    }

}
