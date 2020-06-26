package fr.pyjacpp.diakoluo.edit_test;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

import fr.pyjacpp.diakoluo.R;


public class EditTestPagerAdapterFragment extends FragmentPagerAdapter {
    private static final int NUMBER_VIEW_TEST_TAB = 3;
    private final Context context;

    private ArrayList<Fragment> fragments = new ArrayList<>();

    EditTestPagerAdapterFragment(@NonNull FragmentManager fm, int behavior, Context context) {
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

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        fragments.remove(position);
        super.destroyItem(container, position, object);
    }

    private Fragment createItem(int position) {
        switch (position) {
            case 1:
                return new ColumnEditTestFragment();

            case 2:
                return new AnswerEditTestFragment();

            case 0:
            default:
                return new MainInformationsEditTestFragment();
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
