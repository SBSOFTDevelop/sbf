package ru.sbsoft.client.components.grid.dlgbase.event;

/**
 *
 * @author Kiselev
 */
public interface HasHolderListeners {

    void addHolderListener(UnitLeaderListener l);

    void removeHolderListener(UnitLeaderListener l);
}
