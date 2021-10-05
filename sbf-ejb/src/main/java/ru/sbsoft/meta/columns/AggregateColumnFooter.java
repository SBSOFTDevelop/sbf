package ru.sbsoft.meta.columns;

import java.math.BigDecimal;
import ru.sbsoft.shared.meta.Aggregate;
import ru.sbsoft.shared.meta.BigDecimalAggregatesWrapper;
import ru.sbsoft.shared.meta.IntegerAggregatesWrapper;
import ru.sbsoft.shared.meta.LongAggregatesWrapper;
import ru.sbsoft.shared.meta.Wrapper;

/**
 * Класс для построения агрегированных запросов и вывода значений в футер.
 * Переопределяет метод {@link #build(java.lang.String)}.
 *
 * @author balandin
 * @since May 16, 2014 3:57:42 PM
 */
public class AggregateColumnFooter extends AbstractColumnFooter {

    private final Aggregate aggregate;
    private final String format;

    public AggregateColumnFooter(Aggregate aggregates, String format) {
        super();
        this.aggregate = aggregates;
        this.format = format;
    }

    public Aggregate getAggregate() {
        return aggregate;
    }

    /**
     *
     * @param clause -имя поля по которому идет агрегация
     * @return обёрнутый в функцию NVL (Oracle) агрегат. Например:
     * <code>"NVL(COUNT(field1), 0)".</code>
     */
    @Override
    public String build(String clause) {
        return "$NVL(" + aggregate.name() + '(' + clause + "), 0)";
    }

    @Override
    public Wrapper wrap(Object value) {
        if (value instanceof BigDecimal) {
            return new BigDecimalAggregatesWrapper(aggregate, format, (BigDecimal) value);
        } else if (value instanceof Long) {
            return new LongAggregatesWrapper(aggregate, format, (Long) value);
        } else if (value instanceof Integer) {
            return new IntegerAggregatesWrapper(aggregate, format, (Integer) value);
        }
        throw new IllegalArgumentException("Unsupported type " + value.getClass().getName());
    }
}
