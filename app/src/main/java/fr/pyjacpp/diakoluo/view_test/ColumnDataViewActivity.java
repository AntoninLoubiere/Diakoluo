package fr.pyjacpp.diakoluo.view_test;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;

public class ColumnDataViewActivity extends AppCompatActivity implements ColumnDataViewFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_column_data);

        int columnIndex = getIntent().getIntExtra(ColumnDataViewFragment.ARG_COLUMN_INDEX, 0);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(DiakoluoApplication.getCurrentTest(this).getListColumn().get(columnIndex).getName());
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }

        if (savedInstanceState == null) {

            ColumnDataViewFragment fragment = ColumnDataViewFragment.newInstance(
                    columnIndex
            );

            getSupportFragmentManager().beginTransaction().replace(
                    R.id.columnDataEditFragmentContainer,
                    fragment).commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
