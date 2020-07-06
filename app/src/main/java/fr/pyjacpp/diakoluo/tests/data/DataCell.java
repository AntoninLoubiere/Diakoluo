package fr.pyjacpp.diakoluo.tests.data;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.io.IOException;
import java.io.OutputStream;

import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.test_tests.TestTestContext;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;
import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.column.Column;

public abstract class DataCell {
    public DataCell(DataCell dataCell) {
    }

    public DataCell() {
    }

    public static DataCell copyDataCell(DataCell dataCell) {
        if (dataCell instanceof DataCellString) {
            return new DataCellString((DataCellString) dataCell);
        } else {
            throw new IllegalStateException("Unexpected value: " + dataCell.getClass());
        }
    }

    public static DataCell getDefaultValueCell(Column currentColumn) {
        switch (currentColumn.getInputType()) {
            case String:
                return new DataCellString((String) currentColumn.getDefaultValue());

            default:
                throw new IllegalStateException("Unexpected value: " + currentColumn.getInputType());
        }
    }

    public static void setDefaultCellFromView(View view, DataRow row, Column column) {
        row.getListCells().put(column, getDefaultValueCell(column));
    }

    public abstract Object getValue();

    public static Class<? extends DataCell> getClassByColumnType(ColumnInputType inputType) {
        switch (inputType) {
            case String:
                return DataCellString.class;

            default:
                throw new RuntimeException("Unknown inputType !");
        }
    }

    public abstract void setValue(Object object);

    public View showValue(Context context) {
        MaterialTextView valueTextView = new MaterialTextView(context);
        valueTextView.setTextAppearance(context, R.style.Body0);
        valueTextView.setText(getStringValue());
        return valueTextView;
    }

    public ShowValueResponse showValue(Context context, Object answer) {
        String answerString = getStringFromObjectValue(answer);

        MaterialTextView valueTextView = (MaterialTextView) showValue(context);
        boolean answerIsTrue = verifyAnswer(answer);

        if (answerIsTrue) {
            valueTextView.setTextColor(context.getResources().getColor(R.color.trueAnswer));
        } else {
            if (answerString == null || answerString.length() <= 0)
                valueTextView.setText(R.string.skip);

            valueTextView.setTextColor(context.getResources().getColor(R.color.wrongAnswer));
            valueTextView.setPaintFlags(valueTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        return new ShowValueResponse(valueTextView, answerIsTrue);
    }

    public TextInputLayout showEditValue(Context context, Column column) {
        return column.showColumnEditValue(context, getStringValue());
    }

    private String getStringFromObjectValue(Object answer) {
        return (String) answer;
    }

    public abstract String getStringValue();

    public void setValueFromView(View view) {
        setValue(getValueFromView(view));
    }

    public Object getValueFromView(View view) {
        TextInputLayout inputLayout = (TextInputLayout) view;
        EditText editText = inputLayout.getEditText();
        if (editText != null)
            return editText.getText().toString();
        else
            return null;
    }

    public boolean verifyAnswer(Object answer) {
        return answer == getValue();
    }

    public void verifyAndScoreAnswer(TestTestContext testTestContext, Object answer) {
        if (verifyAnswer(answer))
            testTestContext.addScore(1);
    }

    public abstract void setValueFromCsv(String lineCell);

    public abstract void writeXml(OutputStream fileOutputStream) throws IOException;

    public class ShowValueResponse {
        ShowValueResponse(View valueView, boolean answerIsTrue) {
            this.valueView = valueView;
            this.answerIsTrue = answerIsTrue;
        }

        View valueView;
        boolean answerIsTrue;

        public View getValueView() {
            return valueView;
        }

        public boolean isAnswerTrue() {
            return answerIsTrue;
        }
    }
}