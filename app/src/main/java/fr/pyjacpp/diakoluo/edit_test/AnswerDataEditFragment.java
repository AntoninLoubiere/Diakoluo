package fr.pyjacpp.diakoluo.edit_test;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.RecyclerViewChange;
import fr.pyjacpp.diakoluo.tests.column.Column;
import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.data.DataCell;
import fr.pyjacpp.diakoluo.tests.data.DataCellString;

public class AnswerDataEditFragment extends Fragment {
    static final String ARG_ANSWER_INDEX = "answer_index";

    HashMap<Column, View> columnAnswerEditHashMap = new HashMap<>();

    private int answerIndex;

    private OnFragmentInteractionListener mListener;
    private View inflatedView;

    public AnswerDataEditFragment() {
    }

    public static AnswerDataEditFragment newInstance(int answerIndex) {
        AnswerDataEditFragment fragment = new AnswerDataEditFragment();
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

        inflatedView = inflater.inflate(R.layout.fragment_edit_answer_data, container, false);
        LinearLayout layout = inflatedView.findViewById(R.id.answerListLinearLayout);

        DataRow row = DiakoluoApplication.getCurrentEditTest(inflatedView.getContext()).getListRow().get(answerIndex);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(layout.getLayoutParams());
        params.topMargin = 24;

        ArrayList<Column> listColumn = DiakoluoApplication.getCurrentEditTest(inflatedView.getContext()).getListColumn();
        for (int i = 0; i < listColumn.size(); i++) {
            Column column = listColumn.get(i);

            DataCell dataCell = row.getListCells().get(column);
            TextView columnTitle = new TextView(inflatedView.getContext());

            columnTitle.setTextSize(getResources().getDimension(R.dimen.textAnswerSize));

            if (i > 0)
                columnTitle.setLayoutParams(params);

            columnTitle.setTypeface(null, Typeface.BOLD);

            columnTitle.setText(getString(R.string.column_name_format, column.getName()));
            View columnValue;
            switch (column.getInputType()) {
                case String:
                    DataCellString dataCellString = (DataCellString) dataCell;
                    if (dataCellString == null) {
                        dataCellString = new DataCellString((String) column.getDefaultValue());
                        row.getListCells().put(column, dataCellString);
                    }
                    EditText _columnValue = new EditText(inflatedView.getContext());
                    _columnValue.setText(dataCellString.getValue());
                    _columnValue.setHint(column.getName());
                    _columnValue.setTextSize(getResources().getDimension(R.dimen.textAnswerSize));
                    columnValue = _columnValue;
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + column.getInputType());
            }

            columnAnswerEditHashMap.put(column, columnValue);

            layout.addView(columnTitle);
            layout.addView(columnValue);
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

    @Override
    public void onPause() {
        DataRow row = DiakoluoApplication.getCurrentEditTest(inflatedView.getContext()).getListRow().get(answerIndex);

        ArrayList<Column> listColumn = DiakoluoApplication.getCurrentEditTest(inflatedView.getContext()).getListColumn();
        for (int i = 0; i < listColumn.size(); i++) {
            Column column = listColumn.get(i);

            View answerEdit = columnAnswerEditHashMap.get(column);

            DataCell dataCell = row.getListCells().get(column);
            switch (column.getInputType()) {
                case String:
                    EditText answerEditText = (EditText) answerEdit;
                    DataCellString dataCellString = (DataCellString) dataCell;

                    if (answerEditText != null) {
                        if (dataCellString == null) {
                            row.getListCells().put(column, new DataCellString(answerEditText.getText().toString()));
                        } else {
                            dataCellString.setValue(answerEditText.getText().toString());
                        }
                    }
            }

        }

        RecyclerViewChange recyclerViewChange = DiakoluoApplication.getAnswerListChanged(
                inflatedView.getContext());
        if (recyclerViewChange == null) {
            recyclerViewChange = new RecyclerViewChange(RecyclerViewChange.None);
        }
        recyclerViewChange.setChanges(recyclerViewChange.getChanges() | RecyclerViewChange.ItemChanged);
        recyclerViewChange.setPosition(answerIndex);

        DiakoluoApplication.setAnswerListChanged(inflatedView.getContext(), recyclerViewChange);

        super.onPause();
    }

    interface OnFragmentInteractionListener {
    }
}


