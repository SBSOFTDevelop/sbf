package ru.sbsoft.shared.meta.filter;

import ru.sbsoft.shared.FiltersBean;
import ru.sbsoft.shared.param.DTO;

/**
 *
 * @author Kiselev
 */
public class FilterBox implements DTO {

    private StoredFilterPath path;
    private FiltersBean filter;

    private FilterBox() {
    }

    public FilterBox(FiltersBean filter, StoredFilterPath path) {
        this.path = path;
        this.filter = filter;
    }

    public StoredFilterPath getPath() {
        return path;
    }

    public FiltersBean getFilter() {
        return filter;
    }

}
