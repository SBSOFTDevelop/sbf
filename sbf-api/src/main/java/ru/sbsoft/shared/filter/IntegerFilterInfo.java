package ru.sbsoft.shared.filter;

import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FilterTypeEnum;

public class IntegerFilterInfo extends FilterInfo<Integer> {

	public IntegerFilterInfo() {
        this(null, null);
	}

	public IntegerFilterInfo(String columnName, Integer value) {
		super(columnName, null, FilterTypeEnum.INTEGER, value);
	}
}
