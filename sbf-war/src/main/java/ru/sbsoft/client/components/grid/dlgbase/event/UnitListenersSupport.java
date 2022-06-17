package ru.sbsoft.client.components.grid.dlgbase.event;

import ru.sbsoft.client.components.grid.dlgbase.Unit;

/**
 *
 * @author Kiselev
 */
public abstract class UnitListenersSupport<L> extends ListenersSupport<L> {

    protected final Unit unit;

    public UnitListenersSupport(Unit unit) {
        this.unit = unit;
    }
}
