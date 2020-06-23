package fr.pyjacpp.diakoluo.list_tests;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.edit_test.EditTestActivity;
import fr.pyjacpp.diakoluo.test_tests.TestSettingsActivity;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.view_test.MainInformationViewTestFragment;
import fr.pyjacpp.diakoluo.view_test.ViewTestActivity;

public class ListTestActivity extends AppCompatActivity
        implements ListTestsFragment.OnFragmentInteractionListener,
        MainInformationViewTestFragment.OnFragmentInteractionListener {

    private boolean detailMainInformationTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_test);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(R.string.list_test);

        detailMainInformationTest = findViewById(R.id.testMainInformationFragment) != null;

        FloatingActionButton addButton = findViewById(R.id.addFloatingButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DiakoluoApplication.setCurrentEditTest(ListTestActivity.this, new Test("", ""));
                startActivity(new Intent(ListTestActivity.this, EditTestActivity.class));
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        if (detailMainInformationTest) {
            MainInformationViewTestFragment mainInformationViewTestFragment =(MainInformationViewTestFragment)
                    getSupportFragmentManager().findFragmentById(R.id.testMainInformationFragment);

            if (mainInformationViewTestFragment != null) {
                DiakoluoApplication.setCurrentTest(view.getContext(),
                        DiakoluoApplication.getListTest(view.getContext()).get(position));

                mainInformationViewTestFragment.updateContent(this);
            }
        } else {
            onSeeButtonClick(view, position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_list_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settingsAction:
                startActivity(new Intent(ListTestActivity.this, SettingsActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPlayButtonClick(View view, int position) {
        Test currentTest = DiakoluoApplication.getListTest(view.getContext()).get(position);
        if (currentTest.canBePlay()) {
            DiakoluoApplication.setCurrentTest(view.getContext(),
                    currentTest);

            startActivity(new Intent(view.getContext(), TestSettingsActivity.class));
        } // TODO warning
    }

    @Override
    public void onSeeButtonClick(View view, int position) {
        DiakoluoApplication.setCurrentTest(view.getContext(),
                DiakoluoApplication.getListTest(view.getContext()).get(position));

        startActivity(new Intent(view.getContext(), ViewTestActivity.class));
    }

    @Override
    public void onEditMenuItemClick(View view, int position) {
        DiakoluoApplication.setCurrentIndexEditTest(view.getContext(), position);
        startActivity(new Intent(view.getContext(), EditTestActivity.class));
    }
}
