package fr.pyjacpp.diakoluo.view_test;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;

public class AnswerDataViewActivity extends AppCompatActivity implements AnswerDataViewFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_answer_data);

        int answerIndex = getIntent().getIntExtra(AnswerDataViewFragment.ARG_ANSWER_INDEX, 0);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(DiakoluoApplication.getCurrentTest(this).getFirstCell(answerIndex).getStringValue());
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }

        if (savedInstanceState == null) {
            AnswerDataViewFragment fragment = AnswerDataViewFragment.newInstance(
                    answerIndex
            );

            getSupportFragmentManager().beginTransaction().replace(
                    R.id.answerDataViewFragmentContainer,
                    fragment).commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
