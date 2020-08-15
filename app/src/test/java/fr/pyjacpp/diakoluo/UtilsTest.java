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
        assertEquals("test", Utils.removeUselessSpaces("test"));
    }

    @Test
    public void map() {
        assertEquals(0, Utils.map(0, 0, 20, 0, 100));
        assertEquals(100, Utils.map(20, 0, 20, 0, 100));
        assertEquals(-100, Utils.map(-20, 0, 20, 0, 100));
        assertEquals(50, Utils.map(10, 0, 20, 0, 100));
        assertEquals(2, Utils.map(10, 0, 100, 0, 20));
    }

    @Test
    public void removeUselessSpacesEnd() {
        assertEquals("", Utils.removeUselessSpacesEnd(new StringBuilder("\n\r\t ")));
        assertEquals("test", Utils.removeUselessSpacesEnd(new StringBuilder("test\n\r\t ")));
        assertEquals("\ttest", Utils.removeUselessSpacesEnd(new StringBuilder("\ttest\n\r\t ")));
        assertEquals("test", Utils.removeUselessSpacesEnd(new StringBuilder("test")));
        assertEquals("test\n\r\t test", Utils.removeUselessSpacesEnd(new StringBuilder("test\n\r\t test")));
    }

    @Test
    public void extremeDeceleratorInterpolator() {
        Utils.ExtremeDeceleratorInterpolator extremeDeceleratorInterpolator = new Utils.ExtremeDeceleratorInterpolator();
        float STEP = 0.01f;

        for (float i = 0f; i <= 1f; i += STEP) {
            float interpolation = extremeDeceleratorInterpolator.getInterpolation(i);
            assertTrue(0 <= interpolation && interpolation <= 1);
        }
    }
}