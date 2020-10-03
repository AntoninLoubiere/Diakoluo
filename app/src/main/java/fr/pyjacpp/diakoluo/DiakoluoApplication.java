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

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import fr.pyjacpp.diakoluo.list_tests.ListTestsFragment;
import fr.pyjacpp.diakoluo.save_test.FileManager;
import fr.pyjacpp.diakoluo.test_tests.TestTestContext;
import fr.pyjacpp.diakoluo.tests.CompactTest;
import fr.pyjacpp.diakoluo.tests.Test;

/**
 * The global context that hold importants variables.
 */
public class DiakoluoApplication extends Application {
    public static final String DEFAULT_TEST = "default.dkl";
    public static final int NO_CURRENT_EDIT_TEST = -2;
    public static final int NEW_CURRENT_EDIT_TEST = -1;
    private static final String GLOBAL_SHARED_PREFERENCES = "diakoluo";
    private static final String PREFERENCES_LIST_TEST_FILENAMES = "testFilenames";
    private static final String PREFERENCES_NUMBER_TEST_CREATED_FILENAMES = "numberTestCreated";
    private static final String USER_PROPERTY_NUMBER_TEST_CREATED = "number_test";
    private static final String ANALYTICS_ENABLE = "analytics";
    private static final int ANALYTIC = 1 << 1;
    private static final int CRASHLYTICS = 1 << 2;
    private static final int ANALYTICS_SET = 1;
    private ArrayList<CompactTest> listTest;
    @Nullable
    private Test currentTest = null;
    @Nullable
    private Test currentEditTest = null;
    @Nullable
    private FileManager.ImportContext currentImportContext = null;
    @Nullable
    private TestTestContext testTestContext = null;
    private int currentEditTestIndex = -1;
    private int currentTestIndex = -1;

    private SharedPreferences sharedPreferences;
    private ArrayList<String> listTestFilename;
    private Thread loadCurrentTestThread;
    private Thread loadCurrentEditTestThread;
    private Thread loadingThread;

    /**
     * Get the instance. If the instance haven't been loaded, this method will be locked
     *
     * @param context the context
     * @return the instance of the diakoluo application
     */
    public static DiakoluoApplication get(Context context) {
        DiakoluoApplication dk = (DiakoluoApplication) context.getApplicationContext();
        if (dk.loadingThread != null) {
            try {
                dk.loadingThread.join();
            } catch (InterruptedException ignored) {
            }
        }
        return dk;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                sharedPreferences = getSharedPreferences(GLOBAL_SHARED_PREFERENCES, Context.MODE_PRIVATE);

                listTest = new ArrayList<>();

                loadFilenameList();

                if (listTestFilename == null) {
                    try {
                        Test test = FileManager.loadFromAsset(DiakoluoApplication.this, DEFAULT_TEST);
                        test.setFilename(null);
                        addTest(test);
                    } catch (IOException | XmlPullParserException e) {
                        e.printStackTrace();
                    }
                }

                FirebaseCrashlytics firebaseCrashlytics = FirebaseCrashlytics.getInstance();
                FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(DiakoluoApplication.this);
                firebaseAnalytics.setAnalyticsCollectionEnabled(getAnalyticsEnable());
                firebaseCrashlytics.setCrashlyticsCollectionEnabled(getCrashlyticsEnable());

                loadCompactsTests();
                loadingThread = null;
            }
        });

        loadingThread.start();
    }

    /**
     * Add a test to the list.
     *
     * @param test the test to add
     */
    public void addTest(final Test test) {
        CompactTest e = new CompactTest(test);
        listTest.add(e);
        saveTest(test);
        e.update(test); // update the filename by the filename given
        save();
    }

    /**
     * Save the current test and update compact test.
     *
     * @see #saveTest(Test)
     * @see #saveTest(Test, int)
     */
    public void saveCurrentTest() {
        if (loadCurrentTestThread != null) {
            try {
                loadCurrentTestThread.join();
            } catch (InterruptedException e) {
                return;
            }
        }

        if (currentTest != null) {
            saveTest(currentTest, currentTestIndex);
        }
    }

    /**
     * Save a test and update the compact test attached.
     *
     * @param test     the test to save
     * @param position the position of the test
     * @see #saveCurrentTest()
     * @see #saveTest(Test)
     */
    public void saveTest(Test test, int position) {
        listTest.get(position).update(test);
        saveTest(test);
        save();
    }

    /**
     * Save a test but don't update the compact test attached.
     *
     * @param test the test top save
     * @see #saveCurrentTest()
     * @see #saveTest(Test, int)
     */
    public void saveTest(@NonNull Test test) {
        if (test.getFilename() == null) {
            FileManager.getAvailableFilename(DiakoluoApplication.this, test);
        }
        try {
            FileManager.saveFromPrivateFile(DiakoluoApplication.this, test);
        } catch (IOException e) {
            Log.e("DiakoluoApplication", "Can't saveFromPrivateFile test " + test.getName());
            e.printStackTrace();
        }
        Log.i("DiakoluoApplication", "Test saved");
    }

    /**
     * Save all non-test data
     */
    public void save() {
        int numberTestCreated = sharedPreferences.getInt(PREFERENCES_NUMBER_TEST_CREATED_FILENAMES, -1);
        Gson gson = new Gson();

        ArrayList<String> listTestFilename = new ArrayList<>();


        for (CompactTest compactTest : listTest) {
            listTestFilename.add(compactTest.getFilename());
        }

        if (listTestFilename.size() != numberTestCreated) {
            FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(DiakoluoApplication.this);
            firebaseAnalytics.setUserProperty(USER_PROPERTY_NUMBER_TEST_CREATED,
                    String.valueOf(listTestFilename.size()));
        }

        String s1 = gson.toJson(listTestFilename);

        sharedPreferences
                .edit()
                .putString(PREFERENCES_LIST_TEST_FILENAMES, s1)
                .putInt(PREFERENCES_NUMBER_TEST_CREATED_FILENAMES, listTestFilename.size())
                .apply();
    }

    // static

    /**
     * Load the filename list.
     *
     * @see #loadCompactsTests()
     */
    public void loadFilenameList() {
        Gson gson = new Gson();
        String testListFilenamesJson;
        try {
            testListFilenamesJson = sharedPreferences.getString(PREFERENCES_LIST_TEST_FILENAMES, null);
        } catch (ClassCastException e) {
            // backward compatibility
            Log.e(getClass().getName(), "BackWard compatibility");
            testListFilenamesJson = null;
        }
        listTestFilename = null;
        if (testListFilenamesJson != null) listTestFilename = gson.fromJson(testListFilenamesJson,
                new TypeToken<ArrayList<String>>() {
                }.getType());

        if (listTestFilename == null) {
            listTestFilename = FileManager.getListFilenameTest(this);
        }
    }

    /**
     * Load a complete test from his position.
     *
     * @param position the position of the test to load
     * @return the test loaded or null if can't load the test
     */
    @Nullable
    public Test loadCompleteTest(int position) {
        return FileManager.loadFromPrivateFile(this, listTest.get(position).getFilename());
    }

    /**
     * Load all compact tests. List of filenames must be loaded.
     *
     * @see #loadFilenameList()
     */
    public void loadCompactsTests() {
        for (String filename : listTestFilename) {
            Test e = FileManager.loadFromPrivateFile(this, filename);
            if (e != null)
                listTest.add(new CompactTest(e));
        }

        Log.i("DiakoluoApplication", "Test loaded");
    }

    /**
     * Remove a test from the position.
     *
     * @param position the position to load
     */
    public void removeTest(int position) {
        CompactTest testRemoved = listTest.remove(position);
        FileManager.delete(this, testRemoved);
    }

    /**
     * Remove a test and add to cache the file delete to recover if necessary.
     *
     * @param position the position to load
     * @return the cache file of the test
     */
    public File removeTestAndCache(int position) {
        CompactTest testRemoved = listTest.remove(position);
        return FileManager.deleteAndCache(this, testRemoved);
    }

    /**
     * Set the current test (and load it asynchronously).
     *
     * @param position the position of the test to load
     * @see #setCurrentEditTest(int)
     * @see #getCurrentTest(GetTest)
     */
    public void setCurrentTest(int position) {
        if (position != currentTestIndex) {
            if (loadCurrentTestThread != null) loadCurrentTestThread.interrupt();
            currentTestIndex = position;
            currentTest = null;

            if (position >= 0) {
                loadCurrentTestThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        currentTest = loadCompleteTest(currentTestIndex);
                        if (currentTest == null) {
                            currentTestIndex = -1;
                        }
                        loadCurrentTestThread = null;
                    }
                });
                loadCurrentTestThread.start();
            }
        }
    }

    /**
     * Get the current test.
     *
     * @param runnable the runnable which is executed when test is gotten.
     * @see #getCurrentEditTest(GetTest)
     * @see #setCurrentTest(int)
     */
    public void getCurrentTest(final GetTest runnable) {
        if (loadCurrentTestThread != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        runnable.privateLoadingInProgress();
                        loadCurrentTestThread.join();
                    } catch (InterruptedException ignored) {
                        runnable.privateError(true);
                        return;
                    }
                    if (currentTest == null) {
                        runnable.privateError(true);
                    } else {
                        runnable.privateSuccess(currentTest, true);
                    }
                }
            }).start();
        } else if (currentTest == null) {
            runnable.privateError(false);
        } else {
            runnable.privateSuccess(currentTest, false);
        }
    }

    /**
     * Get the list of compact test.
     *
     * @return the list of compact test.
     */
    public ArrayList<CompactTest> getListTest() {
        return listTest;
    }

    /**
     * Get the number of test.
     *
     * @return the number of test
     */
    public int getNumberTest() {
        return listTest.size();
    }

    /**
     * Get the test context (in testActivities).
     *
     * @return the test context of a test
     */
    @Nullable
    public TestTestContext getTestTestContext() {
        return testTestContext;
    }

    /**
     * Set the test context of a test (in testActivities).
     *
     * @param testTestContext the test context of a test
     */
    public void setTestTestContext(@Nullable TestTestContext testTestContext) {
        this.testTestContext = testTestContext;
    }

    /**
     * Get the current edit test.
     *
     * @param runnable the runnable which is executed when test is gotten.
     * @see #getCurrentTest(GetTest)
     */
    public void getCurrentEditTest(final GetTest runnable) {
        if (loadCurrentEditTestThread != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        runnable.privateLoadingInProgress();
                        loadCurrentEditTestThread.join();
                    } catch (InterruptedException ignored) {
                        runnable.privateError(true);
                        return;
                    }
                    if (currentEditTest == null) {
                        runnable.privateError(true);
                    } else {
                        runnable.privateSuccess(currentEditTest, true);
                    }
                }
            }).start();
        } else if (currentEditTest == null) {
            runnable.privateError(false);
        } else {
            runnable.privateSuccess(currentEditTest, false);
        }
    }

    /**
     * Apply a currentEditTest.
     */
    public void applyCurrentEditTest() {
        if (loadCurrentEditTestThread != null) {
            try {
                loadCurrentEditTestThread.join();
            } catch (InterruptedException e) {
                return;
            }
        }

        if (currentEditTestIndex == NEW_CURRENT_EDIT_TEST && currentEditTest != null) {
            addTest(currentEditTest);
            setCurrentEditTest(NO_CURRENT_EDIT_TEST);
            Intent intent = new Intent();
            intent.setAction(ListTestsFragment.ACTION_BROADCAST_TEST_ADDED_LIST_RECYCLER);
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
            localBroadcastManager.sendBroadcast(intent);
        } else if (currentEditTestIndex >= 0 && currentEditTest != null) {
            saveTest(currentEditTest, currentEditTestIndex);
            currentEditTest.registerModificationDate();

            if (currentTestIndex == currentEditTestIndex) {
                currentTest = currentEditTest;
            }
            Intent intent = new Intent();
            intent.setAction(ListTestsFragment.ACTION_BROADCAST_UPDATE_INDEX_RECYCLER);
            intent.putExtra(ListTestsFragment.EXTRA_INT_POSITION, currentEditTestIndex);
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
            localBroadcastManager.sendBroadcast(intent);

            setCurrentEditTest(NO_CURRENT_EDIT_TEST);
        }
    }

    /**
     * Set the current edit test.
     *
     * @param position the position of the current edit test, could be also
     *                 {@link #NEW_CURRENT_EDIT_TEST} or {@link #NO_CURRENT_EDIT_TEST}
     * @see #getCurrentEditTest(GetTest)
     * @see #setCurrentTest(int)
     * @see #NO_CURRENT_EDIT_TEST
     * @see #NEW_CURRENT_EDIT_TEST
     */
    public void setCurrentEditTest(int position) {
        if (loadCurrentEditTestThread != null) loadCurrentEditTestThread.interrupt();
        currentEditTestIndex = position;
        currentEditTest = null;

        if (position >= 0 || position == NEW_CURRENT_EDIT_TEST) {
            loadCurrentEditTestThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (currentEditTestIndex == NEW_CURRENT_EDIT_TEST) {
                        currentEditTest = new Test(getString(R.string.test_default_name),
                                getString(R.string.test_default_description));
                    } else if (currentEditTestIndex == currentTestIndex && currentTest != null) {
                        currentEditTest = new Test(currentTest);
                    } else {
                        currentEditTest = loadCompleteTest(currentEditTestIndex);
                    }
                    if (currentEditTest == null) {
                        currentEditTestIndex = NO_CURRENT_EDIT_TEST;
                    }
                    loadCurrentEditTestThread = null;
                }
            });
            loadCurrentEditTestThread.start();
        }
    }

    /**
     * Get the import context.
     *
     * @return the import context
     */
    @Nullable
    public FileManager.ImportContext getCurrentImportContext() {
        return currentImportContext;
    }

    /**
     * Set the current import context.
     *
     * @param currentImportTest the import context to set
     */
    public void setCurrentImportContext(@Nullable FileManager.ImportContext currentImportTest) {
        this.currentImportContext = currentImportTest;
    }

    /**
     * Get the current edit test index.
     *
     * @return the current edit test index
     */
    public int getCurrentEditTestIndex() {
        return currentEditTestIndex;
    }

    /**
     * Get if analytics is enable.
     *
     * @return if analytics is enable
     */
    public boolean getAnalyticsEnable() {
        int i = sharedPreferences.getInt(ANALYTICS_ENABLE, 0);  // -1: not enable, 0 not set, 1: enable

        if ((i & ANALYTICS_SET) == 0) {
            return false;
        } else {
            return (i & ANALYTIC) == ANALYTIC;
        }
    }

    /**
     * Set if analytic is enable.
     *
     * @param b if enable analytic
     */
    public void setAnalyticsEnable(boolean b) {
        int i = sharedPreferences.getInt(ANALYTICS_ENABLE, 0);  // -1: not enable, 0 not set, 1: enable

        int set_i = b ? i | ANALYTIC : i & (~ANALYTIC);

        sharedPreferences.edit().putInt(ANALYTICS_ENABLE, set_i).apply();

        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseAnalytics.setAnalyticsCollectionEnabled(b);
    }

    /**
     * Get if crashlytics is enable.
     *
     * @return if crashlytics is enable
     */
    public boolean getCrashlyticsEnable() {
        int i = sharedPreferences.getInt(ANALYTICS_ENABLE, 0);  // -1: not enable, 0 not set, 1: enable

        if ((i & ANALYTICS_SET) == 0) {
            return false;
        } else {
            return (i & CRASHLYTICS) == CRASHLYTICS;
        }
    }

    /**
     * set if crashlytics is enable.
     *
     * @param b if crashlytics is enable
     */
    public void setCrashlyticsEnable(boolean b) {
        int i = sharedPreferences.getInt(ANALYTICS_ENABLE, 0);  // -1: not enable, 0 not set, 1: enable

        int set_i = b ? i | CRASHLYTICS : i & (~CRASHLYTICS);

        sharedPreferences.edit().putInt(ANALYTICS_ENABLE, set_i).apply();

        FirebaseCrashlytics firebaseCrashlytics = FirebaseCrashlytics.getInstance();
        firebaseCrashlytics.setCrashlyticsCollectionEnabled(b);
    }

    /**
     * Get if analytics has been set.
     *
     * @return if analytics has been set
     */
    public boolean getAnalyticsSet() {
        int i = sharedPreferences.getInt(ANALYTICS_ENABLE, 0);  // -1: not enable, 0 not set, 1: enable

        return (i & ANALYTICS_SET) == ANALYTICS_SET;
    }

    /**
     * Set if analytics has been set.
     *
     * @param b if analytics has been set
     */
    public void setAnalyticsSet(boolean b) {
        int i = sharedPreferences.getInt(ANALYTICS_ENABLE, 0);  // -1: not enable, 0 not set, 1: enable

        int set_i = b ? i | ANALYTICS_SET : i & (~ANALYTICS_SET);

        sharedPreferences.edit().putInt(ANALYTICS_ENABLE, set_i).apply();
    }

    /**
     * A runnable that hold actions when get a test.
     *
     * @see GetTest
     */
    public interface GetTestRunnable {
        /**
         * If a thread is currently loading a test.
         */
        void loadingInProgress();

        /**
         * if an error occur (or a cancel).
         *
         * @param canceled if the error is due to a cancel
         */
        void error(boolean canceled);

        /**
         * If it is a success.
         *
         * @param test the test gotten
         */
        void success(@NonNull Test test);
    }

    /**
     * A class that get a test.
     *
     * @see GetTestRunnable
     */
    public static class GetTest {
        private boolean asyncOnly;
        private LoadingDialogFragment loadingDialogFragment = null;
        private AppCompatActivity appCompatActivity;
        private boolean cancelable;
        private GetTestRunnable runnable;

        /**
         * Get a test with no loading popup.
         *
         * @param asyncOnly if the thread is async only
         * @param runnable  the runnable to send results
         */
        public GetTest(boolean asyncOnly, GetTestRunnable runnable) {
            this(asyncOnly, null, false, runnable);
        }

        /**
         * Get a test with a loading popup.
         *
         * @param asyncOnly         if the thread is async only
         * @param appCompatActivity the activity that will hold the popup
         * @param runnable          the runnable to send results
         */
        public GetTest(boolean asyncOnly, AppCompatActivity appCompatActivity,
                       GetTestRunnable runnable) {
            this(asyncOnly, appCompatActivity, true, runnable);
        }

        /**
         * Get a test with a loading popup.
         *
         * @param asyncOnly         if the thread is async only
         * @param appCompatActivity the activity that will hold the popup
         * @param cancelable        if the popup is cancelable
         * @param runnable          the runnable to send results
         */
        public GetTest(boolean asyncOnly,
                       AppCompatActivity appCompatActivity,
                       boolean cancelable, GetTestRunnable runnable) {
            this.asyncOnly = asyncOnly;
            this.appCompatActivity = appCompatActivity;
            this.cancelable = cancelable;
            this.runnable = runnable;
        }

        /**
         * Send an error response.
         *
         * @param inThread if the function is call in a worker thread (not the ui thread)
         */
        private void privateError(boolean inThread) {
            final boolean canceled;

            if (loadingDialogFragment != null) {
                if (loadingDialogFragment.hasBeenCancel()) {
                    canceled = true;
                } else {
                    canceled = false;
                    loadingDialogFragment.dismiss();
                }
            } else {
                canceled = false;
            }

            if (!inThread && asyncOnly) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runnable.error(canceled);
                    }
                }).start();
            } else {
                runnable.error(canceled);
            }
        }

        /**
         * Send a success response.
         *
         * @param test     the test loaded
         * @param inThread if the function is call in a worker thread (not the ui thread)
         */
        private void privateSuccess(final Test test, boolean inThread) {
            if (loadingDialogFragment != null) {
                if (loadingDialogFragment.hasBeenCancel()) {
                    privateError(inThread);
                    return;
                } else {
                    loadingDialogFragment.dismiss();
                }
            }
            if (!inThread && asyncOnly) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runnable.success(test);
                    }
                }).start();
            } else {
                runnable.success(test);
            }
        }

        /**
         * Send aa loading in progress or show a popup.
         */
        private void privateLoadingInProgress() {
            if (appCompatActivity == null) {
                runnable.loadingInProgress();
            } else {
                loadingDialogFragment = new LoadingDialogFragment(cancelable);
                loadingDialogFragment.show(appCompatActivity.getSupportFragmentManager(), null);
            }
        }
    }
}
