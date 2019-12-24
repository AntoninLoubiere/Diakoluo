package fr.pyjacpp.diakoluo.edit_test;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import fr.pyjacpp.diakoluo.R;

public class EditTestActivity extends AppCompatActivity
        implements
        AnswerEditTestFragment.OnFragmentInteractionListener,
        AnswerEditTestRecyclerListFragment.OnFragmentInteractionListener,
        ColumnEditTestFragment.OnFragmentInteractionListener,
        ColumnEditTestRecyclerListFragment.OnFragmentInteractionListener,
        MainInformationsEditTestFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_test);

        TabLayout tabLayout = findViewById(R.id.viewTestTabLayout);
        ViewPager viewPager = findViewById(R.id.viewTestViewPager);

        viewPager.setOffscreenPageLimit(2);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager.setAdapter(new EditTestPagerAdapterFragment(
                getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, this
        ));

    }
}
