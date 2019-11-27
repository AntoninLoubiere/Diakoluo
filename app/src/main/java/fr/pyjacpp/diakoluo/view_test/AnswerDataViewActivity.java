package fr.pyjacpp.diakoluo.view_test;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import fr.pyjacpp.diakoluo.R;

public class AnswerDataViewActivity extends AppCompatActivity implements AnswerDataViewFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_data_view);

        if (savedInstanceState == null) {
            AnswerDataViewFragment fragment = AnswerDataViewFragment.newInstance(
                    getIntent().getIntExtra(AnswerDataViewFragment.ARG_ANSWER_INDEX, 0)
            );

            getFragmentManager().beginTransaction().replace(
                    R.id.answerDataViewFragmentContainer,
                    fragment).commit();
        }
    }
}
