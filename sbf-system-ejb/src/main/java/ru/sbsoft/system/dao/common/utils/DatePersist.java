package ru.sbsoft.system.dao.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import ru.sbsoft.shared.FilterTypeEnum;
import static ru.sbsoft.shared.FilterTypeEnum.DATE;
import static ru.sbsoft.shared.FilterTypeEnum.TIME;
import static ru.sbsoft.shared.FilterTypeEnum.TIMESTAMP;

/**
 * Сериализация дат
 *
 * @author balandin
 * @since Aug 6, 2015
 */
public class DatePersist {

    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    private static final String TIME_FORMAT_PATTERN = "HH:mm:ss";
    private static final String DATETIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final Map<FilterTypeEnum, Formatter> patterns = new HashMap<>();

    static {
        patterns.put(DATE, new Formatter(DATE_FORMAT_PATTERN));
        patterns.put(TIME, new Formatter(TIME_FORMAT_PATTERN));
        patterns.put(TIMESTAMP, new Formatter(DATETIME_FORMAT_PATTERN));
    }

    public static String format(FilterTypeEnum type, Date value) {
        return getFormatter(type).format(value);
    }

    public static Date parse(FilterTypeEnum type, String value) {
        if ("".equals(value)) {
            return null;
        }
        return getFormatter(type).parse(value);
    }

    private static Formatter getFormatter(FilterTypeEnum type) {
        Formatter f = patterns.get(type);
        if (f == null) {
            throw new IllegalArgumentException("formatter not found for type " + type);
        }
        return f;
    }

    private static class Formatter {

        private final String pattern;
        private DateFormat formatter;

        public DateFormat getFormatter() {
            if (formatter == null) {
                formatter = new SimpleDateFormat(pattern);
            }
            return formatter;
        }

        public Formatter(String pattern) {
            this.pattern = pattern;
        }

        private String format(Date value) {
            return getFormatter().format(value);
        }

        private Date parse(String value) {
            try {
                return getFormatter().parse(value);
            } catch (ParseException ex) {
                throw new IllegalArgumentException("invalid date value " + value, ex);
            }
        }
    }
}
