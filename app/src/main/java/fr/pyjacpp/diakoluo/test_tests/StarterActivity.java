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

package fr.pyjacpp.diakoluo.test_tests;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.list_tests.ListTestActivity;

public class StarterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);

        Button validButton = findViewById(R.id.validButton);
        final CheckBox analyticsCheckBox = findViewById(R.id.analyticsCheckBox);
        final CheckBox crashCheckBox = findViewById(R.id.crashCheckBox);

        validButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final boolean analyticsCheckBoxChecked = analyticsCheckBox.isChecked();
                final boolean crashCheckBoxChecked = crashCheckBox.isChecked();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DiakoluoApplication.setAnalyticsSet(StarterActivity.this, true);
                        DiakoluoApplication.setAnalyticsEnable(StarterActivity.this,
                                analyticsCheckBoxChecked);
                        DiakoluoApplication.setCrashlyticsEnable(StarterActivity.this,
                                crashCheckBoxChecked);
                    }
                }).start();
                startActivity(new Intent(StarterActivity.this, ListTestActivity.class));
                finish();
            }
        });
    }
}
