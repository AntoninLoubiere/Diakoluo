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
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.OnSwipeTouchListener;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.tests.column.Column;

public class ColumnDataEditFragment extends Fragment implements DiakoluoApplication.GetTestRunnable {
    static final String ARG_COLUMN_INDEX = "column_index";

    private int columnIndex;

    private OnFragmentInteractionListener mListener;
    @Nullable
    private OnParentFragmentInteractionListener parentListener;

    private View inflatedView;
    private EditText titleEditText;
    private EditText descriptionEditText;

    private Column column;
    @Nullable
    private Test currentEditTest;
    private LinearLayout columnSettingsParent;
    private View editValueView = null;
    private Spinner columnTypeSpinner;

    public ColumnDataEditFragment() {
    }

    @NonNull
    public static ColumnDataEditFragment newInstance(int columnIndex) {
        ColumnDataEditFragment fragment = new ColumnDataEditFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_INDEX, columnIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            columnIndex = getArguments().getInt(ARG_COLUMN_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_edit_column_data, container, false);

        DiakoluoApplication.get(inflatedView.getContext()).getCurrentEditTest(
                new DiakoluoApplication.GetTest(true,
                        (AppCompatActivity) getActivity(), false, this));

        inflatedView.setOnTouchListener(new OnSwipeTouchListener(inflatedView.getContext()) {
            @Override
            public void onSwipeRight() {
                mListener.onSwipeRight();
            }

            @Override
            public void onSwipeLeft() {
                mListener.onSwipeLeft();
            }
        });

        return inflatedView;
    }

    /**
     * Initialize the ui could be in a worker thread.
     *
     * @param test the test to show
     */
    private void initialize(@NonNull Test test) {
        currentEditTest = test;

        titleEditText = inflatedView.findViewById(R.id.titleEditText);
        descriptionEditText = inflatedView.findViewById(R.id.descriptionEditText);
        columnSettingsParent = inflatedView.findViewById(R.id.columnSettings);
        columnTypeSpinner = inflatedView.findViewById(R.id.columnTypeSpinner);

        column = currentEditTest
                .getListColumn().get(columnIndex);

        View.OnFocusChangeListener editTextFocusListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    saveChanges();
                }
            }
        };

        titleEditText.setOnFocusChangeListener(editTextFocusListener);
        descriptionEditText.setOnFocusChangeListener(editTextFocusListener);

        ArrayAdapter<ColumnInputType> adapter = new ArrayAdapter<>(inflatedView.getContext(),
                R.layout.support_simple_spinner_dropdown_item, ColumnInputType.values());
        columnTypeSpinner.setAdapter(adapter);

        columnTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                ColumnInputType inputType = ColumnInputType.values()[position];
                if (column.getInputType() != inputType) {
                    saveChanges(); // save change before migrate
                    Column newColumn = Column.newColumn(inputType);
                    newColumn.migrateColumn(currentEditTest, column);
                    currentEditTest.getListColumn().set(columnIndex, newColumn);
                    column = newColumn;
                    updateColumnSettings();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                titleEditText.setText(column.getName());
                descriptionEditText.setText(column.getDescription());
                columnTypeSpinner.setSelection(column.getInputType().ordinal());
                updateColumnSettings();
            }
        });
    }

    /**
     * Update the column settings, must be in the UI thread
     */
    public void updateColumnSettings() {
        columnSettingsParent.removeAllViews();
        editValueView = column.showEditValueView(columnSettingsParent.getContext(), column.getDefaultValue());
        columnSettingsParent.addView(editValueView);
        column.getEditColumnSettings(getLayoutInflater(), columnSettingsParent);
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

        if (getParentFragment() instanceof OnParentFragmentInteractionListener) {
            parentListener = (OnParentFragmentInteractionListener) getParentFragment();
        } else {
            parentListener = null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        saveChanges();
        super.onPause();
    }

    private void saveChanges() {
        if (currentEditTest != null && columnIndex >= 0) {
            Column column = currentEditTest.getListColumn().get(columnIndex);

            column.setName(titleEditText.getText().toString());
            column.setDescription(descriptionEditText.getText().toString());
            column.setEditColumnSettings(columnSettingsParent);

            if (editValueView != null) {
                column.setDefaultValue(column.getValueFromView(editValueView));
            }

            if (parentListener != null)
                parentListener.updateItem(columnIndex);
            else {
                mListener.updateColumnRecyclerItem(columnIndex);
            }
        }
    }

    int getColumnIndex() {
        return columnIndex;
    }

    void setColumnIndex(int i) {
        columnIndex = i;
        if (getArguments() != null) {
            getArguments().putInt(ARG_COLUMN_INDEX, i);
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
        initialize(test);
    }

    interface OnFragmentInteractionListener {
        void updateColumnRecyclerItem(int position);

        void onSwipeRight();

        void onSwipeLeft();

        void errorFinish(boolean canceled);
    }

    public interface OnParentFragmentInteractionListener {
        void updateItem(int position);
    }


}


