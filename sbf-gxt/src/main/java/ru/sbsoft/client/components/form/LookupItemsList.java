package ru.sbsoft.client.components.form;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author balandin
 */
public class LookupItemsList extends ArrayList<LookupItem> {

    private HashMap<Object, LookupItem> cache;

    public LookupItem findLookupItem(Object key) {
        if (cache == null) {
            cache = new HashMap<Object, LookupItem>();
            for (LookupItem item : this) {
                cache.put(item.getKey(), item);
            }
        }
        return cache.get(key);
    }

    public boolean add(Object key, String value) {
        return add(new LookupItem(key, value));
    }
}
