
package ru.sbsoft.client.components.grid.aggregate.operation;

import java.util.Date;
import ru.sbsoft.client.components.grid.column.NumericColumnConfig;
import ru.sbsoft.client.components.grid.column.TemporalColumnConfig;
import ru.sbsoft.shared.meta.Aggregate;
import ru.sbsoft.shared.meta.Row;

/**
 *
 * @author Kiselev
 */
public class Min extends NumDateAggregate {

    public Min() {
        super(Aggregate.MIN);
    }

    @Override
    protected Number calcNum(NumericColumnConfig<Number> c, Iterable<Row> rows) {
        Number min = null;
        for (Row r : rows) {
            Number num = c.getValueProvider().getValue(r);
            if (min == null || (num != null && compare(min, num) > 0)) {
                min = num;
            }
        }
        return min;
    }

    @Override
    protected Date calcDate(TemporalColumnConfig c, Iterable<Row> rows) {
        Date min = null;
        for (Row r : rows) {
            Date d = c.getValueProvider().getValue(r);
            if (min == null || (d != null && min.compareTo(d) > 0)) {
                min = d;
            }
        }
        return min;
    }

}
