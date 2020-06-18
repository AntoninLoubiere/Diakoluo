package fr.pyjacpp.diakoluo.edit_test;

import android.content.Context;
import android.content.Intent;
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
                    Intent intent = new Intent(view.getContext(), AnswerDataEditActivity.class);
                    intent.putExtra(AnswerDataEditFragment.ARG_ANSWER_INDEX, position);
                    startActivity(intent);
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
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP |
                                ItemTouchHelper.START | ItemTouchHelper.END);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                Test currentEditTest = DiakoluoApplication.getCurrentEditTest(recyclerView.getContext());
                Collections.swap(currentEditTest.getListRow(), viewHolder.getAdapterPosition(),
                        target.getAdapterPosition());
                answerRecyclerViewAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                if (currentEditTest.getNumberColumn() <= 0) {
                    // if the text depends of position, we need to refresh all next elements
                    answerRecyclerViewAdapter.notifyItemRangeChanged(viewHolder.getAdapterPosition() - 1,
                            currentEditTest.getNumberRow() - viewHolder.getAdapterPosition());
                }
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        Context context = getContext();
        if (context != null) {
            RecyclerViewChange testListChanged = DiakoluoApplication.getAnswerListChanged(context);
            if (testListChanged != null) {

                testListChanged.apply(answerRecyclerViewAdapter);
                DiakoluoApplication.setAnswerListChanged(context, null);
            }
        }
    }

    public interface OnFragmentInteractionListener {
    }
}
