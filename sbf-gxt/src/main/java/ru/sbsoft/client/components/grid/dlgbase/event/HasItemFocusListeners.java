package ru.sbsoft.client.components.grid.dlgbase.event;

/**
 *
 * @author Kiselev
 */
public interface HasItemFocusListeners {

    void addUnitFocusListener(UnitFocusListener l);

    void removeUnitFocusListener(UnitFocusListener l);
}
