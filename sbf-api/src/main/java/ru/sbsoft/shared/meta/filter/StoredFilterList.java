package ru.sbsoft.shared.meta.filter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Kiselev
 */
public class StoredFilterList implements IStoredFilterList {

    private Map<StoredFilterType, Set<StoredFilterPath>> filters = new HashMap<StoredFilterType, Set<StoredFilterPath>>();

    @Override
    public Set<StoredFilterPath> get(StoredFilterType type) {
        return filters.get(type);
    }

    public void set(StoredFilterType type, Set<StoredFilterPath> typedPart) {
        if (typedPart != null && !typedPart.isEmpty()) {
            filters.put(type, typedPart);
        } else {
            filters.remove(type);
        }
    }

    public void add(StoredFilterType type, StoredFilterPath f) {
        Set<StoredFilterPath> part = filters.get(type);
        if (part == null) {
            part = new HashSet<StoredFilterPath>();
            filters.put(type, part);
        }
        part.add(f);
    }

    @Override
    public boolean isEmpty() {
        return filters.isEmpty();
    }

}
