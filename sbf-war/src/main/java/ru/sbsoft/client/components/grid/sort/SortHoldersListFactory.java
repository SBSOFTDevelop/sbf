package ru.sbsoft.client.components.grid.sort;

import ru.sbsoft.client.components.browser.filter.fields.HoldersListFactory;
import ru.sbsoft.client.components.grid.column.CustomColumnConfig;

/**
 *
 * @author Kiselev
 */
public class SortHoldersListFactory extends HoldersListFactory {

    @Override
    protected boolean isColumnMatch(CustomColumnConfig c) {
        return super.isColumnMatch(c) && !c.getColumn().isHidden() && c.getColumn().isVisible();
    }
    
}
