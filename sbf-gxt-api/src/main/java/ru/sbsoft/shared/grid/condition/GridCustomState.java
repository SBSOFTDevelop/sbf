package ru.sbsoft.shared.grid.condition;

import ru.sbsoft.shared.meta.filter.StoredFilterPath;

/**
 *
 * @author Kiselev
 */
public class GridCustomState {
    private StoredFilterPath currentFilter = null;

    public StoredFilterPath getCurrentFilter() {
        return currentFilter;
    }

    public void setCurrentFilter(StoredFilterPath currentFilter) {
        this.currentFilter = currentFilter;
    }
    
}
