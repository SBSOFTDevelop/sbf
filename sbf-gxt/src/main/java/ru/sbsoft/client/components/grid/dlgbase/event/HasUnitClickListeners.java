package ru.sbsoft.client.components.grid.dlgbase.event;

/**
 *
 * @author Kiselev
 */
public interface HasUnitClickListeners {

    void addUnitClickListener(UnitClickListener l);

    void removeUnitClickListener(UnitClickListener l);
}
