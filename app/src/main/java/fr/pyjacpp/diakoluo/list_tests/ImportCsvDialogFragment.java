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

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import fr.pyjacpp.diakoluo.R;

public class ImportCsvDialogFragment extends DialogFragment {
    private static final String PREF_FIRST_LINES = "firstLines";
    private static final String PREF_URI = "uri";
    private String[] firstLines;
    private Uri uri;
    private OnValidListener listener;

    private EditText titleEditText;
    private CheckBox loadColumnName;
    private CheckBox loadColumnType;
    private Spinner separatorSpinner;

    public ImportCsvDialogFragment() {
        firstLines = null;
        uri = null;
    }

    public ImportCsvDialogFragment(String[] firstLines, Uri uri) {
        this.firstLines = firstLines;
        this.uri = uri;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            firstLines = savedInstanceState.getStringArray(PREF_FIRST_LINES);
            uri = Uri.parse(savedInstanceState.getString(PREF_URI));
        }

        final View inflatedView = inflater.inflate(R.layout.fragment_dialog_import_csv, container, false);

        titleEditText = inflatedView.findViewById(R.id.titleInput);
        loadColumnName = inflatedView.findViewById(R.id.columnHeaderCheckBox);
        loadColumnType = inflatedView.findViewById(R.id.columnTypeHeaderCheckBox);
        separatorSpinner = inflatedView.findViewById(R.id.separatorSpinner);

        TextView filePreviewTextFile = inflatedView.findViewById(R.id.previewTextView);

        Button cancelButton = inflatedView.findViewById(R.id.cancelButton);
        Button validButton = inflatedView.findViewById(R.id.validButton);

        if (savedInstanceState == null) {
            // first dialog open
            titleEditText.setText(R.string.test_default_name);
        }

        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() < 1) {
                    titleEditText.setError(getString(R.string.error_label_title_blank));
                } else {
                    titleEditText.setError(null);
                }
            }
        });

        StringBuilder previewText = new StringBuilder();

        for (int i = 0; i < firstLines.length; i++) {
            if (i > 0)
                previewText.append("\n\n");

            previewText.append(firstLines[i]);
        }
        filePreviewTextFile.setText(previewText.toString());

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        validButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (titleEditText.getText().length() < 1) {
                    titleEditText.requestFocus();
                } else {
                    listener.loadCsvFile(uri,
                            titleEditText.getText().toString(),
                            (int) separatorSpinner.getSelectedItemId(),
                            loadColumnName.isChecked(), loadColumnType.isChecked());
                    dismiss();
                }
            }
        });

        return inflatedView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray(PREF_FIRST_LINES, firstLines);
        outState.putString(PREF_URI, uri.toString());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnValidListener) {
            listener = (OnValidListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnValidListener");
        }
    }

    public interface OnValidListener {
        void loadCsvFile(Uri fileUri, String name, int separatorId, boolean loadColumnName,
                         boolean loadColumnType);
    }
}
