package ru.sbsoft.sbf.app.model;

import java.io.Serializable;
import ru.sbsoft.shared.consts.Month;

// This object must be immutable
public class YearMonthDay implements Serializable, Comparable<YearMonthDay> {

    private static final int MIN_YEAR = 1919; //min grigorian calendar year on russia

    private final static Character SEPARATOR = '.';
    private final static Character ZERO_SYMBOL = '0';

    private int year;
    private int month;
    private int day;

    public YearMonthDay() {
        this(MIN_YEAR, 1, 1);
    }

    public YearMonthDay(int year, Month month, int day) {
        this(year, month.getNum(), day);
    }

    public YearMonthDay(int year, int month, int day) {
        if (year < MIN_YEAR) {
            throw new RuntimeException("Год должен быть больше " + (MIN_YEAR - 1));
        }
        if (month < 1 || month > 12) {
            throw new RuntimeException("Месяц должен быть в интервале от 1 до 12");
        }
        int maxMonDays = lengthOfMonth();
        if (day < 1 || day > maxMonDays) {
            throw new RuntimeException("День должен быть в интервале от 1 до " + maxMonDays + " для " + month + " месяца " + year + " года ");
        }
        this.year = year;
        this.month = month;
        this.day = day;
    }

    private static boolean isLeapYear(int year) {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }

    public final boolean isLeapYear() {
        return isLeapYear(year);
    }

    private static int lengthOfMonth(int year, int month) {
        switch (month) {
            case 2:
                return (isLeapYear(year) ? 29 : 28);
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            default:
                return 31;
        }

    }

    public final int lengthOfMonth() {
        return lengthOfMonth(year, month);
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public YearMonthDay withDayOfMonth(int day) {
        if (this.day == day) {
            return this;
        }
        return new YearMonthDay(year, month, day);
    }

    public YearMonthDay withMonthsLastDay(int month) {
        int day = lengthOfMonth(this.year, month);
        if (this.month == month && this.day == day) {
            return this;
        }
        return new YearMonthDay(this.year, month, day);
    }

    public YearMonthDay toLastMonthDay() {
        int lmd = lengthOfMonth();
        return withDayOfMonth(lmd);
    }

    public String getPostgreStr() {
        StringBuilder buf = new StringBuilder(20);
        buf.append("'");
        buf.append(Integer.toString(year));
        buf.append('-');
        format(month, buf);
        buf.append('-');
        format(day, buf);
        buf.append("'::date");
        return buf.toString();
    }

    public String getYmdStr() {
        StringBuilder buf = new StringBuilder(8).append(Integer.toString(year));
        format(month, buf);
        format(day, buf);
        return buf.toString();
    }

    public static YearMonthDay parseYmdStr(String s) {
        if (s == null || (s = s.trim()).isEmpty()) {
            throw new IllegalArgumentException("YearMonthDay parse string is empty!");
        }
        if (s.length() != 8) {
            throw new IllegalArgumentException("YearMonthDay parse string has incorrect length (must be yyyymmdd e.g. 8): " + s);
        }
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                throw new IllegalArgumentException("YearMonthDay parse string must contains digits only: " + s);
            }
        }
        int y = Integer.valueOf(s.substring(0, 4));
        int m = Integer.valueOf(s.substring(4, 6));
        int d = Integer.valueOf(s.substring(6, 8));
        return new YearMonthDay(y, m, d);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(10);
        format(day, sb);
        sb.append(SEPARATOR);
        format(month, sb);
        sb.append(SEPARATOR);
        sb.append(Integer.toString(year));
        return sb.toString();
    }
    
    public String toIso8601() {
        final StringBuilder sb = new StringBuilder(10);
        sb.append(Integer.toString(year));
        sb.append('-');
        format(month, sb);
        sb.append('-');
        format(day, sb);
        return sb.toString();
    }

    private static void format(final int value, StringBuilder buf) {
        if (value > 9) {
            buf.append(Integer.toString(value));
        } else {
            buf.append(ZERO_SYMBOL).append((char) (value + 48));
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.year;
        hash = 71 * hash + this.month;
        hash = 71 * hash + this.day;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final YearMonthDay other = (YearMonthDay) obj;
        if (this.year != other.year) {
            return false;
        }
        if (this.month != other.month) {
            return false;
        }
        return this.day == other.day;
    }

    @Override
    public int compareTo(YearMonthDay o) {
        if (null == o) {
            throw new NullPointerException();
        }
        int res = compare(year, o.year);
        if (res == 0) {
            res = compare(month, o.month);
        }
        if (res == 0) {
            res = compare(day, o.day);
        }
        return res;
    }

    private static int compare(int x, int y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

    public boolean before(YearMonthDay when) {
        return compareTo(when) < 0;
    }

    public boolean after(YearMonthDay when) {
        return compareTo(when) > 0;
    }
}
