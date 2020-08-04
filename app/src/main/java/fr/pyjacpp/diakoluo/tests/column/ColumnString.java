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

import androidx.annotation.NonNull;
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
import fr.pyjacpp.diakoluo.tests.data.DataCell;
import fr.pyjacpp.diakoluo.tests.data.DataCellString;


public class ColumnString extends IntSettingsColumn {
    private String defaultValue;

    private static final int CASE_SENSITIVE = 1;
    private static final int REMOVE_USELESS_SPACES = 1 << 1;

    private static final int DEFAULT_SETTINGS = REMOVE_USELESS_SPACES;

    ColumnString() {
        super(ColumnInputType.String);
    }

    @Override
    public void initialize() {
        super.initialize();
        defaultValue = null;
        settings = -1;
        inputType = ColumnInputType.String;
    }

    @Override
    public void initialize(String name, String description) {
        super.initialize(name, description);
        defaultValue = "";
        settings = DEFAULT_SETTINGS;
    }

    @Override
    public boolean verifyAnswer(DataCell dataCell, Object answer) {
        DataCellString dataCellString = (DataCellString) dataCell;
        String value = dataCellString.getValue();
        String a = (String) answer;
        if (isInSettings(REMOVE_USELESS_SPACES)) {
            value = Utils.removeUselessSpaces(value);
            a = Utils.removeUselessSpaces(a);
        }

        if (isInSettings(CASE_SENSITIVE)) {
            return value.equals(a);
        } else {
            return value.equalsIgnoreCase(a);
        }
    }

    @Override
    public void getViewColumnSettings(LayoutInflater layoutInflater, ViewGroup parent) {
        View inflatedView = layoutInflater.inflate(R.layout.fragment_column_settings_view_string,
                parent, true);

        MaterialTextView caseSensitiveTextView =
                inflatedView.findViewById(R.id.caseSensitiveTextView);
        MaterialTextView removeUselessSpacesTextView =
                inflatedView.findViewById(R.id.removeUselessSpaceTextView);

        ViewUtils.setBooleanView(parent.getContext(),
                caseSensitiveTextView, isInSettings(CASE_SENSITIVE));
        ViewUtils.setBooleanView(parent.getContext(),
                removeUselessSpacesTextView, isInSettings(REMOVE_USELESS_SPACES));
    }

    @NonNull
    @Override
    public View getEditColumnSettings(LayoutInflater layoutInflater, ViewGroup parent) {
        View inflatedView =
                layoutInflater.inflate(R.layout.fragment_column_settings_edit_string, parent, true);

        MaterialCheckBox caseSensitiveCheckBox =
                inflatedView.findViewById(R.id.caseSensitiveCheckBox);
        MaterialCheckBox removeUselessSpacesCheckBox =
                inflatedView.findViewById(R.id.removeUselessSpaceCheckBox);

        caseSensitiveCheckBox.setChecked(isInSettings(CASE_SENSITIVE));
        removeUselessSpacesCheckBox.setChecked(isInSettings(REMOVE_USELESS_SPACES));

        return inflatedView;
    }

    @Override
    public void setEditColumnSettings(View columnSettingsView) {
        MaterialCheckBox caseSensitiveCheckBox =
                columnSettingsView.findViewById(R.id.caseSensitiveCheckBox);
        MaterialCheckBox removeUselessSpacesCheckBox =
                columnSettingsView.findViewById(R.id.removeUselessSpaceCheckBox);

        setSettings(CASE_SENSITIVE, caseSensitiveCheckBox.isChecked());
        setSettings(REMOVE_USELESS_SPACES, removeUselessSpacesCheckBox.isChecked());
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
        return super.isValid() && defaultValue != null && settings >= 0;
    }

    @Override
    public void writeXml(OutputStream fileOutputStream) throws IOException {
        fileOutputStream.write(XmlSaver.getCoupleBeacon(FileManager.TAG_DEFAULT_VALUE,
                defaultValue).getBytes());
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
    protected void setDefaultValueBackWardCompatibility() {
        super.setDefaultValueBackWardCompatibility();
        // for version < v0.3.0
        if (settings < 0) {
            settings = DEFAULT_SETTINGS;
        }
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
