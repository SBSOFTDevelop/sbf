package ru.sbsoft.sbf.app.model;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Kiselev
 */
public class YearMonthDayTest {

    @Test
    public void getYmdStrTest() {
        YearMonthDay ymd = new YearMonthDay(2017, 3, 12);
        String s = ymd.getYmdStr();
        Assert.assertEquals("20170312", s);
    }
}
