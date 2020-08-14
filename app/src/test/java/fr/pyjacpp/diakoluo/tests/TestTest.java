package fr.pyjacpp.diakoluo.tests;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import fr.pyjacpp.diakoluo.DefaultTest;
import fr.pyjacpp.diakoluo.tests.column.Column;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TestTest {

    private DefaultTest defaultTest;

    @Before
    public void setUp() {
        defaultTest = new DefaultTest();
    }

    @Test
    public void name() {
        fr.pyjacpp.diakoluo.tests.Test defaultTest = new fr.pyjacpp.diakoluo.tests.Test(this.defaultTest);
        String s = "It is a test";
        defaultTest.setName(s);
        assertEquals(s, defaultTest.getName());
    }

    @Test
    public void description() {
        fr.pyjacpp.diakoluo.tests.Test defaultTest = new fr.pyjacpp.diakoluo.tests.Test(this.defaultTest);
        String s = "It is a description";
        defaultTest.setDescription(s);
        assertEquals(s, defaultTest.getDescription());
    }

    @Test
    public void createdDate() {
        fr.pyjacpp.diakoluo.tests.Test defaultTest = new fr.pyjacpp.diakoluo.tests.Test(this.defaultTest);
        Date date = new Date();
        defaultTest.setCreatedDate(date);
        assertEquals(date, defaultTest.getCreatedDate());
    }

    @Test
    public void lastModificationDate() {
        fr.pyjacpp.diakoluo.tests.Test defaultTest = new fr.pyjacpp.diakoluo.tests.Test(this.defaultTest);
        Date date = new Date();
        defaultTest.setLastModificationDate(date);
        assertEquals(date, defaultTest.getLastModificationDate());
    }

    @Test
    public void registerModificationDate() {
        fr.pyjacpp.diakoluo.tests.Test test = new fr.pyjacpp.diakoluo.tests.Test();
        test.registerModificationDate();
        assertEquals(new Date(), test.getLastModificationDate());
    }

    @Test
    public void numberTestDid() {
        fr.pyjacpp.diakoluo.tests.Test defaultTest = new fr.pyjacpp.diakoluo.tests.Test(this.defaultTest);
        int x = 3;
        defaultTest.setNumberTestDid(x);
        assertEquals(x, defaultTest.getNumberTestDid());
    }

    @Test
    public void incrementTestDid() {
        fr.pyjacpp.diakoluo.tests.Test test = new fr.pyjacpp.diakoluo.tests.Test();
        int x = 3;
        test.setNumberTestDid(x);
        test.addNumberTestDid();
        assertEquals(x + 1, test.getNumberTestDid());
        test.addNumberTestDid();
    }

    @Test
    public void listColumn() {
        fr.pyjacpp.diakoluo.tests.Test defaultTest = new fr.pyjacpp.diakoluo.tests.Test(this.defaultTest);
        fr.pyjacpp.diakoluo.tests.Test test = new fr.pyjacpp.diakoluo.tests.Test();
        test.setListColumn(defaultTest.getListColumn());
        assertEquals(defaultTest.getListColumn(), test.getListColumn());
        assertEquals(defaultTest.getListColumn().size(), defaultTest.getNumberColumn());
        test.addColumn(Column.newColumn(ColumnInputType.DEFAULT_INPUT_TYPE));
        assertEquals(test.getListColumn().size(), test.getNumberColumn());
    }

    @Test
    public void listRow() {
        fr.pyjacpp.diakoluo.tests.Test test = new fr.pyjacpp.diakoluo.tests.Test();
        test.setListColumn(defaultTest.getListColumn());
        test.setListRow(defaultTest.getListRow());
        assertEquals(defaultTest.getListRow(), test.getListRow());
        assertEquals(defaultTest.getListRow().size(), test.getNumberRow());
        assertEquals(defaultTest.getRowFirstCell(0), test.getRowFirstCell(0));
        assertEquals(defaultTest.getRowFirstCellString(null, 0), test.getRowFirstCellString(null, 0));
        test.addRow(new DataRow());
        assertEquals(test.getListRow().size(), test.getNumberRow());
        test.setListColumn(new ArrayList<Column>());
        assertNull(test.getRowFirstCell(0));
        assertEquals("0", test.getRowFirstCellString(null, 0));
    }

    @Test
    public void canBePlay() {
        assertTrue(defaultTest.canBePlay());
    }

    @Test
    public void isValid() {
        fr.pyjacpp.diakoluo.tests.Test test = new fr.pyjacpp.diakoluo.tests.Test();
        fr.pyjacpp.diakoluo.tests.Test defaultTest = new fr.pyjacpp.diakoluo.tests.Test(this.defaultTest);
        assertFalse(test.isValid());
        assertTrue(defaultTest.isValid());
        test = new fr.pyjacpp.diakoluo.tests.Test(defaultTest);
        test.setName(null);
        assertFalse(test.isValid());
        test.setName(defaultTest.getName());
        test.setDescription(null);
        assertFalse(test.isValid());
        test.setDescription(defaultTest.getDescription());
        assertTrue(test.isValid());
        test.setCreatedDate(null);
        assertFalse(test.isValid());
        test.setCreatedDate(defaultTest.getCreatedDate());
        assertTrue(test.isValid());
        test.setLastModificationDate(null);
        assertFalse(test.isValid());
        test.setLastModificationDate(defaultTest.getLastModificationDate());
        assertTrue(test.isValid());
        test.setListRow(null);
        assertFalse(test.isValid());
        test.setListRow(defaultTest.getListRow());
        assertTrue(test.isValid());
        test.setListColumn(null);
        assertFalse(test.isValid());
        test.setListColumn(defaultTest.getListColumn());
        assertTrue(test.isValid());
    }

    @Test
    public void filename() {
        String filename = "Filename";
        defaultTest.setFilename(filename);
        assertEquals(filename, defaultTest.getFilename());
    }

    @Test
    public void getDefaultFilename() {
        String defaultFilename = defaultTest.getDefaultFilename();
        assertFalse(defaultFilename.contains(" "));
        assertFalse(defaultFilename.contains("/"));
        assertFalse(defaultFilename.contains("."));
    }

    @Test
    public void duplicateTest() {
        fr.pyjacpp.diakoluo.tests.Test actual = new fr.pyjacpp.diakoluo.tests.Test(defaultTest);
        assertEquals(defaultTest, actual);
        assertNotSame(defaultTest, actual);
    }

    @Test
    public void constructors() {
        String name = "Test";
        String description = "Test2";
        fr.pyjacpp.diakoluo.tests.Test actual = new fr.pyjacpp.diakoluo.tests.Test(name, description);
        assertEquals(name, actual.getName());
        assertEquals(description, actual.getDescription());
        assertTrue(actual.isValid());
        actual = new fr.pyjacpp.diakoluo.tests.Test(name, description, new Date(), new Date(), 0, new ArrayList<Column>(), new ArrayList<DataRow>());
        assertEquals(name, actual.getName());
        assertEquals(description, actual.getDescription());
        assertTrue(actual.isValid());
    }
}