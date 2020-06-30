package fr.pyjacpp.diakoluo.view_test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;



public class ColumnViewTestFragment extends Fragment implements
        ColumnViewTestRecyclerListFragment.OnParentFragmentInteractionListener {
    private OnFragmentInteractionListener mListener;

    private boolean columnDetail;

    public ColumnViewTestFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_view_column_test, container, false);

        columnDetail = inflatedView.findViewById(R.id.columnDataViewFragmentContainer) != null;

        if (columnDetail && DiakoluoApplication.getCurrentTest(inflatedView.getContext()).getNumberColumn() > 0) {
            onItemClick(inflatedView, 0); // show first element
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
    public void onItemClick(View view, int position) {
        if (columnDetail) {
            getChildFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.fragment_fade_scale_enter, R.anim.fragment_fade_scale_exit)
                    .replace(R.id.columnDataViewFragmentContainer,
                            ColumnDataViewFragment.newInstance(position))
                    .commit();
        } else {
            Intent intent = new Intent(view.getContext(), ColumnDataViewActivity.class);
            intent.putExtra(ColumnDataViewFragment.ARG_COLUMN_INDEX, position);
            startActivity(intent);
        }
    }

    public interface OnFragmentInteractionListener {
    }
}
