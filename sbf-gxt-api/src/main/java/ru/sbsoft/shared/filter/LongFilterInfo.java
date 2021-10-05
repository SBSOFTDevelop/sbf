package ru.sbsoft.shared.filter;

import ru.sbsoft.shared.ComparisonEnum;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FilterTypeEnum;

public class LongFilterInfo extends FilterInfo<Long> {

    public LongFilterInfo() {
        this(null, null);
    }

    public LongFilterInfo(String columnName, Long value) {
        this(columnName, null, value);
    }

    public LongFilterInfo(String columnName, ComparisonEnum comparison, Long value) {
        super(columnName, comparison, FilterTypeEnum.LONG, value);
    }
}
