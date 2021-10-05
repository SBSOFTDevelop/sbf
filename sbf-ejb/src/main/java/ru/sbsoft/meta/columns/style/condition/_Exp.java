package ru.sbsoft.meta.columns.style.condition;

import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.meta.columns.style.IConditionFactory;
import ru.sbsoft.meta.columns.style.IGridConditionFactory;
import ru.sbsoft.shared.grid.condition.ComplexGridCondition;
import ru.sbsoft.shared.grid.condition.IGridCondition;

/**
 * Конструктор логически сложных условий для использования при задании стилей
 * ячеек в шаблонах браузеров. Добавленные методами {@code and} и {@code or}
 * вычисляются как булевы значения, соединенные соответствующими булевыми
 * операторами без группирующих скобок.
 *
 * @author Kiselev
 */
abstract class _Exp<Self extends _Exp<Self>> {

    protected final List<CondInf> conds = new ArrayList<>();

    _Exp(IConditionFactory c) {
        conds.add(new CondInf(c, false));
    }

    public Self or(IGridConditionFactory c) {
        conds.add(new CondInf(c, true));
        return (Self) this;
    }

    public Self and(IGridConditionFactory c) {
        conds.add(new CondInf(c, false));
        return (Self) this;
    }

    public IGridCondition createCondition() {
        ComplexGridCondition cond = new ComplexGridCondition();
        for (CondInf c : conds) {
            if (c.isOr()) {
                cond.addConditionOr(c.getF().createCondition());
            } else {
                cond.addCondition(c.getF().createCondition());
            }
        }
        return cond;
    }

    protected class CondInf {

        private final boolean or;
        private final IConditionFactory f;

        public CondInf(IConditionFactory f, boolean or) {
            this.or = or;
            this.f = f;
        }

        public boolean isOr() {
            return or;
        }

        public IConditionFactory getF() {
            return f;
        }

    }

}
