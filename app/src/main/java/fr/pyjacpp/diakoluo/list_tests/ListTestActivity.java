package fr.pyjacpp.diakoluo.list_tests;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.edit_test.EditTestActivity;
import fr.pyjacpp.diakoluo.save_test.FileManager;
import fr.pyjacpp.diakoluo.test_tests.TestSettingsActivity;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.view_test.MainInformationsViewTestFragment;
import fr.pyjacpp.diakoluo.view_test.ViewTestActivity;

public class ListTestActivity extends AppCompatActivity
        implements ListTestsFragment.OnFragmentInteractionListener,
        MainInformationsViewTestFragment.OnFragmentInteractionListener {

    private boolean detailMainInformationTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_test);

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
            MainInformationsViewTestFragment mainInformationsViewTestFragment =(MainInformationsViewTestFragment)
                    getSupportFragmentManager().findFragmentById(R.id.testMainInformationFragment);

            if (mainInformationsViewTestFragment != null) {
                DiakoluoApplication.setCurrentTest(view.getContext(),
                        DiakoluoApplication.getListTest(view.getContext()).get(position));

                mainInformationsViewTestFragment.updateContent(this);
            }
        } else {
            onSeeButtonClick(view, position);
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

    @Override
    public void onExportMenuItemClick(View view, int position) {
        FileManager.exportTest(this, position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FileManager.exportTestResult(this, requestCode, resultCode, data);
    }
}
