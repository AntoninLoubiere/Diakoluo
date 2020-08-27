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

package fr.pyjacpp.diakoluo.edit_test;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.tests.data.DataCell;

public class AnswerDataEditActivity extends AppCompatActivity implements AnswerDataEditFragment.OnFragmentInteractionListener, DiakoluoApplication.GetTestRunnable {

    private int answerIndex;
    @Nullable
    private Test currentTest;

    private Button previousButton;
    private Button nextButton;
    private TextView navigationTextView;
    private ActionBar actionBar;
    private boolean fragmentCreated;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_answer_data);

        answerIndex = getIntent().getIntExtra(AnswerDataEditFragment.ARG_ANSWER_INDEX, 0);

        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        navigationTextView = findViewById(R.id.navigationTextView);

        actionBar = getSupportActionBar();

        fragmentCreated = savedInstanceState != null;

        DiakoluoApplication.get(this).getCurrentEditTest(
                new DiakoluoApplication.GetTest(true, this, false, this));

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
        AnswerDataEditFragment fragment = AnswerDataEditFragment.newInstance(
                answerIndex
        );

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fragment_fade_scale_enter, R.anim.fragment_fade_scale_exit)
                .replace(R.id.answerDataEditFragmentContainer, fragment)
                .commit();

        updateNavigation();
    }

    private void updateNavigation() {
        if (currentTest != null) {
            if (actionBar != null) {
                DataCell firstCell = currentTest.getRowFirstCell(answerIndex);
                if (firstCell == null)
                    actionBar.setTitle(R.string.app_name);
                else
                    actionBar.setTitle(firstCell.getStringValue(this,
                            currentTest.getListColumn().get(0)));
            }

            navigationTextView.setText(getString(R.string.navigation_info, answerIndex + 1,
                    currentTest.getNumberRow()));
        }


        if (answerIndex > 0) {
            if (!previousButton.isEnabled()) {
                previousButton.setVisibility(View.VISIBLE);
                previousButton.setEnabled(true);
            }
            DataCell firstCell = currentTest.getRowFirstCell(answerIndex - 1);
            if (firstCell == null)
                previousButton.setText(R.string.previous);
            else
                previousButton.setText(firstCell.getStringValue(this,
                        currentTest.getListColumn().get(0)));
        } else {
            previousButton.setEnabled(false);
            previousButton.setVisibility(View.GONE);
        }

        if (answerIndex < currentTest.getNumberRow() - 1) {
            DataCell firstCell = currentTest.getRowFirstCell(answerIndex + 1);
            if (firstCell == null)
                nextButton.setText(R.string.next);
            else
                nextButton.setText(firstCell.getStringValue(this,
                        currentTest.getListColumn().get(0)));
        } else {
            nextButton.setText(R.string.create_new_data_edit);
        }
    }

    @Override
    public void onSwipeRight() {
        if (answerIndex > 0) {
            answerIndex -= 1;
            createFragment();
            getIntent().putExtra(AnswerDataEditFragment.ARG_ANSWER_INDEX, answerIndex);
        }
    }

    @Override
    public void onSwipeLeft() {
        if (currentTest != null) {
            if (answerIndex < currentTest.getNumberRow() - 1) {
                answerIndex += 1;
                createFragment();
                getIntent().putExtra(AnswerDataEditFragment.ARG_ANSWER_INDEX, answerIndex);
            } else {
                new MaterialAlertDialogBuilder(this)
                        .setTitle(R.string.dialog_new_answer_title)
                        .setMessage(R.string.dialog_new_answer_message)
                        .setIcon(R.drawable.ic_add_accent_color_24dp)
                        .setPositiveButton(R.string.dialog_create_new_data_edit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // update recyclers
                                Intent intent = new Intent();
                                intent.setAction(EditTestActivity.ACTION_BROADCAST_NEW_ANSWER_RECYCLER);
                                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(AnswerDataEditActivity.this);
                                localBroadcastManager.sendBroadcastSync(intent);
                                onSwipeLeft();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .show();
            }
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
    public void updateAnswerRecyclerItem(int position) {
        Intent intent = new Intent();
        intent.setAction(EditTestActivity.ACTION_BROADCAST_UPDATE_ANSWER_RECYCLER);
        intent.putExtra(EditTestActivity.EXTRA_INT_POSITION, position);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.sendBroadcast(intent);
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

        if (actionBar != null) {
            DataCell dataCell = currentTest.getRowFirstCell(answerIndex);
            String stringValue = "";
            if (dataCell != null)
                stringValue = dataCell.getStringValue(AnswerDataEditActivity.this,
                        currentTest.getListColumn().get(0));

            if (stringValue.equals(""))
                actionBar.setTitle(R.string.app_name);
            else
                actionBar.setTitle(stringValue);

            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }

        Button validButton = findViewById(R.id.validButton);
        validButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (fragmentCreated) {
            updateNavigation();
        } else {
            createFragment();
        }
    }
}
