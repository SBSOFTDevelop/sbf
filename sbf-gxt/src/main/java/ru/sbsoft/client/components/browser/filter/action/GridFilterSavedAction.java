package ru.sbsoft.client.components.browser.filter.action;

import ru.sbsoft.client.components.browser.actions.*;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.browser.filter.StoredFilterSelector;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.client.utils.DefaultAsyncCallback;
import ru.sbsoft.shared.FiltersBean;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;
import ru.sbsoft.shared.meta.filter.FilterBox;
import ru.sbsoft.shared.meta.filter.StoredFilterPath;

/**
 *
 * @author Kiselev
 */
public class GridFilterSavedAction extends GridAction {

    private final StoredFilterSelector storedFilterSelector;
    private final GridContext gridContext;
    private final LoadFilterHandler handler;

    public GridFilterSavedAction(BaseGrid grid, LoadFilterHandler handler) {
        super(grid);
        if (handler != null) {
            this.handler = handler;
        } else {
            throw new IllegalArgumentException("LoadFilterHandler can't be null");
        }
        this.gridContext = grid.__getGridContext();
        setCaption(I18n.get(SBFBrowserStr.menuFilterSaved));
        setToolTip(I18n.get(SBFBrowserStr.hintFilterSaved));
        setIcon16(SBFResources.BROWSER_ICONS.FilterSaved16());
        setIcon24(SBFResources.BROWSER_ICONS.FilterSaved());
        storedFilterSelector = new StoredFilterSelector();
    }

    public StoredFilterSelector getStoredFilterSelector() {
        return storedFilterSelector;
    }

    @Override
    protected void onExecute() {
        storedFilterSelector.show(gridContext, new DefaultAsyncCallback<StoredFilterPath>() {
            @Override
            public void onResult(final StoredFilterPath result1) {
                if (result1 != null) {
                    SBFConst.CONFIG_SERVICE.loadFilter(gridContext, result1, new DefaultAsyncCallback<FiltersBean>() {
                        @Override
                        public void onResult(FiltersBean result2) {
                            if (result2 != null) {
                                handler.set(new FilterBox(result2, result1));
                            }else{
                                ClientUtils.alertWarning("Filter not found");
                            }
                        }
                    });
                }
            }
        });
    }

}
