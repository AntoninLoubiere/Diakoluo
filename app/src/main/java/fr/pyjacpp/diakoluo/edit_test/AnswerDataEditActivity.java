package fr.pyjacpp.diakoluo.edit_test;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.tests.data.DataCell;

public class AnswerDataEditActivity extends AppCompatActivity implements AnswerDataEditFragment.OnFragmentInteractionListener{

    private int answerIndex;
    private Test currentTest;

    private Button previousButton;
    private Button nextButton;
    private TextView navigationTextView;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_answer_data);

        answerIndex = getIntent().getIntExtra(AnswerDataEditFragment.ARG_ANSWER_INDEX, 0);
        currentTest = DiakoluoApplication.getCurrentEditTest(this);

        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        navigationTextView = findViewById(R.id.navigationTextView);

        actionBar = getSupportActionBar();
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
            createFragment();
        } else {
            updateNavigation();
        }

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSwipeRight();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSwipeLeft();
            }
        });
    }

    private void createFragment() {
        AnswerDataEditFragment fragment = AnswerDataEditFragment.newInstance(
                answerIndex
        );

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fragment_fade_scale_enter, R.anim.fragment_fade_scale_exit)
                .replace(R.id.answerDataEditFragmentContainer, fragment)
                .commit();

        updateNavigation();
    }

    private void updateNavigation() {
        if (actionBar != null) {
            DataCell firstCell = currentTest.getRowFirstCell(answerIndex);
            if (firstCell == null)
                actionBar.setTitle(R.string.app_name);
            else
                actionBar.setTitle(firstCell.getStringValue());
        }

        navigationTextView.setText(getString(R.string.navigation_info, answerIndex + 1,
                currentTest.getNumberRow()));


        if (answerIndex > 0) {
            if (!previousButton.isEnabled()) {
                previousButton.setVisibility(View.VISIBLE);
                previousButton.setEnabled(true);
            }
            DataCell firstCell = currentTest.getRowFirstCell(answerIndex - 1);
            if (firstCell == null)
                previousButton.setText(R.string.previous);
            else
                previousButton.setText(firstCell.getStringValue());
        } else {
            previousButton.setEnabled(false);
            previousButton.setVisibility(View.GONE);
        }

        if (answerIndex < currentTest.getNumberRow() - 1) {
            DataCell firstCell = currentTest.getRowFirstCell(answerIndex + 1);
            if (firstCell == null)
                nextButton.setText(R.string.next);
            else
                nextButton.setText(firstCell.getStringValue());
        } else {
            nextButton.setText(R.string.create_new_data_edit);
        }
    }

    @Override
    public void onSwipeRight() {
        if (answerIndex > 0) {
            answerIndex -= 1;
            createFragment();
            getIntent().putExtra(AnswerDataEditFragment.ARG_ANSWER_INDEX, answerIndex);
        }
    }

    @Override
    public void onSwipeLeft() {
        if (answerIndex < currentTest.getNumberRow() - 1) {
            answerIndex += 1;
            createFragment();
            getIntent().putExtra(AnswerDataEditFragment.ARG_ANSWER_INDEX, answerIndex);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_new_answer_title)
                    .setMessage(R.string.dialog_new_answer_message)
                    .setIcon(R.drawable.ic_add_accent_color_24dp)
                    .setPositiveButton(R.string.dialog_create_new_data_edit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // update recyclers
                            Intent intent = new Intent();
                            intent.setAction(EditTestActivity.ACTION_BROADCAST_NEW_ANSWER_RECYCLER);
                            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(AnswerDataEditActivity.this);
                            localBroadcastManager.sendBroadcastSync(intent);
                            onSwipeLeft();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .show();
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
