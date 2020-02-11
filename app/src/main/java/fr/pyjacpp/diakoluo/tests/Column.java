package fr.pyjacpp.diakoluo.tests;

public class Column {
    private String name;
    private String description;

    private ColumnInputType inputType;
    private Object defaultValue;

    public Column() {
        this.name = "";
        this.description = "";
        this.inputType = ColumnInputType.DEFAULT_INPUT_TYPE;
        this.defaultValue = "";
    }

    public Column(String name, String description, ColumnInputType inputType) {
        this.name = name;
        this.description = description;
        this.inputType = inputType;
        this.defaultValue = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ColumnInputType getInputType() {
        return inputType;
    }

    public void setInputType(ColumnInputType inputType) {
        this.inputType = inputType;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }
}
