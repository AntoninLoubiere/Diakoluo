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

package fr.pyjacpp.diakoluo.view_test;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.OnSwipeTouchListener;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.tests.column.Column;
import fr.pyjacpp.diakoluo.tests.data.DataCell;

public class AnswerDataViewFragment extends Fragment implements DiakoluoApplication.GetTestRunnable {
    static final String ARG_ANSWER_INDEX = "answer_index";

    private int answerIndex;

    private OnFragmentInteractionListener mListener;
    private View inflatedView;

    public AnswerDataViewFragment() {
    }


    public static AnswerDataViewFragment newInstance(int answerIndex) {
        AnswerDataViewFragment fragment = new AnswerDataViewFragment();
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

        inflatedView = inflater.inflate(R.layout.fragment_view_answer_data, container, false);

        DiakoluoApplication.get(requireContext()).getCurrentTest(
                new DiakoluoApplication.GetTest(
                        false, (AppCompatActivity) getActivity(), false,
                        this));

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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        // TODO handle in handler

        DataRow row = test.getListRow().get(answerIndex);

        LinearLayout layout = inflatedView.findViewById(R.id.answerListLinearLayout);

        LinearLayout.LayoutParams params = new TableRow.LayoutParams();
        params.topMargin = 24;

        ArrayList<Column> listColumn = test.getListColumn();
        for (int i = 0; i < listColumn.size(); i++) {
            Column column = listColumn.get(i);

            DataCell dataCell = row.getListCells().get(column);
            if (dataCell != null) {
                View columnTitle = column.showColumnName(requireContext());

                if (i > 0)
                    columnTitle.setLayoutParams(params);

                layout.addView(columnTitle);
                layout.addView(column.showViewValueView(requireContext(), dataCell));
            }
        }
    }

    interface OnFragmentInteractionListener {
        void onSwipeRight();

        void onSwipeLeft();

        void errorFinish(boolean canceled);
    }
}


