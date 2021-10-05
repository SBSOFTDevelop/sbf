package ru.sbsoft.shared.filter;

import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FilterTypeEnum;

/**
 * Класс представляющий элемент пользовательского фильтра типа {@link java.lang.Boolean}.
 * @author panarin
 */
public class BooleanFilterInfo extends FilterInfo<Boolean> {

    public BooleanFilterInfo() {
        this(null, null);
    }

    public BooleanFilterInfo(String columnName, Boolean value) {
        super(columnName, null, FilterTypeEnum.BOOLEAN, value);
    }
}
