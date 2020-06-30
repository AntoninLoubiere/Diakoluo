package fr.pyjacpp.diakoluo.edit_test;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;

public class ColumnDataEditActivity extends AppCompatActivity implements ColumnDataEditFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_column_data);

        int columnIndex = getIntent().getIntExtra(ColumnDataEditFragment.ARG_COLUMN_INDEX, 0);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            String stringValue = DiakoluoApplication.getCurrentEditTest(this).getListColumn().get(columnIndex).getName();
            actionBar.setTitle(stringValue.equals("") ? getString(R.string.app_name) : stringValue);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }

        if (savedInstanceState == null) {
            ColumnDataEditFragment fragment = ColumnDataEditFragment.newInstance(columnIndex
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

    @Override
    public void updateColumnRecyclerItem(int position) {
        Intent intent = new Intent();
        intent.setAction(EditTestActivity.ACTION_BROADCAST_UPDATE_COLUMN_RECYCLER);
        intent.putExtra(EditTestActivity.EXTRA_INT_POSITION, position);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.sendBroadcast(intent);
    }
}
