package ru.sbsoft.meta.columns.style.condition;

import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.shared.grid.condition.EqCellCondition;

/**
 *
 * @author Kiselev
 */
public class _Ne<V> extends _Eq<V> {

    public _Ne(ColumnInfo<V> col, V val) {
        super(col, val);
    }
    
    public _Ne(ColumnInfo<V> col, ColumnInfo<V> col2) {
        super(col, col2);
    }

    @Override
    public EqCellCondition createCondition() {
        return super.createCondition().setNotEq();
    }
    
}
