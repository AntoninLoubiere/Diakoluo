package fr.pyjacpp.diakoluo.view_test;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.Column;
import fr.pyjacpp.diakoluo.tests.Test;

public class ColumnDataViewFragment extends Fragment {
    static final String ARG_COLUMN_INDEX = "column_index";

    private int columnIndex;

    private OnFragmentInteractionListener mListener;

    public ColumnDataViewFragment() {
    }

    public static ColumnDataViewFragment newInstance(int columnIndex) {
        ColumnDataViewFragment fragment = new ColumnDataViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_INDEX, columnIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            columnIndex = getArguments().getInt(ARG_COLUMN_INDEX);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflatedView = inflater.inflate(R.layout.fragment_view_column_data, container, false);

        TextView titleEditText = inflatedView.findViewById(R.id.titleTextView);
        TextView descriptionEditText = inflatedView.findViewById(R.id.descriptionTextView);
        TextView columnTypeTextView = inflatedView.findViewById(R.id.columnTypeTextView);

        final Test currentTest = DiakoluoApplication.getCurrentTest(inflatedView.getContext());
        final Column column = currentTest.getListColumn().get(columnIndex);

        titleEditText.setText(column.getName());
        descriptionEditText.setText(column.getDescription());
        columnTypeTextView.setText(column.getInputType().name());

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


