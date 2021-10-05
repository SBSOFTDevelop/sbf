package ru.sbsoft.common.validation;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author balandin
 * @since Jan 30, 2015 3:36:13 PM
 */
public class NumClassifiersTest extends Assert {

    @Test
    public void test() {
        assertEquals(true, NumClassifiers.valid("000", "___", true));
        assertEquals(true, NumClassifiers.valid("000-000-000 00", "___-___-___ __", true));
        assertEquals(true, NumClassifiers.valid("000-000-000 00", "___-___-___ __", false));
        assertEquals(true, NumClassifiers.valid("000-_00-000 00", "___-___-___ __", false));
    }
}
