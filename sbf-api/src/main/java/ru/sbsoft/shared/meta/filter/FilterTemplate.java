package ru.sbsoft.shared.meta.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ru.sbsoft.shared.param.DTO;

/**
 * @author balandin
 * @since Oct 29, 2015
 */
public class FilterTemplate implements DTO {

    private List<FilterTemplateItem> items = new ArrayList<FilterTemplateItem>();
    private transient HashMap<String, FilterTemplateItem> cache;

    public FilterTemplate() {
    }

    public void setItems(List<FilterTemplateItem> items) {
        this.items = items;
    }

    public List<FilterTemplateItem> getItems() {
        return items;
    }

    public FilterTemplateItem addItem(FilterTemplateItem item) {
        items.add(item);
        return item;
    }

    private HashMap<String, FilterTemplateItem> getCache() {
        if (cache == null) {
            cache = new HashMap<String, FilterTemplateItem>();
            for (FilterTemplateItem item : items) {
                cache.put(item.getAlias(), item);
            }
        }
        return cache;
    }

    public FilterTemplateItem get(String alias) {
        return getCache().get(alias);
    }
}
