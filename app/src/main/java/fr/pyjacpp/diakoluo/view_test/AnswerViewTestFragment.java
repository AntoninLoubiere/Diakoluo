package fr.pyjacpp.diakoluo.view_test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import fr.pyjacpp.diakoluo.R;


public class AnswerViewTestFragment extends Fragment implements
        AnswerViewTestRecyclerListFragment.OnFragmentInteractionParentListener {

    private OnFragmentInteractionListener mListener;

    private boolean detailAnswer;

    public AnswerViewTestFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_view_answer_test, container, false);

        detailAnswer = inflatedView.findViewById(R.id.answerDataViewFragmentContainer) != null;

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
        if (detailAnswer) {
            getChildFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.fragment_fade_scale_enter, R.anim.fragment_fade_scale_exit)
                    .replace(R.id.answerDataViewFragmentContainer,AnswerDataViewFragment.newInstance(position))
                    .commit();
        } else {
            Intent intent = new Intent(view.getContext(), AnswerDataViewActivity.class);
            intent.putExtra(AnswerDataViewFragment.ARG_ANSWER_INDEX, position);
            startActivity(intent);
        }
    }

    public interface OnFragmentInteractionListener {
    }
}
