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

package fr.pyjacpp.diakoluo.test_tests;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.Test;

public class TestSettingsActivity extends AppCompatActivity implements TestSettingsFragment.OnFragmentInteractionListener, DiakoluoApplication.GetTestRunnable {

    @Nullable private Test currentTest;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_settings);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);

            DiakoluoApplication.get(this).getCurrentTest(
                    new DiakoluoApplication.GetTest(false, this, this));
        }
    }

    @Override
    public void onDoTest(int numberQuestionToAsk, int numberColumnToShow, boolean proportionalityScoreMethod) {
        if (currentTest != null) {
            TestTestContext testTestContext = new TestTestContext(currentTest, numberQuestionToAsk,
                    numberColumnToShow, proportionalityScoreMethod);
            DiakoluoApplication.get(this).setTestTestContext(testTestContext);

            Intent intent = new Intent(this, TestActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void errorFinish(boolean canceled) {
        finish();
        if (!canceled) Log.e(getClass().getName(), "No current test, abort.");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void loadingInProgress() {
    }

    @Override
    public void error(boolean canceled) {
        errorFinish(canceled);
    }

    @Override
    public void success(@NonNull Test test) {
        currentTest = test;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                actionBar.setTitle(currentTest.getName());
            }
        });
    }
}
