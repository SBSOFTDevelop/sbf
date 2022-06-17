package ru.sbsoft.client.components.browser;

import ru.sbsoft.client.components.grid.ContextGrid;
import ru.sbsoft.client.components.grid.GridMode;
import ru.sbsoft.client.components.tree.AbstractTreeFactory;
import ru.sbsoft.shared.interfaces.NamedGridType;

/**
 *
 * @author Kiselev
 */
public class TreeBrowserFactory extends BrowserFactory {

    private AbstractTreeFactory treeFactory;

    public TreeBrowserFactory(NamedGridType gridType) {
        super(gridType);
    }

    public TreeBrowserFactory setTreeFactory(AbstractTreeFactory treeFactory) {
        this.treeFactory = treeFactory;
        return this;
    }

    @Override
    protected BaseBrowser createBrowserInstance(String idBrowser, String titleBrowser, ContextGrid grid, GridMode... flags) {
        if (treeFactory != null) {
            TreeBrowser b = new TreeBrowser(idBrowser, titleBrowser, grid, flags);
            b.getBorderLayoutContainer().setWestWidget(treeFactory.createTree(grid), TreeBrowser.createDefaultBorderLayoutData());
            return b;
        }else{
            return super.createBrowserInstance(idBrowser, titleBrowser, grid, flags);
        }
    }
}
