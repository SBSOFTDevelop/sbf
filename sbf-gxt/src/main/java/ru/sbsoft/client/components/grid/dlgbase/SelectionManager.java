package ru.sbsoft.client.components.grid.dlgbase;

import ru.sbsoft.client.components.actions.ActionManager;
import ru.sbsoft.client.components.grid.dlgbase.event.UnitDeleteEvent;
import ru.sbsoft.client.components.grid.dlgbase.event.UnitDeleteListener;
import ru.sbsoft.client.components.grid.dlgbase.event.SelectionEvent;
import ru.sbsoft.client.components.grid.dlgbase.event.SelectionListener;

/**
 *
 * @author Kiselev
 */
public class SelectionManager implements ISelectionManager {

    private final SelectionModel model = new SelectionModel();
    private final UnsupportListener unsupportListener = new UnsupportListener();
    private final ActionUpdater actionUpdater = new ActionUpdater();
    private ActionManager actionManager = null;

    public SelectionManager() {
        model.addSelectionListener(actionUpdater);
    }

    @Override
    public void addSelectionSupport(Unit unit) {
        unit.addUnitClickListener(model);
        unit.addUnitFocusListener(model);
        unit.addUnitDeleteListener(unsupportListener);
    }

    @Override
    public void removeSelectionSupport(Unit unit) {
        unit.removeUnitClickListener(model);
        unit.removeUnitFocusListener(model);
        unit.removeUnitDeleteListener(unsupportListener);
    }

    @Override
    public Unit getSelectedUnit() {
        return model.getSelection();
    }

    @Override
    public void setSelectedUnit(Unit unit) {
        model.setSelection(unit);
    }

    @Override
    public void attach(ActionManager actionManager) {
        this.actionManager = actionManager;
    }

    @Override
    public SelectionModel getModel() {
        return model;
    }

    private class UnsupportListener implements UnitDeleteListener {

        @Override
        public void onUnitDelete(UnitDeleteEvent e) {
            Unit unit = e.getUnit();
            removeSelectionSupport(unit);
            if(model.getSelection() == unit){
                model.setSelection(null);
            }
        }
    }

    private class ActionUpdater implements SelectionListener {

        @Override
        public void selectionChanged(SelectionEvent e) {
            if (actionManager != null) {
                actionManager.updateState();
            }
        }
    }

}
