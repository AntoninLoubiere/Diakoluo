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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.save_test.FileManager;
import fr.pyjacpp.diakoluo.tests.CompactTest;


public class ListTestsFragment extends Fragment {
    public static final String EXTRA_INT_POSITION = "position";
    public static final String ACTION_BROADCAST_UPDATE_INDEX_RECYCLER = "fr.pyjacpp.diakoluo.list_test.UPDATE_INDEX";
    public static final String ACTION_BROADCAST_TEST_ADDED_LIST_RECYCLER = "fr.pyjacpp.diakoluo.list_test.TEST_ADDED";
    private DiakoluoApplication diakoluoApplication;
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
        diakoluoApplication = DiakoluoApplication.get(requireContext());

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
                final ArrayList<CompactTest> listTest =
                        diakoluoApplication.getListTest();

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
        final ArrayList<CompactTest> listTest = diakoluoApplication
                .getListTest();
        testRecyclerView.removeViewAt(position);
        testRecyclerViewAdapter.notifyItemRemoved(position);

        listener.onDeleteTest(position, listTest.size() - 1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final CompactTest testToDelete = listTest.get(position);
                final File testFile = diakoluoApplication.removeTestAndCache(position);

                Snackbar.make(view, getString(R.string.test_deleted, testToDelete.getName()),
                        Snackbar.LENGTH_LONG)
                        .setDuration(getResources().getInteger(R.integer.snackbar_deleted_duration))
                        .setAction(R.string.cancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    FileManager.copyTestFromFile(requireContext(),
                                            testFile, testToDelete.getFilename());
                                    listTest.add(position, testToDelete);
                                    testRecyclerViewAdapter.notifyItemInserted(position);
                                    if (!testFile.delete()) {
                                        Log.e(getClass().getName(), "Can't delete the cache file");
                                    }
                                } catch (IOException e) {
                                    Log.e(getClass().getName(),
                                            "can't recover the deleted test !", e);
                                }
                            }
                        })
                        .setAnchorView(R.id.addFloatingButton).show();
            }
        }).start();
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
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                testRecyclerViewAdapter.notifyItemChanged(intent.getIntExtra(EXTRA_INT_POSITION,
                        0));
            }
        }, new IntentFilter(ACTION_BROADCAST_UPDATE_INDEX_RECYCLER));

        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                testRecyclerViewAdapter.notifyItemInserted(diakoluoApplication.getListTest().size()
                        - 1);
            }
        }, new IntentFilter(ACTION_BROADCAST_TEST_ADDED_LIST_RECYCLER));
    }

    void notifyUpdateInserted(int position) {
        testRecyclerViewAdapter.notifyItemInserted(position);
    }

    public interface OnFragmentInteractionListener {
        void onItemClick(View view, int position);

        void onPlayButtonClick(View view, int position);

        void onSeeButtonClick(View view, int position);

        void onEditMenuItemClick(View view, int position);

        void onDeleteTest(int position, int listTestSize);

        void onExportMenuItemClick(View view, int position);
    }
}
