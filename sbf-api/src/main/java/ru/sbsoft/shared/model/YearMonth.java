package ru.sbsoft.shared.model;

import java.io.Serializable;
import ru.sbsoft.shared.api.i18n.NonLocalizedString;
import ru.sbsoft.shared.api.i18n.consts.SBFExceptionStr;
import ru.sbsoft.shared.exceptions.ApplicationException;
import ru.sbsoft.shared.consts.Month;

/**
 *
 * @author Kiselev
 */
public final class YearMonth implements Serializable, IYearMonth<YearMonth> {

    public static final int MIN_YEAR = 1919; //min grigorian calendar year on russia

    private int year;
    private int month;

    public YearMonth() {
        this(MIN_YEAR, Month.getMinNum());
    }

    public YearMonth(int year, Month month) {
        this(year, month.getNum());
    }

    public YearMonth(int year, int month) {
        if (year < MIN_YEAR) {
            throw new ApplicationException(SBFExceptionStr.yearLess, new NonLocalizedString(String.valueOf(MIN_YEAR)));
        }
        Month.checkMonthNum(month);
        this.year = year;
        this.month = month;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public int getMonthNum() {
        return month;
    }
    
    @Override
    public Month getMonth(){
        return Month.getByNum(month);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        if (month < 10) {
            buf.append('0');
        }
        buf.append(month).append('.').append(year);
        return buf.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof YearMonth) {
            YearMonth y2 = (YearMonth) obj;
            return year == y2.year && month == y2.month;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return year + month;
    }

    @Override
    public int compareTo(YearMonth o) {
        if (o != null) {
            int res = compare(year, o.year);
            if (res == 0) {
                res = compare(month, o.month);
            }
            return res;
        }
        return 1;
    }

    private static int compare(int x, int y) {
        return Integer.compare(x, y);
    }

    public YearMonth getNext() {
        int m = month + 1;
        int y = year;
        if (m > 12) {
            y++;
            m = 1;
        }
        return new YearMonth(y, m);
    }

    public YearMonth getPrev() {
        int m = month - 1;
        int y = year;
        if (m < 1) {
            y--;
            m = 12;
        }
        return new YearMonth(y, m);
    }

    public int monthsFrom(YearMonth startYm) {
        return ((year - startYm.year) * 12) + (month - startYm.month);
    }

    public YearQuarter toQuarter() {
        return new YearQuarter(year, Month.getQuarterNum(month));
    }

    public static YearMonth createSilent(Integer y, Integer m) {
        return new YearMonth(y != null ? y : MIN_YEAR, m != null ? m : Month.getMinNum());
    }

}
