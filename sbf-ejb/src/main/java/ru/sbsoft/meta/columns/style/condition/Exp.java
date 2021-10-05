package ru.sbsoft.meta.columns.style.condition;

import ru.sbsoft.meta.columns.style.IGridConditionFactory;

/**
 * Конструктор логически сложных условий для использования при задании стилей
 * ячеек таблицы в шаблонах браузеров. Добавленные методами {@code and} и
 * {@code or} вычисляются как булевы значения, соединенные соответствующими
 * булевыми операторами без группирующих скобок.
 *
 * @author Kiselev
 */
public class Exp extends _Exp<Exp> implements IGridConditionFactory {

    public Exp(IGridConditionFactory c) {
        super(c);
    }

    @Override
    public Exp and(IGridConditionFactory c) {
        return super.and(c);
    }

    @Override
    public Exp or(IGridConditionFactory c) {
        return super.or(c);
    }
    
}
