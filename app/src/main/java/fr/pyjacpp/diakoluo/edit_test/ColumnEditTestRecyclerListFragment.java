package fr.pyjacpp.diakoluo.edit_test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.RecyclerItemClickListener;
import fr.pyjacpp.diakoluo.RecyclerViewChange;

public class ColumnEditTestRecyclerListFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private RecyclerView.Adapter columnRecyclerViewAdapter;

    public ColumnEditTestRecyclerListFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_recycler_list, container, false);

        RecyclerView columnRecyclerView = inflatedView.findViewById(R.id.recyclerView);
        columnRecyclerViewAdapter = new ColumnAdapter(columnRecyclerView.getContext());
        LinearLayoutManager columnRecyclerViewLayoutManager = new LinearLayoutManager(columnRecyclerView.getContext());

        columnRecyclerView.setHasFixedSize(true);
        columnRecyclerView.setLayoutManager(columnRecyclerViewLayoutManager);
        columnRecyclerView.setAdapter(columnRecyclerViewAdapter);
        //columnRecyclerView.setNestedScrollingEnabled(true);

        columnRecyclerView.addItemDecoration(new DividerItemDecoration(columnRecyclerView.getContext(),
                columnRecyclerViewLayoutManager.getOrientation()));

        columnRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                columnRecyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(view.getContext(), ColumnDataEditActivity.class);
                        intent.putExtra(ColumnDataEditFragment.ARG_COLUMN_INDEX, position);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }
        ));

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
    public void onResume() {
        super.onResume();

        Context context = getContext();
        if (context != null) {
            RecyclerViewChange testListChanged = DiakoluoApplication.getColumnListChanged(context);
            if (testListChanged != null) {
                Log.d("Column", testListChanged.getChanges() + " - " + testListChanged.getPosition());
                testListChanged.apply(columnRecyclerViewAdapter);
                DiakoluoApplication.setColumnListChanged(context, null);
            }
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
