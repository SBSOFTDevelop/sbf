package ru.sbsoft.client.components.grid.dlgbase;

import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.components.grid.dlgbase.event.HasSelectionListeners;
import ru.sbsoft.client.components.grid.dlgbase.event.UnitClickEvent;
import ru.sbsoft.client.components.grid.dlgbase.event.UnitClickListener;
import ru.sbsoft.client.components.grid.dlgbase.event.UnitFocusEvent;
import ru.sbsoft.client.components.grid.dlgbase.event.UnitFocusListener;
import ru.sbsoft.client.components.grid.dlgbase.event.SelectionEvent;
import ru.sbsoft.client.components.grid.dlgbase.event.SelectionListener;

/**
 *
 * @author Kiselev
 */
public class SelectionModel implements HasSelectionListeners, UnitClickListener, UnitFocusListener {

    private Unit selection = null;
    
    private final List<SelectionListener> selectionListeners = new ArrayList<SelectionListener>();

    public void setSelection(Unit el) {
        if (selection != el) {
            if (selection != null) {
                selection.getElement().getStyle().clearBackgroundColor();
            }
            selection = el;
            if (selection != null) {
                selection.getElement().getStyle().setBackgroundColor("#d0d0dd");
            }
            fireSelection(el);
        }
    }

    public Unit getSelection() {
        return selection;
    }

    @Override
    public void addSelectionListener(SelectionListener l) {
        if(!selectionListeners.contains(l)){
            selectionListeners.add(l);
        }
    }

    @Override
    public void removeSelectionListener(SelectionListener l) {
        selectionListeners.remove(l);
    }
    
    private void fireSelection(Unit unit){
        SelectionEvent e = new SelectionEvent(unit);
        for(SelectionListener l : selectionListeners){
            l.selectionChanged(e);
        }
    }

    @Override
    public void unitClicked(UnitClickEvent e) {
        setSelection(e.getUnit());
    }

    @Override
    public void unitFocused(UnitFocusEvent e) {
        setSelection(e.getUnit());
    }
    
}
