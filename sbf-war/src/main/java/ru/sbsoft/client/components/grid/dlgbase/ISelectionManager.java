package ru.sbsoft.client.components.grid.dlgbase;

import ru.sbsoft.client.components.actions.ActionManager;

/**
 *
 * @author Kiselev
 */
public interface ISelectionManager {

    void addSelectionSupport(Unit unit);

    void removeSelectionSupport(Unit unit);

    Unit getSelectedUnit();
    
    void setSelectedUnit(Unit unit);
    
    void attach(ActionManager actionManager);
    
    SelectionModel getModel();
}
