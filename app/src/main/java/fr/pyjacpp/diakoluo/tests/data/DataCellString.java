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
