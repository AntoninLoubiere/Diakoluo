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
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import fr.pyjacpp.diakoluo.save_test.FileManager;
import fr.pyjacpp.diakoluo.test_tests.TestTestContext;
import fr.pyjacpp.diakoluo.tests.CompactTest;
import fr.pyjacpp.diakoluo.tests.Test;

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

    private RecyclerViewChange testListChanged;

    private SharedPreferences sharedPreferences;
    private ArrayList<String> listTestFilename;
    private Thread loadCurrentTestThread;
    private Thread loadCurrentEditTestThread;
    private Thread loadingThread;

    public static DiakoluoApplication get(Context context) {
        DiakoluoApplication dk = (DiakoluoApplication) context.getApplicationContext();
        if (dk.loadingThread != null) {
            try {
                dk.loadingThread.join();
            } catch (InterruptedException ignored) {}
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

    public void addTest(final Test test) {
        saveTest(test);

        listTest.add(new CompactTest(test));
        save();
    }

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

    public void saveTest(Test test, int position) {
        saveTest(test);

        listTest.get(position).update(test);
        save();
    }

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

    @Nullable
    public Test loadCompleteTest(int position) {
        return FileManager.loadFromPrivateFile(this, listTestFilename.get(position));
    }

    public void loadCompactsTests() {
        for (String filename : listTestFilename) {
            Test e = FileManager.loadFromPrivateFile(this, filename);
            if (e != null)
                listTest.add(new CompactTest(e));
        }

        Log.i("DiakoluoApplication", "Test loaded");
    }

    public void removeTest(int position) {
        CompactTest testRemoved = listTest.remove(position);
        FileManager.delete(this, testRemoved);
    }

    public File removeTestAndCache(int position) {
        CompactTest testRemoved = listTest.remove(position);
        return FileManager.deleteAndCache(this, testRemoved);
    }

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

    public ArrayList<CompactTest> getListTest() {
        return listTest;
    }

    public int getNumberTest() {
        return listTest.size();
    }

    @Nullable
    public TestTestContext getTestTestContext() {
        return testTestContext;
    }

    public void setTestTestContext(@Nullable TestTestContext testTestContext) {
        this.testTestContext = testTestContext;
    }

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
            RecyclerViewChange recyclerViewChange = new RecyclerViewChange(
                    RecyclerViewChange.ItemInserted
            );
            recyclerViewChange.setPosition(listTest.size() - 1);
            setTestListChanged(recyclerViewChange);
        } else if (currentEditTestIndex >= 0 && currentEditTest != null) {
            saveTest(currentEditTest, currentEditTestIndex);
            currentEditTest.registerModificationDate();

            setCurrentEditTest(NO_CURRENT_EDIT_TEST);
            RecyclerViewChange recyclerViewChange = new RecyclerViewChange(
                    RecyclerViewChange.ItemChanged
            );
            recyclerViewChange.setPosition(currentEditTestIndex);
            setTestListChanged(recyclerViewChange);
        }
    }

    public void setCurrentEditTest(int position) {
        if (loadCurrentEditTestThread != null) loadCurrentEditTestThread.interrupt();
        currentEditTestIndex = position;
        currentEditTest = null;

        if (position >= 0 | position == NEW_CURRENT_EDIT_TEST) {
            loadCurrentEditTestThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (currentTestIndex == NEW_CURRENT_EDIT_TEST) {
                        currentEditTest = new Test(getString(R.string.test_default_name),
                                getString(R.string.test_default_description));
                    } else {
                        currentEditTest = loadCompleteTest(currentTestIndex);
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

    @Nullable
    public FileManager.ImportContext getCurrentImportContext() {
        return currentImportContext;
    }

    public void setCurrentImportContext(@Nullable FileManager.ImportContext currentImportTest) {
        this.currentImportContext = currentImportTest;
    }

    public int getCurrentEditTestIndex() {
        return currentEditTestIndex;
    }

    public RecyclerViewChange getTestListChanged() {
        return testListChanged;
    }

    public void setTestListChanged(RecyclerViewChange testListChanged) {
        this.testListChanged = testListChanged;
    }

    public boolean getAnalyticsEnable() {
        int i = sharedPreferences.getInt(ANALYTICS_ENABLE, 0);  // -1: not enable, 0 not set, 1: enable

        if ((i & ANALYTICS_SET) == 0) {
            return false;
        } else {
            return (i & ANALYTIC) == ANALYTIC;
        }
    }

    public void setAnalyticsEnable(boolean b) {
        int i = sharedPreferences.getInt(ANALYTICS_ENABLE, 0);  // -1: not enable, 0 not set, 1: enable

        int set_i = b ? i | ANALYTIC : i & (~ANALYTIC);

        sharedPreferences.edit().putInt(ANALYTICS_ENABLE, set_i).apply();

        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseAnalytics.setAnalyticsCollectionEnabled(b);
    }

    public boolean getCrashlyticsEnable() {
        int i = sharedPreferences.getInt(ANALYTICS_ENABLE, 0);  // -1: not enable, 0 not set, 1: enable

        if ((i & ANALYTICS_SET) == 0) {
            return false;
        } else {
            return (i & CRASHLYTICS) == CRASHLYTICS;
        }
    }

    public void setCrashlyticsEnable(boolean b) {
        int i = sharedPreferences.getInt(ANALYTICS_ENABLE, 0);  // -1: not enable, 0 not set, 1: enable

        int set_i = b ? i | CRASHLYTICS : i & (~CRASHLYTICS);

        sharedPreferences.edit().putInt(ANALYTICS_ENABLE, set_i).apply();

        FirebaseCrashlytics firebaseCrashlytics = FirebaseCrashlytics.getInstance();
        firebaseCrashlytics.setCrashlyticsCollectionEnabled(b);
    }

    public boolean getAnalyticsSet() {
        int i = sharedPreferences.getInt(ANALYTICS_ENABLE, 0);  // -1: not enable, 0 not set, 1: enable

        return (i & ANALYTICS_SET) == ANALYTICS_SET;
    }

    public void setAnalyticsSet(boolean b) {
        int i = sharedPreferences.getInt(ANALYTICS_ENABLE, 0);  // -1: not enable, 0 not set, 1: enable

        int set_i = b ? i | ANALYTICS_SET : i & (~ANALYTICS_SET);

        sharedPreferences.edit().putInt(ANALYTICS_ENABLE, set_i).apply();
    }

    public interface GetTestRunnable {
        void loadingInProgress();

        void error(boolean canceled);

        void success(@NonNull Test test);
    }

    public static class GetTest {
        private boolean asyncOnly;
        private LoadingDialogFragment loadingDialogFragment = null;
        private AppCompatActivity appCompatActivity;
        private boolean cancelable;
        private GetTestRunnable runnable;

        public GetTest(boolean asyncOnly, GetTestRunnable runnable) {
            this(asyncOnly, null, false, runnable);
        }

        public GetTest(boolean asyncOnly, AppCompatActivity appCompatActivity,
                       GetTestRunnable runnable) {
            this(asyncOnly, appCompatActivity, true, runnable);
        }

        public GetTest(boolean asyncOnly,
                       AppCompatActivity appCompatActivity,
                       boolean cancelable, GetTestRunnable runnable) {
            this.asyncOnly = asyncOnly;
            this.appCompatActivity = appCompatActivity;
            this.cancelable = cancelable;
            this.runnable = runnable;
        }

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
