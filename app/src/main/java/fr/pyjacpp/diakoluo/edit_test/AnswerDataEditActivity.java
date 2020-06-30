package fr.pyjacpp.diakoluo.edit_test;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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
            DataCell dataCell = DiakoluoApplication.getCurrentEditTest(this).getRowFirstCell(answerIndex);
            String stringValue = "";
            if (dataCell != null)
                stringValue = dataCell.getStringValue();

            if (stringValue.equals(""))
                actionBar.setTitle(R.string.app_name);
            else
                actionBar.setTitle(stringValue);

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

    @Override
    public void updateAnswerRecyclerItem(int position) {
        Intent intent = new Intent();
        intent.setAction(EditTestActivity.ACTION_BROADCAST_UPDATE_ANSWER_RECYCLER);
        intent.putExtra(EditTestActivity.EXTRA_INT_POSITION, position);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.sendBroadcast(intent);
    }
}
