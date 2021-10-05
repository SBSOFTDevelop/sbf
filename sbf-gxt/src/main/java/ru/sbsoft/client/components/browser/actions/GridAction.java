package ru.sbsoft.client.components.browser.actions;

import ru.sbsoft.client.components.actions.AbstractAction;
import ru.sbsoft.client.components.grid.BaseGrid;

/**
 * Базовое действие для пункта меню таблицы. Имеет ссылку на саму таблицу.
 *
 * @author balandin
 */
public abstract class GridAction extends AbstractAction {

    private final BaseGrid grid;

    public GridAction(BaseGrid grid) {
        super();
        this.grid = grid;
    }

    @Override
    public boolean checkEnabled() {
        return getGrid().isInitialized();
    }

    public BaseGrid getGrid() {
        return grid;
    }

    protected boolean isSingeleSelection() {
        return getGrid().isInitialized() && getGrid().getSelectedRecords().size() == 1;
    }

}
