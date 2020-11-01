/*
 * Copyright (c) 2020 LOUBIERE Antonin <https://www.github.com/AntoninLoubiere/>
 *
 * This file is part of Diakôluô project <https://www.github.com/AntoninLoubiere/Diakoluo/>.
 *
 *     Diakôluô is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Diakôluô is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     A copy of the license is available in the root folder of Diakôluô, under the
 *     name of LICENSE.md. You could find it also at <https://www.gnu.org/licenses/gpl-3.0.html>.
 */

package fr.pyjacpp.diakoluo.tests.column;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.Utils;
import fr.pyjacpp.diakoluo.ViewUtils;
import fr.pyjacpp.diakoluo.save_test.FileManager;
import fr.pyjacpp.diakoluo.save_test.XmlLoader;
import fr.pyjacpp.diakoluo.save_test.XmlSaver;
import fr.pyjacpp.diakoluo.test_tests.TestTestContext;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;
import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.tests.data.AnswerValidEnum;
import fr.pyjacpp.diakoluo.tests.data.DataCell;
import fr.pyjacpp.diakoluo.tests.score.Rule;
import fr.pyjacpp.diakoluo.tests.score.ScoreColumn;
import fr.pyjacpp.diakoluo.tests.score.action.base.SetAction;
import fr.pyjacpp.diakoluo.tests.score.condition.base.TrueCondition;

/**
 * A column class that hold parameters for cells, like type, and column specific settings
 *
 * @see DataCell
 */
public abstract class Column {
    /**
     * Settings 1 << 0 to 1 << 1 (included) are reserved
     * SET_DEFAULT should be | with the SET_DEFAULT of the child class
     */
    public static final int SET_CAN_BE_HIDE = 1;
    public static final int SET_CAN_BE_SHOW = 1 << 1;

    public static final float SCORE_RIGHT_DEFAULT = 1;
    public static final float SCORE_WRONG_DEFAULT = 0;
    public static final float SCORE_SKIPPED_DEFAULT = 0;

    protected static final int SET_DEFAULT = SET_CAN_BE_HIDE | SET_CAN_BE_SHOW;

    // if a field is added, all methods with a comment "// fields" should be verified
    @NonNull
    protected ColumnInputType inputType;
    protected int settings = -1;
    @Nullable
    private String name;
    @Nullable
    private String description;
    private ScoreColumn scoreRule;
    private float scoreRight;
    private float scoreWrong;
    private float scoreSkipped;

    /**
     * Default constructor that initialize a non-valid column.
     *
     * @param inputType the input type of the column
     */
    protected Column(@NonNull ColumnInputType inputType) {
        initialize();
        this.inputType = inputType;
    }

    /**
     * Default constructor that initialize a valid column with empty (not null !) name and
     * description.
     *
     * @param columnInputType the input type of the column
     */
    public static Column newColumn(ColumnInputType columnInputType) {
        return newColumn(columnInputType, "", "");
    }

    /**
     * Default constructor that initialize a valid column (or not if name and description are null)
     *
     * @param columnInputType the input type of the column
     * @param name            the name of the column
     * @param description     the description of the column
     */
    public static Column newColumn(ColumnInputType columnInputType, String name,
                                   String description) {
        Column column;
        switch (columnInputType) {
            case String:
                column = new ColumnString();
                break;

            case List:
                column = new ColumnList();
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + columnInputType);
        }
        if (name != null || description != null) column.initialize(name, description);
        return column;
    }

    /**
     * Create a new column from a xml file.
     *
     * @param parser      the XmlPullParser of the file
     * @param fileVersion the version of the file
     * @return the new column
     * @throws XmlPullParserException if while reading the file an exception occur
     * @throws IOException            if while reading the file an exception occur
     */
    public static Column readColumnXml(XmlPullParser parser, int fileVersion)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, FileManager.TAG_COLUMN);
        String attributeValue = parser.getAttributeValue(null,
                FileManager.ATTRIBUTE_INPUT_TYPE);
        if (attributeValue == null) {
            // old version
            return XmlLoader.readColumnXml(parser);
        } else {
            ColumnInputType columnInputType = ColumnInputType.get(attributeValue);
            if (columnInputType == null) {
                throw new XmlPullParserException("Column input type error");
            } else {
                Column column = Column.newColumn(columnInputType, null, null);

                column.loopXmlTags(parser);
                column.setDefaultValueBackWardCompatibility(fileVersion);
                if (column.isValid()) {
                    return column;
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Read columns from xml file.
     *
     * @return the list of columns loaded
     */
    public static ArrayList<Column> readXmlColumns(XmlPullParser parser, int fileVersion)
            throws IOException, XmlPullParserException {
        ArrayList<Column> columns = new ArrayList<>();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                // continue until it is a start tag
                continue;
            }

            if (parser.getName().equals(FileManager.TAG_COLUMN)) {
                Column column = Column.readColumnXml(parser, fileVersion);
                if (column == null) {
                    Log.w(XmlLoader.TAG, "Column isn't valid !");
                } else {
                    columns.add(column);
                }
            } else {
                XmlLoader.skip(parser);
            }
        }
        return columns;
    }

    /**
     * Get the default value of a column, type depend of column.
     *
     * @return the default value
     */
    public abstract Object getDefaultValue();

    /**
     * Set the default value of a column, type depend of column.
     *
     * @param defaultValue the default value to set
     */
    public abstract void setDefaultValue(Object defaultValue);

    /**
     * Verify if a answer inputted by the user is the same that the value stored.
     *
     * @param dataCell the DataCell that hold the value
     * @param answer   the value inputted by the user
     * @return if the a
     */
    public abstract AnswerValidEnum verifyAnswer(DataCell dataCell, Object answer);

    /**
     * Copy the column.
     *
     * @return the column copied
     */
    public abstract Column copyColumn();

    /**
     * Copy column fields of all levels.
     *
     * @param newColumn the column instance
     */
    protected void copyColumn(Column newColumn) {
        // fields
        newColumn.name = name;
        newColumn.description = description;
        newColumn.inputType = inputType;
        newColumn.settings = settings;
        newColumn.scoreRight = scoreRight;
        newColumn.scoreWrong = scoreWrong;
        newColumn.scoreSkipped = scoreSkipped;
        newColumn.scoreRule = scoreRule;
    }

    /**
     * Initialize a non-valid column. Inverse of {@link #initialize(String, String)}
     *
     * @see #initialize(String, String)
     */
    public void initialize() {
        // fields
        this.name = null;
        this.description = null;
        settings = -1;
        scoreRight = SCORE_RIGHT_DEFAULT;
        scoreWrong = SCORE_WRONG_DEFAULT;
        scoreSkipped = SCORE_SKIPPED_DEFAULT;
        scoreRule = null;
    }

    /**
     * Initialize a valid column. Inverse of {@link #initialize()}.
     *
     * @param name        the name of the column
     * @param description the description of the column
     * @see #initialize()
     */
    protected void initialize(String name, String description) {
        // fields
        this.name = name;
        this.description = description;
        settings = SET_DEFAULT;
        scoreRight = SCORE_RIGHT_DEFAULT;
        scoreWrong = SCORE_WRONG_DEFAULT;
        scoreSkipped = SCORE_SKIPPED_DEFAULT;
        scoreRule = getDefaultScoreColumn();
    }

    /**
     * Get the default score column of the column.
     *
     * @return the default score column
     */
    protected ScoreColumn getDefaultScoreColumn() {
        ArrayList<Rule> rules = new ArrayList<>();
        rules.add(new Rule(new TrueCondition(), new SetAction(0f)));
        return new ScoreColumn(rules, 1f);
    }

    /**
     * Get the name of the column.
     *
     * @return the name of the column
     */
    @Nullable
    public String getName() {
        return name;
    }

    /**
     * Set the name of the column.
     *
     * @param name the new name of the column
     */
    public void setName(@Nullable String name) {
        this.name = name;
    }

    /**
     * Get the description of the column.
     *
     * @return the description of the file
     */
    @Nullable
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the file.
     *
     * @param description the description of the file
     */
    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    /**
     * Get the score of the column
     *
     * @return the score of the column
     */
    public float getScoreRight() {
        return scoreRight;
    }

    /**
     * Get the input type of the file
     *
     * @return the input type of the file
     */
    @NonNull
    public ColumnInputType getInputType() {
        return inputType;
    }

    /**
     * Get if the column is a valid column.
     *
     * @return if the column is valid
     */
    public boolean isValid() {
        return !(name == null || description == null) && settings >= 0;
    }

    /**
     * Verify if the answer is correct and give score depending
     *
     * @param testTestContext the test context
     * @param dataCell        the dataCell to verify
     * @param answer          the answer given by the user
     */
    public void verifyAndScoreAnswer(TestTestContext testTestContext, DataCell dataCell,
                                     Object answer) {
        AnswerValidEnum verifyAnswer = verifyAnswer(dataCell, answer);
        if (verifyAnswer == AnswerValidEnum.RIGHT) {
            testTestContext.addScore(scoreRight, scoreRight);
        } else if (verifyAnswer == AnswerValidEnum.SKIPPED) {
            testTestContext.addScore(scoreSkipped, scoreRight);
        } else {
            testTestContext.addScore(scoreWrong, scoreRight);
        }
    }

    /**
     * Get the view who show the column name.
     *
     * @param context the context to create widgets
     * @return the view to add to show the column name
     */
    public View showColumnName(Context context) {
        TextView columnNameTextView = new TextView(context);
        columnNameTextView.setTextAppearance(context, R.style.BoldHeadline5);
        columnNameTextView.setText(context.getString(R.string.column_name_format, name));
        return columnNameTextView;
    }

    /**
     * Show the value to the user (view only).
     *
     * @param context  the context to show the value cell
     * @param dataCell the dataCell to show
     * @return the view which contain the value
     * @see Column#showViewValueView(Context, DataCell, Object)
     */
    @NonNull
    public View showViewValueView(Context context, DataCell dataCell) {
        MaterialTextView valueTextView = new MaterialTextView(context);
        valueTextView.setTextAppearance(context, R.style.Body0);
        valueTextView.setText(dataCell.getStringValue(context, this));
        return valueTextView;
    }

    /**
     * Show the value formatted given by the user in test. Show the value stroked if the user has
     * wrong, green or red...
     *
     * @param context  the context to show the value cell
     * @param dataCell the dataCell to show
     * @param answer   the answer of the user
     * @return the view which contain the value of the user
     * @see #showViewValueView(Context, DataCell)
     */
    public ShowValueResponse showViewValueView(Context context, DataCell dataCell, Object answer) {
        MaterialTextView valueTextView = (MaterialTextView) showViewValueView(context, dataCell);
        AnswerValidEnum answerValid = verifyAnswer(dataCell, answer);


        if (answerValid == AnswerValidEnum.RIGHT) {
            valueTextView.setTextColor(context.getResources().getColor(R.color.answer_right));
        } else if (answerValid == AnswerValidEnum.SKIPPED) {
            valueTextView.setText(R.string.skip);
            valueTextView.setTypeface(null, Typeface.ITALIC);
            valueTextView.setTextColor(context.getResources().getColor(R.color.answer_skipped));
        } else {
            valueTextView.setPaintFlags(valueTextView.getPaintFlags() |
                    Paint.STRIKE_THRU_TEXT_FLAG);
            valueTextView.setText(dataCell.getStringValue(context, this, answer));
            valueTextView.setTextColor(context.getResources().getColor(R.color.answer_wrong));
        }

        return new ShowValueResponse(valueTextView, answerValid);
    }

    /**
     * Get the view to answer the data cell (type depend on the column).
     * If override, {@link #getValueFromView(View)} may need to be override.
     *
     * @param context      the context to create widgets
     * @param defaultValue the default value of the input, can be null
     * @return the view to add to show the edit input
     * @see #getValueFromView(View)
     */
    public View showEditValueView(Context context, @Nullable Object defaultValue) {
        TextInputLayout inputLayout = new TextInputLayout(context, null,
                R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox);
        TextInputEditText inputField = new TextInputEditText(context);
        inputLayout.setHint(name);
        inputLayout.addView(inputField);
        inputField.setOnFocusChangeListener(ViewUtils.TRANSFER_TO_PARENT_FOCUS_LISTENER);

        if (defaultValue != null)
            inputField.setText((String) defaultValue);

        return inputLayout;
    }

    /**
     * Get the value of the data cell from a view (type depend on the column).
     * If override, {@link #showEditValueView(Context, Object)} may need to be override too
     *
     * @param view the view which contain the value of the cell
     * @return the value in the view (type variable)
     * @see #setValueFromView(DataCell, View)
     * @see #showEditValueView(Context, Object)
     */
    public Object getValueFromView(View view) {
        TextInputLayout inputLayout = (TextInputLayout) view;
        EditText editText = inputLayout.getEditText();
        if (editText != null)
            return editText.getText().toString();
        else
            return null;
    }

    /**
     * Get the view to answer the data cell (type depend on the column) in a test.
     * The view should have the possibility to skip.
     * If override, {@link #getValueFromTestView(View)} may need to be override.
     *
     * @param context      the context to create widgets
     * @param defaultValue the default value of the input, can be null
     * @return the view to add to show the edit input
     * @see #getValueFromTestView(View)
     */
    public View showEditValueTestView(Context context, @Nullable Object defaultValue) {
        return showEditValueView(context, defaultValue);
    }

    /**
     * Get the value of the data cell from a view (type depend on the column).
     * If override, {@link #showEditValueTestView(Context, Object)} may need to be override too
     *
     * @param view the view which contain the value of the cell
     * @return the value in the view (type variable)
     * @see #setValueFromView(DataCell, View)
     * @see #showEditValueTestView(Context, Object)
     */
    public Object getValueFromTestView(View view) {
        return getValueFromView(view);
    }

    /**
     * Set the value from a view.
     *
     * @param dataCell the data cell which will contain the value
     * @param view     the view which contain the value
     * @see #getValueFromView(View)
     * @see #showEditValueView(Context, Object)
     */
    public void setValueFromView(DataCell dataCell, View view) {
        dataCell.setValue(getValueFromView(view));
    }

    /**
     * Set the settings view of the column.
     * Override method must add to root the view and must call the super method
     *
     * @param layoutInflater a layout inflater to inflate the layout
     * @param parent         the parent which receive the inflated layout
     * @see #getEditColumnSettings(LayoutInflater, ViewGroup)
     * @see #setEditColumnSettings(ViewGroup)
     */
    public void getViewColumnSettings(LayoutInflater layoutInflater, ViewGroup parent) {
        View inflatedView = layoutInflater.inflate(R.layout.fragment_column_settings_view_default,
                parent, true);

        Context context = parent.getContext();

        MaterialTextView scoreTextView =
                inflatedView.findViewById(R.id.scoreTextView);
        MaterialTextView canBeHideTextView =
                inflatedView.findViewById(R.id.canBeHideTextView);
        MaterialTextView canBeShowTextView =
                inflatedView.findViewById(R.id.canBeShowTextView);

        scoreTextView.setText(context.getString(R.string.column_settings_score_view,
                Utils.formatScore(scoreRight),
                Utils.formatScore(scoreSkipped),
                Utils.formatScore(scoreWrong)));

        ViewUtils.setBooleanView(context,
                canBeHideTextView, isInSettings(SET_CAN_BE_HIDE));
        ViewUtils.setBooleanView(context,
                canBeShowTextView, isInSettings(SET_CAN_BE_SHOW));
    }

    /**
     * Set and return the edit settings view of the column
     * Override method must add to root the view.
     * Params are updated by {@link #setEditColumnSettings}.
     *
     * @param layoutInflater a layout inflater to inflate the layout
     * @param parent         the parent which receive the inflated layout
     * @see #getViewColumnSettings(LayoutInflater, ViewGroup)
     * @see #setEditColumnSettings(ViewGroup)
     */
    public void getEditColumnSettings(LayoutInflater layoutInflater, ViewGroup parent) {
        View inflatedView =
                layoutInflater.inflate(R.layout.fragment_column_settings_edit_default, parent, true);

        final Context context = parent.getContext();

        final TextInputEditText scoreRightInputEditText =
                inflatedView.findViewById(R.id.scoreRightEditText);
        final TextInputEditText scoreWrongInputEditText =
                inflatedView.findViewById(R.id.scoreWrongEditText);
        final TextInputEditText scoreSkippedInputEditText =
                inflatedView.findViewById(R.id.scoreSkippedEditText);
        MaterialCheckBox canBeHideTextView =
                inflatedView.findViewById(R.id.canBeHideCheckBox);
        MaterialCheckBox canBeShowTextView =
                inflatedView.findViewById(R.id.canBeShowCheckBox);

        boolean canHide = isInSettings(SET_CAN_BE_HIDE);

        scoreRightInputEditText.setText(String.valueOf(scoreRight));
        scoreRightInputEditText.setEnabled(canHide);
        scoreWrongInputEditText.setText(String.valueOf(scoreWrong));
        scoreWrongInputEditText.setEnabled(canHide);
        scoreSkippedInputEditText.setText(String.valueOf(scoreSkipped));
        scoreSkippedInputEditText.setEnabled(canHide);

        canBeHideTextView.setChecked(canHide);
        canBeShowTextView.setChecked(isInSettings(SET_CAN_BE_SHOW));

        final TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                boolean[] scoresErrors = {false, false, false};
                try {
                    scoreRight = Float.parseFloat(scoreRightInputEditText.getEditableText().toString());
                } catch (NumberFormatException ignored) {
                    scoreRightInputEditText.setError(context.getString(R.string.number_required));
                    scoreRight = SCORE_RIGHT_DEFAULT;
                    scoresErrors[0] = true;
                }

                try {
                    scoreWrong = Float.parseFloat(scoreWrongInputEditText.getEditableText().toString());
                } catch (NumberFormatException ignored) {
                    scoreWrongInputEditText.setError(context.getString(R.string.number_required));
                    scoreWrong = SCORE_WRONG_DEFAULT;
                    scoresErrors[1] = true;
                }

                try {
                    scoreSkipped = Float.parseFloat(scoreSkippedInputEditText.getEditableText().toString());
                } catch (NumberFormatException ignored) {
                    scoreSkippedInputEditText.setError(context.getString(R.string.number_required));
                    scoreSkipped = SCORE_SKIPPED_DEFAULT;
                    scoresErrors[2] = true;
                }

                if (!scoresErrors[0] && scoreRight <= scoreSkipped) {
                    scoreRightInputEditText.setError(
                            context.getString(R.string.score_right_lesser_skipped));
                } else if (!scoresErrors[0]) {
                    scoreRightInputEditText.setError(null);
                }
                if (!scoresErrors[1] && scoreWrong >= scoreRight) {
                    scoreWrongInputEditText.setError(
                            context.getString(R.string.score_wrong_greater_right));
                } else if (!scoresErrors[1]) {
                    scoreWrongInputEditText.setError(null);
                }
                if (!scoresErrors[2] && scoreSkipped < scoreWrong) {
                    scoreSkippedInputEditText.setError(
                            context.getString(R.string.score_skipped_lesser_wrong));
                } else if (!scoresErrors[2]) {
                    scoreSkippedInputEditText.setError(null);
                }
            }
        };


        canBeHideTextView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                scoreRightInputEditText.setEnabled(b);
                scoreWrongInputEditText.setEnabled(b);
                scoreSkippedInputEditText.setEnabled(b);
                if (b) {
                    watcher.afterTextChanged(scoreRightInputEditText.getEditableText());
                } else {
                    scoreRightInputEditText.setError(null);
                    scoreWrongInputEditText.setError(null);
                    scoreSkippedInputEditText.setError(null);
                }
            }
        });
        scoreRightInputEditText.addTextChangedListener(watcher);
        scoreWrongInputEditText.addTextChangedListener(watcher);
        scoreSkippedInputEditText.addTextChangedListener(watcher);

        if (canHide) watcher.afterTextChanged(scoreRightInputEditText.getEditableText());
    }

    /**
     * Set column params from view. Params inputted by the user are update there.
     *
     * @param parent the parent which contain inflated layouts (generated from
     *               {@link #getEditColumnSettings(LayoutInflater, ViewGroup)}).
     * @see #getViewColumnSettings(LayoutInflater, ViewGroup)
     * @see #getEditColumnSettings(LayoutInflater, ViewGroup)
     */
    public void setEditColumnSettings(ViewGroup parent) {
        TextInputEditText scoreRightInputEditText =
                parent.findViewById(R.id.scoreRightEditText);
        TextInputEditText scoreWrongInputEditText =
                parent.findViewById(R.id.scoreWrongEditText);
        TextInputEditText scoreSkippedInputEditText =
                parent.findViewById(R.id.scoreSkippedEditText);
        MaterialCheckBox canBeHideTextView =
                parent.findViewById(R.id.canBeHideCheckBox);
        MaterialCheckBox canBeShowTextView =
                parent.findViewById(R.id.canBeShowCheckBox);

        scoreRight = ViewUtils.getFloatFromEditText(scoreRightInputEditText, SCORE_RIGHT_DEFAULT);
        scoreWrong = ViewUtils.getFloatFromEditText(scoreWrongInputEditText, SCORE_WRONG_DEFAULT);
        scoreSkipped = ViewUtils.getFloatFromEditText(scoreSkippedInputEditText, SCORE_SKIPPED_DEFAULT);

        setSettings(SET_CAN_BE_HIDE, canBeHideTextView.isChecked());
        setSettings(SET_CAN_BE_SHOW, canBeShowTextView.isChecked());
    }

    /**
     * If a settings is true
     *
     * @param parameter the settings wanted
     * @return if the settings is true
     */
    public boolean isInSettings(int parameter) {
        return (settings & parameter) == parameter;
    }

    /**
     * Set the settings to a value
     *
     * @param parameter the settings to set
     * @param value     the value of the settings
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    public void setSettings(int parameter, boolean value) {
        if (value) {
            settings = settings | parameter;
        } else {
            settings = settings & ~parameter;
        }
    }

    /**
     * Write the column into a xml file.
     * This method should be call by {@link #writeXml(OutputStream)} only.
     * If override you should call the super.
     *
     * @param fileOutputStream the FileOutputStream of the xml file
     * @throws IOException if while writing the file an error occur
     * @see #writeXml(OutputStream)
     */
    protected void writeXmlInternal(OutputStream fileOutputStream) throws IOException {
        // fields
        XmlSaver.writeData(fileOutputStream, FileManager.TAG_NAME, name);
        XmlSaver.writeData(fileOutputStream, FileManager.TAG_DESCRIPTION, description);
        XmlSaver.writeData(fileOutputStream, FileManager.TAG_SETTINGS, settings);
        XmlSaver.writeData(fileOutputStream, FileManager.TAG_SCORE_RIGHT, scoreRight);
        XmlSaver.writeData(fileOutputStream, FileManager.TAG_SCORE_WRONG, scoreWrong);
        XmlSaver.writeData(fileOutputStream, FileManager.TAG_SCORE_SKIPPED, scoreSkipped);
        scoreRule.writeXml(fileOutputStream);
    }

    /**
     * Write the column into a xml file.
     * Do not override this method. Override {@link #writeXmlInternal(OutputStream)} instead
     *
     * @param fileOutputStream the FileOutputStream of the xml file
     * @throws IOException if while writing the file an error occur
     * @see #writeXmlInternal(OutputStream)
     */
    public void writeXml(OutputStream fileOutputStream) throws IOException {
        XmlSaver.writeStartBeacon(fileOutputStream, FileManager.TAG_COLUMN,
                FileManager.ATTRIBUTE_INPUT_TYPE, inputType.name());
        writeXmlInternal(fileOutputStream);
        XmlSaver.writeEndBeacon(fileOutputStream, FileManager.TAG_COLUMN);
    }

    /**
     * Get columns params from a xml file.
     *
     * @param parser the parser of the xml file
     * @throws IOException            if an error occur while the file reading the file
     * @throws XmlPullParserException if an error occur while reading the file
     */
    protected void readColumnXmlTag(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        // fields
        switch (parser.getName()) {
            case FileManager.TAG_NAME:
                name = XmlLoader.readText(parser);
                break;

            case FileManager.TAG_DESCRIPTION:
                description = XmlLoader.readText(parser);
                break;

            case FileManager.TAG_SETTINGS:
                settings = XmlLoader.readInt(parser);
                break;

            case FileManager.TAG_SCORE_RIGHT:
                scoreRight = XmlLoader.readFloat(parser, SCORE_RIGHT_DEFAULT);
                break;

            case FileManager.TAG_SCORE_WRONG:
                scoreWrong = XmlLoader.readFloat(parser, SCORE_WRONG_DEFAULT);
                break;

            case FileManager.TAG_SCORE_SKIPPED:
                scoreSkipped = XmlLoader.readFloat(parser, SCORE_SKIPPED_DEFAULT);
                break;

            case FileManager.TAG_SCORE_COLUMN:
                scoreRule = ScoreColumn.readScoreColumn(parser, inputType);
                break;

            default:
                XmlLoader.skip(parser);
                break;
        }
    }

    /**
     * Set default values for backward compatibility.
     *
     * @param fileVersion the version of the file
     */
    protected void setDefaultValueBackWardCompatibility(int fileVersion) {
        if (fileVersion < FileManager.VER_V_0_3_0) {
            if (scoreRight < 0) scoreRight = SCORE_RIGHT_DEFAULT;
            if (scoreWrong < 0) scoreWrong = SCORE_WRONG_DEFAULT;
            if (scoreSkipped < 0) scoreSkipped = SCORE_SKIPPED_DEFAULT;
        }

        if (scoreRule == null) scoreRule = getDefaultScoreColumn(); // FIXME move to the < VER_V_0_3_0
    }

    /**
     * Loop over all xml tag while reading an xml file that represent the column.
     *
     * @param parser the parser that represents the xml file
     * @throws IOException            if an error occur while the file is reading
     * @throws XmlPullParserException if an error occur while the file is reading
     */
    private void loopXmlTags(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, FileManager.TAG_COLUMN);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                // continue until it is a start tag
                continue;
            }

            readColumnXmlTag(parser);
        }
    }

    /**
     * Migrate cells from a column to this column (new)
     *
     * @param currentTest    the current test which is updated
     * @param previousColumn the previous column which will change into this column
     */
    public void migrateColumn(Test currentTest, Column previousColumn) {
        // fields
        name = previousColumn.name;
        description = previousColumn.description;
        scoreRight = previousColumn.scoreRight;
        scoreRule = previousColumn.scoreRule;
        setSettings(SET_CAN_BE_HIDE, previousColumn.isInSettings(SET_CAN_BE_HIDE));
        setSettings(SET_CAN_BE_SHOW, previousColumn.isInSettings(SET_CAN_BE_SHOW));

        ArrayList<DataRow> listRow = currentTest.getListRow();
        for (int i = 0, listRowSize = listRow.size(); i < listRowSize; i++) {
            DataRow row = listRow.get(i);
            HashMap<Column, DataCell> listCells = row.getListCells();
            DataCell dataCell = listCells.get(previousColumn);
            if (dataCell == null) {
                listCells.put(this, DataCell.newCellWithDefaultValue(this));
            } else {
                listCells.remove(previousColumn); // should be remove before put in case of
                // previousColumn == this
                listCells.put(this, DataCell.newCellMigrate(this, previousColumn,
                        dataCell));
            }
        }
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Column) {
            Column c = (Column) obj;
            // fields
            return Objects.equals(c.name, name) && Objects.equals(c.description, description) &&
                    c.settings == settings && c.scoreRight == scoreRight &&
                    c.scoreWrong == scoreWrong && c.scoreSkipped == scoreSkipped &&
                    c.scoreRule.equals(scoreRule);
        }
        return false;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public void setScore(int scoreRight, int scoreWrong, int scoreSkipped) {
        this.scoreRight = scoreRight;
        this.scoreWrong = scoreWrong;
        this.scoreSkipped = scoreSkipped;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public void setScore(Column c) {
        this.scoreRight = c.scoreRight;
        this.scoreWrong = c.scoreWrong;
        this.scoreSkipped = c.scoreSkipped;
    }

    /**
     * An response object in test
     */
    public class ShowValueResponse {
        private final View valueView;
        private final AnswerValidEnum answerValid;

        /**
         * Constructor
         *
         * @param valueView   the view which contain the value
         * @param answerValid if the answer is right
         */
        ShowValueResponse(View valueView, AnswerValidEnum answerValid) {
            this.valueView = valueView;
            this.answerValid = answerValid;
        }

        /**
         * Get the view which contain the value to show
         *
         * @return the view to show
         */
        public View getValueView() {
            return valueView;
        }

        /**
         * Get if the answer is right
         *
         * @return if the answer is right
         */
        public boolean isAnswerRight() {
            return answerValid == AnswerValidEnum.RIGHT;
        }

        /**
         * Get a score view that show the score given
         *
         * @param context the context
         * @return the view that show the score
         */
        public View getScoreView(Context context) {
            Resources resources = context.getResources();

            MaterialTextView view = new MaterialTextView(context);
            view.setTextAppearance(context, R.style.TestScore);

            float scoreGiven;
            if (answerValid == AnswerValidEnum.RIGHT) {
                scoreGiven = scoreRight;
                view.setTextColor(resources.getColor(R.color.answer_right));
            } else if (answerValid == AnswerValidEnum.SKIPPED) {
                view.setTextColor(resources.getColor(R.color.answer_skipped));
                scoreGiven = scoreSkipped;
            } else {
                view.setTextColor(resources.getColor(R.color.answer_wrong));
                scoreGiven = scoreWrong;
            }

            view.setText(Utils.formatScore(scoreGiven));

            return view;
        }
    }
}
