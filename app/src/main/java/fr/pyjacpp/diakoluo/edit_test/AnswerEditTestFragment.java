package fr.pyjacpp.diakoluo.edit_test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.RecyclerViewChange;
import fr.pyjacpp.diakoluo.tests.DataRow;


class AnswerEditTestFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    public AnswerEditTestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_edit_answer_test, container, false);

        Button addAnswerButton = inflatedView.findViewById(R.id.addAnswerButton);
        addAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<DataRow> listRow = DiakoluoApplication.getCurrentEditTest(view.getContext()).getListRow();
                listRow.add(new DataRow());

                RecyclerViewChange answerListChanged = new RecyclerViewChange(
                        RecyclerViewChange.ItemInserted
                );
                answerListChanged.setPosition(listRow.size() - 1);
                DiakoluoApplication.setAnswerListChanged(view.getContext(), answerListChanged);

                Intent intent = new Intent(view.getContext(), AnswerDataEditActivity.class);
                intent.putExtra(AnswerDataEditFragment.ARG_ANSWER_INDEX, listRow.size() - 1);
                startActivity(intent);
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

    public interface OnFragmentInteractionListener {
    }
}
