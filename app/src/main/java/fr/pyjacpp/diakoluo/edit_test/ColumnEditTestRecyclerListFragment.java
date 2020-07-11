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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.RecyclerViewChange;
import fr.pyjacpp.diakoluo.tests.column.Column;
import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.Test;

public class ColumnEditTestRecyclerListFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private OnParentFragmentInteractionListener parentListener;

    private ColumnAdapter columnRecyclerViewAdapter;

    public ColumnEditTestRecyclerListFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_recycler_list, container, false);

        final RecyclerView columnRecyclerView = inflatedView.findViewById(R.id.recyclerView);
        columnRecyclerViewAdapter = new ColumnAdapter(columnRecyclerView.getContext(), new ColumnAdapter.ColumnViewListener() {
            @Override
            public void onItemClickListener(View view, View itemView) {
                int position = columnRecyclerView.getChildAdapterPosition(itemView);

                if (position >= 0) {
                    parentListener.onItemClick(view, position);
                }
            }

            @Override
            public void onDeleteClickListener(View view, View itemView) {
                int position = columnRecyclerView.getChildAdapterPosition(itemView);

                if (position >= 0) {

                    Test currentEditTest = DiakoluoApplication.getCurrentEditTest(view.getContext());
                    Column columnRemoved = currentEditTest.getListColumn().remove(position);

                    // remove all column in memory
                    for (DataRow row : currentEditTest.getListRow()) {
                        row.getListCells().remove(columnRemoved);
                    }

                    parentListener.onDeleteItem(view, position);
                    columnRecyclerViewAdapter.notifyItemRemoved(position);

                    if (position == 0) {
                        RecyclerViewChange setAnswerListChanged = new RecyclerViewChange(
                                RecyclerViewChange.ItemRangeChanged
                        );
                        setAnswerListChanged.setPositionStart(0);
                        setAnswerListChanged.setPositionEnd(currentEditTest.getNumberRow() - 1);
                        mListener.updateAnswerRecycler(setAnswerListChanged);
                    }
                }
            }
        });
        LinearLayoutManager columnRecyclerViewLayoutManager = new LinearLayoutManager(columnRecyclerView.getContext());

        columnRecyclerView.setLayoutManager(columnRecyclerViewLayoutManager);
        columnRecyclerView.setAdapter(columnRecyclerViewAdapter);

//        columnRecyclerView.addItemDecoration(new DividerItemDecoration(columnRecyclerView.getContext(),
//                columnRecyclerViewLayoutManager.getOrientation()));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

            private int dragFrom = -1;
            private int dragTo = -1;

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP |
                                ItemTouchHelper.START | ItemTouchHelper.END);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int dragFrom = viewHolder.getAdapterPosition();
                dragTo = target.getAdapterPosition();

                if (this.dragFrom == -1) {
                    this.dragFrom = dragFrom;
                }

                Test currentEditTest = DiakoluoApplication.getCurrentEditTest(recyclerView.getContext());

                Collections.swap(currentEditTest.getListColumn(), dragFrom,
                        dragTo);

                columnRecyclerViewAdapter.notifyItemMoved(dragFrom, dragTo);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);

                if (dragFrom != dragTo) {
                    if (dragFrom == 0 || dragTo == 0) {
                        updateAnswerRecycler(recyclerView);
                    }
                    parentListener.onSwap(dragFrom, dragTo);
                }

                dragFrom = -1;
                dragTo = -1;
            }

            private void updateAnswerRecycler(@NonNull RecyclerView recyclerView) {
                Test currentEditTest = DiakoluoApplication.getCurrentEditTest(recyclerView.getContext());
                RecyclerViewChange setAnswerListChanged = new RecyclerViewChange(
                        RecyclerViewChange.ItemRangeChanged
                );
                setAnswerListChanged.setPositionStart(0);
                setAnswerListChanged.setPositionEnd(currentEditTest.getNumberRow());
                mListener.updateAnswerRecycler(setAnswerListChanged);

            }

        });

        itemTouchHelper.attachToRecyclerView(columnRecyclerView);


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
            throw new RuntimeException("Parent listener must implement OnParentFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    void updateItem(int position) {
        columnRecyclerViewAdapter.notifyItemChanged(position);
    }

    void applyRecyclerChanges(RecyclerViewChange columnListChanged) {
        columnListChanged.apply(columnRecyclerViewAdapter);
    }

    public interface OnFragmentInteractionListener {
        void updateAnswerRecycler(RecyclerViewChange recyclerViewChange);
    }

    public interface OnParentFragmentInteractionListener {
        void onItemClick(View view, int position);
        void onSwap(int dragFrom, int dragTo);
        void onDeleteItem(View view, int position);
    }
}
