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
import fr.pyjacpp.diakoluo.tests.Column;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;
import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.tests.data.DataCellString;


public class ColumnEditTestFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    public ColumnEditTestFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View inflatedView = inflater.inflate(R.layout.fragment_edit_column_test, container, false);


        Button addColumnButton = inflatedView.findViewById(R.id.addColumnButton);
        addColumnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Test currentEditTest = DiakoluoApplication.getCurrentEditTest(view.getContext());
                ArrayList<Column> listColumn = currentEditTest.getListColumn();
                Column column = new Column("", "", ColumnInputType.DEFAULT_INPUT_TYPE);
                listColumn.add(column);

                RecyclerViewChange columnListChanged = new RecyclerViewChange(
                        RecyclerViewChange.ItemInserted
                );
                columnListChanged.setPosition(listColumn.size() - 1);
                DiakoluoApplication.setColumnListChanged(view.getContext(), columnListChanged);

                RecyclerViewChange recyclerViewChange = new RecyclerViewChange(RecyclerViewChange.ItemRangeChanged);
                recyclerViewChange.setPositionStart(0);
                recyclerViewChange.setPositionEnd(currentEditTest.getNumberRow() - 1);
                DiakoluoApplication.setAnswerListChanged(view.getContext(), recyclerViewChange);

                for (DataRow row : currentEditTest.getListRow()) {
                    // TODO: improve
                    row.getListCells().put(column, new DataCellString((String) column.getDefaultValue()));
                }

                Intent intent = new Intent(view.getContext(), ColumnDataEditActivity.class);
                intent.putExtra(ColumnDataEditFragment.ARG_COLUMN_INDEX, listColumn.size() - 1);
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
