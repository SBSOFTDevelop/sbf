package ru.sbsoft.shared.util;

import java.util.Comparator;

/**
 *
 * @author Kiselev
 */
public class SimpleComparator implements Comparator<Object>{

    @Override
    public int compare(Object o1, Object o2) {
        if(o1 == o2){
            return 0;
        }
        if(o1 == null){
            return 1;
        }
        if(o2 == null){
            return -1;
        }
        return o1.toString().compareToIgnoreCase(o2.toString());
    }
    
}
