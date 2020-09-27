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

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.Test;


public class TestSettingsFragment extends Fragment
        implements DiakoluoApplication.GetTestRunnable {

    private OnFragmentInteractionListener mListener;
    private ColumnToShow numberColumnToShow;
    @Nullable
    private Test currentTest;
    private TextView numberColumnToShowSeekBarTextView;
    private SeekBar numberColumnToShowSeekBar;
    private Spinner numberQuestionToAskSpinner;
    private Spinner scoreMethodSpinner;
    private Button validButton;

    public TestSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_test_settings, container, false);

        numberColumnToShowSeekBarTextView = inflatedView.findViewById(R.id.numberColumnToShowSeekBarTextView);
        numberColumnToShowSeekBar = inflatedView.findViewById(R.id.numberColumnToShowSeekBar);
        numberQuestionToAskSpinner = inflatedView.findViewById(R.id.numberQuestionToAskSpinner);
        scoreMethodSpinner = inflatedView.findViewById(R.id.scoreMethod);
        validButton = inflatedView.findViewById(R.id.validButton);

        DiakoluoApplication.get(requireContext()).getCurrentTest(
                new DiakoluoApplication.GetTest(true, (AppCompatActivity) getActivity(), false, this));

        return inflatedView;
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
        currentTest = test;

        numberColumnToShow = currentTest.getNumberColumnToAsk();

        ArrayList<TestQuestionPossibility> listOfPossibility = new ArrayList<>();

        for (int i = 0; i < currentTest.getNumberRow() - 10; i += 10) {
            listOfPossibility.add(new TestQuestionPossibility(
                    i + 10,
                    i + 10 + "/" + currentTest.getNumberRow())
            );
        }
        listOfPossibility.add(new TestQuestionPossibility(
                currentTest.getNumberRow(),
                currentTest.getNumberRow() + "/" + currentTest.getNumberRow())
        );


        final ArrayAdapter<TestQuestionPossibility> adapter = new ArrayAdapter<>(requireContext(),
                R.layout.support_simple_spinner_dropdown_item, listOfPossibility);

        int max = numberColumnToShow.numberColumnToShowMax -
                numberColumnToShow.numberColumnToShowMin;
        if (max < 1) {
            numberColumnToShowSeekBar.setVisibility(View.INVISIBLE);
        } else {
            numberColumnToShowSeekBar.setMax(max);  // -1 because 0 is a possibility
        }
        numberColumnToShowSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                numberColumnToShowSeekBarTextView.setText(getString(
                        R.string.number_column_to_show_seekbar_textview,
                        numberColumnToShow.numberColumnToShowMin + i,
                        numberColumnToShow.numberColumnToShowMax,
                        numberColumnToShow.numberColumnTotal));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        validButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentTest != null) {
                    int nQTA = ((TestQuestionPossibility)
                            numberQuestionToAskSpinner.getSelectedItem()).possibility;
                    int nbColToShow = numberColumnToShowSeekBar.getProgress() +
                            numberColumnToShow.numberColumnToShowMin;
                    boolean proportionalityScoreMethod;
                    if (scoreMethodSpinner.getSelectedItemPosition() == 0) {
                        proportionalityScoreMethod = currentTest.getScoreMethod();
                    } else {
                        proportionalityScoreMethod = scoreMethodSpinner.getSelectedItemPosition() == 1;
                    }
                    mListener.onDoTest(nQTA, nbColToShow, proportionalityScoreMethod);
                }
            }
        });

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                numberQuestionToAskSpinner.setAdapter(adapter);
                numberColumnToShowSeekBarTextView.setText(getString(
                        R.string.number_column_to_show_seekbar_textview,
                        numberColumnToShow.numberColumnToShowMin,
                        numberColumnToShow.numberColumnToShowMax,
                        numberColumnToShow.numberColumnTotal));
            }
        });
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

    public interface OnFragmentInteractionListener {
        void onDoTest(int numberQuestionToAsk, int numberColumnToShow, boolean proportionalityScoreMethod);

        void errorFinish(boolean canceled);
    }

    private static class TestQuestionPossibility {
        final int possibility;
        final String toShow;

        TestQuestionPossibility(int possibility, String toShow) {
            this.possibility = possibility;
            this.toShow = toShow;
        }

        @NonNull
        @Override
        public String toString() {
            return toShow;
        }
    }
}
