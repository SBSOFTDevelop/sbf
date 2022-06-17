package ru.sbsoft.shared.filter;

import java.util.List;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FilterTypeEnum;

public class ListFilterInfo extends FilterInfo<List<String>> {

    public ListFilterInfo() {
        this(null, null);
    }

    public ListFilterInfo(String columnName, List<String> value) {
        super(columnName, null, FilterTypeEnum.LIST, value);
    }
}
