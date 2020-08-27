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

package fr.pyjacpp.diakoluo.edit_test;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.Utils;
import fr.pyjacpp.diakoluo.tests.Test;

public class MainInformationEditTestFragment extends Fragment implements DiakoluoApplication.GetTestRunnable {
    private OnFragmentInteractionListener mListener;
    private View inflatedView;
    private TextView numberTestDid;
    @Nullable private Test currentEditTest;
    private EditText title;
    private DateFormat dateFormat;
    private DateFormat timeFormat;
    private EditText description;
    private TextView createdDate;
    private TextView lastModification;

    public MainInformationEditTestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_edit_main_informations_test, container, false);

        inflatedView.findViewById(R.id.titleTextView);

        dateFormat = android.text.format.DateFormat.getLongDateFormat(container.getContext().getApplicationContext());
        timeFormat = android.text.format.DateFormat.getTimeFormat(container.getContext().getApplicationContext());


        title = inflatedView.findViewById(R.id.titleEditText);
        description = inflatedView.findViewById(R.id.descriptionEditText);
        createdDate = inflatedView.findViewById(R.id.createdDateTextView);
        lastModification = inflatedView.findViewById(R.id.lastModificationTextView);
        numberTestDid = inflatedView.findViewById(R.id.numberTestDid);

        DiakoluoApplication.get(container.getContext()).getCurrentEditTest(
                new DiakoluoApplication.GetTest(
                        true, (AppCompatActivity) getActivity(), false, this));

        return inflatedView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        getFromTestVar();
    }

    private void getFromTestVar() {
        if (currentEditTest != null) {
            Spinner scoreMethod = inflatedView.findViewById(R.id.scoreMethodSpinner);
            scoreMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                TextView helperSpinnerText = inflatedView.findViewById(R.id.spinnerHelperText);

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    helperSpinnerText.setText(
                            getResources().getStringArray(R.array.score_method_helper_text)[i]
                    );
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            title.setText(currentEditTest.getName());
            description.setText(currentEditTest.getDescription());
            scoreMethod.setSelection(currentEditTest.getScoreMethod() ? 0 : 1);
        }
    }

    public void updateTestDid() {
        if (currentEditTest != null) {
            numberTestDid.setText(
                    String.format(
                            getString(R.string.number_test_did_format),
                            currentEditTest.getNumberTestDid()
                    )
            );
        }
    }

    @Override
    public void loadingInProgress() {
    }

    @Override
    public void error(boolean canceled) {
        mListener.errorFinish(canceled);
    }

    @Override
    public void success(@NonNull Test test) {
        currentEditTest = test;

        title.addTextChangedListener(new EditTextTextWatcher(title) {
            @Override
            Utils.EditValidator validatorFunction(String text) {
                return mListener.titleEditTestValidator(text);
            }
        });
        description.addTextChangedListener(new EditTextTextWatcher(description) {
            @Override
            Utils.EditValidator validatorFunction(String text) {
                return mListener.descriptionEditTestValidator(text);
            }
        });


        createdDate.setText(
                String.format(
                        getString(R.string.created_date_test_format),
                        dateFormat.format(currentEditTest.getCreatedDate()),
                        timeFormat.format(currentEditTest.getCreatedDate())
                )
        );

        lastModification.setText(
                String.format(
                        getString(R.string.last_modification_test_format),
                        dateFormat.format(currentEditTest.getLastModificationDate()),
                        timeFormat.format(currentEditTest.getLastModificationDate())
                )
        );

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                updateTestDid();
                getFromTestVar();
            }
        });
    }

    public interface OnFragmentInteractionListener {
        Utils.EditValidator titleEditTestValidator(String text);

        Utils.EditValidator descriptionEditTestValidator(String text);

        void errorFinish(boolean canceled);
    }

    abstract class EditTextTextWatcher implements TextWatcher {

        private final EditText editText;

        EditTextTextWatcher(EditText editText) {
            super();
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            Utils.EditValidator validatorResponse = validatorFunction(editable.toString());

            if (validatorResponse.isError()) {
                String msg = getString(validatorResponse.getErrorMessageResourceId());
                if (validatorResponse.isWarning()) {
                    Drawable warningIcon = ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_warning_yellow_24dp, null);
                    if (warningIcon != null) {
                        warningIcon.setBounds(0, 0, warningIcon.getIntrinsicWidth(), warningIcon.getIntrinsicHeight());
                    }

                    editText.setError(msg, warningIcon);
                } else {
                    editText.setError(msg);
                }
            } else {
                editText.setError(null);
            }
        }

        abstract Utils.EditValidator validatorFunction(String text);
    }
}
