package ru.sbsoft.shared.meta.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ru.sbsoft.shared.param.DTO;

/**
 * @author balandin
 * @since Oct 29, 2015
 */
public class FilterDefinitions implements DTO {

    private final transient List<FilterDefinition> items = new ArrayList<FilterDefinition>();
    //
    private List<EditorFilterDefinition> dataDefs = new ArrayList<EditorFilterDefinition>();
    private List<LookupFilterDefinition> linkDefs = new ArrayList<LookupFilterDefinition>();
    //
    private transient HashMap<String, FilterDefinition> cache;

    public FilterDefinitions() {
    }

    public List<FilterDefinition> getServerItems() {
        return items;
    }

    public void add(FilterDefinition def) {
        items.add(def);
    }

    private HashMap<String, FilterDefinition> getCache() {
        if (cache == null) {
            cache = new HashMap<String, FilterDefinition>();
            if (!items.isEmpty()) {
                fillCache(items);
            } else {
                fillCache(dataDefs);
                fillCache(linkDefs);
            }
        }
        return cache;
    }

    private void fillCache(List<? extends FilterDefinition> defs) {
        for (FilterDefinition item : defs) {
            cache.put(item.getAlias(), item);
        }
    }

    public FilterDefinition get(String alias) {
        return getCache().get(alias);
    }

    public List<EditorFilterDefinition> getDataDefs() {
        return dataDefs;
    }

    public void setDataDefs(List<EditorFilterDefinition> dataDefs) {
        this.dataDefs = dataDefs;
    }

    public List<LookupFilterDefinition> getLinkDefs() {
        return linkDefs;
    }

    public void setLinkDefs(List<LookupFilterDefinition> linkDefs) {
        this.linkDefs = linkDefs;
    }
}
