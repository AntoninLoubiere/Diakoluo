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

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textview.MaterialTextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.OutputStream;

import fr.pyjacpp.diakoluo.R;
import fr.pyjacpp.diakoluo.ViewUtils;
import fr.pyjacpp.diakoluo.save_test.FileManager;
import fr.pyjacpp.diakoluo.save_test.XmlLoader;
import fr.pyjacpp.diakoluo.save_test.XmlSaver;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;

class ColumnString extends Column {
    private String defaultValue;

    private static final int CASE_SENSITIVE = 1;
    private static final int REMOVE_USELESS_SPACES = 1 << 1;
    private static final int ALLOW_AUTO_COMPLETION = 1 << 2;

    private static final int DEFAULT_SETTINGS = CASE_SENSITIVE | ALLOW_AUTO_COMPLETION;

    private static final String SETTINGS_TAG = "settings";

    public int settings = -1;

    ColumnString() {
        super();
    }

    @Override
    void initialize() {
        super.initialize();
        defaultValue = null;
        settings = -1;
        inputType = ColumnInputType.String;
    }

    @Override
    public void initializeChildValue() {
        defaultValue = "";
        settings = 0;
    }

    @Override
    public void getViewColumnSettings(LayoutInflater layoutInflater, ViewGroup parent) {
        View inflatedView = layoutInflater.inflate(R.layout.fragment_column_settings_view_string, parent, true);

        MaterialTextView caseSensitiveTextView = inflatedView.findViewById(R.id.caseSensitiveTextView);
        MaterialTextView removeUselessSpacesTextView = inflatedView.findViewById(R.id.removeUselessSpaceTextView);
        MaterialTextView allowAutoCompletionTextView = inflatedView.findViewById(R.id.allowAutoCompletionTextView);

        ViewUtils.setBooleanView(parent.getContext(), caseSensitiveTextView, isInSettings(CASE_SENSITIVE));
        ViewUtils.setBooleanView(parent.getContext(), removeUselessSpacesTextView, isInSettings(REMOVE_USELESS_SPACES));
        ViewUtils.setBooleanView(parent.getContext(), allowAutoCompletionTextView, isInSettings(ALLOW_AUTO_COMPLETION));
    }

    @NonNull
    @Override
    public View getEditColumnSettings(LayoutInflater layoutInflater, ViewGroup parent) {
        View inflatedView = layoutInflater.inflate(R.layout.fragment_column_settings_edit_string, parent, true);

        MaterialCheckBox caseSensitiveCheckBox = inflatedView.findViewById(R.id.caseSensitiveCheckBox);
        MaterialCheckBox removeUselessSpacesCheckBox = inflatedView.findViewById(R.id.removeUselessSpaceCheckBox);
        MaterialCheckBox allowAutoCompletionCheckBox = inflatedView.findViewById(R.id.allowAutoCompletionCheckBox);

        caseSensitiveCheckBox.setChecked(isInSettings(CASE_SENSITIVE));
        removeUselessSpacesCheckBox.setChecked(isInSettings(REMOVE_USELESS_SPACES));
        allowAutoCompletionCheckBox.setChecked(isInSettings(ALLOW_AUTO_COMPLETION));

        return inflatedView;
    }

    @Override
    public void setEditColumnSettings(View columnSettingsView) {
        MaterialCheckBox caseSensitiveCheckBox = columnSettingsView.findViewById(R.id.caseSensitiveCheckBox);
        MaterialCheckBox removeUselessSpacesCheckBox = columnSettingsView.findViewById(R.id.removeUselessSpaceCheckBox);
        MaterialCheckBox allowAutoCompletionCheckBox = columnSettingsView.findViewById(R.id.allowAutoCompletionCheckBox);

        setSettings(CASE_SENSITIVE, caseSensitiveCheckBox.isChecked());
        setSettings(REMOVE_USELESS_SPACES, removeUselessSpacesCheckBox.isChecked());
        setSettings(ALLOW_AUTO_COMPLETION, allowAutoCompletionCheckBox.isChecked());
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

    private boolean isInSettings(int parameter) {
        return (settings & parameter) == parameter;
    }

    private void setSettings(int parameter, boolean value) {
        if (value) {
            settings = settings | parameter;
        } else {
            settings = settings & ~ parameter;
        }
    }

    @Override
    public void writeXmlHeader(OutputStream fileOutputStream) throws IOException {
        fileOutputStream.write(XmlSaver.getCoupleBeacon(FileManager.TAG_DEFAULT_VALUE,
                defaultValue).getBytes());
        fileOutputStream.write(XmlSaver.getCoupleBeacon(SETTINGS_TAG,
                String.valueOf(settings)).getBytes());
    }

    @Override
    void readColumnXmlTag(XmlPullParser parser) throws IOException, XmlPullParserException {
        switch (parser.getName()) {
            case FileManager.TAG_DEFAULT_VALUE:
                defaultValue = XmlLoader.readText(parser);
                break;

            case SETTINGS_TAG:
                settings = XmlLoader.readInt(parser);
                break;

            default:
                super.readColumnXmlTag(parser);
                break;
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

    static ColumnString privateCopyColumn(ColumnString baseColumn) {
        ColumnString newColumn = new ColumnString();
        newColumn.defaultValue = baseColumn.defaultValue;
        newColumn.settings = baseColumn.settings;
        Column.privateCopyColumn(baseColumn, newColumn);
        return newColumn;
    }
}
