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
import fr.pyjacpp.diakoluo.tests.Test;

public class AnswerEditTestRecyclerListFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private RecyclerView.Adapter answerRecyclerViewAdapter;
    private OnParentFragmentInteractionListener parentListener;

    public AnswerEditTestRecyclerListFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_recycler_list, container, false);

        final RecyclerView answerRecyclerView = inflatedView.findViewById(R.id.recyclerView);
        answerRecyclerViewAdapter = new AnswerAdapter(answerRecyclerView.getContext(), new AnswerAdapter.AnswerViewListener() {
            @Override
            public void onItemClick(View view, View itemView) {
                int position = answerRecyclerView.getChildAdapterPosition(itemView);
                if (position >= 0) {
                    parentListener.itemClick(view, position);
                }
            }

            @Override
            public void onDeleteClick(View view, View itemView) {
                int position = answerRecyclerView.getChildAdapterPosition(itemView);

                if (position >= 0) {

                    Test currentEditTest = DiakoluoApplication.getCurrentEditTest(view.getContext());
                    currentEditTest.getListRow().remove(position);
                    answerRecyclerViewAdapter.notifyItemRemoved(position);

                    if (currentEditTest.getNumberColumn() <= 0) {
                        // if the text depends of position, we need to refresh all next elements
                        answerRecyclerViewAdapter.notifyItemRangeChanged(position, currentEditTest.getNumberRow() - 1);
                    }
                }
            }
        });
        LinearLayoutManager answerRecyclerViewLayout = new LinearLayoutManager(answerRecyclerView.getContext());

        answerRecyclerView.setHasFixedSize(true);
        answerRecyclerView.setLayoutManager(answerRecyclerViewLayout);
        answerRecyclerView.setAdapter(answerRecyclerViewAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            int dragFrom = -1;
            int dragTo = -1;

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

                if (this.dragFrom <= -1)
                    this.dragFrom = dragFrom;

                Test currentEditTest = DiakoluoApplication.getCurrentEditTest(recyclerView.getContext());

                Collections.swap(currentEditTest.getListRow(), dragFrom,
                        dragTo);

                answerRecyclerViewAdapter.notifyItemMoved(dragFrom, dragTo);
                if (currentEditTest.getNumberColumn() <= 0) {
                    // if the text depends of position, we need to refresh all next elements
                    answerRecyclerViewAdapter.notifyItemRangeChanged(dragFrom - 1,
                            currentEditTest.getNumberRow() - dragFrom);
                }
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);

                if (dragFrom != dragTo) {
                    parentListener.onSwap(dragFrom, dragTo);
                }

                dragFrom = -1;
                dragTo = -1;
            }
        });

        itemTouchHelper.attachToRecyclerView(answerRecyclerView);

//        answerRecyclerView.addItemDecoration(new DividerItemDecoration(answerRecyclerView.getContext(),
//                answerRecyclerViewLayout.getOrientation()));

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
            throw new RuntimeException("Parent fragment must implement OnParentFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    void updateItem(int position) {
        answerRecyclerViewAdapter.notifyItemChanged(position);
    }

    void applyRecyclerChanges(RecyclerViewChange columnListChanged) {
        columnListChanged.apply(answerRecyclerViewAdapter);
    }

    public interface OnFragmentInteractionListener {
    }

    interface OnParentFragmentInteractionListener {
        void itemClick(View view, int position);
        void onSwap(int dragFrom, int dragTo);
    }
}
