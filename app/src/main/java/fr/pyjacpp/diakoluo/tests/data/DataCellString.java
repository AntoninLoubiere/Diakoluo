package fr.pyjacpp.diakoluo.tests.data;

public class DataCellString extends DataCell {
    private String value;

    public DataCellString(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
