package ru.sbsoft.server.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import ru.sbsoft.sbf.app.model.YearMonthDay;

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
        return truncateDate(calendar.getTime());
    }

    public static Date truncateDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, c.getActualMinimum(Calendar.HOUR_OF_DAY));
        c.set(Calendar.MINUTE, c.getActualMinimum(Calendar.MINUTE));
        c.set(Calendar.SECOND, c.getActualMinimum(Calendar.SECOND));
        c.set(Calendar.MILLISECOND, c.getActualMinimum(Calendar.MILLISECOND));

//        if (c.getTime().compareTo(date) != 0) {
//            throw new AppException("%s != %s", c.getTime(), date);
//        }
        return c.getTime();

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
        return new YearMonthDay(d.getYear(), d.getMonthValue(), d.getDayOfMonth());
    }
    
    public static LocalDate toLocalDate(YearMonthDay d){
        return LocalDate.of(d.getYear(), d.getMonth(), d.getDay());
    }
    
    public static YearMonthDay add(final YearMonthDay sbDate, ChronoUnit unit, int amount) {
        if(amount == 0){
            return sbDate;
        }
        return convert(toLocalDate(sbDate).plus(amount, unit));
    }
    
    public static YearMonthDay addDays(final YearMonthDay sbDate, int amount) {
        if(amount == 0){
            return sbDate;
        }
        return convert(toLocalDate(sbDate).plusDays(amount));
    }
    
    public static YearMonthDay addMonths(final YearMonthDay sbDate, int amount) {
        if(amount == 0){
            return sbDate;
        }
        return convert(toLocalDate(sbDate).plusMonths(amount));
    }
    
    public static YearMonthDay addYears(final YearMonthDay sbDate, int amount) {
        if(amount == 0){
            return sbDate;
        }
        return convert(toLocalDate(sbDate).plusYears(amount));
    }
}
