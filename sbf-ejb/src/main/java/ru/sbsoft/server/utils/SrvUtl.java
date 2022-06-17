package ru.sbsoft.server.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.reflections.Reflections;
import ru.sbsoft.sbf.app.model.YearMonthDay;
import ru.sbsoft.shared.WeekDay;
import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.api.i18n.consts.SBFExceptionStr;
import ru.sbsoft.shared.consts.Month;
import ru.sbsoft.shared.exceptions.ApplicationException;
import ru.sbsoft.shared.interfaces.IdItem;
import ru.sbsoft.shared.interfaces.NamedItem;
import ru.sbsoft.shared.model.YearMonth;

/**
 *
 * @author Kiselev
 */
public class SrvUtl {

    private static final SimpleDateFormat DateFormatDDMMYYYY = new SimpleDateFormat("dd.MM.yyyy");
    private static final SimpleDateFormat DateFormatDDMMYY = new SimpleDateFormat("dd.MM.yy");
    private static final SimpleDateFormat DateFormatMMYYYY = new SimpleDateFormat("MM.yyyy");
    private static final DecimalFormat MoneyFormat = new DecimalFormat("###,###,###,##0.00");
    private static final DecimalFormat FloatFormat = new DecimalFormat("###########0.00");
    private static final DecimalFormat IntgrFormat = new DecimalFormat("###,###,###,##0");
    private static final DecimalFormat IntgrFormatAll = new DecimalFormat("#0");
    private static final DecimalFormat DecFormat10 = new DecimalFormat("0000000000");
    private static final DecimalFormat DecFormat2 = new DecimalFormat("00");
    private static final DecimalFormat DECIMAL_FMT = new DecimalFormat("########0.00");

    public static String formatDate(Date d) {
        return d != null ? getDateFormatDDMMYYYY().format(d) : "??.??.????";
    }

    public static SimpleDateFormat getDateFormatDDMMYYYY() {
        return (SimpleDateFormat) DateFormatDDMMYYYY.clone();
    }

    public static SimpleDateFormat getDateFormatDDMMYY() {
        return (SimpleDateFormat) DateFormatDDMMYY.clone();
    }

    public static SimpleDateFormat getDateFormatMMYYYY() {
        return (SimpleDateFormat) DateFormatMMYYYY.clone();
    }

    public static DecimalFormat getMoneyFormat() {
        return (DecimalFormat) MoneyFormat.clone();
    }

    public static DecimalFormat getFloatFormat() {
        return (DecimalFormat) FloatFormat.clone();
    }

    public static DecimalFormat getIntgrFormat() {
        return (DecimalFormat) IntgrFormat.clone();
    }

    public static DecimalFormat getIntgrFormatAll() {
        return (DecimalFormat) IntgrFormatAll.clone();
    }

    public static DecimalFormat getDecFormat10() {
        return (DecimalFormat) DecFormat10.clone();
    }

    public static DecimalFormat getDecFormat2() {
        return (DecimalFormat) DecFormat2.clone();
    }

    public static DecimalFormat getDECIMAL_FMT() {
        return (DecimalFormat) DECIMAL_FMT.clone();
    }

    public static Date getDate(final YearMonth ym, final int day) {
        return getDate(ym.getYear(), ym.getMonthNum(), day);
    }

    public static Date getDate(final int year, final int month, final int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, day);
        truncToDate(c);
        return c.getTime();
    }

    public static Date addDay(final Date d, final int days) {
        Calendar c = getCalendar(d);
        c.add(Calendar.DAY_OF_MONTH, days);
        return c.getTime();
    }

    public static boolean isLastMonthDay(Calendar c) {
        Calendar w = (Calendar) c.clone();
        truncToMonth(w);
        w.add(Calendar.MONTH, 1);
        w.add(Calendar.DAY_OF_MONTH, -1);
        return w.get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH);
    }

    public static Date truncToMonth(final Date d) {
        Calendar c;
        truncToMonth(c = getCalendar(d));
        return c.getTime();
    }

    public static void truncToYear(final Calendar c) {
        c.set(Calendar.MONTH, c.getActualMinimum(Calendar.MONTH));
        truncToMonth(c);
    }

    public static void truncToMonth(final Calendar c) {
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        truncToDate(c);
    }

    public static Date truncToDate(final Date d) {
        Calendar c;
        truncToDate(c = getCalendar(d));
        return c.getTime();
    }

    public static void truncToDate(final Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, c.getActualMinimum(Calendar.HOUR_OF_DAY));
        c.set(Calendar.MINUTE, c.getActualMinimum(Calendar.MINUTE));
        c.set(Calendar.SECOND, c.getActualMinimum(Calendar.SECOND));
        c.set(Calendar.MILLISECOND, c.getActualMinimum(Calendar.MILLISECOND));
    }

    public static BigDecimal roundPay(BigDecimal pay) {
        return pay.setScale(2, RoundingMode.HALF_UP);
    }

    public static int getMonthNum(final Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return c.get(Calendar.MONTH) + 1;
    }

    public static Month getMonth(Calendar c) {
        return Month.getByNum(c.get(Calendar.MONTH) + 1);
    }

    public static Month getMonth(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return getMonth(c);
    }

    public static NamedItem getMonthName(Calendar c) {
        return getMonth(c);
    }

    public static NamedItem getMonthName(Date d) {
        return getMonth(d);
    }

    public static YearMonth getYearMonth(Calendar c) {
        return new YearMonth(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
    }

    public static YearMonth getYearMonth(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return getYearMonth(c);
    }

    public static YearMonthDay getYearMonthDay(Date d) {
        return YearMonthDayConverter.convert(d);
    }

    public static Date toDate(YearMonth ym) {
        return getDate(ym.getYear(), ym.getMonthNum(), 1);
    }

    public static Date toDate(YearMonthDay ymd) {
        return YearMonthDayConverter.convert(ymd);
    }

    public static int getDay(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static Calendar getCalendar(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return c;
    }

    public static String findFieldNameByAnnotationMarker(Class<?> clazz, Class<? extends Annotation> annotationClazz) {
        Class c = clazz;
        while (c != null) {
            for (Field f : c.getDeclaredFields()) {
                if (f.isAnnotationPresent(annotationClazz)) {
                    return f.getName();
                }
            }
            c = c.getSuperclass();
        }
        return null;
    }

    public static boolean isFieldPresent(Class<?> clazz, String fieldName) {
        Class c = clazz;
        while (c != null) {
            for (Field f : c.getDeclaredFields()) {
                if (fieldName.equals(f.getName())) {
                    return true;
                }
            }
            c = c.getSuperclass();
        }
        return false;
    }

    public static <T> T firstNonNull(T... values) {
        for (T v : values) {
            if (v != null) {
                return v;
            }
        }
        return null;
    }

    public static Date getMonthStart(Date d) {
        return truncToMonth(d);
    }

    public static void toMonthEnd(Calendar c) {
        truncToMonth(c);
        c.add(Calendar.MONTH, 1);
        c.add(Calendar.MILLISECOND, -1);
    }

    public static void toMonthEndDate(Calendar c) {
        toMonthEnd(c);
        truncToDate(c);
    }

    public static Date getMonthEnd(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        toMonthEnd(c);
        return c.getTime();
    }

    public static Date getMonthEndDate(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        toMonthEndDate(c);
        return c.getTime();
    }

    public static <T> T notNull(T o, String msg) {
        if (o == null) {
            throw new IllegalArgumentException(msg);
        }
        return o;
    }

    public static <T> T notNull(T o, I18nResourceInfo resourceInfo, String... parameters) {
        if (o == null) {
            throw new ApplicationException(resourceInfo, parameters);
        }
        return o;
    }

    public static <E extends Enum> void checkIn(E e, E check1, E... otherChecks) {
        notNull(check1, SBFExceptionStr.compareMustValue);
        Set<E> s = EnumSet.of(check1, otherChecks);
        if (e == null || !s.contains(e)) {

            throw new ApplicationException(SBFExceptionStr.valueNotIncuded, e != null ? e.name() : null, s.toString());
        }
    }

    public static <K, T extends IdItem<K>> Map<K, List<T>> collectClassesById(String packagePath, Class<T> type, Map<K, List<T>> store) {
        if (store == null) {
            store = new HashMap<>();
        }
        Reflections reflections = new Reflections(packagePath);
        Set<Class<? extends T>> proClasses = reflections.getSubTypesOf(type);
        for (Class<? extends T> c : proClasses) {
            try {
                if (!Modifier.isAbstract(c.getModifiers()) && Modifier.isPublic(c.getModifiers())) {
                    T p = c.newInstance();
                    K id = p.getId();
                    if (id != null) {
                        List<T> l = store.get(id);
                        if (l == null) {
                            l = new ArrayList<>();
                            store.put(id, l);
                        }
                        l.add(p);
                    } else {
                        Logger.getLogger(SrvUtl.class.getName()).log(Level.SEVERE, "Can''t collect class {0}: getId() is null!", c.getName());
                    }
                }
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(SrvUtl.class.getName()).log(Level.SEVERE, "Can't instantiate: " + c.getName(), ex);
            }
        }
        return store;
    }

    public static boolean isOverlaps(Date start1, Date end1, Date start2, Date end2) {
        return start1.compareTo(end2) <= 0 && start2.compareTo(end1) <= 0;
    }

    public static boolean isOverlaps(YearMonthDay start1, YearMonthDay end1, YearMonthDay start2, YearMonthDay end2) {
        return start1.compareTo(end2) <= 0 && start2.compareTo(end1) <= 0;
    }

    public static BigDecimal toBigDecimal(Number n) {
        if (n == null || (n instanceof BigDecimal)) {
            return (BigDecimal) n;
        } else if (n instanceof BigInteger) {
            return new BigDecimal((BigInteger) n);
        } else if ((n instanceof Integer) || (n instanceof Long) || (n instanceof Short)) {
            return BigDecimal.valueOf(n.longValue());
        } else if (n instanceof Float) {
            if (((Float) n).isInfinite() || ((Float) n).isNaN()) {
                return null;
            } else {
                return BigDecimal.valueOf(n.doubleValue());
            }
        } else if (n instanceof Double) {
            if (((Double) n).isInfinite() || ((Double) n).isNaN()) {
                return null;
            } else {
                return BigDecimal.valueOf(n.doubleValue());
            }
        } else {
            return new BigDecimal(n.toString());
        }
    }

    public static BigDecimal toBigDecimal(Object n) {
        if (n instanceof Number) {
            return toBigDecimal((Number) n);
        }
        throw new IllegalArgumentException("Not a number type (for BigDecimal): " + n.getClass().getName());
    }

    public static WeekDay toWeekDay(DayOfWeek wd) {
        switch (wd) {
            case MONDAY:
                return WeekDay.MONDAY;
            case TUESDAY:
                return WeekDay.TUESDAY;
            case WEDNESDAY:
                return WeekDay.WEDNESDAY;
            case THURSDAY:
                return WeekDay.THURSDAY;
            case FRIDAY:
                return WeekDay.FRIDAY;
            case SATURDAY:
                return WeekDay.SATURDAY;
            case SUNDAY:
                return WeekDay.SUNDAY;
            default:
                throw new IllegalArgumentException("No instance of WeekDay matches to DayOfWeek's " + wd.toString());
        }
    }

    public static DayOfWeek toDayOfWeek(WeekDay wd) {
        switch (wd) {
            case MONDAY:
                return DayOfWeek.MONDAY;
            case TUESDAY:
                return DayOfWeek.TUESDAY;
            case WEDNESDAY:
                return DayOfWeek.WEDNESDAY;
            case THURSDAY:
                return DayOfWeek.THURSDAY;
            case FRIDAY:
                return DayOfWeek.FRIDAY;
            case SATURDAY:
                return DayOfWeek.SATURDAY;
            case SUNDAY:
                return DayOfWeek.SUNDAY;
            default:
                throw new IllegalArgumentException("No instance of DayOfWeek matches to WeekDay's " + wd.toString());
        }
    }
}
