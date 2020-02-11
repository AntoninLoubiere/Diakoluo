package fr.pyjacpp.diakoluo.edit_test;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import fr.pyjacpp.diakoluo.R;

public class ColumnDataEditActivity extends AppCompatActivity implements ColumnDataEditFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_column_data_edit);

        if (savedInstanceState == null) {
            ColumnDataEditFragment fragment = ColumnDataEditFragment.newInstance(
                    getIntent().getIntExtra(ColumnDataEditFragment.ARG_COLUMN_INDEX, 0)
            );

            getFragmentManager().beginTransaction().replace(
                    R.id.columnDataEditFragmentContainer,
                    fragment).commit();
        }
    }

}
