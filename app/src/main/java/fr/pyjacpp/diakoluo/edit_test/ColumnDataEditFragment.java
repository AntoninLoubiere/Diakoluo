package fr.pyjacpp.diakoluo.edit_test;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.RecyclerViewChange;
import fr.pyjacpp.diakoluo.tests.Column;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;
import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.tests.data.DataCellString;

public class ColumnDataEditFragment extends Fragment {
    static final String ARG_COLUMN_INDEX = "column_index";

    private int columnIndex;

    private OnFragmentInteractionListener mListener;
    private View inflatedView;
    private EditText titleEditText;
    private EditText descriptionEditText;

    public ColumnDataEditFragment() {
    }

    public static ColumnDataEditFragment newInstance(int columnIndex) {
        ColumnDataEditFragment fragment = new ColumnDataEditFragment();
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

        inflatedView = inflater.inflate(R.layout.fragment_edit_column_data, container, false);

        titleEditText = inflatedView.findViewById(R.id.titleEditText);
        descriptionEditText = inflatedView.findViewById(R.id.descriptionEditText);
        Spinner columnTypeSpinner = inflatedView.findViewById(R.id.columnTypeSpinner);

        final Test currentEditTest = DiakoluoApplication.getCurrentEditTest(inflatedView.getContext());
        final Column column = currentEditTest
                .getListColumn().get(columnIndex);

        titleEditText.setText(column.getName());
        descriptionEditText.setText(column.getDescription());


        ArrayAdapter<ColumnInputType> adapter = new ArrayAdapter<>(inflatedView.getContext(),
                R.layout.support_simple_spinner_dropdown_item, ColumnInputType.values());
        columnTypeSpinner.setAdapter(adapter);

        columnTypeSpinner.setSelection(column.getInputType().ordinal());

        columnTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO warning
                ColumnInputType inputType = ColumnInputType.values()[position];
                if (column.getInputType() != inputType) {
                    column.setInputType(inputType);
                    // update all cells

                    for (DataRow row : currentEditTest.getListRow()) {
                        switch (inputType) {
                            case String:
                                row.getListCells().put(column, new DataCellString(""));
                                break;

                            default:
                                throw new IllegalStateException("Unexpected value: " + column.getInputType());
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
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
    public void onPause() {

        Column column = DiakoluoApplication.getCurrentEditTest(inflatedView.getContext())
                .getListColumn().get(columnIndex);

        column.setName(titleEditText.getText().toString());
        column.setDescription(descriptionEditText.getText().toString());

        RecyclerViewChange recyclerViewChange = DiakoluoApplication.getColumnListChanged(
                inflatedView.getContext());
        if (recyclerViewChange == null) {
            recyclerViewChange = new RecyclerViewChange(RecyclerViewChange.None);
        }
        recyclerViewChange.setChanges(recyclerViewChange.getChanges() | RecyclerViewChange.ItemChanged);
        recyclerViewChange.setPosition(columnIndex);

        DiakoluoApplication.setColumnListChanged(inflatedView.getContext(), recyclerViewChange);

        super.onPause();
    }

    interface OnFragmentInteractionListener {
    }
}


