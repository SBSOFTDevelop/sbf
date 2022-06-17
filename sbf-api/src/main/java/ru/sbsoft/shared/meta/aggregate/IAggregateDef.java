package ru.sbsoft.shared.meta.aggregate;

import ru.sbsoft.shared.meta.Aggregate;
import ru.sbsoft.shared.param.DTO;

/**
 *
 * @author Kiselev
 */
public interface IAggregateDef extends DTO {
    
    String ALIAS_TOTAL_COUNT = "TOTAL_COUNT";

    Aggregate getAggregate();

    String getAggregateAlias();

    String getColumnAlias();
    
}
