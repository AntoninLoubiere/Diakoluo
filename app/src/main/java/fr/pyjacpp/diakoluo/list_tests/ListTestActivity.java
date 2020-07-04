package fr.pyjacpp.diakoluo.list_tests;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.edit_test.EditTestActivity;
import fr.pyjacpp.diakoluo.save_test.FileManager;
import fr.pyjacpp.diakoluo.test_tests.TestSettingsActivity;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.view_test.MainInformationViewTestFragment;
import fr.pyjacpp.diakoluo.view_test.ViewTestActivity;

public class ListTestActivity extends AppCompatActivity
        implements ListTestsFragment.OnFragmentInteractionListener,
        MainInformationViewTestFragment.OnFragmentInteractionListener,
        ExportDialogFragment.OnValidListener,
        ImportXmlDialogFragment.OnValidListener{

    private boolean detailMainInformationTest;
    private MainInformationViewTestFragment mainInformationViewTestFragment;

    private int currentTestSelected = -1;
    private FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_test);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(R.string.list_test);

        detailMainInformationTest = findViewById(R.id.testMainInformationFragment) != null;
        mainInformationViewTestFragment = (MainInformationViewTestFragment)
                getSupportFragmentManager().findFragmentById(R.id.testMainInformationFragment);

        addButton = findViewById(R.id.addFloatingButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DiakoluoApplication.setCurrentEditTest(ListTestActivity.this, new Test("", ""));
                startActivity(new Intent(ListTestActivity.this, EditTestActivity.class));
            }
        });

        if (detailMainInformationTest) {
            if (DiakoluoApplication.getListTest(this).size() > 0)
            updateDetail(0);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (detailMainInformationTest) {
            updateDetail(position);
        } else {
            onSeeButtonClick(view, position);
        }
    }

    private void updateDetail(int position) {
        if (mainInformationViewTestFragment != null) {
            if (position < 0) {
                DiakoluoApplication.setCurrentTest(this, null);
            } else {
                DiakoluoApplication.setCurrentTest(this,
                        DiakoluoApplication.getListTest(this).get(position));
            }

            currentTestSelected = position;

            mainInformationViewTestFragment.updateContent(this);
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

            case R.id.importAction:
                FileManager.importTest(this);
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

    @Override
    public void onDeleteTest(int position) {
        ArrayList<Test> listTest = DiakoluoApplication.getListTest(this);
        if (position == currentTestSelected || currentTestSelected == -1) {
            if (listTest.size() > 0)
                updateDetail(0);
            else
                updateDetail(-1);
        }
    }

    @Override
    public void onExportMenuItemClick(View view, int position) {
        ExportDialogFragment exportDialogFragment = new ExportDialogFragment(position);
        exportDialogFragment.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FileManager.exportTestResult(this, requestCode, resultCode, data, addButton, new FileManager.ResultListener() {
            @Override
            public void showXmlImportDialog(Test testLoaded) {
                DiakoluoApplication.setCurrentImportTest(ListTestActivity.this, testLoaded);
                ImportXmlDialogFragment importXmlDialogFragment = new ImportXmlDialogFragment();
                importXmlDialogFragment.show(getSupportFragmentManager(), "dialog");
            }
        });
    }

    @Override
    public void createXmlFile(int position, boolean saveNumberTestDone) {
        FileManager.exportXmlTest(ListTestActivity.this, position, saveNumberTestDone);
    }

    @Override
    public void createCsvFile(int position, boolean columnHeader, boolean columnTypeHeader, String separator, String lineSeparator) {
        FileManager.exportCsvTest(ListTestActivity.this, position, columnHeader, columnTypeHeader, separator, lineSeparator);
    }

    @Override
    public void saveXmlFile() {
        // Add test and update recycler
        DiakoluoApplication diakoluoApplication = DiakoluoApplication.getDiakoluoApplication(this);
        Test currentImportTest = diakoluoApplication.getCurrentImportTest();
        ArrayList<Test> listTest = diakoluoApplication.getListTest();
        listTest.add(currentImportTest);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentListTest);
        if (fragment != null) {
            ((ListTestsFragment) fragment).notifyUpdateInserted(listTest.size());
        }

        diakoluoApplication.setCurrentImportTest(null);

    }
}
