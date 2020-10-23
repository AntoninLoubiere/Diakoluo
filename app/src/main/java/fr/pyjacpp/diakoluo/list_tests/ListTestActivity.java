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
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileInputStream;
import java.io.IOException;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.edit_test.EditTestActivity;
import fr.pyjacpp.diakoluo.save_test.CsvLoader;
import fr.pyjacpp.diakoluo.save_test.CsvSaver;
import fr.pyjacpp.diakoluo.save_test.FileManager;
import fr.pyjacpp.diakoluo.test_tests.TestSettingsActivity;
import fr.pyjacpp.diakoluo.tests.CompactTest;
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

    private DiakoluoApplication diakoluoApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_test);

        diakoluoApplication = DiakoluoApplication.get(this);

        // first time open it
        if (diakoluoApplication.getCurrentEditTestIndex() != DiakoluoApplication.NO_CURRENT_EDIT_TEST) {
            diakoluoApplication.getCurrentEditTest(
                    new DiakoluoApplication.GetTest(false, this, false,
                            new DiakoluoApplication.GetTestRunnable() {
                                @Override
                                public void loadingInProgress() {
                                }

                                @Override
                                public void error(boolean canceled) {
                                }

                                @Override
                                public void success(@NonNull Test test) {
                                    showRecoverEditTest(test.getName());
                                }
                            })
            );
        }

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
                diakoluoApplication.setCurrentEditTest(
                        DiakoluoApplication.NEW_CURRENT_EDIT_TEST);
                startActivity(new Intent(ListTestActivity.this, EditTestActivity.class));
            }
        });

        if (detailMainInformationTest) {
            if (diakoluoApplication.getListTest().size() > 0)
                updateDetail(0);
        }
    }

    private void showRecoverEditTest(String editTestName) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.dialog_edit_test_existing_title)
                .setMessage(getString(R.string.dialog_edit_test_existing_message, editTestName))
                .setCancelable(false)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        diakoluoApplication.setCurrentEditTest(DiakoluoApplication.NO_CURRENT_EDIT_TEST);
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(ListTestActivity.this,
                                EditTestActivity.class));
                        dialogInterface.dismiss();
                    }
                })
                .show();
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
                diakoluoApplication.setCurrentTest(
                        DiakoluoApplication.NO_CURRENT_EDIT_TEST);
            } else {
                diakoluoApplication.setCurrentTest(position);
            }

            currentTestSelected = position;

            mainInformationViewTestFragment.updateContent();
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
        CompactTest currentTest = diakoluoApplication.getListTest().get(position);
        if (currentTest.isPlayable()) {
            diakoluoApplication.setCurrentTest(position);

            startActivity(new Intent(view.getContext(), TestSettingsActivity.class));
        } // TODO warning
    }

    @Override
    public void onSeeButtonClick(View view, int position) {
        diakoluoApplication.setCurrentTest(position);

        startActivity(new Intent(view.getContext(), ViewTestActivity.class));
    }

    @Override
    public void onEditMenuItemClick(View view, int position) {
        diakoluoApplication.setCurrentEditTest(position);
        startActivity(new Intent(view.getContext(), EditTestActivity.class));
    }

    @Override
    public void onDeleteTest(int position, int listTestSize) {
        if (position == currentTestSelected || currentTestSelected == -1) {
            if (listTestSize > 0)
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
            public void showXmlImportDialog(Test test) {
                ImportXmlDialogFragment importXmlDialogFragment = new ImportXmlDialogFragment(test);
                importXmlDialogFragment.show(getSupportFragmentManager(), "dialog");
            }

            @Override
            public void showCsvImportDialog(String[] firstLines, Uri uri) {
                ImportCsvDialogFragment importCsvDialogFragment = new ImportCsvDialogFragment(firstLines, uri);
                importCsvDialogFragment.show(getSupportFragmentManager(), "dialog");
            }
        });
    }

    @Override
    public void createXmlFile(int position, boolean saveNumberTestDone) {
        diakoluoApplication.setCurrentTest(position);
        FileManager.exportXmlTest(ListTestActivity.this, saveNumberTestDone);
    }

    @Override
    public void createCsvFile(int position, boolean columnHeader, boolean columnTypeHeader, String separator, String lineSeparator) {
        diakoluoApplication.setCurrentTest(position);
        FileManager.exportCsvTest(ListTestActivity.this, columnHeader, columnTypeHeader, separator, lineSeparator);
    }

    @Override
    public void loadXmlFile(Test importedTest) {
        // Add test and update recycler
        importTest(diakoluoApplication, importedTest);
    }


    @Override
    public void loadCsvFile(Uri fileUri, String name, int separatorId, boolean loadColumnName,
                            boolean loadColumnType) {
        ParcelFileDescriptor pfd = null;
        try {
            pfd = getContentResolver().openFileDescriptor(fileUri, "r");
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
            new MaterialAlertDialogBuilder(this)
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
            new MaterialAlertDialogBuilder(this)
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
            new MaterialAlertDialogBuilder(this)
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
            int numberTest = diakoluoApplication.getNumberTest();
            diakoluoApplication.addTest(currentImportTest);

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentListTest);
            if (fragment != null) {
                ((ListTestsFragment) fragment).notifyUpdateInserted(numberTest);
            }
        }
    }
}
