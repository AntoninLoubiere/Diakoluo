package fr.pyjacpp.diakoluo.view_test;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import fr.pyjacpp.diakoluo.R;

public class ColumnDataViewActivity extends AppCompatActivity implements ColumnDataViewFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_column_data_edit);

        if (savedInstanceState == null) {
            ColumnDataViewFragment fragment = ColumnDataViewFragment.newInstance(
                    getIntent().getIntExtra(ColumnDataViewFragment.ARG_COLUMN_INDEX, 0)
            );

            getFragmentManager().beginTransaction().replace(
                    R.id.columnDataEditFragmentContainer,
                    fragment).commit();
        }
    }

}
