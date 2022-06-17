package ru.sbsoft.client.components.form;

import java.util.Comparator;

public class LookupKeyComparator implements Comparator<LookupItem> {

    @Override
    public int compare(LookupItem o1, LookupItem o2) {
        String v1 = toKey(o1);
        String v2 = toKey(o2);
        return v1.compareToIgnoreCase(v2);
    }

    private String toKey(LookupItem l) {
        return l != null && l.getKey() != null ? l.getKey().toString().trim() : "";
    }

}
