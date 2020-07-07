/*
 * Copyright (c) 2020 LOUBIERE Antonin <https://www.github.com/AntoninLoubiere/>
 *
 * This file is part of Diakôluô project <https://www.github.com/AntoninLoubiere/Diakoluo/>.
 *
 *     Diakôluô is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Diakôluô is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     A copy of the license is available in the root folder of Diakôluô, under the
 *     name of LICENSE.md. You could find it also at <https://www.gnu.org/licenses/gpl-3.0.html>.
 */

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

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.ScoreUtils;


public class TestScoreFragment extends Fragment {

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

        scoreProgressBar.setMax(testTestContext.getMaxProgressScore());

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
