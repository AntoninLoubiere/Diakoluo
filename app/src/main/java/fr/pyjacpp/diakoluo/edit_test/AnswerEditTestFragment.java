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
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.RecyclerViewChange;
import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.Test;


public class AnswerEditTestFragment extends Fragment implements
        AnswerEditTestRecyclerListFragment.OnParentFragmentInteractionListener,
        AnswerDataEditFragment.OnParentFragmentInteractionListener, DiakoluoApplication.GetTestRunnable {
    private OnFragmentInteractionListener mListener;

    private boolean answerDetail;
    @Nullable
    private AnswerDataEditFragment answerDataEditFragment;
    private AnswerEditTestRecyclerListFragment answerEditTestRecyclerListFragment;
    @Nullable private Test editTest;

    public AnswerEditTestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View inflatedView = inflater.inflate(R.layout.fragment_edit_answer_test, container, false);

        answerDetail = inflatedView.findViewById(R.id.answerDataEditFragmentContainer) != null;
        answerEditTestRecyclerListFragment = (AnswerEditTestRecyclerListFragment)
                getChildFragmentManager().findFragmentById(R.id.answerEditTestRecyclerListFragment);

        DiakoluoApplication.get(inflatedView.getContext()).getCurrentEditTest(
                new DiakoluoApplication.GetTest(true,
                        (AppCompatActivity) getActivity(), false, this));

        Button addAnswerButton = inflatedView.findViewById(R.id.addAnswerButton);
        addAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTest != null) {
                    updateNewItem();
                    onItemClick(editTest.getNumberRow() - 1);
                }
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
    public void onItemClick(int position) {
        onItemClick(position, false);
    }

    public void onItemClick(int position, boolean forceUpdate) {
        if (answerDetail) {
            if (answerDataEditFragment == null || answerDataEditFragment.getAnswerIndex() != position | forceUpdate) {
                if (position >= 0) {
                    answerDataEditFragment = AnswerDataEditFragment.newInstance(position);
                    getChildFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.fragment_fade_scale_enter, R.anim.fragment_fade_scale_exit)
                            .replace(R.id.answerDataEditFragmentContainer,
                                    answerDataEditFragment)
                            .commit();
                } else if (answerDataEditFragment != null) {
                    getChildFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.fragment_fade_scale_enter, R.anim.fragment_fade_scale_exit)
                            .remove(answerDataEditFragment)
                            .commit();
                    answerDataEditFragment = null;
                }
            }
        } else {
            Intent intent = new Intent(requireContext(), AnswerDataEditActivity.class);
            intent.putExtra(AnswerDataEditFragment.ARG_ANSWER_INDEX, position);
            startActivity(intent);
        }
    }

    @Override
    public void onSwap(int dragFrom, int dragTo) {
        // detect if fragment need to update index due to a swap
        if (answerDataEditFragment != null) {
            int i = answerDataEditFragment.getAnswerIndex();
            if (dragFrom < dragTo) {
                if (dragFrom < i) {
                    if (i <= dragTo) {
                        answerDataEditFragment.setAnswerIndex(i - 1);
                    }
                } else if (i == dragFrom) {
                    answerDataEditFragment.setAnswerIndex(dragTo);
                }
            } else {
                if (dragTo <= i) {
                    if (i < dragFrom) {
                        answerDataEditFragment.setAnswerIndex(i + 1);
                    } else if (i == dragFrom) {
                        answerDataEditFragment.setAnswerIndex(dragTo);
                    }
                }
            }
        }
    }

    @Override
    public void onDelete(View view, int position) {
        if (answerDetail && answerDataEditFragment != null && editTest != null) {
            int answerIndex = answerDataEditFragment.getAnswerIndex();
            if (position == answerIndex) {
                answerDataEditFragment.setAnswerIndex(-1);

                if (position < editTest.getNumberRow()) {
                    onItemClick(position, true);
                } else if (editTest.getNumberRow() > 0) {
                    onItemClick(editTest.getNumberRow() - 1, true);
                } else {
                    onItemClick(-1, true);
                }
            } else if (position < answerIndex) {
                answerDataEditFragment.setAnswerIndex(answerIndex - 1);
            }
        }
    }

    @Override
    public void updateItem(int position) {
        answerEditTestRecyclerListFragment.updateItem(position);
    }

    void updateAnswerRecycler(RecyclerViewChange recyclerViewChange) {
        answerEditTestRecyclerListFragment.applyRecyclerChanges(recyclerViewChange);
    }

    void updateNewItem() {
        if (editTest != null) {
            ArrayList<DataRow> listRow = editTest.getListRow();
            listRow.add(new DataRow());

            RecyclerViewChange answerListChanged = new RecyclerViewChange(
                    RecyclerViewChange.ItemInserted
            );
            answerListChanged.setPosition(listRow.size() - 1);
            answerEditTestRecyclerListFragment.applyRecyclerChanges(answerListChanged);
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
        editTest = test;
        if (answerDetail && editTest.getNumberRow() > 0) {
            onItemClick(0); // show first element
        }
    }

    public interface OnFragmentInteractionListener {
        void errorFinish(boolean canceled);
    }
}
