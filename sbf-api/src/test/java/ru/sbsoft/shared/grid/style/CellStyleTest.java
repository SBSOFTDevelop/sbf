package ru.sbsoft.shared.grid.style;

import ru.sbsoft.shared.grid.style.CStyle;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Kiselev
 */
public class CellStyleTest {
    @Test
    public void testSetColor(){
        CStyle s = new CStyle();
        s.setColor(192,192,192);
        assertEquals((Integer)0xC0C0C0, s.getColor());
        s.setColor(0,0,255);
        assertEquals((Integer)0xFF, s.getColor());
        List<String> css = s.getCssStyles();
        assertNotNull(css);
        assertEquals(1, css.size());
        assertEquals("color:#0000ff;", css.get(0));
    }
}
