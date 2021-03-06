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

package fr.pyjacpp.diakoluo.list_tests;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.RecyclerViewChange;
import fr.pyjacpp.diakoluo.tests.Test;


public class ListTestsFragment extends Fragment {
    private OnFragmentInteractionListener listener;
    private RecyclerView testRecyclerView;
    private TestAdapter testRecyclerViewAdapter;

    public ListTestsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View inflatedLayout = inflater.inflate(R.layout.fragment_recycler_list, container, true);

        testRecyclerView = inflatedLayout.findViewById(R.id.recyclerView);
        LinearLayoutManager testRecyclerViewLayoutManager = new LinearLayoutManager(getContext());
        testRecyclerViewAdapter = new TestAdapter(getContext(), new TestAdapter.TestViewListener() {
            @Override
            public void onItemClick(View view, int position) {
                listener.onItemClick(view, position);
            }

            @Override
            public void onPlayButtonClick(View view, int position) {
                listener.onPlayButtonClick(view, position);
            }

            @Override
            public void onSeeButtonClick(View view, int position) {
                listener.onSeeButtonClick(view, position);
            }

            @Override
            public void onDeleteMenuItemClick(final View view, final int position) {
                final ArrayList<Test> listTest = DiakoluoApplication.getListTest(view.getContext());

                new MaterialAlertDialogBuilder(view.getContext())
                        .setTitle(R.string.dialog_delete_test_title)
                        .setMessage(getString(R.string.dialog_delete_test_message, listTest.get(position).getName()))
                        .setCancelable(true)
                        .setIcon(R.drawable.ic_delete_forever_accent_color_24dp)
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteTest(inflatedLayout, position);
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }

            @Override
            public void onEditMenuItemClick(View view, int position) {
                listener.onEditMenuItemClick(view, position);
            }

            @Override
            public void onExportMenuItemClick(View view, int position) {
                listener.onExportMenuItemClick(view, position);
            }
        });



        testRecyclerView.setHasFixedSize(true);
        testRecyclerView.setLayoutManager(testRecyclerViewLayoutManager);
        testRecyclerView.setAdapter(testRecyclerViewAdapter);

//        testRecyclerView.addItemDecoration(new DividerItemDecoration(testRecyclerView.getContext(),
//                testRecyclerViewLayoutManager.getOrientation()));

        return inflatedLayout;
    }

    private void deleteTest(final View view, final int position) {
        final ArrayList<Test> listTest = DiakoluoApplication.getListTest(view.getContext());
        testRecyclerView.removeViewAt(position);
        testRecyclerViewAdapter.notifyItemRemoved(position);

        listener.onDeleteTest(position, listTest.size() - 1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Test testToDelete = listTest.get(position);
                DiakoluoApplication.removeTest(view.getContext(), position);

                Snackbar.make(view, getString(R.string.test_deleted, testToDelete.getName()),
                        Snackbar.LENGTH_LONG)
                        .setDuration(getResources().getInteger(R.integer.snackbar_deleted_duration))
                        .setAction(R.string.cancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                listTest.add(position, testToDelete);
                                DiakoluoApplication.saveTest(view.getContext());
                                testRecyclerViewAdapter.notifyItemInserted(position);
                            }
                        })
                        .setAnchorView(R.id.addFloatingButton).show();
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        Context context = getContext();
        if (context != null) {
            RecyclerViewChange testListChanged = DiakoluoApplication.getTestListChanged(context);
            if (testListChanged != null) {
                testListChanged.apply(testRecyclerViewAdapter);
                DiakoluoApplication.setTestListChanged(context, null);
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    void notifyUpdateInserted(int position) {
        testRecyclerViewAdapter.notifyItemInserted(position);
    }

    public interface OnFragmentInteractionListener{
        void onItemClick(View view, int position);
        void onPlayButtonClick(View view, int position);
        void onSeeButtonClick(View view, int position);
        void onEditMenuItemClick(View view, int position);
        void onDeleteTest(int position, int listTestSize);
        void onExportMenuItemClick(View view, int position);
    }
}
