package ru.sbsoft.shared.filter;

import java.util.Date;
import ru.sbsoft.shared.ComparisonEnum;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FilterTypeEnum;

/**
 * Класс представляющий элемент пользовательского фильтра типа {@link java.util.Date}.
 * @author balandin
 */
public class DateFilterInfo extends FilterInfo<Date> {

    public DateFilterInfo() {
        this(null, null);
    }

    public DateFilterInfo(String columnName, Date value) {
        this(columnName, null, value);
    }

    public DateFilterInfo(String columnName, ComparisonEnum comparisonm, Date value) {
        super(columnName, comparisonm, FilterTypeEnum.DATE, value);
    }

    public static DateFilterInfo createCurrentDateFilter(final Date value) {
        return new DateFilterInfo(FilterConsts.CURRENT_DATE_FIELD, ComparisonEnum.eq, value);
    }
}
