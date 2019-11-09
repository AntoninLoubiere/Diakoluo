package fr.pyjacpp.diakoluo;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.Date;

import fr.pyjacpp.diakoluo.tests.Column;
import fr.pyjacpp.diakoluo.tests.ColumnInputType;
import fr.pyjacpp.diakoluo.tests.DataRow;
import fr.pyjacpp.diakoluo.tests.Test;
import fr.pyjacpp.diakoluo.tests.data.DataCellString;

public class DiakoluoApplication extends Application {
    private ArrayList<Test> listTest;
    private Test currentTest;

    @Override
    public void onCreate() {
        super.onCreate();

        listTest = new ArrayList<>();
        ArrayList<Column> columns = new ArrayList<>();
        columns.add(new Column("Test - C1", "Test - C1", ColumnInputType.String));
        columns.add(new Column("Test - C2", "Test - C2", ColumnInputType.String));
        columns.add(new Column("Test - C3", "Test - C3", ColumnInputType.String));
        ArrayList<DataRow> dataRows = new ArrayList<>();
        dataRows.add(new DataRow());
        dataRows.get(0).getListCells().put(columns.get(0), new DataCellString("Test - D1C1"));
        dataRows.get(0).getListCells().put(columns.get(1), new DataCellString("Test - D2C1"));
        dataRows.get(0).getListCells().put(columns.get(2), new DataCellString("Test - D3C1"));

        dataRows.add(new DataRow());
        dataRows.get(1).getListCells().put(columns.get(0), new DataCellString("Test - D1C2"));
        dataRows.get(1).getListCells().put(columns.get(1), new DataCellString("Test - D2C2"));
        dataRows.get(1).getListCells().put(columns.get(2), new DataCellString("Test - D3C2"));

        dataRows.add(new DataRow());
        dataRows.get(2).getListCells().put(columns.get(0), new DataCellString("Test - D1C3"));
        dataRows.get(2).getListCells().put(columns.get(1), new DataCellString("Test - D2C3"));
        dataRows.get(2).getListCells().put(columns.get(2), new DataCellString("Test - D3C3"));


        listTest.add(new Test(
                "Test 1", getString(R.string.very_long_description), new Date(), new Date(), 1, columns, dataRows
        ));

        listTest.add(new Test(
                "Test 2", "It's the second test", new Date(), new Date(), 2, columns, dataRows
        ));

        listTest.add(new Test(
                "Test 3", "It's the third test", new Date(), new Date(), 3, columns, dataRows
        ));

        setCurrentTest(0);
    }

    public void setCurrentTest(Test currentTest) {
        this.currentTest = currentTest;
    }

    public void setCurrentTest(int currentTest) {
        this.currentTest = getListTest().get(currentTest);
    }

    public Test getCurrentTest() {
        return currentTest;
    }

    public ArrayList<Test> getListTest() {
        return listTest;
    }

    public static void setCurrentTest(Context context, Test currentTest) {
        ((DiakoluoApplication) context.getApplicationContext()).setCurrentTest(currentTest);
    }

    public static Test getCurrentTest(Context context) {
        return ((DiakoluoApplication) context.getApplicationContext()).getCurrentTest();
    }

    public static ArrayList<Test> getListTest(Context context) {
        return ((DiakoluoApplication) context.getApplicationContext()).getListTest();
    }
}
