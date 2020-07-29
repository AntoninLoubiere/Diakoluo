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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.Test;


public class TestSettingsFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

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

    public TestSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_test_settings, container, false);

        final TextView numberColumnToShowSeekBarTextView = inflatedView.findViewById(R.id.numberColumnToShowSeekBarTextView);
        final SeekBar numberColumnToShowSeekBar = inflatedView.findViewById(R.id.numberColumnToShowSeekBar);
        final Spinner numberQuestionToAskSpinner = inflatedView.findViewById(R.id.numberQuestionToAskSpinner);

        Button validButton = inflatedView.findViewById(R.id.validButton);

        final Test currentTest = DiakoluoApplication.getCurrentTest(inflatedView.getContext());

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


        ArrayAdapter<TestQuestionPossibility> adapter = new ArrayAdapter<>(inflatedView.getContext(),
                R.layout.support_simple_spinner_dropdown_item, listOfPossibility);
        numberQuestionToAskSpinner.setAdapter(adapter);

        numberColumnToShowSeekBar.setMax(currentTest.getNumberColumn() - 2);

        numberColumnToShowSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                numberColumnToShowSeekBarTextView.setText(getString(
                        R.string.number_column_to_show_seekbar_textview,
                        i + 1, currentTest.getNumberColumn()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        numberColumnToShowSeekBarTextView.setText(getString(
                R.string.number_column_to_show_seekbar_textview,
                1, currentTest.getNumberColumn()));

        validButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numberQuestionToAsk = ((TestQuestionPossibility) numberQuestionToAskSpinner.getSelectedItem()).possibility;
                int numberColumnToShow = numberColumnToShowSeekBar.getProgress() + 1;
                mListener.onDoTest(numberQuestionToAsk, numberColumnToShow);
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

    public interface OnFragmentInteractionListener {
        void onDoTest(int numberQuestionToAsk, int numberColumnToShow);
    }
}
