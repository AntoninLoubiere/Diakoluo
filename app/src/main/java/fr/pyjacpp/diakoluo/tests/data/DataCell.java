package fr.pyjacpp.diakoluo.tests.data;

import fr.pyjacpp.diakoluo.tests.ColumnInputType;

public class DataCell {
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
}
