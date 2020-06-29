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
import fr.pyjacpp.diakoluo.tests.Column;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;
import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.tests.data.DataCellString;


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
                Test currentEditTest = DiakoluoApplication.getCurrentEditTest(view.getContext());
                ArrayList<Column> listColumn = currentEditTest.getListColumn();
                Column column = new Column("", "", ColumnInputType.DEFAULT_INPUT_TYPE);
                listColumn.add(column);

                RecyclerViewChange columnListChanged = new RecyclerViewChange(
                        RecyclerViewChange.ItemInserted
                );
                columnListChanged.setPosition(listColumn.size() - 1);
                columnEditTestRecyclerListFragment.applyRecyclerChanges(columnListChanged);

                if (listColumn.size() <= 1) {
                    RecyclerViewChange recyclerViewChange = new RecyclerViewChange(RecyclerViewChange.ItemRangeChanged);
                    recyclerViewChange.setPositionStart(0);
                    recyclerViewChange.setPositionEnd(currentEditTest.getNumberRow() - 1);
                    mListener.updateAnswerRecycler(recyclerViewChange);
                }

                for (DataRow row : currentEditTest.getListRow()) {
                    // TODO: improve
                    row.getListCells().put(column, new DataCellString((String) column.getDefaultValue()));
                }
                onItemClick(view, listColumn.size() - 1);
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
            } else if (position > columnIndex) {
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
