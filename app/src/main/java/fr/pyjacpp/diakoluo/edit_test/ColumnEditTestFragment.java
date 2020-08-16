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
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.RecyclerViewChange;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;
import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.tests.column.Column;
import fr.pyjacpp.diakoluo.tests.data.DataCell;


public class ColumnEditTestFragment extends Fragment implements
        ColumnEditTestRecyclerListFragment.OnParentFragmentInteractionListener,
        ColumnDataEditFragment.OnParentFragmentInteractionListener{
    private OnFragmentInteractionListener mListener;

    private boolean columnDetail;

    @Nullable
    private ColumnDataEditFragment columnDataEditFragment = null;
    private ColumnEditTestRecyclerListFragment columnEditTestRecyclerListFragment;

    public ColumnEditTestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View inflatedView = inflater.inflate(R.layout.fragment_edit_column_test, container, false);

        columnDetail = inflatedView.findViewById(R.id.columnDataEditFragmentContainer) != null;

        columnEditTestRecyclerListFragment = (ColumnEditTestRecyclerListFragment)
                getChildFragmentManager().findFragmentById(R.id.columnEditTestRecyclerListFragment);

        Button addColumnButton = inflatedView.findViewById(R.id.addColumnButton);
        addColumnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNewItem(view.getContext());
                onItemClick(view, DiakoluoApplication.getCurrentEditTest(view.getContext()).getNumberColumn() - 1);
            }
        });

        if (columnDetail && DiakoluoApplication.getCurrentEditTest(inflatedView.getContext()).getNumberColumn() > 0) {
            onItemClick(inflatedView, 0); // show first element
        }

        return inflatedView;
    }

    void updateNewItem(Context context) {
        Test currentEditTest = DiakoluoApplication.getCurrentEditTest(context);
        ArrayList<Column> listColumn = currentEditTest.getListColumn();
        Column column = Column.newColumn(ColumnInputType.DEFAULT_INPUT_TYPE, context.getString(R.string.default_column_name, listColumn.size() + 1), "");
        listColumn.add(column);

        for (DataRow row : currentEditTest.getListRow()) {
            row.getListCells().put(column, DataCell.newCellWithDefaultValue(column));
        }
        RecyclerViewChange columnListChanged = new RecyclerViewChange(
                RecyclerViewChange.ItemInserted
        );
        columnListChanged.setPosition(listColumn.size() - 1);
        columnEditTestRecyclerListFragment.applyRecyclerChanges(columnListChanged);

        if (currentEditTest.getNumberColumn() <= 1) {
            RecyclerViewChange recyclerViewChange = new RecyclerViewChange(RecyclerViewChange.ItemRangeChanged);
            recyclerViewChange.setPositionStart(0);
            recyclerViewChange.setPositionEnd(currentEditTest.getNumberRow() - 1);
            mListener.updateAnswerRecycler(recyclerViewChange);
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

    @Override
    public void onItemClick(View view, int position) {
        onItemClick(view, position, false);
    }

    public void onItemClick(View view, int position, boolean forceUpdate) {
        if (columnDetail) {
            if (columnDataEditFragment == null || columnDataEditFragment.getColumnIndex() != position || forceUpdate) {
                if (position >= 0) {
                    columnDataEditFragment = ColumnDataEditFragment.newInstance(position);

                    getChildFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.fragment_fade_scale_enter, R.anim.fragment_fade_scale_exit)
                            .replace(R.id.columnDataEditFragmentContainer,
                                    columnDataEditFragment)
                            .commit();
                } else if (columnDataEditFragment != null) {
                    getChildFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.fragment_fade_scale_enter, R.anim.fragment_fade_scale_exit)
                            .remove(columnDataEditFragment)
                            .commit();
                    columnDataEditFragment = null;
                }
            }
        } else {
            Intent intent = new Intent(view.getContext(), ColumnDataEditActivity.class);
            intent.putExtra(ColumnDataEditFragment.ARG_COLUMN_INDEX, position);
            startActivity(intent);
        }
    }

    @Override
    public void onSwap(int dragFrom, int dragTo) {
        // detect if fragment need to update index due to a swap
        if (columnDataEditFragment != null) {
            int i = columnDataEditFragment.getColumnIndex();
            if (dragFrom < dragTo) {
                if (dragFrom < i) {
                    if (i <= dragTo) {
                        columnDataEditFragment.setColumnIndex(i - 1);
                    }
                } else if (i == dragFrom) {
                    columnDataEditFragment.setColumnIndex(dragTo);
                }
            } else {
                if (dragTo <= i) {
                    if (i < dragFrom) {
                        columnDataEditFragment.setColumnIndex(i + 1);
                    } else if (i == dragFrom) {
                        columnDataEditFragment.setColumnIndex(dragTo);
                    }
                }
            }
        }
    }

    @Override
    public void onDeleteItem(View view, int position) {
        if (columnDetail && columnDataEditFragment != null) {
            int columnIndex = columnDataEditFragment.getColumnIndex();
            if (position == columnIndex) {
                columnDataEditFragment.setColumnIndex(-1);

                Test currentEditTest = DiakoluoApplication.getCurrentEditTest(view.getContext());
                if (position < currentEditTest.getNumberColumn()) {
                    onItemClick(view, position, true);
                } else if (currentEditTest.getNumberColumn() > 0) {
                    onItemClick(view, currentEditTest.getNumberColumn() - 1, true);
                } else {
                    onItemClick(view, -1, true);
                }
            } else if (position < columnIndex) {
                columnDataEditFragment.setColumnIndex(columnIndex - 1);
            }
        }
    }

    @Override
    public void updateItem(int position) {
        columnEditTestRecyclerListFragment.updateItem(position);
    }

    public interface OnFragmentInteractionListener {
        void updateAnswerRecycler(RecyclerViewChange recyclerViewChange);
    }
}
