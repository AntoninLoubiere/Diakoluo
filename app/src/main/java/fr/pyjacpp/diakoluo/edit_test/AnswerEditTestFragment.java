package fr.pyjacpp.diakoluo.edit_test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.RecyclerViewChange;
import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.Test;


public class AnswerEditTestFragment extends Fragment implements
        AnswerEditTestRecyclerListFragment.OnParentFragmentInteractionListener,
        AnswerDataEditFragment.OnParentFragmentInteractionListener {
    private OnFragmentInteractionListener mListener;

    private boolean answerDetail;
    @Nullable
    private AnswerDataEditFragment answerDataEditFragment;
    private AnswerEditTestRecyclerListFragment answerEditTestRecyclerListFragment;

    public AnswerEditTestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_edit_answer_test, container, false);

        answerDetail = inflatedView.findViewById(R.id.answerDataEditFragmentContainer) != null;
        answerEditTestRecyclerListFragment = (AnswerEditTestRecyclerListFragment)
                getChildFragmentManager().findFragmentById(R.id.answerEditTestRecyclerListFragment);

        Button addAnswerButton = inflatedView.findViewById(R.id.addAnswerButton);
        addAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<DataRow> listRow = DiakoluoApplication.getCurrentEditTest(view.getContext()).getListRow();
                listRow.add(new DataRow());

                RecyclerViewChange answerListChanged = new RecyclerViewChange(
                        RecyclerViewChange.ItemInserted
                );
                answerListChanged.setPosition(listRow.size() - 1);
                answerEditTestRecyclerListFragment.applyRecyclerChanges(answerListChanged);

                Intent intent = new Intent(view.getContext(), AnswerDataEditActivity.class);
                intent.putExtra(AnswerDataEditFragment.ARG_ANSWER_INDEX, listRow.size() - 1);
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

    @Override
    public void onItemClick(View view, int position) {
        onItemClick(view, position, false);
    }

    public void onItemClick(View view, int position, boolean forceUpdate) {
        if (answerDetail) {
            if (answerDataEditFragment == null || answerDataEditFragment.getAnswerIndex() != position | forceUpdate) {
                if (position >= 0) {
                    answerDataEditFragment = AnswerDataEditFragment.newInstance(position);
                    getChildFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.fragment_fade_scale_enter, R.anim.fragment_fade_scale_exit)
                            .replace(R.id.answerDataEditFragmentContainer,
                                    answerDataEditFragment)
                            .commit();
                } else if (answerDataEditFragment != null) {
                    getChildFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.fragment_fade_scale_enter, R.anim.fragment_fade_scale_exit)
                            .remove(answerDataEditFragment)
                            .commit();
                    answerDataEditFragment = null;
                }
            }
        } else {
            Intent intent = new Intent(view.getContext(), AnswerDataEditActivity.class);
            intent.putExtra(AnswerDataEditFragment.ARG_ANSWER_INDEX, position);
            startActivity(intent);
        }
    }

    @Override
    public void onSwap(int dragFrom, int dragTo) {
        // detect if fragment need to update index due to a swap
        if (answerDataEditFragment != null) {
            int i = answerDataEditFragment.getAnswerIndex();
            if (dragFrom < dragTo) {
                if (dragFrom < i) {
                    if (i <= dragTo) {
                        answerDataEditFragment.setAnswerIndex(i - 1);
                    }
                } else if (i == dragFrom) {
                    answerDataEditFragment.setAnswerIndex(dragTo);
                }
            } else {
                if (dragTo <= i) {
                    if (i < dragFrom) {
                        answerDataEditFragment.setAnswerIndex(i + 1);
                    } else if (i == dragFrom) {
                        answerDataEditFragment.setAnswerIndex(dragTo);
                    }
                }
            }
        }
    }

    @Override
    public void onDelete(View view, int position) {
        if (answerDetail && answerDataEditFragment != null) {
            int answerIndex = answerDataEditFragment.getAnswerIndex();
            if (position == answerIndex) {
                answerDataEditFragment.setAnswerIndex(-1);

                Test currentEditTest = DiakoluoApplication.getCurrentEditTest(view.getContext());
                if (position < currentEditTest.getNumberRow()) {
                    onItemClick(view, position, true);
                } else if (currentEditTest.getNumberRow() > 0) {
                    onItemClick(view, currentEditTest.getNumberRow() - 1, true);
                } else {
                    onItemClick(view, -1, true);
                }
            } else if (position < answerIndex) {
                answerDataEditFragment.setAnswerIndex(answerIndex - 1);
            }
        }
    }

    @Override
    public void updateItem(int position) {
        answerEditTestRecyclerListFragment.updateItem(position);
    }

    void updateAnswerRecycler(RecyclerViewChange recyclerViewChange) {
        answerEditTestRecyclerListFragment.applyRecyclerChanges(recyclerViewChange);
    }

    public interface OnFragmentInteractionListener {
    }
}
