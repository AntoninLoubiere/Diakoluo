package fr.pyjacpp.diakoluo.edit_test;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;

public class AnswerDataEditActivity extends AppCompatActivity implements AnswerDataEditFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_answer_data);

        int answerIndex = getIntent().getIntExtra(AnswerDataEditFragment.ARG_ANSWER_INDEX, 0);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            String stringValue = DiakoluoApplication.getCurrentEditTest(this).getFirstCell(answerIndex).getStringValue();
            actionBar.setTitle(stringValue.equals("") ? getString(R.string.app_name) : stringValue);
        }

        if (savedInstanceState == null) {
            AnswerDataEditFragment fragment = AnswerDataEditFragment.newInstance(
                    answerIndex
            );

            getFragmentManager().beginTransaction().replace(
                    R.id.answerDataEditFragmentContainer,
                    fragment).commit();
        }
    }
}
