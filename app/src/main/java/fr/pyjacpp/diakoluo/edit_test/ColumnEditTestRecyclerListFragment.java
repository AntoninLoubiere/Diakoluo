package fr.pyjacpp.diakoluo.edit_test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.RecyclerViewChange;
import fr.pyjacpp.diakoluo.tests.Column;
import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.Test;

class ColumnEditTestRecyclerListFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private RecyclerView.Adapter columnRecyclerViewAdapter;

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
                    Intent intent = new Intent(view.getContext(), ColumnDataEditActivity.class);
                    intent.putExtra(ColumnDataEditFragment.ARG_COLUMN_INDEX, position);
                    startActivity(intent);
                }
            }

            @Override
            public void onDeleteClickListener(View view, View itemView) {
                int position = columnRecyclerView.getChildAdapterPosition(itemView);

                if (position >= 0) {

                    Test currentEditTest = DiakoluoApplication.getCurrentEditTest(view.getContext());
                    Column columnRemoved = currentEditTest.getListColumn().remove(position);
                    columnRecyclerViewAdapter.notifyItemRemoved(position);

                    // remove all column in memory
                    for (DataRow row : currentEditTest.getListRow()) {
                        row.getListCells().remove(columnRemoved);
                    }

                    if (position == 0) {
                        RecyclerViewChange setAnswerListChanged = new RecyclerViewChange(
                                RecyclerViewChange.ItemRangeChanged
                        );
                        setAnswerListChanged.setPositionStart(0);
                        setAnswerListChanged.setPositionEnd(currentEditTest.getNumberRow() - 1);
                        DiakoluoApplication.setAnswerListChanged(view.getContext(),
                                setAnswerListChanged);
                    }
                }
            }
        });
        LinearLayoutManager columnRecyclerViewLayoutManager = new LinearLayoutManager(columnRecyclerView.getContext());

        columnRecyclerView.setHasFixedSize(true);
        columnRecyclerView.setLayoutManager(columnRecyclerViewLayoutManager);
        columnRecyclerView.setAdapter(columnRecyclerViewAdapter);

        columnRecyclerView.addItemDecoration(new DividerItemDecoration(columnRecyclerView.getContext(),
                columnRecyclerViewLayoutManager.getOrientation()));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP |
                                ItemTouchHelper.START | ItemTouchHelper.END);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                Test currentEditTest = DiakoluoApplication.getCurrentEditTest(recyclerView.getContext());
                Collections.swap(currentEditTest.getListColumn(), viewHolder.getAdapterPosition(),
                        target.getAdapterPosition());
                columnRecyclerViewAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());

                if (viewHolder.getAdapterPosition() == 0) {
                    RecyclerViewChange setAnswerListChanged = new RecyclerViewChange(
                            RecyclerViewChange.ItemRangeChanged
                    );
                    setAnswerListChanged.setPositionStart(0);
                    setAnswerListChanged.setPositionEnd(currentEditTest.getNumberRow());
                    DiakoluoApplication.setAnswerListChanged(recyclerView.getContext(),
                            setAnswerListChanged);
                }
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
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
    }

    @Override
    public void onResume() {
        super.onResume();

        Context context = getContext();
        if (context != null) {
            RecyclerViewChange testListChanged = DiakoluoApplication.getColumnListChanged(context);
            if (testListChanged != null) {
                testListChanged.apply(columnRecyclerViewAdapter);
                DiakoluoApplication.setColumnListChanged(context, null);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
    }
}
