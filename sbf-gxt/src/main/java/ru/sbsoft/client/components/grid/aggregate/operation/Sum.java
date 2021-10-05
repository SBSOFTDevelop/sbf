package ru.sbsoft.client.components.grid.aggregate.operation;

import java.math.BigDecimal;
import ru.sbsoft.client.components.grid.column.CustomColumnConfig;
import ru.sbsoft.client.components.grid.column.NumericColumnConfig;
import ru.sbsoft.shared.meta.Aggregate;
import ru.sbsoft.shared.meta.Row;

/**
 *
 * @author Kiselev
 */
public class Sum extends AbstractAggregate {

    public Sum() {
        super(Aggregate.SUM, NumericColumnConfig.class);
    }

    @Override
    protected String calcImpl(CustomColumnConfig c, Iterable<Row> rows) {
        if (c instanceof NumericColumnConfig) {
            NumericColumnConfig<Number> nc = (NumericColumnConfig)c;
            BigDecimal sum = BigDecimal.ZERO;
            for (Row r : rows) {
                BigDecimal num = toBigDecimal(nc.getValueProvider().getValue(r));
                if (num != null) {
                    sum = sum.add(num);
                }
            }
            return format(sum, nc);
        } else {
            return NO_RESULT;
        }
    }

}
