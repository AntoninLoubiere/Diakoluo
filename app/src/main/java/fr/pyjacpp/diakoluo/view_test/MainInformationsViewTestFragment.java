package fr.pyjacpp.diakoluo.view_test;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;

public class MainInformationsViewTestFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    public MainInformationsViewTestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_main_informations_view_test, container, false);

        inflatedView.findViewById(R.id.titleTextView);

        DateFormat dateFormat = android.text.format.DateFormat.getLongDateFormat(container.getContext().getApplicationContext());
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(container.getContext().getApplicationContext());


        TextView title = inflatedView.findViewById(R.id.titleTextView);
        TextView description = inflatedView.findViewById(R.id.descriptionTextView);
        TextView createdDate = inflatedView.findViewById(R.id.createdDateTextView);
        TextView lastModification = inflatedView.findViewById(R.id.lastModificationTextView);
        TextView numberTedtDid = inflatedView.findViewById(R.id.numberTestDid);

        title.setText(DiakoluoApplication.getCurrentTest(container.getContext()).getName());
        description.setText(DiakoluoApplication.getCurrentTest(container.getContext()).getDescription());
        createdDate.setText(
                String.format(
                        getString(R.string.created_date_test_format),
                        dateFormat.format(DiakoluoApplication.getCurrentTest(container.getContext()).getCreatedDate()),
                        timeFormat.format(DiakoluoApplication.getCurrentTest(container.getContext()).getCreatedDate())
                )
        );

        lastModification.setText(
                String.format(
                        getString(R.string.last_modification_test_format),
                        dateFormat.format(DiakoluoApplication.getCurrentTest(container.getContext()).getLastModificationDate()),
                        timeFormat.format(DiakoluoApplication.getCurrentTest(container.getContext()).getLastModificationDate())
                )
        );

        numberTedtDid.setText(
                String.format(
                        getString(R.string.number_test_did_format),
                        DiakoluoApplication.getCurrentTest(container.getContext()).getNumberTestDid()
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
    public interface OnFragmentInteractionListener {
        // TODO
    }
}
