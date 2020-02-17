package fr.pyjacpp.diakoluo.test_tests;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import fr.pyjacpp.diakoluo.R;

public class TestActivity extends AppCompatActivity implements TestFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    @Override
    public void showScore(TestTestContext context) {
        context.getTest().addNumberTestDid();
        startActivity(new Intent(getApplicationContext(), TestScoreActivity.class));
        finish();
    }
}
