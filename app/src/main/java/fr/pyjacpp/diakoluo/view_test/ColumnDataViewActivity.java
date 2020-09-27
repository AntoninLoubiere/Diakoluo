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

package fr.pyjacpp.diakoluo.view_test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.Test;

public class ColumnDataViewActivity extends AppCompatActivity
        implements ColumnDataViewFragment.OnFragmentInteractionListener, DiakoluoApplication.GetTestRunnable {

    private int columnIndex;
    private ActionBar actionBar;
    @Nullable
    private Test currentTest;
    private Button previousButton;
    private Button nextButton;
    private TextView navigationTextView;
    private boolean fragmentCreated;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_column_data);

        columnIndex = getIntent().getIntExtra(ColumnDataViewFragment.ARG_COLUMN_INDEX, 0);

        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        navigationTextView = findViewById(R.id.navigationTextView);

        fragmentCreated = savedInstanceState != null;

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }

        DiakoluoApplication.get(this).getCurrentTest(
                new DiakoluoApplication.GetTest(true, this, false,
                        this));


        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSwipeRight();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSwipeLeft();
            }
        });
    }

    private void createFragment() {
        fragmentCreated = true;
        ColumnDataViewFragment fragment = ColumnDataViewFragment.newInstance(
                columnIndex
        );

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fragment_fade_scale_enter, R.anim.fragment_fade_scale_exit)
                .replace(R.id.columnDataEditFragmentContainer, fragment).commit();

        updateNavigation();
    }

    private void updateNavigation() {
        if (currentTest != null) {
            if (actionBar != null) {
                actionBar.setTitle(currentTest.getListColumn().get(columnIndex).getName());
            }

            navigationTextView.setText(getString(R.string.navigation_info, columnIndex + 1,
                    currentTest.getNumberColumn()));

            if (columnIndex > 0) {
                if (!previousButton.isEnabled()) {
                    previousButton.setVisibility(View.VISIBLE);
                    previousButton.setEnabled(true);
                }
                previousButton.setText(currentTest.getListColumn().get(columnIndex - 1).getName());
            } else {
                previousButton.setEnabled(false);
                previousButton.setVisibility(View.GONE);
            }

            if (columnIndex < currentTest.getNumberColumn() - 1) {
                if (!nextButton.isEnabled()) {
                    nextButton.setVisibility(View.VISIBLE);
                    nextButton.setEnabled(true);
                }
                nextButton.setText(currentTest.getListColumn().get(columnIndex + 1).getName());
            } else {
                nextButton.setEnabled(false);
                nextButton.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onSwipeRight() {
        if (columnIndex > 0) {
            columnIndex -= 1;
            createFragment();
            getIntent().putExtra(ColumnDataViewFragment.ARG_COLUMN_INDEX, columnIndex);
        }
    }

    @Override
    public void onSwipeLeft() {
        if (currentTest != null) {
            if (columnIndex < currentTest.getNumberColumn() - 1) {
                columnIndex += 1;
                createFragment();
                getIntent().putExtra(ColumnDataViewFragment.ARG_COLUMN_INDEX, columnIndex);
            }
        }
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
        finish();
        if (!canceled) Log.e(getClass().getName(), "No current test, abort.");
    }

    @Override
    public void success(@NonNull Test test) {
        currentTest = test;
        if (fragmentCreated) {
            updateNavigation();
        } else {
            createFragment();
        }
    }
}
