package ru.sbsoft.client.components.browser.filter;

import ru.sbsoft.client.components.browser.filter.fields.HoldersList;
import ru.sbsoft.client.components.grid.SystemGrid;

/**
 *
 * @author Kiselev
 */
public class FilterHoldersList extends HoldersList {

    private final SystemGrid grid;

    FilterHoldersList(SystemGrid sysGrid) {
        this.grid = sysGrid;
    }

    public SystemGrid getGrid() {
        return grid;
    }

}
