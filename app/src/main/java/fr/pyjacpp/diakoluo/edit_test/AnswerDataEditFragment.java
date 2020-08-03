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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.OnSwipeTouchListener;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.column.Column;
import fr.pyjacpp.diakoluo.tests.data.DataCell;

public class AnswerDataEditFragment extends Fragment {
    static final String ARG_ANSWER_INDEX = "answer_index";

    private final HashMap<Column, View> columnAnswerEditHashMap = new HashMap<>();

    private OnFragmentInteractionListener mListener;
    @Nullable
    private OnParentFragmentInteractionListener parentListener;

    private View inflatedView;
    private int answerIndex;

    public AnswerDataEditFragment() {
    }

    @NonNull
    public static AnswerDataEditFragment newInstance(int answerIndex) {
        AnswerDataEditFragment fragment = new AnswerDataEditFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ANSWER_INDEX, answerIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            answerIndex = getArguments().getInt(ARG_ANSWER_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_edit_answer_data, container, false);

        if (answerIndex >= 0) {
            LinearLayout layout = inflatedView.findViewById(R.id.answerListLinearLayout);

            DataRow row = DiakoluoApplication.getCurrentEditTest(inflatedView.getContext()).getListRow().get(answerIndex);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(layout.getLayoutParams());
            params.topMargin = 24;

            View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b)
                        saveChanges();
                }
            };

            ArrayList<Column> listColumn = DiakoluoApplication.getCurrentEditTest(inflatedView.getContext()).getListColumn();
            for (int i = 0; i < listColumn.size(); i++) {
                Column column = listColumn.get(i);

                DataCell dataCell = row.getListCells().get(column);
                View columnTitle = column.showColumnName(inflatedView.getContext());

                if (i > 0)
                    columnTitle.setLayoutParams(params);

                layout.addView(columnTitle);

                if (dataCell == null) {
                    dataCell = DataCell.getDefaultValueCell(column);
                    row.getListCells().put(column, dataCell);
                }

                View columnValue = dataCell.showEditValue(inflatedView.getContext(), column);
                columnAnswerEditHashMap.put(column, columnValue);
                columnValue.setOnFocusChangeListener(onFocusChangeListener);
                layout.addView(columnValue);
            }
        }

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
        if (answerIndex >= 0) {
            DataRow row = DiakoluoApplication.getCurrentEditTest(inflatedView.getContext()).getListRow().get(answerIndex);

            ArrayList<Column> listColumn = DiakoluoApplication.getCurrentEditTest(inflatedView.getContext()).getListColumn();
            for (int i = 0; i < listColumn.size(); i++) {
                Column column = listColumn.get(i);

                View answerEdit = columnAnswerEditHashMap.get(column);

                DataCell dataCell = row.getListCells().get(column);

                if (answerEdit != null) {
                    if (dataCell == null) {
                        DataCell.setDefaultCellFromView(answerEdit, row, column);
                    } else {
                        dataCell.setValueFromView(answerEdit);
                    }
                }
            }
            if (parentListener != null)
                parentListener.updateItem(answerIndex);
            else {
                mListener.updateAnswerRecyclerItem(answerIndex);
            }

        }
    }

    int getAnswerIndex() {
        return answerIndex;
    }

    void setAnswerIndex(int i) {
        answerIndex = i;
        if (getArguments() != null) {
            getArguments().putInt(ARG_ANSWER_INDEX, i);
        }
    }

    interface OnFragmentInteractionListener {
        void updateAnswerRecyclerItem(int position);
        void onSwipeRight();
        void onSwipeLeft();
    }

    interface OnParentFragmentInteractionListener {
        void updateItem(int position);
    }
}


