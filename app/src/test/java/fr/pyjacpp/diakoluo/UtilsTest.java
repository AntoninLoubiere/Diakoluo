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
}