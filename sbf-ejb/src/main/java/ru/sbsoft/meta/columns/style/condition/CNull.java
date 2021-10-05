package ru.sbsoft.meta.columns.style.condition;

import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.meta.columns.style.IColumnConditionFactory;

/**
 *
 * @author Kiselev
 */
public class CNull<V> extends _Null implements IColumnConditionFactory<V> {

    public CNull() {
        super(null);
    }

    @Override
    public void setColumn(ColumnInfo<V> col) {
        this.col = col;
    }
    
}
