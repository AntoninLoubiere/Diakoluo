package fr.pyjacpp.diakoluo.test_tests;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Random;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.ScoreUtils;


class TestScoreFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private TestTestContext testTestContext;

    private ProgressBar scoreProgressBar;

    public TestScoreFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_test_score, container, false);

        TextView primaryScoreTextView = inflatedView.findViewById(R.id.scoreTextView);
        TextView secondaryScoreTextView = inflatedView.findViewById(R.id.secondaryScoreTextView);
        scoreProgressBar = inflatedView.findViewById(R.id.scoreProgressBar);
        Button restartButton = inflatedView.findViewById(R.id.restartButton);
        Button mainMenuButton = inflatedView.findViewById(R.id.mainMenuButton);

        scoreProgressBar.setMax(testTestContext.getMaxScore());

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator animation = ObjectAnimator.ofInt(scoreProgressBar, "progress", testTestContext.getProgressScore());
                animation.setDuration(3000);
                animation.setInterpolator(new DecelerateInterpolator());
                animation.start();
                scoreProgressBar.setProgress(testTestContext.getProgressScore());
            }
        }, 1000);
        secondaryScoreTextView.setText(getString(R.string.score_precise, testTestContext.getScore(), testTestContext.getMaxScore()));

        primaryScoreTextView.setText(getString(R.string.score_20, ScoreUtils.getScore20(testTestContext)));

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.restartButton();
            }
        });

        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.mainMenuButton();
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
        testTestContext = DiakoluoApplication.getTestTestContext(context);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void mainMenuButton();
        void restartButton();
    }
}
