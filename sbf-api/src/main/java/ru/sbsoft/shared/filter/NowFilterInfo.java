package ru.sbsoft.shared.filter;

import java.util.Date;
import ru.sbsoft.shared.ComparisonEnum;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FilterTypeEnum;

/**
 * Класс представляющий элемент пользовательского фильтра типа {@link java.util.Date}.
 * @author balandin
 */
public class NowFilterInfo extends FilterInfo<Date> {

    public NowFilterInfo() {
        this(null);
    }

    public NowFilterInfo(String columnName) {
        this(columnName, null);
    }

    public NowFilterInfo(String columnName, ComparisonEnum comparisonm) {
        super(columnName, comparisonm, FilterTypeEnum.DATE, null);
    }

    @Override
    public Date getValue() {
        return new Date();
    }
}
