package ru.sbsoft.client.components.browser;

import ru.sbsoft.svc.core.client.util.Margins;
import ru.sbsoft.svc.widget.core.client.container.BorderLayoutContainer;
import ru.sbsoft.svc.widget.core.client.container.Container;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.components.grid.GridMode;

/**
 * Браузер с деревом-фильтром.
 * @author balandin
 * @since Apr 12, 2013 12:32:27 PM
 */
public class TreeBrowser extends BaseBrowser {

    public TreeBrowser(String idBrowser, String titleBrowser, BaseGrid grid, GridMode... flags) {
        super(idBrowser, titleBrowser, grid, flags);
    }

    @Override
    public Container createGridContainer() {
        final Container c = super.createGridContainer();
        return (c instanceof BaseGrid) ? new BorderLayoutContainer() : c;
    }

    public BorderLayoutContainer getBorderLayoutContainer() {
        return (BorderLayoutContainer) getGridContainer();
    }

    public static BorderLayoutContainer.BorderLayoutData createDefaultBorderLayoutData() {
        final BorderLayoutContainer.BorderLayoutData data = new BorderLayoutContainer.BorderLayoutData();
        data.setSize(300);
        data.setCollapsible(true);
        data.setCollapseMini(true);
        data.setFloatable(false);
        data.setMargins(new Margins(0, 5, 0, 0));
        return data;
    }
}
