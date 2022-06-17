package ru.sbsoft.shared.meta;

import java.math.BigDecimal;

/**
 *
 * @author sokolov
 */
public class BigDecimalAggregatesWrapper extends AggregatesWrapper<BigDecimal>{

    public BigDecimalAggregatesWrapper() {
    }

    public BigDecimalAggregatesWrapper(Aggregate aggregates, String format, BigDecimal value) {
        super(aggregates, format, value);
    }
    
}
