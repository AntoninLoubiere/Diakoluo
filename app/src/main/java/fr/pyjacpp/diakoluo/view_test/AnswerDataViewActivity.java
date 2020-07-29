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

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.tests.data.DataCell;

public class AnswerDataViewActivity extends AppCompatActivity implements AnswerDataViewFragment.OnFragmentInteractionListener{

    private Button previousButton;
    private Button nextButton;
    private TextView navigationTextView;
    private ActionBar actionBar;

    private int answerIndex;
    private Test currentTest;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_answer_data);

        answerIndex = getIntent().getIntExtra(AnswerDataViewFragment.ARG_ANSWER_INDEX, 0);
        currentTest = DiakoluoApplication.getCurrentTest(this);

        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        navigationTextView = findViewById(R.id.navigationTextView);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }

        if (savedInstanceState == null) {
            createFragment();
        } else {
            updateNavigation();
        }

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
        AnswerDataViewFragment fragment = AnswerDataViewFragment.newInstance(
                answerIndex
        );

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fragment_fade_scale_enter, R.anim.fragment_fade_scale_exit)
                .replace(R.id.answerDataViewFragmentContainer, fragment).commit();

        updateNavigation();
    }

    private void updateNavigation() {
        if (actionBar != null) {
            DataCell firstCell = currentTest.getRowFirstCell(answerIndex);
            if (firstCell == null)
                actionBar.setTitle(R.string.app_name);
            else
                actionBar.setTitle(firstCell.getStringValue());
        }

        navigationTextView.setText(getString(R.string.navigation_info, answerIndex + 1,
                currentTest.getNumberRow()));


        if (answerIndex > 0) {
            if (!previousButton.isEnabled()) {
                previousButton.setVisibility(View.VISIBLE);
                previousButton.setEnabled(true);
            }
            DataCell firstCell = currentTest.getRowFirstCell(answerIndex - 1);
            if (firstCell == null)
                previousButton.setText(R.string.previous);
            else
                previousButton.setText(firstCell.getStringValue());
        } else {
            previousButton.setEnabled(false);
            previousButton.setVisibility(View.GONE);
        }

        if (answerIndex < currentTest.getNumberRow() - 1) {
            if (!nextButton.isEnabled()) {
                nextButton.setVisibility(View.VISIBLE);
                nextButton.setEnabled(true);
            }
            DataCell firstCell = currentTest.getRowFirstCell(answerIndex + 1);
            if (firstCell == null)
                nextButton.setText(R.string.next);
            else
                nextButton.setText(firstCell.getStringValue());
        } else {
            nextButton.setEnabled(false);
            nextButton.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onSwipeRight() {
        if (answerIndex > 0) {
            answerIndex -= 1;
            createFragment();
            getIntent().putExtra(AnswerDataViewFragment.ARG_ANSWER_INDEX, answerIndex);
        }
    }

    @Override
    public void onSwipeLeft() {
        if (answerIndex < currentTest.getNumberRow() - 1) {
            answerIndex += 1;
            createFragment();
            getIntent().putExtra(AnswerDataViewFragment.ARG_ANSWER_INDEX, answerIndex);
        }
    }
}
