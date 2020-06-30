package fr.pyjacpp.diakoluo.view_test;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.Test;

public class ColumnDataViewActivity extends AppCompatActivity implements ColumnDataViewFragment.OnFragmentInteractionListener{

    private int columnIndex;
    private ActionBar actionBar;
    private Test currentTest;
    private Button previousButton;
    private Button nextButton;
    private TextView navigationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_column_data);

        currentTest = DiakoluoApplication.getCurrentTest(this);
        columnIndex = getIntent().getIntExtra(ColumnDataViewFragment.ARG_COLUMN_INDEX, 0);

        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        navigationTextView = findViewById(R.id.navigationTextView);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }

        if (savedInstanceState == null) {
            createFragment();
        } else {
            updateNavigation();
        }

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
        ColumnDataViewFragment fragment = ColumnDataViewFragment.newInstance(
                columnIndex
        );

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fragment_fade_scale_enter, R.anim.fragment_fade_scale_exit)
                .replace(R.id.columnDataEditFragmentContainer, fragment).commit();

        updateNavigation();
    }

    private void updateNavigation() {
        if (actionBar != null) {
            actionBar.setTitle(currentTest.getListColumn().get(columnIndex).getName());
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
            if (!nextButton.isEnabled()) {
                nextButton.setVisibility(View.VISIBLE);
                nextButton.setEnabled(true);
            }
            nextButton.setText(currentTest.getListColumn().get(columnIndex + 1).getName());
        } else {
            nextButton.setEnabled(false);
            nextButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSwipeRight() {
        if (columnIndex > 0) {
            columnIndex -= 1;
            createFragment();
            getIntent().putExtra(ColumnDataViewFragment.ARG_COLUMN_INDEX, columnIndex);
        }
    }

    @Override
    public void onSwipeLeft() {
        if (columnIndex < currentTest.getNumberColumn() - 1) {
            columnIndex += 1;
            createFragment();
            getIntent().putExtra(ColumnDataViewFragment.ARG_COLUMN_INDEX, columnIndex);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
