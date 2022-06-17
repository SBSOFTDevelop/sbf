package ru.sbsoft.meta.columns.consts;

import ru.sbsoft.meta.columns.ColumnKind;
import ru.sbsoft.meta.columns.IdNameColumnInfo;
import ru.sbsoft.shared.Condition;
import ru.sbsoft.shared.util.IdNameLong;

/**
 * Интерфейс определяет константы для типов колонок сетки и условий.
 *
 * @author balandin
 * @since Feb 25, 2015 4:22:31 PM
 */
public interface ColumnDefinitions {

    String DISTINCT = "DISTINCT";

    ColumnKind KEY = ColumnKind.KEY;
    ColumnKind TEMPORAL_KEY = ColumnKind.TEMPORAL_KEY;
    ColumnKind IDENTIFIER = ColumnKind.IDENTIFIER;
    ColumnKind DATE = ColumnKind.DATE;
    ColumnKind DATE_TIME = ColumnKind.DATE_TIME;
    ColumnKind TIMESTAMP = ColumnKind.TIMESTAMP;
    ColumnKind VCHAR = ColumnKind.VCHAR;
    ColumnKind BOOL = ColumnKind.BOOL;
    ColumnKind INTEGER = ColumnKind.INTEGER;
    ColumnKind ADDRESS = ColumnKind.ADDRESS;
    ColumnKind CURRENCY = ColumnKind.CURRENCY;
    ColumnKind YMDAY = ColumnKind.YMDAY;
    ColumnKind<IdNameLong, IdNameColumnInfo> ID_NAME = ColumnKind.ID_NAME;

// alias
    ColumnKind NUMBER = ColumnKind.CURRENCY;
    ColumnKind TEXT = ColumnKind.VCHAR;
    //
    Condition EQ = Condition.EQUAL;
    Condition GT = Condition.GREATER;
    Condition LT = Condition.LESS;
    Condition GE = Condition.GREATER_OR_EQUAL;
    Condition LE = Condition.LESS_OR_EQUAL;
    Condition CONTAINS = Condition.CONTAINS;
    Condition STARTS_WITH = Condition.STARTS_WITH;
    Condition IN_RANGE = Condition.IN_RANGE;
    Condition LIKE = Condition.LIKE;
}
