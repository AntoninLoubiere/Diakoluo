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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayDeque;
import java.util.ArrayList;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.RecyclerViewChange;
import fr.pyjacpp.diakoluo.Utils;
import fr.pyjacpp.diakoluo.tests.CompactTest;
import fr.pyjacpp.diakoluo.tests.Test;

public class EditTestActivity extends AppCompatActivity
        implements
        AnswerEditTestFragment.OnFragmentInteractionListener,
        AnswerEditTestRecyclerListFragment.OnFragmentInteractionListener,
        AnswerDataEditFragment.OnFragmentInteractionListener,
        ColumnEditTestFragment.OnFragmentInteractionListener,
        ColumnDataEditFragment.OnFragmentInteractionListener,
        ColumnEditTestRecyclerListFragment.OnFragmentInteractionListener,
        MainInformationEditTestFragment.OnFragmentInteractionListener, DiakoluoApplication.GetTestRunnable {

    public static final String ACTION_BROADCAST_UPDATE_COLUMN_RECYCLER = "fr.pyjacpp.diakoluo.edit_test.UPDATE_COLUMN_RECYCLER";
    public static final String ACTION_BROADCAST_UPDATE_ANSWER_RECYCLER = "fr.pyjacpp.diakoluo.edit_test.UPDATE_ANSWER_RECYCLER";
    public static final String ACTION_BROADCAST_NEW_COLUMN_RECYCLER = "fr.pyjacpp.diakoluo.edit_test.NEW_COLUMN_RECYCLER";
    public static final String ACTION_BROADCAST_NEW_ANSWER_RECYCLER = "fr.pyjacpp.diakoluo.edit_test.NEW_ANSWER_RECYCLER";
    public static final String EXTRA_INT_POSITION = "position";

    private EditTestPagerAdapterFragment adapter;
    @Nullable
    private Test editTest;
    private int editTestIndex;
    private ArrayList<CompactTest> listTest;
    private TextView title;
    private DiakoluoApplication diakoluoApplication;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Button cancelButton;
    private Button validButton;
    private ImageButton resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_test);

        title = findViewById(R.id.title);
        ImageButton navigation = findViewById(R.id.navigationIcon);

        diakoluoApplication = DiakoluoApplication.get(this);


        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSupportNavigateUp();
            }
        });

        tabLayout = findViewById(R.id.viewTestTabLayout);
        viewPager = findViewById(R.id.viewTestViewPager);
        cancelButton = findViewById(R.id.cancelButton);
        validButton = findViewById(R.id.validButton);
        resetButton = findViewById(R.id.resetButton);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        diakoluoApplication.getCurrentEditTest(
                new DiakoluoApplication.GetTest(false,
                        this, this));

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateColumnRecyclerItem(intent.getIntExtra(EXTRA_INT_POSITION, 0));
            }
        }, new IntentFilter(ACTION_BROADCAST_UPDATE_COLUMN_RECYCLER));

        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateAnswerRecyclerItem(intent.getIntExtra(EXTRA_INT_POSITION, 0));
            }
        }, new IntentFilter(ACTION_BROADCAST_UPDATE_ANSWER_RECYCLER));

        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateNewColumn();
            }
        }, new IntentFilter(ACTION_BROADCAST_NEW_COLUMN_RECYCLER));

        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateNewAnswer();
            }
        }, new IntentFilter(ACTION_BROADCAST_NEW_ANSWER_RECYCLER));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void createModifyEditTest() {
        finish();
        new Thread(new Runnable() {
            @Override
            public void run() {
                DiakoluoApplication.get(EditTestActivity.this).applyCurrentEditTest();
            }
        }).start();
    }

    private void saveInTestVar() {
        if (editTest != null) {
            EditText title = findViewById(R.id.titleEditText);
            EditText description = findViewById(R.id.descriptionEditText);
            Spinner scoreMethod = findViewById(R.id.scoreMethodSpinner);

            editTest.setName(title.getText().toString());
            editTest.setDescription(description.getText().toString());
            editTest.setScoreMethod(scoreMethod.getSelectedItemPosition() == 0);
        }
    }

    private void updateNewColumn() {
        Fragment columnEditTestFragment = adapter.getFragmentAtPosition(1);

        if (columnEditTestFragment != null) {
            ((ColumnEditTestFragment) columnEditTestFragment).updateNewItem(this);
        }
    }

    private void updateNewAnswer() {
        Fragment answerEditTestFragment = adapter.getFragmentAtPosition(2);

        if (answerEditTestFragment != null) {
            ((AnswerEditTestFragment) answerEditTestFragment).updateNewItem();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (editTest != null)
            saveInTestVar();
    }

    @Override
    public Utils.EditValidator titleEditTestValidator(String text) {
        if (text.length() <= 0) {
            return new Utils.EditValidator(R.string.error_label_title_blank);
        } else {
            for (int i = 0; i < listTest.size(); i++) {
                CompactTest test = listTest.get(i);

                if (test.getName().equalsIgnoreCase(text) && (editTestIndex != i)) {

                    return new Utils.EditValidator(R.string.error_label_title_already_exist, true);
                }
            }
            return new Utils.EditValidator();
        }
    }

    @Override
    public Utils.EditValidator descriptionEditTestValidator(String text) {
        return new Utils.EditValidator();
    }

    @Override
    public void updateAnswerRecycler(final RecyclerViewChange recyclerViewChange) {
        Fragment answerEditTestFragment = adapter.getFragmentAtPosition(2);

        if (answerEditTestFragment != null) {
            ((AnswerEditTestFragment) answerEditTestFragment).updateAnswerRecycler(recyclerViewChange);
        }
    }

    @Override
    public void updateAnswerRecyclerItem(int position) {
        RecyclerViewChange change = new RecyclerViewChange(RecyclerViewChange.ItemChanged);
        change.setPosition(position);
        updateAnswerRecycler(change);
    }

    @Override
    public void updateColumnRecyclerItem(final int position) {
        Fragment columnEditTestFragment = adapter.getFragmentAtPosition(1);

        if (columnEditTestFragment != null) {
            ((ColumnEditTestFragment) columnEditTestFragment).updateItem(position);
        }
    }

    @Override
    public void onSwipeRight() {
    }

    @Override
    public void onSwipeLeft() {
    }

    @Override
    public void errorFinish(boolean canceled) {
        finish();
        if (!canceled) Log.e(getClass().getName(), "No current test, abort.");
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
        editTest = test;

        listTest = diakoluoApplication.getListTest();
        editTestIndex = diakoluoApplication.getCurrentEditTestIndex();

        viewPager.setOffscreenPageLimit(2);

        tabLayout.setupWithViewPager(viewPager);
        adapter = new EditTestPagerAdapterFragment(
                getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, this
        );

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        validButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTest != null) {
                    ArrayDeque<Utils.EditValidator> errorValidatorDeque = new ArrayDeque<>();

                    saveInTestVar();

                    Utils.EditValidator response = titleEditTestValidator(editTest.getName());
                    errorValidatorDeque.add(response);

                    response = descriptionEditTestValidator(editTest.getDescription());
                    errorValidatorDeque.add(response);

                    Utils.VerifyAndAsk verifyAndAsk = new Utils.VerifyAndAsk(EditTestActivity.this, new Runnable() {
                        @Override
                        public void run() {
                            createModifyEditTest();
                        }
                    });
                    verifyAndAsk.run(errorValidatorDeque);
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(EditTestActivity.this)
                        .setTitle(R.string.dialog_reset_title)
                        .setMessage(R.string.dialog_reset_message)
                        .setIcon(R.drawable.ic_reset_accent_color_24dp)
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton(R.string.reset, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (editTest != null) {
                                    editTest.reset();
                                    MainInformationEditTestFragment mainInformationEditTestFragment =
                                            (MainInformationEditTestFragment) adapter.getFragmentAtPosition(0);
                                    mainInformationEditTestFragment.updateTestDid();
                                    dialogInterface.dismiss();
                                }
                            }
                        })
                        .show();
            }
        });

        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                String name = editTest.getName();
                title.setText(name.equals("") ?
                        getString(R.string.app_name) : name);
                viewPager.setAdapter(adapter);
            }
        });
    }
}