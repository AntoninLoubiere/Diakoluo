package fr.pyjacpp.diakoluo.edit_test;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import fr.pyjacpp.diakoluo.tests.Column;
import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.Test;

public class ColumnEditTestRecyclerListFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private OnParentFragmentInteractionListener parentListener;

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
                    parentListener.onItemClick(view, position);
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
                        mListener.updateAnswerRecycler(setAnswerListChanged);
                    }
                }
            }
        });
        LinearLayoutManager columnRecyclerViewLayoutManager = new LinearLayoutManager(columnRecyclerView.getContext());

        columnRecyclerView.setHasFixedSize(true);
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

                Log.d("Test", dragFrom + " to " + dragTo);

                if (dragFrom != dragTo && (dragFrom == 0 || dragTo == 0)) {
                    Log.d("Test", "update");
                    updateAnswerRecycler(recyclerView);
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
    public void onResume() {
        super.onResume();
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
    }
}
