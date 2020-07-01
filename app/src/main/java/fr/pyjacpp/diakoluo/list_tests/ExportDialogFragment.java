package fr.pyjacpp.diakoluo.list_tests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import fr.pyjacpp.diakoluo.R;

public class ExportDialogFragment extends DialogFragment {

    private static final long DIAKOLUO_FILE_TYPE_INDEX = 0;

    private static final String SAVE_NUMBER_TEST_DONE = "numberTestDone";
    private static final String COLUMN_HEADER = "columnHeader";
    private static final String COLUMN_TYPE_HEADER = "columnTypeHeader";

    private CheckBox saveNumberTestDoneCheckBox;
    private CheckBox columnHeaderCheckBox;
    private CheckBox columnTypeHeaderCheckBox;
    private TextView warningExportFileTypeTextView;

    private boolean saveNumberTestDoneChecked;
    private boolean columnHeaderChecked;
    private boolean columnTypeHeaderChecked;

    private OnValidListener listener;

    ExportDialogFragment(OnValidListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_dialog_export, container, false);

        final Spinner exportFileType = inflatedView.findViewById(R.id.exportFileTypeSpinner);
        saveNumberTestDoneCheckBox = inflatedView.findViewById(R.id.testDoneCheckBox);
        columnHeaderCheckBox = inflatedView.findViewById(R.id.columnHeaderCheckBox);
        columnTypeHeaderCheckBox = inflatedView.findViewById(R.id.columnTypeHeaderCheckBox);
        warningExportFileTypeTextView = inflatedView.findViewById(R.id.warningFileTypeTextView);
        Button cancelButton = inflatedView.findViewById(R.id.cancelButton);
        Button validButton = inflatedView.findViewById(R.id.validButton);

        if (savedInstanceState == null) {
            saveNumberTestDoneChecked = true;
            columnHeaderChecked = true;
            columnTypeHeaderChecked = true;
        } else {
            saveNumberTestDoneChecked = savedInstanceState.getBoolean(SAVE_NUMBER_TEST_DONE, true);
            columnHeaderChecked = savedInstanceState.getBoolean(COLUMN_HEADER, true);
            columnTypeHeaderChecked = savedInstanceState.getBoolean(COLUMN_TYPE_HEADER, true);
        }

        // lock updates
        saveNumberTestDoneCheckBox.setEnabled(false);
        columnHeaderCheckBox.setEnabled(false);
        columnTypeHeaderCheckBox.setEnabled(false);

        exportFileType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (l == DIAKOLUO_FILE_TYPE_INDEX) {
                    warningExportFileTypeTextView.setVisibility(View.GONE);
                    // update checkboxs
                    saveNumberTestDoneCheckBox.setChecked(saveNumberTestDoneChecked);
                    saveNumberTestDoneCheckBox.setEnabled(true);
                    columnHeaderCheckBox.setEnabled(false);
                    columnHeaderCheckBox.setChecked(true);
                    columnTypeHeaderCheckBox.setEnabled(false);
                    columnTypeHeaderCheckBox.setChecked(true);
                } else {
                    warningExportFileTypeTextView.setVisibility(View.VISIBLE);
                    saveNumberTestDoneCheckBox.setEnabled(false);
                    saveNumberTestDoneCheckBox.setChecked(false);
                    columnHeaderCheckBox.setChecked(columnHeaderChecked);
                    columnHeaderCheckBox.setEnabled(true);
                    columnTypeHeaderCheckBox.setChecked(columnTypeHeaderChecked);
                    columnTypeHeaderCheckBox.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        saveNumberTestDoneCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isEnabled())
                    saveNumberTestDoneChecked = b;
            }
        });

        columnHeaderCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isEnabled())
                    columnHeaderChecked = b;
            }
        });

        columnTypeHeaderCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isEnabled())
                    columnTypeHeaderChecked = b;
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        validButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (exportFileType.getSelectedItemId() == DIAKOLUO_FILE_TYPE_INDEX) {
                    listener.createXmlFile(saveNumberTestDoneCheckBox.isChecked());
                } else {
                    // TODO
                }
                dismiss();
            }
        });

        return inflatedView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVE_NUMBER_TEST_DONE, saveNumberTestDoneChecked);
        outState.putBoolean(COLUMN_HEADER, columnHeaderChecked);
        outState.putBoolean(COLUMN_TYPE_HEADER, columnTypeHeaderChecked);
    }

    public interface OnValidListener {
        void createXmlFile(boolean saveNumberTestDone);
    }
}
