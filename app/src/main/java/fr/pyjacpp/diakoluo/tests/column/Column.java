package fr.pyjacpp.diakoluo.tests.column;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.OutputStream;

import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.save_test.FileManager;
import fr.pyjacpp.diakoluo.save_test.XmlLoader;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;

public abstract class Column {
    private String name;
    private String description;

    ColumnInputType inputType;
    protected Column() {
        initialize();
    }

    protected Column(XmlPullParser parser) throws IOException, XmlPullParserException {
        initialize();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                // continue until it is a start tag
                continue;
            }

            readColumnXmlTag(parser);
        }
    }

    void initialize() {
        this.name = null;
        this.description = null;
        this.inputType = null;
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

    public abstract Object getDefaultValue();

    public abstract void setDefaultValue(Object defaultValue);

    public abstract void initializeDefaultValue();

    public boolean isValid() {
        return !(name == null ||
                description == null ||
                inputType == null);
    }

    public void writeXmlHeader(OutputStream fileOutputStream) throws IOException {
        throw new RuntimeException("Not implemented");
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

    void readColumnXmlTag(XmlPullParser parser) throws IOException, XmlPullParserException {
        switch (parser.getName()) {
            case FileManager.TAG_NAME:
                name = XmlLoader.readName(parser);
                break;

            case FileManager.TAG_DESCRIPTION:
                description = XmlLoader.readDescription(parser);
                break;

            default:
                XmlLoader.skip(parser);
                break;
        }
    }

    public static Column newColumn(ColumnInputType columnInputType) {
        return newColumn(columnInputType, "", "");
    }

    public static Column newColumn(ColumnInputType columnInputType, String name, String description) {
        switch (columnInputType) {
            case String:
                ColumnString columnString = new ColumnString();
                columnString.setName(name);
                columnString.setDescription(description);
                columnString.initializeDefaultValue();
                return columnString;

            default:
                throw new IllegalStateException("Unexpected value: " + columnInputType);
        }
    }

    static void privateCopyColumn(Column baseColumn, Column newColumn) {
        newColumn.name = baseColumn.name;
        newColumn.description = baseColumn.description;
        newColumn.inputType = baseColumn.inputType;
    }

    public static Column readColumnXml(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, FileManager.TAG_COLUMN);
        String attributeValue = parser.getAttributeValue(null, FileManager.ATTRIBUTE_INPUT_TYPE);
        if (attributeValue == null) {
            // old version
            return XmlLoader.readColumnXml(parser);
        } else {
            ColumnInputType columnInputType = ColumnInputType.get(attributeValue);
            if (columnInputType == null) {
                throw new XmlPullParserException("Column input type error");
            } else {
                Column column;
                switch (columnInputType) {
                    case String:
                        column = new ColumnString(parser);
                        break;

                    default:
                        throw new IllegalStateException("InputType " + columnInputType.name() + " not implemented");
                }
                if (column.isValid()) {
                    return column;
                } else {
                    return null;
                }
            }
        }
    }

    public static Column copyColumn(Column column) {
        if (column instanceof ColumnString) {
            return ColumnString.privateCopyColumn((ColumnString) column);
        } else {
            throw new IllegalStateException("Column type not detected");
        }
    }
}
