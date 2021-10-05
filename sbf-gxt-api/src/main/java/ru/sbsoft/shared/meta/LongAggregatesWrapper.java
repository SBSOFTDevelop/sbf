package ru.sbsoft.shared.meta;

/**
 *
 * @author sokolov
 */
public class LongAggregatesWrapper extends AggregatesWrapper<Long> {

    public LongAggregatesWrapper() {
    }

    public LongAggregatesWrapper(Aggregate aggregates, String format, Long value) {
        super(aggregates, format, value);
    }

}
