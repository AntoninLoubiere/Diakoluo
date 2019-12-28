package fr.pyjacpp.diakoluo.edit_test;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.Test;

public class MainInformationsEditTestFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private View inflatedView;

    public MainInformationsEditTestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_edit_main_informations_test, container, false);

        inflatedView.findViewById(R.id.titleTextView);

        DateFormat dateFormat = android.text.format.DateFormat.getLongDateFormat(container.getContext().getApplicationContext());
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(container.getContext().getApplicationContext());


        EditText title = inflatedView.findViewById(R.id.titleEditText);
        EditText description = inflatedView.findViewById(R.id.descriptionEditText);
        TextView createdDate = inflatedView.findViewById(R.id.createdDateTextView);
        TextView lastModification = inflatedView.findViewById(R.id.lastModificationTextView);
        TextView numberTedtDid = inflatedView.findViewById(R.id.numberTestDid);

        Test currentEditTest = DiakoluoApplication.getCurrentEditTest(container.getContext());

        title.addTextChangedListener(new EditTextTextWacher(title) {
            @Override
            EditTestActivity.EditTestValidator validatorFunction(String text) {
                return mListener.titleEditTestValidator(text);
            }
        });
        description.addTextChangedListener(new EditTextTextWacher(description) {
            @Override
            EditTestActivity.EditTestValidator validatorFunction(String text) {
                return mListener.descriptionEditTestValidator(text);
            }
        });


        createdDate.setText(
                String.format(
                        getString(R.string.created_date_test_format),
                        dateFormat.format(currentEditTest.getCreatedDate()),
                        timeFormat.format(currentEditTest.getCreatedDate())
                )
        );

        lastModification.setText(
                String.format(
                        getString(R.string.last_modification_test_format),
                        dateFormat.format(currentEditTest.getLastModificationDate()),
                        timeFormat.format(currentEditTest.getLastModificationDate())
                )
        );

        numberTedtDid.setText(
                String.format(
                        getString(R.string.number_test_did_format),
                        currentEditTest.getNumberTestDid()
                )
        );

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
    public void onStart() {
        super.onStart();
        getFromTestVar();
    }

    private void getFromTestVar() {
        Test currentEditTest = DiakoluoApplication.getCurrentEditTest(inflatedView.getContext());

        EditText title = inflatedView.findViewById(R.id.titleEditText);
        EditText description = inflatedView.findViewById(R.id.descriptionEditText);

        title.setText(currentEditTest.getName());
        description.setText(currentEditTest.getDescription());
    }

    public interface OnFragmentInteractionListener {
        EditTestActivity.EditTestValidator titleEditTestValidator(String text);
        EditTestActivity.EditTestValidator descriptionEditTestValidator(String text);
    }

    public abstract class EditTextTextWacher implements TextWatcher {

        private EditText editText;

        EditTextTextWacher(EditText editText) {
            super();
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            EditTestActivity.EditTestValidator validatorReponse = validatorFunction(editable.toString());

            if (validatorReponse.isError()) {
                String msg = getString(validatorReponse.getErrorMessageRessourceId());
                if (validatorReponse.isWarning()) {
                    Drawable warningIcon= getResources().getDrawable(R.drawable.ic_warning_yellow_24dp);
                    warningIcon.setBounds(0, 0, warningIcon.getIntrinsicWidth(), warningIcon.getIntrinsicHeight());

                    editText.setError(msg, warningIcon);
                } else {
                    editText.setError(msg);
                }
            } else {
                editText.setError(null);
            }
        }

        abstract EditTestActivity.EditTestValidator validatorFunction(String text);
    }
}
