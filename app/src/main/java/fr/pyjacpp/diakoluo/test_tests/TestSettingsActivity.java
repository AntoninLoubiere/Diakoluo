package fr.pyjacpp.diakoluo.test_tests;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;

public class TestSettingsActivity extends AppCompatActivity implements TestSettingsFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_settings);

    }

    @Override
    public void onDoTest(int numberQuestionToAsk, int numberColumnToShow) {
        TestTestContext testTestContext = new TestTestContext(this, numberQuestionToAsk, numberColumnToShow);
        testTestContext.selectShowColumn();
        DiakoluoApplication.setTestTestContext(this, testTestContext);

        startActivity(new Intent(this, TestActivity.class));
        finish();
    }
}
