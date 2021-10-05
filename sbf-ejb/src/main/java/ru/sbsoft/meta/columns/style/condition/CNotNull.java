package ru.sbsoft.meta.columns.style.condition;

import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.meta.columns.style.IColumnConditionFactory;

/**
 *
 * @author Kiselev
 */
public class CNotNull<V> extends _NotNull implements IColumnConditionFactory<V>  {

    public CNotNull() {
        super(null);
    }
    
    @Override
    public void setColumn(ColumnInfo<V> col) {
        this.col = col;
    }
    
}
