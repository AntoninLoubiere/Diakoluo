package fr.pyjacpp.diakoluo.list_tests;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.RecyclerViewChange;
import fr.pyjacpp.diakoluo.tests.Test;


public class ListTestsFragment extends Fragment {
    private OnFragmentInteractionListener listener;
    private RecyclerView testRecyclerView;
    private RecyclerView.Adapter testRecyclerViewAdapter;

    public ListTestsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View inflatedLayout = inflater.inflate(R.layout.fragment_recycler_list, container, true);

        testRecyclerView = inflatedLayout.findViewById(R.id.recyclerView);
        LinearLayoutManager testRecyclerViewLayoutManager = new LinearLayoutManager(getContext());
        testRecyclerViewAdapter = new TestAdapter(getContext(), new TestAdapter.TestViewListener() {
            @Override
            public void onItemClick(View view, int position) {
                listener.onItemClick(view, position);
            }

            @Override
            public void onPlayButtonClick(View view, int position) {
                listener.onPlayButtonClick(view, position);
            }

            @Override
            public void onSeeButtonClick(View view, int position) {
                listener.onSeeButtonClick(view, position);
            }

            @Override
            public void onDeleteMenuItemClick(final View view, final int position) {
                final ArrayList<Test> listTest = DiakoluoApplication.getListTest(view.getContext());

                new AlertDialog.Builder(view.getContext())
                        .setTitle(R.string.dialog_delete_test_title)
                        .setMessage(getString(R.string.dialog_delete_test_message, listTest.get(position).getName()))
                        .setCancelable(true)
                        .setIcon(R.drawable.ic_delete_forever_accent_color_24dp)
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteTest(inflatedLayout, position);
                                listener.onDeleteTest(position);
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }

            @Override
            public void onEditMenuItemClick(View view, int position) {
                listener.onEditMenuItemClick(view, position);
            }

            @Override
            public void onExportMenuItemClick(View view, int position) {
                listener.onExportMenuItemClick(view, position);
            }
        });



        testRecyclerView.setHasFixedSize(true);
        testRecyclerView.setLayoutManager(testRecyclerViewLayoutManager);
        testRecyclerView.setAdapter(testRecyclerViewAdapter);

//        testRecyclerView.addItemDecoration(new DividerItemDecoration(testRecyclerView.getContext(),
//                testRecyclerViewLayoutManager.getOrientation()));

        return inflatedLayout;
    }

    private void deleteTest(final View view, final int position) {
        final ArrayList<Test> listTest = DiakoluoApplication.getListTest(view.getContext());
        testRecyclerView.removeViewAt(position);
        testRecyclerViewAdapter.notifyItemRemoved(position);
        testRecyclerViewAdapter.notifyItemRangeChanged(position, listTest.size());

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Test testToDelete = listTest.get(position);
                DiakoluoApplication.removeTest(view.getContext(), position);

                Snackbar.make(view, getString(R.string.test_deleted, testToDelete.getName()),
                        Snackbar.LENGTH_LONG)
                        .setDuration(getResources().getInteger(R.integer.snackbar_deleted_duration)).
                        setAnchorView(R.id.addFloatingButton).show();
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        Context context = getContext();
        if (context != null) {
            RecyclerViewChange testListChanged = DiakoluoApplication.getTestListChanged(context);
            if (testListChanged != null) {
                testListChanged.apply(testRecyclerViewAdapter);
                DiakoluoApplication.setTestListChanged(context, null);
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    void notifyUpdateInserted(int position) {
        testRecyclerViewAdapter.notifyItemInserted(position);
    }

    public interface OnFragmentInteractionListener{
        void onItemClick(View view, int position);
        void onPlayButtonClick(View view, int position);
        void onSeeButtonClick(View view, int position);
        void onEditMenuItemClick(View view, int position);
        void onDeleteTest(int position);
        void onExportMenuItemClick(View view, int position);
    }
}
