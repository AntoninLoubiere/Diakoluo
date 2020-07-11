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

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

import fr.pyjacpp.diakoluo.R;


public class ViewTestPagerAdapterFragment extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private static final int NUMBER_VIEW_TEST_TAB = 3;
    private final Context context;

    ViewTestPagerAdapterFragment(@NonNull FragmentManager fm, int behavior, Context context) {
        super(fm, behavior);
        this.context = context;

        fragments.clear();
        for (int i = 0; i < NUMBER_VIEW_TEST_TAB; i++) {
            fragments.add(null);
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment f = createItem(position);
        fragments.set(position, f);
        return f;
    }


    private Fragment createItem(int position) {
        switch (position) {
            case 1:
                return new ColumnViewTestFragment();

            case 2:
                return new AnswerViewTestFragment();

            case 0:
            default:
                return new MainInformationViewTestFragment();
        }
    }

    @Override
    public int getCount() {
        return NUMBER_VIEW_TEST_TAB;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.view_test_title_main_fragment);

            case 1:
                return context.getString(R.string.view_test_title_column_fragment);

            case 2:
                return context.getString(R.string.view_test_title_answer_fragment);

            default:
                return null;
        }
    }

    Fragment getFragmentAtPosition(int position) {
        return fragments.get(position);
    }
}
