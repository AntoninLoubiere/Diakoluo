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

import java.util.Objects;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.Test;

public class ColumnDataEditActivity extends AppCompatActivity implements ColumnDataEditFragment.OnFragmentInteractionListener, DiakoluoApplication.GetTestRunnable {

    private int columnIndex;
    @Nullable private Test currentTest;

    private Button previousButton;
    private Button nextButton;
    private TextView navigationTextView;
    private ActionBar actionBar;
    private boolean fragmentCreated;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_column_data);

        columnIndex = getIntent().getIntExtra(ColumnDataEditFragment.ARG_COLUMN_INDEX, 0);

        fragmentCreated = savedInstanceState != null;

        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        navigationTextView = findViewById(R.id.navigationTextView);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }

        DiakoluoApplication.get(this).getCurrentEditTest(
                new DiakoluoApplication.GetTest(true, this,
                        true, this));

        Button validButton = findViewById(R.id.validButton);
        validButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
        ColumnDataEditFragment fragment = ColumnDataEditFragment.newInstance(columnIndex);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fragment_fade_scale_enter, R.anim.fragment_fade_scale_exit)
                .replace(R.id.columnDataEditFragmentContainer, fragment)
                .commit();
        updateNavigation();
    }

    private void updateNavigation() {
        if (currentTest != null) {
            if (actionBar != null) {
                String name = currentTest.getListColumn().get(columnIndex).getName();
                if (Objects.equals(name, ""))
                    actionBar.setTitle(R.string.app_name);
                else
                    actionBar.setTitle(name);
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
                nextButton.setText(currentTest.getListColumn().get(columnIndex + 1).getName());
            } else {
                nextButton.setText(R.string.create_new_data_edit);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onSwipeRight() {
        if (columnIndex > 0) {
            columnIndex -= 1;
            createFragment();
            getIntent().putExtra(ColumnDataEditFragment.ARG_COLUMN_INDEX, columnIndex);
        }
    }

    @Override
    public void onSwipeLeft() {
        if (currentTest != null) {
            if (columnIndex < currentTest.getNumberColumn() - 1) {
                columnIndex += 1;
                createFragment();
                getIntent().putExtra(ColumnDataEditFragment.ARG_COLUMN_INDEX, columnIndex);
            } else {
                // new
                new MaterialAlertDialogBuilder(this)
                        .setTitle(R.string.dialog_new_column_title)
                        .setMessage(R.string.dialog_new_column_message)
                        .setIcon(R.drawable.ic_add_accent_color_24dp)
                        .setPositiveButton(R.string.dialog_create_new_data_edit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // update recyclers
                                Intent intent = new Intent();
                                intent.setAction(EditTestActivity.ACTION_BROADCAST_NEW_COLUMN_RECYCLER);
                                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(ColumnDataEditActivity.this);
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
    public void updateColumnRecyclerItem(int position) {
        Intent intent = new Intent();
        intent.setAction(EditTestActivity.ACTION_BROADCAST_UPDATE_COLUMN_RECYCLER);
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
        if (fragmentCreated) {
            updateNavigation();
        } else {
            createFragment();
        }
    }
}
