package fr.pyjacpp.diakoluo.view_test;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import fr.pyjacpp.diakoluo.R;

public class ViewTestActivity extends AppCompatActivity
        implements
        AnswerViewTestFragment.OnFragmentInteractionListener,
        AnswerViewTestRecyclerListFragment.OnFragmentInteractionListener,
        AnswerDataViewFragment.OnFragmentInteractionListener,
        ColumnViewTestFragment.OnFragmentInteractionListener,
        ColumnViewTestRecyclerListFragment.OnFragmentInteractionListener,
        ColumnDataViewFragment.OnFragmentInteractionListener,
        MainInformationViewTestFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_test);

        TabLayout tabLayout = findViewById(R.id.viewTestTabLayout);
        ViewPager viewPager = findViewById(R.id.viewTestViewPager);

        viewPager.setOffscreenPageLimit(2);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager.setAdapter(new ViewTestPagerAdapterFragment(
                getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, this
        ));

    }
}
