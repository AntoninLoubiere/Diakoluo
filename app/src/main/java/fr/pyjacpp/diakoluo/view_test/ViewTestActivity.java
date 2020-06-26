package fr.pyjacpp.diakoluo.view_test;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
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

        TextView title = findViewById(R.id.title);
        ImageButton navigation = findViewById(R.id.navigationIcon);
        title.setText(DiakoluoApplication.getCurrentTest(this).getName());
        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSupportNavigateUp();
            }
        });

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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
