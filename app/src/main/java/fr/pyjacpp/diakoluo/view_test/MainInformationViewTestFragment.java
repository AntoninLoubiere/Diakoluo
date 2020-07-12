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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.Test;

public class MainInformationViewTestFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private TextView title;
    private TextView description;
    private TextView createdDate;
    private TextView lastModification;
    private TextView numberTestDid;
    private View separator1;
    private View separator2;
    private Test currentTest;

    public MainInformationViewTestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_view_main_information_test, container, false);

        title = inflatedView.findViewById(R.id.titleTextView);
        description = inflatedView.findViewById(R.id.descriptionTextView);
        createdDate = inflatedView.findViewById(R.id.createdDateTextView);
        lastModification = inflatedView.findViewById(R.id.lastModificationTextView);
        numberTestDid = inflatedView.findViewById(R.id.numberTestDid);

        separator1 = inflatedView.findViewById(R.id.separator1);
        separator2 = inflatedView.findViewById(R.id.separator2);

        updateContent(inflatedView.getContext());

        return inflatedView;
    }

    public void updateContent(Context context){

        DateFormat dateFormat = android.text.format.DateFormat.getLongDateFormat(context.getApplicationContext());
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context.getApplicationContext());

        currentTest = DiakoluoApplication.getCurrentTest(context);

        if (currentTest != null) {
            title.setVisibility(View.VISIBLE);
            title.setText(currentTest.getName());

            description.setVisibility(View.VISIBLE);
            description.setText(currentTest.getDescription());

            createdDate.setVisibility(View.VISIBLE);
            createdDate.setText(
                    String.format(
                            getString(R.string.created_date_test_format),
                            dateFormat.format(currentTest.getCreatedDate()),
                            timeFormat.format(currentTest.getCreatedDate())
                    )
            );

            lastModification.setVisibility(View.VISIBLE);
            lastModification.setText(
                    String.format(
                            getString(R.string.last_modification_test_format),
                            dateFormat.format(currentTest.getLastModificationDate()),
                            timeFormat.format(currentTest.getLastModificationDate())
                    )
            );

            numberTestDid.setVisibility(View.VISIBLE);
            updateTestDid();

            separator1.setVisibility(View.VISIBLE);
            separator2.setVisibility(View.VISIBLE);
        } else {
            title.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
            createdDate.setVisibility(View.GONE);
            lastModification.setVisibility(View.GONE);
            numberTestDid.setVisibility(View.GONE);
            separator1.setVisibility(View.GONE);
            separator2.setVisibility(View.GONE);
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    void updateTestDid() {
        numberTestDid.setText(
                String.format(
                        getString(R.string.number_test_did_format),
                        currentTest.getNumberTestDid()
                )
        );
    }

    public interface OnFragmentInteractionListener {
    }
}
