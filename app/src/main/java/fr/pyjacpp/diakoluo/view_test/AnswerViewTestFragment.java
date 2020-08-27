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
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.Test;


public class AnswerViewTestFragment extends Fragment implements
        AnswerViewTestRecyclerListFragment.OnParentFragmentInteractionListener, DiakoluoApplication.GetTestRunnable {

    private OnFragmentInteractionListener mListener;

    private boolean detailAnswer;
    private View inflatedView;

    public AnswerViewTestFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.fragment_view_answer_test, container, false);

        detailAnswer = inflatedView.findViewById(R.id.answerDataViewFragmentContainer) != null;

        DiakoluoApplication.get(requireContext()).getCurrentTest(
                new DiakoluoApplication.GetTest(true, this));
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
    public void onItemClick(View view, int position) {
        if (detailAnswer) {
            getChildFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.fragment_fade_scale_enter, R.anim.fragment_fade_scale_exit)
                    .replace(R.id.answerDataViewFragmentContainer, AnswerDataViewFragment.newInstance(position))
                    .commit();
        } else {
            Intent intent = new Intent(requireContext(), AnswerDataViewActivity.class);
            intent.putExtra(AnswerDataViewFragment.ARG_ANSWER_INDEX, position);
            startActivity(intent);
        }
    }

    @Override
    public void loadingInProgress() {
    }

    @Override
    public void error(boolean canceled) {

    }

    @Override
    public void success(@NonNull Test test) {
        if (detailAnswer && test.getNumberRow() > 0) {
            onItemClick(inflatedView, 0); // show first element
        }
    }

    public interface OnFragmentInteractionListener {
    }
}
