package fr.pyjacpp.diakoluo.tests;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;

import fr.pyjacpp.diakoluo.tests.data.DataCell;

public class Test {
    private String name;
    private String description;

    private String filename;
    private Date createdDate;
    private Date lastModification;

    private int numberTestDid;

    private ArrayList<Column> listColumn;
    private ArrayList<DataRow> listRow;

    public Test() {
        // new test
        this.name = null;
        this.description = null;

        createdDate = null;
        lastModification = null;
        numberTestDid = 0;

        listColumn = null;
        listRow = null;
    }

    public Test(Test test) {
        name = test.name;
        description = test.description;
        createdDate = new Date(test.createdDate.getTime());
        lastModification = new Date(test.lastModification.getTime());
        numberTestDid = test.numberTestDid;
        listColumn = new ArrayList<>(test.listColumn);
        listRow = new ArrayList<>(test.listRow);
        filename = test.filename;
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

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModificationDate() {
        return lastModification;
    }

    public void setLastModificationDate(Date lastModification) {
        this.lastModification = lastModification;
    }

    public void registerModificationDate() {
        lastModification = new Date();
    }

    public int getNumberTestDid() {
        return numberTestDid;
    }

    public void setNumberTestDid(int numberTestDid) {
        this.numberTestDid = numberTestDid;
    }

    public void incrementTestDid() {
        numberTestDid++;
    }

    public ArrayList<Column> getListColumn() {
        return listColumn;
    }

    public void setListColumn(ArrayList<Column> listColumn) {
        this.listColumn = listColumn;
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

    public void setListRow(ArrayList<DataRow> listRow) {
        this.listRow = listRow;
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

    public boolean isValid() {
        return !(name == null ||
                description == null ||
                createdDate == null ||
                lastModification == null ||
                listColumn == null ||
                listRow == null
        );
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    @Nullable
    public DataCell getRowFirstCell(int rowPosition) {
        if (listColumn.size() > 0) {
            return listRow.get(rowPosition).getListCells().get(listColumn.get(0));
        } else {
            return null;
        }
    }

    public String getRowFirstCellString(int rowPosition) {
        DataCell firstCell = getRowFirstCell(rowPosition);
        if (firstCell == null) {
            return String.valueOf(rowPosition);
        } else {
            return firstCell.getStringValue();
        }
    }
}
