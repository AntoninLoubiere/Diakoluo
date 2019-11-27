package fr.pyjacpp.diakoluo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import fr.pyjacpp.diakoluo.list_tests.ListTestActivity;

public class SplashScreenActivity extends AppCompatActivity {

    public static final int LOADING_TIME_SPLASH_SCREEN = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(
                        new Intent(SplashScreenActivity.this, ListTestActivity.class)
                );
                finish();
            }
        }, LOADING_TIME_SPLASH_SCREEN);
    }
}
