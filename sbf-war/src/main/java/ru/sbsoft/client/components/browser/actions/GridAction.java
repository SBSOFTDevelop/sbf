package ru.sbsoft.client.components.browser.actions;

import ru.sbsoft.svc.widget.core.client.Component;
import ru.sbsoft.client.components.actions.AbstractAction;
import ru.sbsoft.client.components.browser.Browser;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.components.grid.GridUtils;

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

    protected Browser getGridBrowser() {
        if (grid != null) {
            return GridUtils.findParentBrowser(grid);
        }
        return null;
    }
    
    protected Component getMaskComponent(){
        Browser b = getGridBrowser();
        return b != null ? b : grid;
    }
    
    protected void doMask(String msg){
        Component maskComponent = getMaskComponent();
        if(maskComponent != null){
            maskComponent.mask(msg);
        }
    }
    
    protected void doUnmask(){
        Component maskComponent = getMaskComponent();
        if(maskComponent != null){
            maskComponent.unmask();
        }
    }
}
