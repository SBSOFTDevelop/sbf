package ru.sbsoft.client.components.browser.actions;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.browser.Browser;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Скрывает/показывает панель с быстрым фильтром таблицы.
 * @author balandin
 * @since Dec 25, 2014 7:17:00 AM
 */
public class GridExpandFilterAction extends GridAction {

    public GridExpandFilterAction(Browser browser) {
        super(browser.getGrid());

        setCaption(I18n.get(SBFBrowserStr.menuFilterShowHide));
        setToolTip(I18n.get(SBFBrowserStr.hintFilterShowHide));

        setIcon16(SBFResources.BROWSER_ICONS.Filter16());
        setIcon24(SBFResources.BROWSER_ICONS.Filter());
    }

    @Override
    public boolean checkEnabled() {
        return true;
    }

    @Override
    protected void onExecute() {
        getGrid().showFilter();
    }
}
