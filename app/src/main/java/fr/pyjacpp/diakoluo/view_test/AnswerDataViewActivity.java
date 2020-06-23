package fr.pyjacpp.diakoluo.view_test;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.Test;

public class AnswerDataViewActivity extends AppCompatActivity implements AnswerDataViewFragment.OnFragmentInteractionListener{

    private int answerIndex;
    private Button previousButton;
    private Button nextButton;
    private TextView navigationTextView;
    private Test currentTest;
    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_answer_data);

        answerIndex = getIntent().getIntExtra(AnswerDataViewFragment.ARG_ANSWER_INDEX, 0);

        currentTest = DiakoluoApplication.getCurrentTest(this);

        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        navigationTextView = findViewById(R.id.navigationTextView);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(currentTest.getFirstCell(answerIndex).getStringValue());
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }

        if (savedInstanceState == null) {
            createFragment();
        } else {
            updateNavigation();
        }

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answerIndex > 0) {
                    answerIndex -= 1;
                    createFragment();
                    getIntent().putExtra(AnswerDataViewFragment.ARG_ANSWER_INDEX, answerIndex);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answerIndex < currentTest.getNumberRow() - 1) {
                    answerIndex += 1;
                    createFragment();
                    getIntent().putExtra(AnswerDataViewFragment.ARG_ANSWER_INDEX, answerIndex);
                }
            }
        });
    }

    private void createFragment() {
        AnswerDataViewFragment fragment = AnswerDataViewFragment.newInstance(
                answerIndex
        );

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fragment_fade_scale_enter, R.anim.fragment_fade_scale_exit)
                .replace(R.id.answerDataViewFragmentContainer, fragment).commit();

        updateNavigation();
    }

    private void updateNavigation() {
        if (actionBar != null)
            actionBar.setTitle(currentTest.getFirstCell(answerIndex).getStringValue());

        navigationTextView.setText(getString(R.string.navigation_info, answerIndex + 1,
                currentTest.getNumberRow()));


        if (answerIndex > 0) {
            if (!previousButton.isEnabled()) {
                previousButton.setVisibility(View.VISIBLE);
                previousButton.setEnabled(true);
            }
            previousButton.setText(currentTest.getFirstCell(answerIndex - 1).getStringValue());
        } else {
            previousButton.setEnabled(false);
            previousButton.setVisibility(View.GONE);
        }

        if (answerIndex < currentTest.getNumberRow() - 1) {
            if (!nextButton.isEnabled()) {
                nextButton.setVisibility(View.VISIBLE);
                nextButton.setEnabled(true);
            }
            nextButton.setText(currentTest.getFirstCell(answerIndex + 1).getStringValue());
        } else {
            nextButton.setEnabled(false);
            nextButton.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
