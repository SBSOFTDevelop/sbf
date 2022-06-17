package ru.sbsoft.server.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import ru.sbsoft.sbf.app.model.YearMonthDay;
import ru.sbsoft.shared.WeekDay;

/**
 *
 * @author Fedor Resnyanskiy
 */
public class YearMonthDayConverter {

    public static Date convert(final YearMonthDay sbDate) {
        if (null == sbDate) {
            return null;
        }
        final Calendar calendar = new GregorianCalendar(sbDate.getYear(), sbDate.getMonth() - 1, sbDate.getDay());
        SrvUtl.truncToDate(calendar);
        return calendar.getTime();
    }

    public static YearMonthDay convert(final Date date) {
        if (null == date) {
            return null;
        }
        final Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return new YearMonthDay(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    public static YearMonthDay convert(final LocalDate d) {
        return d != null ? new YearMonthDay(d.getYear(), d.getMonthValue(), d.getDayOfMonth()) : null;
    }

    public static LocalDate toLocalDate(YearMonthDay d) {
        return d != null ? LocalDate.of(d.getYear(), d.getMonth(), d.getDay()) : null;
    }

    public static YearMonthDay add(final YearMonthDay sbDate, ChronoUnit unit, int amount) {
        if (amount == 0) {
            return sbDate;
        }
        return convert(toLocalDate(sbDate).plus(amount, unit));
    }

    public static YearMonthDay addDays(final YearMonthDay sbDate, int amount) {
        if (amount == 0) {
            return sbDate;
        }
        return convert(toLocalDate(sbDate).plusDays(amount));
    }

    public static YearMonthDay addMonths(final YearMonthDay sbDate, int amount) {
        if (amount == 0) {
            return sbDate;
        }
        return convert(toLocalDate(sbDate).plusMonths(amount));
    }

    public static YearMonthDay addYears(final YearMonthDay sbDate, int amount) {
        if (amount == 0) {
            return sbDate;
        }
        return convert(toLocalDate(sbDate).plusYears(amount));
    }

    public static WeekDay getWeekDay(final YearMonthDay ymd) {
        return SrvUtl.toWeekDay(toLocalDate(ymd).getDayOfWeek());
    }
}
