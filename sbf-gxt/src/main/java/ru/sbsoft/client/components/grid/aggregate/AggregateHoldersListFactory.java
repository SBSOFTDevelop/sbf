package ru.sbsoft.client.components.grid.aggregate;

import java.util.List;
import ru.sbsoft.client.components.browser.filter.fields.HoldersListFactory;
import ru.sbsoft.client.components.grid.column.CustomColumnConfig;

/**
 *
 * @author Kiselev
 */
public class AggregateHoldersListFactory extends HoldersListFactory {

    private final List<IAggregate> operations;

    public AggregateHoldersListFactory(List<IAggregate> operations) {
        this.operations = operations;
    }

    @Override
    protected boolean isColumnMatch(CustomColumnConfig c) {
        return super.isColumnMatch(c) && !c.getColumn().isHidden() && c.getColumn().isVisible() && hasOper(c);
    }

    private boolean hasOper(CustomColumnConfig c) {
        if (c != null) {
            for (IAggregate op : operations) {
                if (op.isSupported(c.getClass())) {
                    return true;
                }
            }
        }
        return false;
    }

}
