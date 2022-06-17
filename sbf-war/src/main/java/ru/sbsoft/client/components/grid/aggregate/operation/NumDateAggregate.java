package ru.sbsoft.client.components.grid.aggregate.operation;

import java.math.BigDecimal;
import java.util.Date;
import ru.sbsoft.client.components.grid.column.CustomColumnConfig;
import ru.sbsoft.client.components.grid.column.NumericColumnConfig;
import ru.sbsoft.client.components.grid.column.TemporalColumnConfig;
import ru.sbsoft.shared.meta.Aggregate;
import ru.sbsoft.shared.meta.Row;

/**
 *
 * @author Kiselev
 */
public abstract class NumDateAggregate extends AbstractAggregate {

    protected NumDateAggregate(Aggregate aggregate) {
        super(aggregate, NumericColumnConfig.class, TemporalColumnConfig.class);
    }

    @Override
    protected String calcImpl(CustomColumnConfig c, Iterable<Row> rows) {
        if (c instanceof NumericColumnConfig) {
            return format(calcNum((NumericColumnConfig) c, rows), (NumericColumnConfig)c);
        } else if (c instanceof TemporalColumnConfig) {
            return format(calcDate((TemporalColumnConfig) c, rows), (TemporalColumnConfig)c);
        } else {
            return NO_RESULT;
        }
    }
    
    protected abstract Number calcNum(NumericColumnConfig<Number> c, Iterable<Row> rows);

    protected abstract Date calcDate(TemporalColumnConfig c, Iterable<Row> rows);

    protected final int compare(Number num1, Number num2) {
        if(num1 == num2){
            return 0;
        }
        if(num1 == null){
            return -1;
        }
        if(num2 == null){
            return 1;
        }
        BigDecimal num1bd = toBigDecimal(num1);
        BigDecimal num2bd = toBigDecimal(num2);
        return num1bd.compareTo(num2bd);
    }

    protected final Number max(Number num1, Number num2) {
        return compare(num1, num2) < 0 ? num2 : num1;
    }

}
