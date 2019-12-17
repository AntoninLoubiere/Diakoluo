package fr.pyjacpp.diakoluo.test_tests;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import fr.pyjacpp.diakoluo.R;

public class TestActivity extends AppCompatActivity implements TestFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    @Override
    public void showScore(TestTestContext context) {
        finish();
        Toast.makeText(this,
                context.getScore() + "/" + context.getMaxScore(),
                Toast.LENGTH_LONG).show();
    }
}
