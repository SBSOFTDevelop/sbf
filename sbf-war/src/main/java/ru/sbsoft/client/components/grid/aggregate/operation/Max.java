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
public class Max extends NumDateAggregate {

    public Max() {
        super(Aggregate.MAX);
    }

    @Override
    protected Number calcNum(NumericColumnConfig<Number> c, Iterable<Row> rows) {
        Number max = null;
        for (Row r : rows) {
            max = max(max, c.getValueProvider().getValue(r));
        }
        return max;
    }

    @Override
    protected Date calcDate(TemporalColumnConfig c, Iterable<Row> rows) {
        Date max = null;
        for (Row r : rows) {
            Date d = c.getValueProvider().getValue(r);
            if (max == null || (d != null && max.compareTo(d) < 0)) {
                max = d;
            }
        }
        return max;
    }

}
