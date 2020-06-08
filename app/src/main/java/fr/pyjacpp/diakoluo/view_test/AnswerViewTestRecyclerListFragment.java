package fr.pyjacpp.diakoluo.view_test;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.RecyclerItemClickListener;

public class AnswerViewTestRecyclerListFragment extends Fragment {
    private OnFragmentInteractionListener listener;
    private OnFragmentInteractionParentListener parentListener;

    public AnswerViewTestRecyclerListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_recycler_list, container, false);

        RecyclerView answerRecyclerView = inflatedView.findViewById(R.id.recyclerView);
        RecyclerView.Adapter answerRecyclerViewAdapter = new AnswerAdapter(answerRecyclerView.getContext());
        LinearLayoutManager answerRecyclerViewLayout = new LinearLayoutManager(answerRecyclerView.getContext());

        answerRecyclerView.setHasFixedSize(true);
        answerRecyclerView.setLayoutManager(answerRecyclerViewLayout);
        answerRecyclerView.setAdapter(answerRecyclerViewAdapter);

        answerRecyclerView.addItemDecoration(new DividerItemDecoration(answerRecyclerView.getContext(),
                answerRecyclerViewLayout.getOrientation()));

        answerRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                answerRecyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        parentListener.onItemClick(view, position);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }
        ));

        return inflatedView;
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

        if (getParentFragment() instanceof OnFragmentInteractionParentListener) {
            parentListener = (OnFragmentInteractionParentListener) getParentFragment();
        } else {
            throw new RuntimeException("Parent fragment must implement OnFragmentInteractionParentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        parentListener = null;
    }

    public interface OnFragmentInteractionListener {
    }

    public interface OnFragmentInteractionParentListener {
        void onItemClick(View view, int position);
    }
}
