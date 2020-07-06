package fr.pyjacpp.diakoluo.tests;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public enum ColumnInputType {
    String;

    public static final ColumnInputType DEFAULT_INPUT_TYPE = String;

    @Nullable
    public static ColumnInputType get(@NonNull String attributeValue) {
        switch (attributeValue) {
            case "String":
                return String;

            default:
                return null;
        }
    }
}
