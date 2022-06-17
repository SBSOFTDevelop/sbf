package ru.sbsoft.client.components.form;

import java.util.Comparator;

/**
 *
 * @author Kiselev
 */
public class LookupValComparator implements Comparator<LookupItem>{

    @Override
    public int compare(LookupItem o1, LookupItem o2) {
        String v1 = toVal(o1);
        String v2 = toVal(o2);
        return v1.compareToIgnoreCase(v2);
    }
    
    private String toVal(LookupItem l){
        return l != null && l.getValue() != null ? l.getValue().trim() : "";
    }
    
}
