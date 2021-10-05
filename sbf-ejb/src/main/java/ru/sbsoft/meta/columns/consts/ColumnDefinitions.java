package ru.sbsoft.meta.columns.consts;

import ru.sbsoft.meta.columns.ColumnKind;
import ru.sbsoft.shared.Condition;

/**
 * Интерфейс определяет константы для типов колонок сетки и условий.
 *
 * @author balandin
 * @since Feb 25, 2015 4:22:31 PM
 */
public interface ColumnDefinitions {

    public static final String DISTINCT = "DISTINCT";

    public static final ColumnKind KEY = ColumnKind.KEY;
    public static final ColumnKind TEMPORAL_KEY = ColumnKind.TEMPORAL_KEY;
    public static final ColumnKind IDENTIFIER = ColumnKind.IDENTIFIER;
    public static final ColumnKind DATE = ColumnKind.DATE;
    public static final ColumnKind DATE_TIME = ColumnKind.DATE_TIME;
    public static final ColumnKind TIMESTAMP = ColumnKind.TIMESTAMP;
    public static final ColumnKind VCHAR = ColumnKind.VCHAR;
    public static final ColumnKind BOOL = ColumnKind.BOOL;
    public static final ColumnKind INTEGER = ColumnKind.INTEGER;
    public static final ColumnKind ADDRESS = ColumnKind.ADDRESS;
    public static final ColumnKind CURRENCY = ColumnKind.CURRENCY;
    public static final ColumnKind YMDAY = ColumnKind.YMDAY;

// alias
    public static final ColumnKind NUMBER = ColumnKind.CURRENCY;
    public static final ColumnKind TEXT = ColumnKind.VCHAR;
    //
    public static final Condition EQ = Condition.EQUAL;
    public static final Condition GT = Condition.GREATER;
    public static final Condition LT = Condition.LESS;
    public static final Condition GE = Condition.GREATER_OR_EQUAL;
    public static final Condition LE = Condition.LESS_OR_EQUAL;
    public static final Condition CONTAINS = Condition.CONTAINS;
    public static final Condition STARTS_WITH = Condition.STARTS_WITH;
    public static final Condition IN_RANGE = Condition.IN_RANGE;
    public static final Condition LIKE = Condition.LIKE;
}
