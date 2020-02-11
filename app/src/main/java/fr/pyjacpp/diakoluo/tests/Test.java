package fr.pyjacpp.diakoluo.tests;

import java.util.ArrayList;
import java.util.Date;

public class Test {
    private String name;
    private String description;

    /* TODO private ?PathClass? testPath; */

    private Date createdDate;
    private Date lastModification;

    private int numberTestDid;

    private ArrayList<Column> listColumn;
    private ArrayList<DataRow> listRow;

    public Test() {
        init("", "");
    }

    public Test(Test test) {
        name = test.name;
        description = test.description;
        createdDate = new Date(test.createdDate.getTime());
        lastModification = new Date(test.lastModification.getTime());
        numberTestDid = test.numberTestDid;
        listColumn = new ArrayList<>(test.listColumn);
        listRow = new ArrayList<>(test.listRow);
    }

    public Test(String name, String description) {
        init(name, description);
    }


    public Test(String name, String description, Date createdDate, Date lastModification, int numberTestDid,
                ArrayList<Column> listColumn, ArrayList<DataRow> listRow) {
        // previous test
        this.name = name;
        this.description = description;

        this.createdDate = createdDate;
        this.lastModification = lastModification;
        this.numberTestDid = numberTestDid;

        this.listColumn = listColumn;
        this.listRow = listRow;
    }

    private void init(String name, String description) {
        // new test
        this.name = name;
        this.description = description;

        createdDate = new Date();
        lastModification = new Date();
        numberTestDid = 0;

        listColumn = new ArrayList<>();
        listRow = new ArrayList<>();
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getLastModificationDate() {
        return lastModification;
    }

    public void registerModificationDate() {
        lastModification = new Date();
    }

    public int getNumberTestDid() {
        return numberTestDid;
    }

    public void incrementTestDid() {
        numberTestDid++;
    }

    public ArrayList<Column> getListColumn() {
        return listColumn;
    }

    public int getNumberColumn() {
        return listColumn.size();
    }

    public void addColumn(Column column) {
        listColumn.add(column);
    }

    public ArrayList<DataRow> getListRow() {
        return listRow;
    }

    public int getNumberRow() {
        return listRow.size();
    }

    public void addRow(DataRow row) {
        listRow.add(row);
    }

    public void addNumberTestDid() {
        numberTestDid++;
    }

    public boolean canBePlay() {
        return getNumberColumn() > 1 && getNumberRow() > 0;
    }
}
