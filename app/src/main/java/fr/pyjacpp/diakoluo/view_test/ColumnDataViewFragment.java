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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.OnSwipeTouchListener;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.tests.column.Column;

public class ColumnDataViewFragment extends Fragment {
    static final String ARG_COLUMN_INDEX = "column_index";

    private int columnIndex;

    private OnFragmentInteractionListener mListener;
    private View inflatedView;

    public ColumnDataViewFragment() {
    }

    public static ColumnDataViewFragment newInstance(int columnIndex) {
        ColumnDataViewFragment fragment = new ColumnDataViewFragment();
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_view_column_data, container, false);

        DiakoluoApplication.get(requireContext()).getCurrentTest(
                new DiakoluoApplication.GetTest(true, (AppCompatActivity) getActivity(),
                        false, new DiakoluoApplication.GetTestRunnable() {
                    @Override
                    public void loadingInProgress() {
                    }

                    @Override
                    public void error(boolean canceled) {

                    }

                    @Override
                    public void success(@NonNull Test test) {
                        Column column = test.getListColumn().get(columnIndex);

                        TextView titleEditText = inflatedView.findViewById(R.id.titleTextView);
                        TextView descriptionEditText = inflatedView.findViewById(R.id.descriptionTextView);
                        TextView columnTypeTextView = inflatedView.findViewById(R.id.columnTypeTextView);
                        LinearLayout columnSettingsParent = inflatedView.findViewById(R.id.columnSettings);

                        titleEditText.setText(column.getName());
                        descriptionEditText.setText(column.getDescription());
                        columnTypeTextView.setText(column.getInputType().name());

                        column.getViewColumnSettings(inflater, columnSettingsParent); // FIXME  handle in handler

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
                    }
                }));

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

    interface OnFragmentInteractionListener {
        void onSwipeRight();
        void onSwipeLeft();
    }
}


