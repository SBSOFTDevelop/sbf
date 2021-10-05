package ru.sbsoft.client.utils;

import ru.sbsoft.client.utils.BitCache;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author balandin
 * @since Feb 1, 2013 2:31:12 PM
 */
public class BitCacheTest extends Assert {
    
    @Test
    public void test() {
        BitCache array = new BitCache(1024 * 1024);
        for (int i = 0; i < array.getSize(); i++) {
            assertTrue(array.get(i) == false);
            array.set(i, true);
            assertTrue(array.get(i) == true);
            array.set(i, false);
            assertTrue(array.get(i) == false);
        }
    }
}
