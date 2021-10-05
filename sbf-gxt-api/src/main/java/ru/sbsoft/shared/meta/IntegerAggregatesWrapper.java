package ru.sbsoft.shared.meta;

/**
 *
 * @author sokolov
 */
public class IntegerAggregatesWrapper extends AggregatesWrapper<Integer> {

    public IntegerAggregatesWrapper() {
    }

    public IntegerAggregatesWrapper(Aggregate aggregates, String format, Integer value) {
        super(aggregates, format, value);
    }
    
}
