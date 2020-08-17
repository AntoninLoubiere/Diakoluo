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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textview.MaterialTextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.OutputStream;

import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.Utils;
import fr.pyjacpp.diakoluo.ViewUtils;
import fr.pyjacpp.diakoluo.save_test.FileManager;
import fr.pyjacpp.diakoluo.save_test.XmlLoader;
import fr.pyjacpp.diakoluo.save_test.XmlSaver;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;
import fr.pyjacpp.diakoluo.tests.data.AnswerValidEnum;
import fr.pyjacpp.diakoluo.tests.data.DataCell;
import fr.pyjacpp.diakoluo.tests.data.DataCellString;

/**
 * A column of string values.
 * @see DataCellString
 */
public class ColumnString extends Column {
    private String defaultValue;

    private static final int SET_CASE_SENSITIVE = 1 << 2;
    private static final int SET_REMOVE_USELESS_SPACES = 1 << 3;

    private static final int SET_DEFAULT = SET_REMOVE_USELESS_SPACES | Column.SET_DEFAULT;

    /**
     * The default constructor
     */
    ColumnString() {
        super(ColumnInputType.String);
    }

    @Override
    public void initialize() {
        super.initialize();
        defaultValue = null;
        inputType = ColumnInputType.String;
    }

    @Override
    public void initialize(String name, String description) {
        super.initialize(name, description);
        defaultValue = "";
        settings = SET_DEFAULT;
    }

    @Override
    public AnswerValidEnum verifyAnswer(DataCell dataCell, Object answer) {
        DataCellString dataCellString = (DataCellString) dataCell;
        String value = dataCellString.getValue();
        String a = (String) answer;
        if (isInSettings(SET_REMOVE_USELESS_SPACES)) {
            value = Utils.removeUselessSpaces(value);
            a = Utils.removeUselessSpaces(a);
        }


        if (isInSettings(SET_CASE_SENSITIVE)) {
            return value.equals(a) ? AnswerValidEnum.RIGHT : AnswerValidEnum.WRONG;
        } else if (a.length() <= 0) {
            return AnswerValidEnum.SKIPPED;
        } else {
            return value.equalsIgnoreCase(a) ? AnswerValidEnum.RIGHT : AnswerValidEnum.WRONG;
        }
    }

    @Override
    public void getViewColumnSettings(LayoutInflater layoutInflater, ViewGroup parent) {
        super.getViewColumnSettings(layoutInflater, parent);
        View inflatedView = layoutInflater.inflate(R.layout.fragment_column_settings_view_string,
                parent, true);

        MaterialTextView caseSensitiveTextView =
                inflatedView.findViewById(R.id.caseSensitiveTextView);
        MaterialTextView removeUselessSpacesTextView =
                inflatedView.findViewById(R.id.removeUselessSpaceTextView);

        ViewUtils.setBooleanView(parent.getContext(),
                caseSensitiveTextView, isInSettings(SET_CASE_SENSITIVE));
        ViewUtils.setBooleanView(parent.getContext(),
                removeUselessSpacesTextView, isInSettings(SET_REMOVE_USELESS_SPACES));
    }

    @Override
    public void getEditColumnSettings(LayoutInflater layoutInflater, ViewGroup parent) {
        super.getEditColumnSettings(layoutInflater, parent);
        View inflatedView =
                layoutInflater.inflate(R.layout.fragment_column_settings_edit_string, parent, true);

        MaterialCheckBox caseSensitiveCheckBox =
                inflatedView.findViewById(R.id.caseSensitiveCheckBox);
        MaterialCheckBox removeUselessSpacesCheckBox =
                inflatedView.findViewById(R.id.removeUselessSpaceCheckBox);

        caseSensitiveCheckBox.setChecked(isInSettings(SET_CASE_SENSITIVE));
        removeUselessSpacesCheckBox.setChecked(isInSettings(SET_REMOVE_USELESS_SPACES));
    }

    @Override
    public void setEditColumnSettings(ViewGroup parent) {
        super.setEditColumnSettings(parent);
        MaterialCheckBox caseSensitiveCheckBox =
                parent.findViewById(R.id.caseSensitiveCheckBox);
        MaterialCheckBox removeUselessSpacesCheckBox =
                parent.findViewById(R.id.removeUselessSpaceCheckBox);

        setSettings(SET_CASE_SENSITIVE, caseSensitiveCheckBox.isChecked());
        setSettings(SET_REMOVE_USELESS_SPACES, removeUselessSpacesCheckBox.isChecked());
    }

    @Override
    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = (String) defaultValue;
    }

    @Override
    public Object getDefaultValue() {
        return defaultValue;
    }

    @Override
    public boolean isValid() {
        return super.isValid() && defaultValue != null;
    }

    @Override
    public void writeXmlInternal(OutputStream fileOutputStream) throws IOException {
        super.writeXmlInternal(fileOutputStream);
        XmlSaver.writeData(fileOutputStream, FileManager.TAG_DEFAULT_VALUE, defaultValue);
    }

    @Override
    protected void readColumnXmlTag(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        if (FileManager.TAG_DEFAULT_VALUE.equals(parser.getName())) {
            defaultValue = XmlLoader.readText(parser);
        } else {
            super.readColumnXmlTag(parser);
        }
    }

    @Override
    protected void setDefaultValueBackWardCompatibility(int fileVersion) {
        super.setDefaultValueBackWardCompatibility(fileVersion);
        if (fileVersion < FileManager.VER_V_0_3_0 && settings < 0) settings = SET_DEFAULT;
    }

    @Override
    public Column copyColumn() {
        ColumnString newColumn = new ColumnString();
        copyColumn(newColumn);
        return newColumn;
    }

    @Override
    protected void copyColumn(Column newColumn) {
        super.copyColumn(newColumn);
        ((ColumnString) newColumn).defaultValue = defaultValue;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof ColumnString && super.equals(obj)) {
            ColumnString cS = (ColumnString) obj;
            return cS.defaultValue.equals(defaultValue);
        }
        return false;
    }
}
