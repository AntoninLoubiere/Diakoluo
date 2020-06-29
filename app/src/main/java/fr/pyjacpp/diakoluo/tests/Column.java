package fr.pyjacpp.diakoluo.tests;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import fr.pyjacpp.diakoluo.R;

public class Column {
    private String name;
    private String description;

    private ColumnInputType inputType;
    private Object defaultValue;

    public Column() {
        this.name = null;
        this.description = null;
        this.inputType = null;
        this.defaultValue = null;
    }

    public Column(String name, String description, ColumnInputType inputType) {
        this.name = name;
        this.description = description;
        this.inputType = inputType;
        this.defaultValue = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ColumnInputType getInputType() {
        return inputType;
    }

    public void setInputType(ColumnInputType inputType) {
        this.inputType = inputType;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isValid() {
        return !(name == null ||
                description == null ||
                inputType == null ||
                defaultValue == null);
    }

    public View showColumnName(Context context) {
        TextView columnNameTextView = new TextView(context);
        columnNameTextView.setTextAppearance(context, R.style.BoldHeadline5);
        columnNameTextView.setText(context.getString(R.string.column_name_format, name));
        return columnNameTextView;
    }

    private TextInputLayout showColumnEditValue(Context context) {
        TextInputLayout inputLayout = new TextInputLayout(context, null, R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox);
        TextInputEditText inputField = new TextInputEditText(context);
        inputLayout.setHint(name);

        inputLayout.addView(inputField);

        return inputLayout;
    }


    public TextInputLayout showColumnEditValue(Context context, Object defaultValue) {
        TextInputLayout inputLayout = showColumnEditValue(context);
        EditText inputField = inputLayout.getEditText();
        if (inputField != null && defaultValue != null)
            inputField.setText((String) defaultValue);

        return inputLayout;
    }
}
