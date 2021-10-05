package ru.sbsoft.shared.filter;

import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.ComparisonEnum;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FilterTypeEnum;

public class StringFilterInfo extends FilterInfo<String> {

    public StringFilterInfo() {
        this(null, null);
    }

    public StringFilterInfo(String columnName, String value) {
        this(columnName, null, value);
    }

    public StringFilterInfo(String columnName, ComparisonEnum comparisonEnum, String value) {
        super(columnName, comparisonEnum, FilterTypeEnum.STRING, value);
    }

    @Override
    public String getSQLParameterName(int num) {
        return super.getSQLParameterName(num).concat(isCaseSensitive() ? Strings.EMPTY : "_UP");
    }
}
