package fr.pyjacpp.diakoluo;

import org.junit.Test;

import static org.junit.Assert.*;

public class UtilsTest {
    @Test
    public void removeUselessSpaces() {
        assertEquals("", Utils.removeUselessSpaces("   \n \t"));
        assertEquals("test", Utils.removeUselessSpaces("  test \n \t"));
        assertEquals("test", Utils.removeUselessSpaces("test   \n \t"));
        assertEquals("test", Utils.removeUselessSpaces("   \n \ttest"));
        assertEquals("test   \n \ttest", Utils.removeUselessSpaces("test   \n \ttest"));
    }

    @Test
    public void map() {
        assertEquals(0, Utils.map(0, 0, 20, 0, 100));
        assertEquals(100, Utils.map(20, 0, 20, 0, 100));
        assertEquals(-100, Utils.map(-20, 0, 20, 0, 100));
        assertEquals(50, Utils.map(10, 0, 20, 0, 100));
        assertEquals(2, Utils.map(10, 0, 100, 0, 20));
    }
}