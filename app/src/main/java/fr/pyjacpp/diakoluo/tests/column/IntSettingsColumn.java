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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.OutputStream;

import fr.pyjacpp.diakoluo.save_test.XmlLoader;
import fr.pyjacpp.diakoluo.save_test.XmlSaver;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;

public abstract class IntSettingsColumn extends Column {
    protected int settings = -1;
    private static final String TAG_SETTINGS = "settings";

    /**
     * Default constructor that initialize a non-valid column
     * @param inputType the input type of the column
     */
    protected IntSettingsColumn(@NonNull ColumnInputType inputType) {
        super(inputType);
    }

    protected boolean isInSettings(int parameter) {
        return (settings & parameter) == parameter;
    }

    protected void setSettings(int parameter, boolean value) {
        if (value) {
            settings = settings | parameter;
        } else {
            settings = settings & ~parameter;
        }
    }

    @Override
    public void initialize() {
        super.initialize();
        settings = -1;
    }

    @Override
    protected void initialize(String name, String description) {
        super.initialize(name, description);
        settings = 0;
    }

    @Override
    public boolean isValid() {
        return super.isValid() && settings >= 0;
    }

    @Override
    public void writeXml(OutputStream fileOutputStream) throws IOException {
        fileOutputStream.write(XmlSaver.getCoupleBeacon(TAG_SETTINGS,
                String.valueOf(settings)).getBytes());
    }

    @Override
    protected void readColumnXmlTag(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        if (parser.getName().equals(TAG_SETTINGS)) {
            settings = XmlLoader.readInt(parser);
        } else {
            super.readColumnXmlTag(parser);
        }
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof IntSettingsColumn && super.equals(obj)) {
            IntSettingsColumn iSC = (IntSettingsColumn) obj;
            return iSC.settings == settings;
        } else {
            return false;
        }
    }

    @Override
    protected void copyColumn(Column newColumn) {
        super.copyColumn(newColumn);
        ((IntSettingsColumn) newColumn).settings = settings;
    }
}
