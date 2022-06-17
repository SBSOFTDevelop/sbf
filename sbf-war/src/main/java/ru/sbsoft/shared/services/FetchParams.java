package ru.sbsoft.shared.services;

import ru.sbsoft.svc.data.shared.loader.PagingLoadConfigBean;
import java.util.List;
import ru.sbsoft.shared.FilterInfo;

public class FetchParams extends PagingLoadConfigBean {

    private List<FilterInfo> parentFilters;

    public FetchParams(int offset, int limit) {
        super(offset, limit);
    }

    public FetchParams() {
        this(0, 200);
    }

    public List<FilterInfo> getParentFilters() {
        return parentFilters;
    }

    public void setParentFilters(List<FilterInfo> parentFilters) {
        this.parentFilters = parentFilters;
    }
}
