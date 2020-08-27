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

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.Test;

public class ViewTestActivity extends AppCompatActivity
        implements
        AnswerViewTestFragment.OnFragmentInteractionListener,
        AnswerViewTestRecyclerListFragment.OnFragmentInteractionListener,
        AnswerDataViewFragment.OnFragmentInteractionListener,
        ColumnViewTestFragment.OnFragmentInteractionListener,
        ColumnViewTestRecyclerListFragment.OnFragmentInteractionListener,
        ColumnDataViewFragment.OnFragmentInteractionListener,
        MainInformationViewTestFragment.OnFragmentInteractionListener, DiakoluoApplication.GetTestRunnable {

    private ViewTestPagerAdapterFragment adapter;
    @Nullable private Test currentTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_test);

        DiakoluoApplication.get(this).getCurrentTest(
                new DiakoluoApplication.GetTest(true, this,
                        false, this));

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
        currentTest = test;

        final TextView title = findViewById(R.id.title);
        ImageButton navigation = findViewById(R.id.navigationIcon);
        ImageButton resetButton = findViewById(R.id.resetButton);

        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSupportNavigateUp();
            }
        });

        TabLayout tabLayout = findViewById(R.id.viewTestTabLayout);
        final ViewPager viewPager = findViewById(R.id.viewTestViewPager);

        viewPager.setOffscreenPageLimit(2);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new ViewTestPagerAdapterFragment(
                getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                ViewTestActivity.this
        );
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(ViewTestActivity.this)
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
                                currentTest.reset();
                                DiakoluoApplication.get(ViewTestActivity.this).saveCurrentTest();
                                MainInformationViewTestFragment mainInformationViewTestFragment =
                                        (MainInformationViewTestFragment) adapter.getFragmentAtPosition(0);
                                mainInformationViewTestFragment.updateTestDid();
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                viewPager.setAdapter(adapter);
                title.setText(currentTest.getName());
            }
        });

    }
}
