package fr.pyjacpp.diakoluo.tests.data;

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
}
