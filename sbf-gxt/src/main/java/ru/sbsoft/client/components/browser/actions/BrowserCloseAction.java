package ru.sbsoft.client.components.browser.actions;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.actions.AbstractAction;
import ru.sbsoft.client.components.browser.Browser;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Действие закрытия браузера.
 * @author balandin
 * @since Oct 3, 2013 3:45:50 PM
 */
public class BrowserCloseAction extends AbstractAction {

    private final Browser browser;

    public BrowserCloseAction(Browser browser) {
        super();

        setCaption(I18n.get(SBFBrowserStr.menuFileExit));
        setToolTip(I18n.get(SBFBrowserStr.hintFileExit));
        setIcon16(SBFResources.GENERAL_ICONS.Exit16());
        setIcon24(SBFResources.GENERAL_ICONS.Exit());

        this.browser = browser;
    }

    @Override
    protected void onExecute() {
        browser.onExit();
    }
}
