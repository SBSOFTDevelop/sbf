package ru.sbsoft.client.components.grid.dlgbase.event;

import ru.sbsoft.client.components.grid.dlgbase.Unit;

/**
 *
 * @author Kiselev
 */
public class UnitEvent {

    private final Unit unit;

    public UnitEvent(Unit unit) {
        this.unit = unit;
    }

    public Unit getUnit() {
        return unit;
    }
}
