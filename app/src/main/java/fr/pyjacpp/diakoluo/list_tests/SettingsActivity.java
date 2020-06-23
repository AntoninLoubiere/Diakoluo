package fr.pyjacpp.diakoluo.list_tests;

import android.os.Bundle;
import android.widget.CheckBox;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;

public class SettingsActivity extends AppCompatActivity {

    private CheckBox analyticsCheckBox;
    private CheckBox crashCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.settings);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }

        analyticsCheckBox = findViewById(R.id.analyticsCheckBox);
        crashCheckBox = findViewById(R.id.crashCheckBox);

        analyticsCheckBox.setChecked(DiakoluoApplication.getAnalyticsEnable(this));
        crashCheckBox.setChecked(DiakoluoApplication.getCrashlyticsEnable(this));
    }

    @Override
    protected void onStop() {
        DiakoluoApplication.setAnalyticsEnable(this, analyticsCheckBox.isChecked());
        DiakoluoApplication.setCrashlyticsEnable(this, crashCheckBox.isChecked());
        super.onStop();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
