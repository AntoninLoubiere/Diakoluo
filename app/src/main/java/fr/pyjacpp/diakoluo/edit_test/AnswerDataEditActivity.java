package fr.pyjacpp.diakoluo.edit_test;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import fr.pyjacpp.diakoluo.R;

public class AnswerDataEditActivity extends AppCompatActivity implements AnswerDataEditFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_answer_data);

        if (savedInstanceState == null) {
            AnswerDataEditFragment fragment = AnswerDataEditFragment.newInstance(
                    getIntent().getIntExtra(AnswerDataEditFragment.ARG_ANSWER_INDEX, 0)
            );

            getFragmentManager().beginTransaction().replace(
                    R.id.answerDataEditFragmentContainer,
                    fragment).commit();
        }
    }
}
