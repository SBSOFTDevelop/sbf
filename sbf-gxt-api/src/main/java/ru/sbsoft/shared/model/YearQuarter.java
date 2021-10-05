package ru.sbsoft.shared.model;

import java.io.Serializable;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.consts.Quarter;
import ru.sbsoft.shared.exceptions.ApplicationException;

/**
 *
 * @author Kiselev
 */
public final class YearQuarter implements Serializable, IYearMonth<YearQuarter> {

    public static final int MIN_YEAR = YearMonth.MIN_YEAR;
    public static final int MIN_QUARTER = Quarter.MIN_QUARTER_NUM;
    public static final int MAX_QUARTER = Quarter.MAX_QUARTER_NUM;

    private int year;
    private int quarter;
    private int month;

    public YearQuarter() {
        this(MIN_YEAR, MIN_QUARTER);
    }

    public YearQuarter(int year, int quarter) {
        if (year < MIN_YEAR) {
            throw new ApplicationException(SBFGeneralStr.msgYearFrom, String.valueOf(MIN_YEAR));
        }
        if (quarter < MIN_QUARTER || quarter > MAX_QUARTER) {
            throw new ApplicationException(SBFGeneralStr.msgQuarterBetween, String.valueOf(MIN_QUARTER), String.valueOf(MAX_QUARTER));
        }
        this.year = year;
        this.quarter = quarter;
        this.month = Quarter.getMonthNum(quarter);
    }

    @Override
    public final int getYear() {
        return year;
    }

    public final int getQuarter() {
        return quarter;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof YearQuarter) {
            YearQuarter y2 = (YearQuarter) obj;
            return year == y2.year && quarter == y2.quarter;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return year + quarter;
    }

    @Override
    public int compareTo(YearQuarter o) {
        if (o != null) {
            int res = compare(year, o.year);
            if (res == 0) {
                res = compare(quarter, o.quarter);
            }
            return res;
        }
        return 1;
    }

    private static int compare(int x, int y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

    public YearQuarter getNext() {
        int q = quarter + 1;
        int y = year;
        if (q > 4) {
            y++;
            q = 1;
        }
        return new YearQuarter(y, q);
    }

    public YearQuarter getPrev() {
        int q = quarter - 1;
        int y = year;
        if (q < 1) {
            y--;
            q = 4;
        }
        return new YearQuarter(y, q);
    }

    public YearMonth toMonth() {
        return new YearMonth(year, month);
    }

    @Override
    public int getMonth() {
        return month;
    }

}
