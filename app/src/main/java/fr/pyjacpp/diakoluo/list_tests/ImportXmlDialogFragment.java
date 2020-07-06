package fr.pyjacpp.diakoluo.list_tests;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.save_test.FileManager;


public class ImportXmlDialogFragment extends DialogFragment {

    private OnValidListener listener;

    private EditText titleEditText;

    public ImportXmlDialogFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View inflatedView = inflater.inflate(R.layout.fragment_dialog_import_xml, container, false);

        FileManager.ImportContext _currentImportContext = DiakoluoApplication.getCurrentImportContext(inflatedView.getContext());
        final FileManager.ImportXmlContext currentImportContext;
        if (_currentImportContext instanceof FileManager.ImportXmlContext) {
            currentImportContext = (FileManager.ImportXmlContext) _currentImportContext;
        } else {
            dismiss();
            return new View(inflatedView.getContext());
        }

        titleEditText = inflatedView.findViewById(R.id.titleInput);

        Button cancelButton = inflatedView.findViewById(R.id.cancelButton);
        Button validButton = inflatedView.findViewById(R.id.validButton);

        if (savedInstanceState == null) {
            // first dialog open
            titleEditText.setText(currentImportContext.importTest.getName());
        }

        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() < 1) {
                    titleEditText.setError(getString(R.string.error_label_title_blank));
                } else {
                    titleEditText.setError(null);
                }
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
                if (titleEditText.getText().length() < 1) {
                    titleEditText.requestFocus();
                } else {
                    currentImportContext.importTest.setName(titleEditText.getText().toString());
                    listener.loadXmlFile();
                    dismiss();
                }
            }
        });

        return inflatedView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnValidListener) {
            listener = (OnValidListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnValidListener");
        }
    }

    public interface OnValidListener {
        void loadXmlFile();
    }
}
