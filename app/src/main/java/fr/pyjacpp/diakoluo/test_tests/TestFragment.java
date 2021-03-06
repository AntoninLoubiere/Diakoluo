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

package fr.pyjacpp.diakoluo.test_tests;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.HashMap;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.column.Column;
import fr.pyjacpp.diakoluo.tests.data.DataCell;


public class TestFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private View inflatedView;
    private TestTestContext testTestContext;

    private final HashMap<Column, LinearLayout> columnLinearLayoutHashMap = new HashMap<>();
    private final HashMap<Column, View> columnViewHashMap = new HashMap<>();

    public TestFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.fragment_test, container, false);

        Button validButton = inflatedView.findViewById(R.id.validButton);
        LinearLayout linearLayout = inflatedView.findViewById(R.id.answerListLinearLayout);
        ProgressBar progressBar = inflatedView.findViewById(R.id.progressBar);

        LinearLayout.LayoutParams params = new TableRow.LayoutParams();
        params.topMargin = 24;

        for (Column column : testTestContext.getTest().getListColumn()) {
            LinearLayout answerRow = new LinearLayout(inflatedView.getContext());
            answerRow.setOrientation(LinearLayout.HORIZONTAL);

            linearLayout.addView(column.showColumnName(inflatedView.getContext()), params);

            linearLayout.addView(answerRow);

            columnLinearLayoutHashMap.put(column, answerRow);
            columnViewHashMap.put(column, null);
        }

        validButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validButtonClick();
            }
        });

        progressBar.setMax(testTestContext.getMaxProgress());

        updateAnswer();

        return inflatedView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        inflatedView = null;
    }

    private void updateAnswer() {
        if (inflatedView != null) {
            Button validButton = inflatedView.findViewById(R.id.validButton);
            ProgressBar progressBar = inflatedView.findViewById(R.id.progressBar);

            LinearLayout.LayoutParams params = new TableRow.LayoutParams();
            params.weight = 1;

            if (testTestContext.isAnswerGive()) {
                // if the answer is give

                validButton.setText(R.string.continue_text);
                ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress",
                        testTestContext.getProgress() + TestTestContext.PROGRESS_BAR_PRECISION);
                animation.setDuration(300);
                animation.setInterpolator(new AccelerateDecelerateInterpolator());
                animation.start();
                progressBar.setProgress(testTestContext.getProgress() + TestTestContext.PROGRESS_BAR_PRECISION);

                for (Column column : testTestContext.getTest().getListColumn()) {
                    LinearLayout answerRow = columnLinearLayoutHashMap.get(column);

                    if (answerRow != null) {
                        answerRow.removeAllViews();

                        Boolean columnShow = testTestContext.getShowColumn().get(column);
                        DataCell dataCell = testTestContext.getCurrentRow().getListCells().get(column);

                        if (columnShow != null && dataCell != null) {
                            if (columnShow) {
                                addAnswer(column, answerRow, params);
                            } else {
                                Object answer = testTestContext.getUserAnswer().get(column);
                                if (answer != null) {
                                    DataCell.ShowValueResponse showValueResponse = dataCell.showValue(
                                            inflatedView.getContext(), answer);

                                    answerRow.addView(showValueResponse.getValueView(), params);
                                    if (!showValueResponse.isAnswerTrue()) {
                                        addAnswer(column, answerRow, params);
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // if the user need to enter the answer

                validButton.setText(R.string.valid);
                progressBar.setProgress(testTestContext.getProgress());

                for (Column column : testTestContext.getTest().getListColumn()) {
                    LinearLayout answerRow = columnLinearLayoutHashMap.get(column);

                    if (answerRow != null) {
                        answerRow.removeAllViews();


                        Boolean columnShow = testTestContext.getShowColumn().get(column);
                        if (columnShow != null && columnShow) {
                            addAnswer(column, answerRow, params);
                        } else {
                            View answerEdit = column.showColumnEditValue(
                                    inflatedView.getContext(),
                                    testTestContext.getUserAnswer().get(column));

                            answerRow.addView(answerEdit, params);

                            columnViewHashMap.put(column, answerEdit);
                        }
                    }
                }
            }
        }
    }

    private void addAnswer(Column column, LinearLayout row, LinearLayout.LayoutParams params) {
        DataCell dataCell = testTestContext.getCurrentRow().getListCells().get(column);
        if (dataCell != null) {
            View answer = dataCell.showValue(row.getContext());
            row.addView(answer, params);
            columnViewHashMap.put(column, answer);
        }
    }

    private void validButtonClick() {
        if (testTestContext.isAnswerGive()) {
            if (testTestContext.incrementCurrentIndex()) {
                testTestContext.selectShowColumn();
                testTestContext.removeUserInput();
                testTestContext.setAnswerGive(false);
                updateAnswer();
            } else {
                mListener.showScore(testTestContext);
            }
        } else {
            saveAnswer();
            addScore();
            testTestContext.setAnswerGive(true);
            updateAnswer();
        }
    }

    private void addScore() {
        for (Column column : testTestContext.getTest().getListColumn()) {
            Boolean columnShow = testTestContext.getShowColumn().get(column);
            Object answer = testTestContext.getUserAnswer().get(column);
            DataCell dataCell = testTestContext.getCurrentRow().getListCells().get(column);

            if (columnShow != null && answer != null && dataCell != null && !columnShow) {
                dataCell.verifyAndScoreAnswer(testTestContext, answer);
            }
        }
    }

    private void saveAnswer() {
        for (Column column : testTestContext.getTest().getListColumn()) {
            Boolean columnShow = testTestContext.getShowColumn().get(column);
            View valueView = columnViewHashMap.get(column);
            DataCell dataCell = testTestContext.getCurrentRow().getListCells().get(column);

            if (columnShow != null && valueView != null && dataCell != null && !columnShow) {
                testTestContext.getUserAnswer().put(column, dataCell.getValueFromView(valueView));
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!testTestContext.isAnswerGive()) {
            saveAnswer();
        }
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

        testTestContext = DiakoluoApplication.getTestTestContext(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void showScore(TestTestContext context);
    }
}
