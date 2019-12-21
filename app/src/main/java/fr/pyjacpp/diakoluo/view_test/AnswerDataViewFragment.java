package fr.pyjacpp.diakoluo.view_test;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.Column;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;
import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.data.DataCell;

public class AnswerDataViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    static final String ARG_ANSWER_INDEX = "answer_index";

    // TODO: Rename and change types of parameters
    private int answerIndex;

    private OnFragmentInteractionListener mListener;

    public AnswerDataViewFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param answerIndex Index of answer.
     * @return A new instance of fragment AnswerDataViewFragment.
     */
    public static AnswerDataViewFragment newInstance(int answerIndex) {
        AnswerDataViewFragment fragment = new AnswerDataViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ANSWER_INDEX, answerIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            answerIndex = getArguments().getInt(ARG_ANSWER_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflatedView = inflater.inflate(R.layout.fragment_answer_data_view, container, false);
        LinearLayout layout = inflatedView.findViewById(R.id.answerListLinearLayout);

        DataRow row = DiakoluoApplication.getCurrentTest(inflatedView.getContext()).getListRow().get(answerIndex);

        LinearLayout.LayoutParams params = new TableRow.LayoutParams();
        params.topMargin = 24;

        ArrayList<Column> listColumn = DiakoluoApplication.getCurrentTest(inflatedView.getContext()).getListColumn();
        for (int i = 0; i < listColumn.size(); i++) {
            Column column = listColumn.get(i);

            DataCell dataCell = row.getListCells().get(column);
            if (dataCell != null) {
                TextView columnTitle = new TextView(inflatedView.getContext());
                TextView columnValue = new TextView(inflatedView.getContext());

                columnTitle.setTextSize(getResources().getDimension(R.dimen.textAnswerSize));
                columnValue.setTextSize(getResources().getDimension(R.dimen.textAnswerSize));
                
                if (i > 0)
                    columnTitle.setLayoutParams(params);

                columnTitle.setTypeface(null, Typeface.BOLD);

                columnTitle.setText(column.getName() + ":");
                if (column.getInputType() == ColumnInputType.String) {
                    columnValue.setText((String) dataCell.getValue());
                }

                layout.addView(columnTitle);
                layout.addView(columnValue);
            }
        }

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

    interface OnFragmentInteractionListener {
    }
}


