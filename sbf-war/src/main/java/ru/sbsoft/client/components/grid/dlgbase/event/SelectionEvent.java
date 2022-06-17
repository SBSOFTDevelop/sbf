package ru.sbsoft.client.components.grid.dlgbase.event;

import ru.sbsoft.client.components.grid.dlgbase.Unit;

/**
 *
 * @author Kiselev
 */
public class SelectionEvent extends UnitEvent {

    public SelectionEvent(Unit unit) {
        super(unit);
    }
}
