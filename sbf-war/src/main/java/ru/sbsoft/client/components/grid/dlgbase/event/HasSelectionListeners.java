package ru.sbsoft.client.components.grid.dlgbase.event;

/**
 *
 * @author Kiselev
 */
public interface HasSelectionListeners {

    void addSelectionListener(SelectionListener l);

    void removeSelectionListener(SelectionListener l);
}
