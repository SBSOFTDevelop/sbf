package ru.sbsoft.meta.columns.style.condition;

import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.meta.columns.style.IColumnConditionFactory;
import ru.sbsoft.meta.columns.style.IGridConditionFactory;

/**
 * Конструктор логически сложных условий для использования при задании стилей ячеек колонки в шаблонах браузеров.
 * Добавленные методами {@code and} и {@code or} вычисляются как булевы значения, соединенные соответствующими булевыми операторами без группирующих скобок.
 * @author Kiselev
 */
public class CExp<V> extends _Exp<CExp<V>> implements IColumnConditionFactory<V> {

    public CExp(IGridConditionFactory c) {
        super(c);
    }
    
    public CExp(IColumnConditionFactory c) {
        super(c);
    }

    public CExp<V> or(IColumnConditionFactory<V> c) {
        conds.add(new CondInf(c, true));
        return this;
    }

    public CExp<V> and(IColumnConditionFactory<V> c) {
        conds.add(new CondInf(c, false));
        return this;
    }

    @Override
    public void setColumn(ColumnInfo<V> col) {
        for(CondInf c : conds){
            if(c instanceof IColumnConditionFactory){
                ((IColumnConditionFactory)c).setColumn(col);
            }
        }
    }
    
}
