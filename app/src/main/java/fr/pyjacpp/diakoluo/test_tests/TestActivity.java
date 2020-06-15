package fr.pyjacpp.diakoluo.test_tests;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;

public class TestActivity extends AppCompatActivity implements TestFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(DiakoluoApplication.getCurrentTest(this).getName());

        setContentView(R.layout.activity_test);
    }

    @Override
    public void showScore(TestTestContext context) {
        context.getTest().addNumberTestDid();
        startActivity(new Intent(getApplicationContext(), TestScoreActivity.class));
        finish();
    }
}
