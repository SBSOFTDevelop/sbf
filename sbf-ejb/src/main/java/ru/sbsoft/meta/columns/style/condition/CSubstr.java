package ru.sbsoft.meta.columns.style.condition;

import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.meta.columns.style.IColumnConditionFactory;

/**
 *
 * @author Kiselev
 */
public class CSubstr extends _Substr implements IColumnConditionFactory<String> {

    public CSubstr(String substr) {
        super(null, substr);
    }

    @Override
    public void setColumn(ColumnInfo<String> col) {
        this.col = col;
    }
    
}
