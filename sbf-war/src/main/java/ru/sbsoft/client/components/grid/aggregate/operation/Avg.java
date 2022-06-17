package ru.sbsoft.client.components.grid.aggregate.operation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import ru.sbsoft.client.components.grid.column.CustomColumnConfig;
import ru.sbsoft.client.components.grid.column.NumericColumnConfig;
import ru.sbsoft.shared.meta.Aggregate;
import ru.sbsoft.shared.meta.Row;

/**
 *
 * @author Kiselev
 */
public class Avg extends AbstractAggregate {

    public Avg() {
        super(Aggregate.AVG, NumericColumnConfig.class);
    }

    @Override
    protected String calcImpl(CustomColumnConfig c, Iterable<Row> rows) {
        if (c instanceof NumericColumnConfig) {
            NumericColumnConfig<Number> nc = (NumericColumnConfig)c;
            BigDecimal sum = BigDecimal.ZERO;
            long count = 0;
            for (Row r : rows) {
                BigDecimal num = toBigDecimal(nc.getValueProvider().getValue(r));
                if (num != null) {
                    sum = sum.add(num);
                    count++;
                }
            }
            return count == 0 ? NO_RESULT : format(sum.divide(BigDecimal.valueOf(count), 100, RoundingMode.HALF_UP), nc);
        } else {
            return NO_RESULT;
        }
    }

}
