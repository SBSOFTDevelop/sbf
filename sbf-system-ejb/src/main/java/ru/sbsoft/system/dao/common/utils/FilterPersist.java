package ru.sbsoft.system.dao.common.utils;

import java.math.BigDecimal;
import java.util.Date;
import ru.sbsoft.sbf.app.model.YearMonthDay;
import ru.sbsoft.shared.FilterTypeEnum;
import static ru.sbsoft.shared.FilterTypeEnum.DATE;
import static ru.sbsoft.shared.FilterTypeEnum.LIST;
import static ru.sbsoft.shared.FilterTypeEnum.LOOKUP;
import static ru.sbsoft.shared.FilterTypeEnum.TIME;
import static ru.sbsoft.shared.FilterTypeEnum.TIMESTAMP;

/**
 * Сериализация значений фильтров
 *
 * @author balandin
 * @since Aug 6, 2015
 */
public class FilterPersist {

    private static final String BOOL_TRUE = "1";
    private static final String BOOL_FALSE = "0";

    public static String valueToString(FilterTypeEnum type, Object value) {
        if (value == null) {
            return null;
        }
        switch (type) {

            case INTEGER:
            case LONG:
            case SHORT:
                if (value instanceof BigDecimal) {

                    return String.valueOf(((BigDecimal) value).longValue());

                }
                break;

            case BOOLEAN:
                return (Boolean) value ? BOOL_TRUE : BOOL_FALSE;
            case DATE:
            case TIME:
            case TIMESTAMP:
                return DatePersist.format(type, (Date) value);
            case YMD:
                return ((YearMonthDay) value).getYmdStr();
            case LIST:
                // return Strings.join(((List<String>) value).toArray(), ",", true);
                throw new UnsupportedOperationException();
            case LOOKUP:
                // entity.setFILTER_LOOKUPS(createMergedLookUpFilters(entity, (List<LookupInfoModel>) filterInfo.getValue()));
                throw new UnsupportedOperationException();
        }
        return String.valueOf(value);
    }

    public static Object stringToValue(FilterTypeEnum type, String value) {
        if (value == null) {
            return null;
        }
        switch (type) {
            case BOOLEAN:
                if (BOOL_TRUE.equals(value)) {
                    return true;
                }
                if (BOOL_FALSE.equals(value)) {
                    return false;
                }
                throw new IllegalArgumentException("illegal boolean value " + value);
            case INTEGER:
                return Integer.valueOf(value);
            case LONG:
                return Long.valueOf(value);
            case NUMERIC:
                return new BigDecimal(value);
            case DATE:
            case TIME:
            case TIMESTAMP:
                return DatePersist.parse(type, value);
            case YMD:
                return YearMonthDay.parseYmdStr(value);
            case LIST:
                // return Arrays.asList(value.split(","));
                throw new UnsupportedOperationException();
            case LOOKUP:
                // entity.setFILTER_LOOKUPS(createMerg  edLookUpFilters(entity, (List<LookupInfoModel>) filterInfo.getValue()));
                throw new UnsupportedOperationException();
        }
        return value;
    }
}
