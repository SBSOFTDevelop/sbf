package ru.sbsoft.client.components.treebrowser;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.actions.AbstractAction;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 *
 * @author sokolov
 */
public class TreeBrowserCloseAction extends AbstractAction {

    private final ITreeBrowser browser;

    public TreeBrowserCloseAction(ITreeBrowser browser) {
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
