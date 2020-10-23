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
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.Test;


public class ImportXmlDialogFragment extends DialogFragment {

    private final Test importedTest;
    private OnValidListener listener;
    private EditText titleEditText;

    public ImportXmlDialogFragment() {
        importedTest = null;
    }

    public ImportXmlDialogFragment(Test test) {
        importedTest = test;
        setRetainInstance(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (importedTest == null) {
            // abort the test has been forgot
            dismiss();
            return new View(requireContext());
        }

        final View inflatedView = inflater.inflate(R.layout.fragment_dialog_import_xml, container, false);

        titleEditText = inflatedView.findViewById(R.id.titleInput);

        Button cancelButton = inflatedView.findViewById(R.id.cancelButton);
        Button validButton = inflatedView.findViewById(R.id.validButton);

        if (savedInstanceState == null) {
            // first dialog open
            titleEditText.setText(importedTest.getName());
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
                    importedTest.setName(titleEditText.getText().toString());
                    listener.loadXmlFile(importedTest);
                    dismiss();
                }
            }
        });

        return inflatedView;
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
        void loadXmlFile(Test importedTest);
    }
}
