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
