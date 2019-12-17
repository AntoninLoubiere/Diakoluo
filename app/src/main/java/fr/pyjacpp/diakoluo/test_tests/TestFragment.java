package fr.pyjacpp.diakoluo.test_tests;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;

import fr.pyjacpp.diakoluo.DiakoluoApplication;
import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.tests.Column;
import fr.pyjacpp.diakoluo.tests.data.DataCell;
import fr.pyjacpp.diakoluo.tests.data.DataCellString;


public class TestFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private View inflatedView;
    private TestTestContext testTestContext;

    private HashMap<Column, LinearLayout> columnLinearLayoutHashMap = new HashMap<>();
    private HashMap<Column, View> columnViewHashMap = new HashMap<>();

    public TestFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.fragment_test, container, false);

        TextView testTitle = inflatedView.findViewById(R.id.testTitle);
        Button validButton = inflatedView.findViewById(R.id.validButton);
        LinearLayout linearLayout = inflatedView.findViewById(R.id.answerListLinearLayout);
        ProgressBar progressBar = inflatedView.findViewById(R.id.progressBar);

        LinearLayout.LayoutParams params = new TableRow.LayoutParams();
        params.topMargin = 24;

        testTitle.setText(testTestContext.getTest().getName());

        for (Column column : testTestContext.getTest().getListColumn()) {
            LinearLayout answerRow = new LinearLayout(inflatedView.getContext());
            answerRow.setOrientation(LinearLayout.HORIZONTAL);

            TextView columnName = new TextView(inflatedView.getContext());
            columnName.setTextSize(getResources().getDimension(R.dimen.textAnswerSize));
            columnName.setText(column.getName() + ":");

            columnName.setTextColor(getResources().getColor(R.color.colorAccent));

            linearLayout.addView(columnName, params);

            linearLayout.addView(answerRow);

            columnLinearLayoutHashMap.put(column, answerRow);
            columnViewHashMap.put(column, null);
        }

        validButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validButtonClick();
            }
        });

        progressBar.setMax(testTestContext.getListRowToAsk().size());

        updateAnswer();

        return inflatedView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        inflatedView = null;
    }

    private void updateAnswer() {
        if (inflatedView != null) {
            Button validButton = inflatedView.findViewById(R.id.validButton);
            ProgressBar progressBar = inflatedView.findViewById(R.id.progressBar);

            LinearLayout.LayoutParams params = new TableRow.LayoutParams();
            params.weight = 1;

            if (testTestContext.isAnswerGive()) {
                validButton.setText(R.string.continue_text);
                progressBar.setProgress(testTestContext.getCurrentIndex() + 1);

                for (Column column : testTestContext.getTest().getListColumn()) {
                    LinearLayout answerRow = columnLinearLayoutHashMap.get(column);

                    if (answerRow != null) {
                        answerRow.removeAllViews();

                        Boolean columnShow = testTestContext.getShowColumn().get(column);
                        DataCellString dataCellString = (DataCellString) testTestContext.getCurrentRow().getListCells().get(column);

                        if (columnShow != null && dataCellString != null) {
                            if (columnShow) {
                                addAnswer(column, answerRow, params);
                            } else {
                                Object answer = testTestContext.getUserAnswer().get(column);
                                if (answer != null) {
                                    switch (column.getInputType()) {
                                        case String:
                                            String answerS = (String) answer;

                                            TextView answerTextView = new TextView(inflatedView.getContext());

                                            answerRow.addView(answerTextView, params);

                                            if (answerS.length() <= 0) {
                                                answerS = getString(R.string.skip);

                                                answerTextView.setTextColor(getResources().getColor(
                                                        R.color.wrongAnswer
                                                ));

                                                answerTextView.setTypeface(null, Typeface.ITALIC);
                                                addAnswer(column, answerRow, params);

                                            } else if (dataCellString.getValue().equalsIgnoreCase(answerS)) {
                                                answerTextView.setTextColor(getResources().getColor(
                                                        R.color.trueAnswer
                                                ));

                                                testTestContext.addScore(1);

                                            } else {
                                                answerTextView.setTextColor(getResources().getColor(
                                                        R.color.wrongAnswer
                                                ));
                                                answerTextView.setPaintFlags(answerTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                                addAnswer(column, answerRow, params);
                                            }

                                            answerTextView.setTextSize(getResources().getDimension(R.dimen.textAnswerSize));
                                            answerTextView.setText(answerS);

                                            columnViewHashMap.put(column, answerTextView);
                                            break;
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                validButton.setText(R.string.valid);
                progressBar.setProgress(testTestContext.getCurrentIndex());

                for (Column column : testTestContext.getTest().getListColumn()) {
                    LinearLayout answerRow = columnLinearLayoutHashMap.get(column);

                    if (answerRow != null) {
                        answerRow.removeAllViews();


                        Boolean columnShow = testTestContext.getShowColumn().get(column);
                        if (columnShow != null && columnShow) {
                            addAnswer(column, answerRow, params);
                        } else {
                            switch (column.getInputType()) {
                                case String:
                                    EditText answer = new EditText(inflatedView.getContext());
                                    answer.setTextSize(getResources().getDimension(R.dimen.textAnswerSize));
                                    answer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
                                    answer.setHint(column.getName());

                                    answerRow.addView(answer, params);

                                    columnViewHashMap.put(column, answer);
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void addAnswer(Column column, LinearLayout row, LinearLayout.LayoutParams params) {
        switch (column.getInputType()) {
            case String:
                TextView answer = new TextView(inflatedView.getContext());
                answer.setTextSize(getResources().getDimension(R.dimen.textAnswerSize));

                DataCellString dataCellString = (DataCellString) testTestContext.getCurrentRow().getListCells().get(column);
                if (dataCellString != null)
                    answer.setText(dataCellString.getValue());

                answer.setTextColor(getResources().getColor(R.color.primaryText));

                row.addView(answer, params);

                columnViewHashMap.put(column, answer);
                break;
        }
    }

    private void validButtonClick() {
        if (testTestContext.isAnswerGive()) {
            if (testTestContext.incrementCurrentIndex()) {
                testTestContext.selectShowColumn();
                testTestContext.removeUserInput();
                testTestContext.setAnswerGive(false);
                updateAnswer();
            } else {
                mListener.showScore(testTestContext);
            }
        } else {
            saveAnswer();
            testTestContext.setAnswerGive(true);
            updateAnswer();
        }
    }

    private void saveAnswer() {
        for (Column column : testTestContext.getTest().getListColumn()) {
            View columnView = columnViewHashMap.get(column);
            DataCell currentCell = testTestContext.getCurrentRow().getListCells().get(column);
            Boolean columnShow = testTestContext.getShowColumn().get(column);

            if (columnShow != null && columnView != null && currentCell != null && !columnShow) {
                switch (column.getInputType()) {
                    case String:
                        EditText editText = (EditText) columnView;
                        testTestContext.getUserAnswer().put(column, editText.getText().toString());
                        break;
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!testTestContext.isAnswerGive()) {
            saveAnswer();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        testTestContext = DiakoluoApplication.getTestTestContext(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void showScore(TestTestContext context);
    }
}
