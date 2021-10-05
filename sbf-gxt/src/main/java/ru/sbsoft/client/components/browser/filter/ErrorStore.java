package ru.sbsoft.client.components.browser.filter;

import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.shared.FilterInfo;

/**
 * @author balandin
 * @since Nov 9, 2015
 */
public class ErrorStore {

    public class Entry {

        private final FilterItem filterItem;
        private final FilterInfo filterInfo;
        private final String message;
        private final List<Object> params;

        public Entry(FilterItem filterItem, FilterInfo filterInfo, String message, List<Object> params) {
            this.filterItem = filterItem;
            this.filterInfo = filterInfo;
            this.message = message;
            this.params = params;
        }

        public FilterItem getFilterItem() {
            return filterItem;
        }

        public FilterInfo getFilterInfo() {
            return filterInfo;
        }

        public String getMessage() {
            return message;
        }

        public List<Object> getParams() {
            return params;
        }
    }

    private final List<Entry> items = new ArrayList<Entry>();

    public ErrorStore() {
    }

    public List<Entry> getItems() {
        return items;
    }

    public void showErrors() {
        for (Entry e : items) {
            e.getFilterItem().setError(e.getMessage());
        }
    }

    public void add(FilterItem filterItem, FilterInfo filterInfo, FilterSetupException ex) {
        add(filterItem, filterInfo, ex.getMessage(), ex.getParams());
    }

    public void add(FilterItem filterItem, FilterInfo filterInfo, String message) {
        add(filterItem, filterInfo, message, null);
    }

    public void add(FilterItem filterItem, FilterInfo filterInfo, String message, List<Object> params) {
        getItems().add(new Entry(filterItem, filterInfo, message, params));
    }
}
