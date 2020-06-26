package fr.pyjacpp.diakoluo.edit_test;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.data.DataCell;

public class AnswerDataEditActivity extends AppCompatActivity implements AnswerDataEditFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_answer_data);

        int answerIndex = getIntent().getIntExtra(AnswerDataEditFragment.ARG_ANSWER_INDEX, 0);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            DataCell dataCell = DiakoluoApplication.getCurrentEditTest(this).getFirstCell(answerIndex);
            String stringValue = "";
            if (dataCell != null)
                stringValue = dataCell.getStringValue();

            actionBar.setTitle(stringValue.equals("") ? getString(R.string.app_name) : stringValue);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }

        if (savedInstanceState == null) {
            AnswerDataEditFragment fragment = AnswerDataEditFragment.newInstance(
                    answerIndex
            );

            getSupportFragmentManager().beginTransaction().replace(
                    R.id.answerDataEditFragmentContainer,
                    fragment).commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
