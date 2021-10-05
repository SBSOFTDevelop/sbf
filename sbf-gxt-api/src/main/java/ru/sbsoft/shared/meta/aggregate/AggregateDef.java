package ru.sbsoft.shared.meta.aggregate;

import ru.sbsoft.shared.meta.Aggregate;

/**
 *
 * @author Kiselev
 */
public class AggregateDef implements IAggregateDef {

    private String columnAlias;
    private Aggregate aggregate;
    private transient String aggregateAlias = null;

    private AggregateDef() {
    }

    public AggregateDef(String columnAlias, Aggregate aggregate) {
        this.columnAlias = columnAlias;
        this.aggregate = aggregate;
    }

    @Override
    public String getColumnAlias() {
        return columnAlias;
    }

    @Override
    public Aggregate getAggregate() {
        return aggregate;
    }

    @Override
    public String getAggregateAlias() {
        if(aggregateAlias == null){
            aggregateAlias = new StringBuilder(columnAlias.length() + aggregate.name().length()).append(columnAlias).append(aggregate.name()).toString();
        }
        return aggregateAlias;
    }
}
