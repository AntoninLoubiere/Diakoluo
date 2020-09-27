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
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.RecyclerItemClickListener;
import fr.pyjacpp.diakoluo.tests.Test;

public class ColumnViewTestRecyclerListFragment extends Fragment implements DiakoluoApplication.GetTestRunnable {
    private OnFragmentInteractionListener mListener;
    private OnParentFragmentInteractionListener parentListener;
    private View inflatedView;

    public ColumnViewTestRecyclerListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.fragment_recycler_list, container, false);

        DiakoluoApplication.get(requireContext()).getCurrentTest(
                new DiakoluoApplication.GetTest(true, (AppCompatActivity) getActivity(),
                        false, this));

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
            throw new RuntimeException("Parent fragment must implement OnFragmentInteractionListener");
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
        final RecyclerView columnRecyclerView = inflatedView.findViewById(R.id.recyclerView);
        ColumnAdapter columnRecyclerViewAdapter = new ColumnAdapter(test);
        LinearLayoutManager columnRecyclerViewLayoutManager = new LinearLayoutManager(columnRecyclerView.getContext());

        columnRecyclerView.setHasFixedSize(true);
        columnRecyclerView.setLayoutManager(columnRecyclerViewLayoutManager);
        columnRecyclerView.setAdapter(columnRecyclerViewAdapter);

//        columnRecyclerView.addItemDecoration(new DividerItemDecoration(columnRecyclerView.getContext(),
//                columnRecyclerViewLayoutManager.getOrientation()));

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                columnRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(columnRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        parentListener.onItemClick(view, position);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));
            }
        });
    }

    public interface OnFragmentInteractionListener {
        void errorFinish(boolean canceled);
    }

    public interface OnParentFragmentInteractionListener {
        void onItemClick(View view, int position);
    }
}
