package ru.sbsoft.meta.columns.style.condition;

import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.meta.columns.style.IColumnConditionFactory;

/**
 *
 * @author Kiselev
 */
public class CEq<V> extends _Eq<V> implements IColumnConditionFactory<V>{

    public CEq(V val) {
        super(null, val);
    }
    
    public CEq(ColumnInfo<V> col2) {
        super(null, col2);
    }
    
    @Override
    public void setColumn(ColumnInfo<V> col) {
        this.col = col;
    }
    
}
