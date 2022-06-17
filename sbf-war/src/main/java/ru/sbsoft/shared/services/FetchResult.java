package ru.sbsoft.shared.services;

import ru.sbsoft.svc.data.shared.loader.PagingLoadResultBean;
import java.util.Map;
import ru.sbsoft.shared.meta.Wrapper;
import ru.sbsoft.shared.meta.filter.FilterBox;

/**
 * @author balandin
 * @since May 8, 2014 4:02:40 PM
 */
public class FetchResult<Data> extends PagingLoadResultBean<Data> {

    private Map<String, Wrapper> aggs;

    private FilterBox actualFilter;

    public FetchResult() {
    }

    public Map<String, Wrapper> getAggs() {
        return aggs;
    }

    public void setAggs(Map<String, Wrapper> aggs) {
        this.aggs = aggs;
    }

    public FilterBox getActualFilter() {
        return actualFilter;
    }

    public void setActualFilter(FilterBox actualFilter) {
        this.actualFilter = actualFilter;
    }
}
