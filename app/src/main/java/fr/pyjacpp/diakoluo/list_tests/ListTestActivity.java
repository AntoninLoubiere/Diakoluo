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

package fr.pyjacpp.diakoluo.list_tests;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.edit_test.EditTestActivity;
import fr.pyjacpp.diakoluo.save_test.CsvLoader;
import fr.pyjacpp.diakoluo.save_test.CsvSaver;
import fr.pyjacpp.diakoluo.save_test.FileManager;
import fr.pyjacpp.diakoluo.test_tests.TestSettingsActivity;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.view_test.MainInformationViewTestFragment;
import fr.pyjacpp.diakoluo.view_test.ViewTestActivity;

public class ListTestActivity extends AppCompatActivity
        implements ListTestsFragment.OnFragmentInteractionListener,
        MainInformationViewTestFragment.OnFragmentInteractionListener,
        ExportDialogFragment.OnValidListener,
        ImportXmlDialogFragment.OnValidListener,
        ImportCsvDialogFragment.OnValidListener {

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
            public void showXmlImportDialog(FileManager.ImportXmlContext importContext) {
                DiakoluoApplication.setCurrentImportContext(ListTestActivity.this, importContext);
                ImportXmlDialogFragment importXmlDialogFragment = new ImportXmlDialogFragment();
                importXmlDialogFragment.show(getSupportFragmentManager(), "dialog");
            }

            @Override
            public void showCsvImportDialog(FileManager.ImportCsvContext importContext) {
                DiakoluoApplication.setCurrentImportContext(ListTestActivity.this, importContext);
                ImportCsvDialogFragment importCsvDialogFragment = new ImportCsvDialogFragment();
                importCsvDialogFragment.show(getSupportFragmentManager(), "dialog");
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
    public void loadXmlFile() {
        // Add test and update recycler
        DiakoluoApplication diakoluoApplication = DiakoluoApplication.getDiakoluoApplication(this);
        FileManager.ImportXmlContext currentImportContext = (FileManager.ImportXmlContext) diakoluoApplication.getCurrentImportContext();
        importTest(diakoluoApplication, currentImportContext.importTest);

    }


    @Override
    public void loadCsvFile(String name, int separatorId, boolean loadColumnName, boolean loadColumnType) {
        DiakoluoApplication diakoluoApplication = DiakoluoApplication.getDiakoluoApplication(this);

        FileManager.ImportCsvContext importContext = (FileManager.ImportCsvContext) diakoluoApplication.getCurrentImportContext();
        ParcelFileDescriptor pfd = null;
        try {
            pfd = getContentResolver().openFileDescriptor(importContext.fileUri, "r");
            if (pfd != null) {
                FileInputStream inputStream;
                inputStream = new FileInputStream(pfd.getFileDescriptor());
                Test testLoaded = CsvLoader.load(this, inputStream,
                        CsvSaver.SEPARATORS[separatorId].charAt(0),
                        loadColumnName,
                        loadColumnType,
                        name);
                importTest(diakoluoApplication, testLoaded);
            }
        } catch (CsvLoader.CsvException e) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_import_error_title)
                    .setMessage(R.string.dialog_export_error_csv_message)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setIcon(R.drawable.ic_error_red_24dp)
                    .show();
        } catch (IOException e) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_import_error_title)
                    .setMessage(R.string.dialog_import_error_message)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setIcon(R.drawable.ic_error_red_24dp)
                    .show();
        } finally {
            if (pfd != null)
                try {
                    pfd.close();
                } catch (IOException ignored) {
                }
        }
    }

    private void importTest(DiakoluoApplication diakoluoApplication, Test currentImportTest) {
        if (currentImportTest == null) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_import_error_title)
                    .setMessage(R.string.dialog_import_error_message)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setIcon(R.drawable.ic_error_red_24dp)
                    .show();
        } else {
            ArrayList<Test> listTest = diakoluoApplication.getListTest();
            listTest.add(currentImportTest);

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentListTest);
            if (fragment != null) {
                ((ListTestsFragment) fragment).notifyUpdateInserted(listTest.size());
            }
        }
        diakoluoApplication.setCurrentImportContext(null);
    }
}
