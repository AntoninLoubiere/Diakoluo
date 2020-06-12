package fr.pyjacpp.diakoluo.tests.data;

import java.io.IOException;
import java.io.OutputStream;

import fr.pyjacpp.diakoluo.save_test.FileManager;
import fr.pyjacpp.diakoluo.save_test.XmlSaver;

public class DataCellString extends DataCell {
    private String value;

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
    public void writeXml(OutputStream fileOutputStream) throws IOException {
        fileOutputStream.write(XmlSaver.getCoupleBalise(FileManager.TAG_CELL, value).getBytes());
    }
}
