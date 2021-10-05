package ru.sbsoft.client.components.browser;

import ru.sbsoft.client.components.ISelectBrowser;
import ru.sbsoft.client.components.ISelectBrowserMaker;
import ru.sbsoft.shared.consts.BrowserMode;
import ru.sbsoft.shared.interfaces.NamedGridType;

/**
 *
 * @author Kiselev
 */
public class SelectBrowserFactory extends BaseBrowserFactory<SelectBrowserFactory> implements ISelectBrowserMaker {

    protected String keyColumn;
    protected String nameColumn;
    protected String semanticKeyColumn;

    public SelectBrowserFactory(NamedGridType gridType) {
        super(gridType);
        addGridModifiers(BrowserMode.SELECT);
    }

    @Override
    public ISelectBrowser createBrowser() {
        SelectBaseBrowser b = new SelectBaseBrowser(createGrid(), getBrowserFlags()) {
        };
        b.setCaption(getCaption());
        return b;
    }
}
