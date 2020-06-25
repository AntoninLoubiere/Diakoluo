package fr.pyjacpp.diakoluo.edit_test;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import fr.pyjacpp.diakoluo.R;

public class ColumnDataEditActivity extends AppCompatActivity implements ColumnDataEditFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_column_data);

        if (savedInstanceState == null) {
            ColumnDataEditFragment fragment = ColumnDataEditFragment.newInstance(
                    getIntent().getIntExtra(ColumnDataEditFragment.ARG_COLUMN_INDEX, 0)
            );

            getSupportFragmentManager().beginTransaction().replace(
                    R.id.columnDataEditFragmentContainer,
                    fragment).commit();
        }
    }

}
