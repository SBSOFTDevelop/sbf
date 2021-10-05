package ru.sbsoft.client.components.grid.dlgbase.event;

import ru.sbsoft.client.components.grid.dlgbase.Group;
import ru.sbsoft.client.components.grid.dlgbase.Unit;

/**
 *
 * @author Kiselev
 */
public interface DragListener {
    void dragComplete(Unit e, Group from, Group to);
}
