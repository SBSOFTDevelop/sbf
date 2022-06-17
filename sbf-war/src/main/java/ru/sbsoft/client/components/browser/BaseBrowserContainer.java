package ru.sbsoft.client.components.browser;

import com.google.gwt.safehtml.shared.SafeHtml;
import ru.sbsoft.svc.widget.core.client.tips.ToolTipConfig;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.model.MenuItemModel;

/**
 * Родительская панель для браузера.
 *
 * @author balandin
 * @since May 28, 2013 6:49:22 PM
 */
public class BaseBrowserContainer extends AbstractContainer implements IBaseBrowserContainer {

    public BaseBrowserContainer(BaseBrowserManager baseBrowserManager, MenuItemModel menuItem, BaseBrowser browser) {
        super(baseBrowserManager, menuItem, browser);

        final ToolTipConfig toolTipCfg = new ToolTipConfig();
        toolTipCfg.setMaxWidth(500);
        getHeader().setToolTipConfig(toolTipCfg);
    }

    private BaseBrowser getBrowser() {
        return (BaseBrowser) managed;
    }

    @Override
    protected void onAfterFirstAttach() {
        super.onAfterFirstAttach();

        getHeader().setIcon(SBFResources.APP_ICONS.Table());
        getBrowser().init();

        setWidget(getBrowser());
        forceLayout();
    }

    public void exit() {
        getBaseBrowserManager().removeBrowser(this);
    }

    @Override
    public void setHeading(SafeHtml text, SafeHtml tooltip) {
        super.setHeading(text);
        if (tooltip == null) {
            getHeader().removeToolTip();
        } else {
            getHeader().setToolTip(tooltip);
        }
    }
}
