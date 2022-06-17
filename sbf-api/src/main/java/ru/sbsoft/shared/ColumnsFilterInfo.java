package ru.sbsoft.shared;

import ru.sbsoft.shared.filter.StringFilterInfo;

public class ColumnsFilterInfo extends StringFilterInfo {

    public ColumnsFilterInfo() {
    }

    public ColumnsFilterInfo(String columnName, ComparisonEnum comparisonEnum, String value) {
        super(columnName, comparisonEnum, value);
    }

    public String getColumnName2() {
        return getValue();
    }

    public void setColumnName2(String name) {
        setValue(name);
    }
}
