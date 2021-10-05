package ru.sbsoft.shared.filter;

import java.math.BigDecimal;
import ru.sbsoft.shared.ComparisonEnum;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FilterTypeEnum;

/**
 * Класс представляющий элемент пользовательского фильтра типа {@link java.math.BigDecimal}.
 * @author balandin
 */
public class BigDecimalFilterInfo extends FilterInfo<BigDecimal> {

    public BigDecimalFilterInfo() {
        this(null, null);
    }

    public BigDecimalFilterInfo(String columnName, BigDecimal value) {
        this(columnName, null, value);
    }

    public BigDecimalFilterInfo(String columnName, ComparisonEnum comparison, BigDecimal value) {
        super(columnName, comparison, FilterTypeEnum.NUMERIC, value);
    }
}
