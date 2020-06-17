package fr.pyjacpp.diakoluo.test_tests;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;

public class TestScoreActivity extends AppCompatActivity implements TestScoreFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_score);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(DiakoluoApplication.getCurrentTest(this).getName());
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        }
    }

    @Override
    public void mainMenuButton() {
        finish();
    }

    @Override
    public void restartButton() {
        TestTestContext testTestContext = DiakoluoApplication.getTestTestContext(this);
        testTestContext.reset();
        testTestContext.selectShowColumn();
        startActivity(new Intent(this, TestActivity.class));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
