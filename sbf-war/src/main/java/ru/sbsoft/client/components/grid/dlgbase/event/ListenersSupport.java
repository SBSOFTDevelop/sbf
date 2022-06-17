package ru.sbsoft.client.components.grid.dlgbase.event;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kiselev
 */
public abstract class ListenersSupport<L> {

    protected final List<L> listeners = new ArrayList<L>();

    public boolean addListener(L l) {
        if (!listeners.contains(l)) {
            listeners.add(l);
            return true;
        }
        return false;
    }

    public boolean removeListener(L l) {
        return listeners.remove(l);
    }
    
    public void clear(){
        listeners.clear();
    }

}
