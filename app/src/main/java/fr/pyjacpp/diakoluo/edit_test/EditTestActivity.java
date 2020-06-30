package fr.pyjacpp.diakoluo.edit_test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayDeque;
import java.util.ArrayList;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.RecyclerViewChange;
import fr.pyjacpp.diakoluo.tests.Test;

public class EditTestActivity extends AppCompatActivity
        implements
        AnswerEditTestFragment.OnFragmentInteractionListener,
        AnswerEditTestRecyclerListFragment.OnFragmentInteractionListener,
        AnswerDataEditFragment.OnFragmentInteractionListener,
        ColumnEditTestFragment.OnFragmentInteractionListener,
        ColumnDataEditFragment.OnFragmentInteractionListener,
        ColumnEditTestRecyclerListFragment.OnFragmentInteractionListener,
        MainInformationEditTestFragment.OnFragmentInteractionListener {

    public static final String ACTION_BROADCAST_UPDATE_COLUMN_RECYCLER = "fr.pyjacpp.diakoluo.edit_test.UPDATE_COLUMN_RECYCLER";
    public static final String ACTION_BROADCAST_UPDATE_ANSWER_RECYCLER = "fr.pyjacpp.diakoluo.edit_test.UPDATE_ANSWER_RECYCLER";
    public static final String ACTION_BROADCAST_NEW_COLUMN_RECYCLER = "fr.pyjacpp.diakoluo.edit_test.NEW_COLUMN_RECYCLER";
    public static final String EXTRA_INT_POSITION = "position";

    private ArrayDeque<EditTestValidator> errorValidatorDeque;
    private boolean errorInDeque;

    private EditTestPagerAdapterFragment adapter;

    class EditTestValidator {
        private final boolean error;
        private final boolean warning;
        private final Integer errorMessageResourceId;

        EditTestValidator() {
            errorMessageResourceId = null;
            warning = false;
            error = false;
        }

        EditTestValidator(Integer errorMessageResourceId) {
            this.errorMessageResourceId = errorMessageResourceId;
            warning = false;
            error = true;
        }

        EditTestValidator(Integer errorMessageResourceId, boolean warning) {
            this.errorMessageResourceId = errorMessageResourceId;
            this.warning = warning;
            error = true;
        }

        Integer getErrorMessageResourceId() {
            return errorMessageResourceId;
        }

        boolean isError() {
            return error;
        }

        boolean isWarning() {
            return warning;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_test);

        TextView title = findViewById(R.id.title);
        ImageButton navigation = findViewById(R.id.navigationIcon);

        Test currentEditTest = DiakoluoApplication.getCurrentEditTest(this);
        title.setText(currentEditTest == null || currentEditTest.getName().equals("") ?
                getString(R.string.app_name) : currentEditTest.getName());
        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSupportNavigateUp();
            }
        });

        TabLayout tabLayout = findViewById(R.id.viewTestTabLayout);
        ViewPager viewPager = findViewById(R.id.viewTestViewPager);
        Button cancelButton = findViewById(R.id.cancelButton);
        Button validButton = findViewById(R.id.validButton);

        viewPager.setOffscreenPageLimit(2);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new EditTestPagerAdapterFragment(
                getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, this
        );
        viewPager.setAdapter(adapter);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        validButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Test editTest = DiakoluoApplication.getCurrentEditTest(EditTestActivity.this);

                errorValidatorDeque = new ArrayDeque<>();
                errorInDeque = false;

                saveInTestVar();

                EditTestValidator response = titleEditTestValidator(editTest.getName());
                addValidator(response);

                response = descriptionEditTestValidator(editTest.getDescription());
                addValidator(response);

                verifyAndAsk();
            }
        });

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateColumnRecyclerItem(intent.getIntExtra(EXTRA_INT_POSITION, 0));
            }
        }, new IntentFilter(ACTION_BROADCAST_UPDATE_COLUMN_RECYCLER));

        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateAnswerRecyclerItem(intent.getIntExtra(EXTRA_INT_POSITION, 0));
            }
        }, new IntentFilter(ACTION_BROADCAST_UPDATE_ANSWER_RECYCLER));

        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateNewColumn();
            }
        }, new IntentFilter(ACTION_BROADCAST_NEW_COLUMN_RECYCLER));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void addValidator(EditTestValidator testValidator) {
        if (testValidator.isError() && !testValidator.isWarning()) {
            errorInDeque = true;
            errorValidatorDeque.add(testValidator);
        } else if (testValidator.isWarning()) {
            errorValidatorDeque.add(testValidator);
        }
    }

    private void createModifyEditTest() {
        finish();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Test> listTest = DiakoluoApplication.getListTest(EditTestActivity.this);
                Test editTest = DiakoluoApplication.getCurrentEditTest(EditTestActivity.this);
                Integer currentIndex = DiakoluoApplication.getCurrentIndexEditTest(EditTestActivity.this);

                if (currentIndex == null) {
                    listTest.add(editTest);
                    DiakoluoApplication.setCurrentIndexEditTest(EditTestActivity.this, null);
                    RecyclerViewChange recyclerViewChange = new RecyclerViewChange(
                            RecyclerViewChange.ItemInserted
                    );
                    recyclerViewChange.setPosition(listTest.size() - 1);
                    DiakoluoApplication.setTestListChanged(EditTestActivity.this, recyclerViewChange);
                } else {
                    listTest.set(currentIndex, editTest);
                    DiakoluoApplication.setCurrentIndexEditTest(EditTestActivity.this, null);
                    RecyclerViewChange recyclerViewChange = new RecyclerViewChange(
                            RecyclerViewChange.ItemChanged
                    );
                    editTest.registerModificationDate();
                    recyclerViewChange.setPosition(currentIndex);
                    DiakoluoApplication.setTestListChanged(EditTestActivity.this, recyclerViewChange);
                }
                DiakoluoApplication.saveTest(EditTestActivity.this);
            }
        }).start();
    }

    private void saveInTestVar() {
        Test editTest = DiakoluoApplication.getCurrentEditTest(EditTestActivity.this);

        EditText title = findViewById(R.id.titleEditText);
        EditText description = findViewById(R.id.descriptionEditText);

        editTest.setName(title.getText().toString());
        editTest.setDescription(description.getText().toString());
    }

    private void verifyAndAsk() {
        if (errorValidatorDeque.isEmpty()) {
            if (!errorInDeque) createModifyEditTest();
        } else {
            EditTestValidator validator = errorValidatorDeque.pop();

            if (validator.isError() && !validator.isWarning()) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.error)
                        .setMessage(validator.getErrorMessageResourceId())
                        .setIcon(R.drawable.ic_error_red_24dp)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                verifyAndAsk();
                            }
                        })
                        .show();

            } else if (validator.isWarning() && !errorInDeque) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.warning)
                        .setMessage(validator.getErrorMessageResourceId())
                        .setIcon(R.drawable.ic_warning_yellow_24dp)
                        .setPositiveButton(R.string.continue_text, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                verifyAndAsk();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();

            } else {
                verifyAndAsk();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (DiakoluoApplication.getCurrentEditTest(EditTestActivity.this) != null)
            saveInTestVar();
    }

    @Override
    public EditTestValidator titleEditTestValidator(String text) {
        if (text.length() <= 0) {
            return new EditTestValidator(R.string.error_label_title_blank);
        } else {
            ArrayList<Test> listTest = DiakoluoApplication.getListTest(this);
            Integer currentIndexEditTest = DiakoluoApplication.getCurrentIndexEditTest(this);

            for (int i = 0; i < listTest.size(); i++) {
                Test test = listTest.get(i);

                if (test.getName().equalsIgnoreCase(text) && (currentIndexEditTest == null || currentIndexEditTest != i)) {

                    return new EditTestValidator(R.string.error_label_title_already_exist, true);
                }
            }
            return new EditTestValidator();
        }
    }

    @Override
    public EditTestValidator descriptionEditTestValidator(String text) {
        return new EditTestValidator();
    }

    @Override
    public void updateAnswerRecycler(final RecyclerViewChange recyclerViewChange) {
        Fragment answerEditTestFragment  = adapter.getFragmentAtPosition(2);

        if (answerEditTestFragment != null) {
            ((AnswerEditTestFragment) answerEditTestFragment).updateAnswerRecycler(recyclerViewChange);
        }
    }

    @Override
    public void updateAnswerRecyclerItem(int position) {
        RecyclerViewChange change = new RecyclerViewChange(RecyclerViewChange.ItemChanged);
        change.setPosition(position);
        updateAnswerRecycler(change);
    }

    @Override
    public void updateColumnRecyclerItem(final int position) {
        Fragment columnEditTestFragment  = adapter.getFragmentAtPosition(1);

        if (columnEditTestFragment != null) {
            ((ColumnEditTestFragment) columnEditTestFragment).updateItem(position);
        }
    }

    @Override
    public void onSwipeRight() {
    }

    @Override
    public void onSwipeLeft() {
    }

    private void updateNewColumn() {
        Fragment columnEditTestFragment  = adapter.getFragmentAtPosition(1);

        if (columnEditTestFragment != null) {
            ((ColumnEditTestFragment) columnEditTestFragment).updateNewItem(this);
        }
    }
}