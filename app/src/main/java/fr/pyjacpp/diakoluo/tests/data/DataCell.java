package fr.pyjacpp.diakoluo.tests.data;

import fr.pyjacpp.diakoluo.tests.Column;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;

public class DataCell {
    public static DataCell getDefaultValueCell(Column currentColumn) {
        switch (currentColumn.getInputType()) {
            case String:
                return new DataCellString((String) currentColumn.getDefaultValue());

            default:
                throw new IllegalStateException("Unexpected value: " + currentColumn.getInputType());
        }
    }

    public Object getValue() {
        throw new RuntimeException("Not implemented");
    }

    public static Class<? extends DataCell> getClassByColumnType(ColumnInputType inputType) {
        switch (inputType) {
            case String:
                return DataCellString.class;

            default:
                throw new RuntimeException("Unknow inputType !");
        }
    }

    public void setValue(Object object) {
        throw new RuntimeException("Not implemented");
    }
}