package fr.pyjacpp.diakoluo.tests;

class CompactTest {
    private String name;
    private String compactDescription;

    public CompactTest(String name, String description) {
        this.name = name;
        this.compactDescription = description; // FIXME need to be cut ?
    }

    private String filename;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompactDescription() {
        return compactDescription;
    }

    public void setCompactDescription(String compactDescription) {
        this.compactDescription = compactDescription;
    }
}
