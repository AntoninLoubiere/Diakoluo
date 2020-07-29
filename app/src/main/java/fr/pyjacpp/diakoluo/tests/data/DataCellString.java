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

package fr.pyjacpp.diakoluo.tests.data;

import java.io.IOException;
import java.io.OutputStream;

import fr.pyjacpp.diakoluo.save_test.FileManager;
import fr.pyjacpp.diakoluo.save_test.XmlSaver;

public class DataCellString extends DataCell {
    private String value;

    DataCellString(DataCellString dataCellString) {
        super(dataCellString);
        value = dataCellString.value;
    }

    public DataCellString(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(Object object) {
        value = (String) object;
    }

    @Override
    public String getStringValue() {
        return value;
    }

    @Override
    protected String getStringValue(Object answer) {
        return (String) answer;
    }

    @Override
    public boolean verifyAnswer(Object answer) {
        return ((String) answer).equalsIgnoreCase(value);
    }

    @Override
    public void setValueFromCsv(String lineCell) {
        value = lineCell;
    }

    @Override
    public void writeXml(OutputStream fileOutputStream) throws IOException {
        fileOutputStream.write(XmlSaver.getCoupleBeacon(FileManager.TAG_CELL, value).getBytes());
    }
}
