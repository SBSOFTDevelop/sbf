package ru.sbsoft.meta.columns.style.condition;

import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.meta.columns.style.IGridConditionFactory;

/**
 *
 * @author Kiselev
 */
public class Substr extends _Substr implements IGridConditionFactory {

    public Substr(ColumnInfo<String> col, String substr) {
        super(col, substr);
    }

}
