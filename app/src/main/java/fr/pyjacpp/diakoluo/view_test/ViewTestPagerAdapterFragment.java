package fr.pyjacpp.diakoluo.view_test;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import fr.pyjacpp.diakoluo.R;


public class ViewTestPagerAdapterFragment extends FragmentPagerAdapter {
    private static final int NUMBER_VIEW_TEST_TAB = 3;
    private final Context context;

    ViewTestPagerAdapterFragment(@NonNull FragmentManager fm, int behavior, Context context) {
        super(fm, behavior);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return new ColumnViewTestFragment();

            case 2:
                return new AnswerViewTestFragment();

            case 0:
            default:
                return new MainInformationsViewTestFragment();
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
}
