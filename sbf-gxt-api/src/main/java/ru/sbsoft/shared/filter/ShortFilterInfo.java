package ru.sbsoft.shared.filter;

import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FilterTypeEnum;

public class ShortFilterInfo extends FilterInfo<Short> {

	public ShortFilterInfo() {
        this(null, null);
	}

	public ShortFilterInfo(String columnName, Short value) {
		super(columnName, null, FilterTypeEnum.SHORT, value);
	}
}
