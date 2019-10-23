package fr.pyjacpp.diakoluo.tests.data;

import fr.pyjacpp.diakoluo.tests.ColumnInputType;

public abstract class DataCell {
    protected abstract Object getValue();

    public static Class<? extends DataCell> getClassByColumnType(ColumnInputType inputType) {
        switch (inputType) {
            case String:
                return DataCellString.class;

            default:
                throw new RuntimeException("Unknow inputType !");
        }
    }
}
