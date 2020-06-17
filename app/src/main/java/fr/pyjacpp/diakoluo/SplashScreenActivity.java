package fr.pyjacpp.diakoluo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import fr.pyjacpp.diakoluo.list_tests.ListTestActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int LOADING_TIME_SPLASH_SCREEN = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (savedInstanceState == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                startActivity(
                        new Intent(SplashScreenActivity.this, ListTestActivity.class)
                );
                finish();
                }
            }, LOADING_TIME_SPLASH_SCREEN);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                finish();
                }
            }, LOADING_TIME_SPLASH_SCREEN);
        }
    }
}
