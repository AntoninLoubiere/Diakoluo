/*
 * Copyright (c) 2020 LOUBIERE Antonin <https://www.github.com/AntoninLoubiere/>
 *
 * This file is part of Diakôluô project <https://www.github.com/AntoninLoubiere/Diakoluo/>.
 *
 *     Diakôluô is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Diakôluô is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     A copy of the license is available in the root folder of Diakôluô, under the
 *     name of LICENSE.md. You could find it also at <https://www.gnu.org/licenses/gpl-3.0.html>.
 */

package fr.pyjacpp.diakoluo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import fr.pyjacpp.diakoluo.list_tests.ListTestActivity;
import fr.pyjacpp.diakoluo.test_tests.StarterActivity;

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
                    if (DiakoluoApplication.get(SplashScreenActivity.this).getAnalyticsSet())
                        startActivity(
                                new Intent(SplashScreenActivity.this, ListTestActivity.class)
                        );
                    else {
                        startActivity(
                                new Intent(SplashScreenActivity.this, StarterActivity.class)
                        );
                    }
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
