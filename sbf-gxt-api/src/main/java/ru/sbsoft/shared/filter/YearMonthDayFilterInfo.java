package ru.sbsoft.shared.filter;

import ru.sbsoft.sbf.app.model.YearMonthDay;
import ru.sbsoft.shared.ComparisonEnum;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FilterTypeEnum;

/**
 * Класс представляющий элемент пользовательского фильтра типа {@link ru.sbsoft.sbf.app.model.YearMonthDay}.
 * @author balandin
 */
public class YearMonthDayFilterInfo extends FilterInfo<YearMonthDay> {

    public YearMonthDayFilterInfo() {
        this(null, null);
    }

    public YearMonthDayFilterInfo(String columnName, YearMonthDay value) {
        this(columnName, ComparisonEnum.eq, value);
    }

    public YearMonthDayFilterInfo(String columnName, ComparisonEnum comparison, YearMonthDay value) {
        super(columnName, comparison, FilterTypeEnum.YMD, value);
    }
}
