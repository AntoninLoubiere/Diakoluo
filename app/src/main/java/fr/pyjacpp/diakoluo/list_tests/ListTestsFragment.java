package fr.pyjacpp.diakoluo.list_tests;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import fr.pyjacpp.diakoluo.view_test.ViewTestActivity;


public class ListTestsFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    public ListTestsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedLayout = inflater.inflate(R.layout.fragment_recycler_list, container, false);

        RecyclerView testRecyclerView = inflatedLayout.findViewById(R.id.recyclerView);
        LinearLayoutManager testRecyclerViewLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView.Adapter testRecyclerViewAdapter = new TestAdapter(getContext());



        testRecyclerView.setHasFixedSize(true);
        testRecyclerView.setLayoutManager(testRecyclerViewLayoutManager);
        testRecyclerView.setAdapter(testRecyclerViewAdapter);

        testRecyclerView.addItemDecoration(new DividerItemDecoration(testRecyclerView.getContext(),
                testRecyclerViewLayoutManager.getOrientation()));

        testRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                testRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                DiakoluoApplication.setCurrentTest(view.getContext(),
                        DiakoluoApplication.getListTest(view.getContext()).get(position));

                startActivity(new Intent(view.getContext(), ViewTestActivity.class));
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }
        ));

        return inflatedLayout;
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
