package ru.sbsoft.meta.columns.style.condition;

import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.meta.columns.style.IColumnConditionFactory;

/**
 *
 * @author Kiselev
 */
public class CNe<V> extends _Ne<V> implements IColumnConditionFactory<V> {

    public CNe(V val) {
        super(null, val);
    }
    
    public CNe(ColumnInfo<V> col2) {
        super(null, col2);
    }

    @Override
    public void setColumn(ColumnInfo<V> col) {
        this.col = col;
    }
    
}
