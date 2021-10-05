package ru.sbsoft.client.components.browser;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.widget.core.client.ContentPanel;
import ru.sbsoft.client.components.ManagedBrowser;
import ru.sbsoft.client.utils.CliUtil;
import ru.sbsoft.shared.model.MenuItemModel;

/**
 * Панель данных главного окна приложения. Окно может содержать несколько
 * открытых панелей разного типа.
 *
 * @author balandin
 * @since May 29, 2013 1:18:34 PM
 */
public abstract class AbstractContainer extends ContentPanel {

    private final BaseBrowserManager baseBrowserManager;
    private final MenuItemModel menuItem;
    protected final ManagedBrowser managed;

    public AbstractContainer(BaseBrowserManager baseBrowserManager, MenuItemModel menuItem, ManagedBrowser managed) {
        super();
        this.baseBrowserManager = baseBrowserManager;
        this.menuItem = menuItem;
        this.managed = managed;
        super.setHeaderVisible(false);
    }

    @Override
    public void setHeading(String text) {
        super.setHeading(text);
        updateHeaderVisibility();
    }

    @Override
    public void setHeading(SafeHtml html) {
        super.setHeading(html);
        updateHeaderVisibility();
    }

    private void updateHeaderVisibility() {
        CliUtil.updateHeaderVisibility(this);
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        updateHeaderVisibility();
    }

    public BaseBrowserManager getBaseBrowserManager() {
        return baseBrowserManager;
    }

    public MenuItemModel getMenuItem() {
        return menuItem;
    }

    public String getIdBrowser() {
        return managed.getIdBrowser();
    }

    public String getShortName() {
        return managed.getShortName() != null ? managed.getShortName() : getCaption();
    }

    protected String getCaption() {
        return managed.getCaption() != null ? managed.getCaption() : getIdBrowser();
    }

}
