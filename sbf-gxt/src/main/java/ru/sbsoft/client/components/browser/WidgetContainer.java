package ru.sbsoft.client.components.browser;

import ru.sbsoft.client.components.ManagedBrowser;
import ru.sbsoft.shared.model.MenuItemModel;

/**
 * Родительская панель для произвольного компонента типа
 * {@link com.google.gwt.user.client.ui.Widget}
 *
 * @author balandin
 * @since May 29, 2013 4:39:28 PM
 */
public class WidgetContainer extends AbstractContainer {

    public WidgetContainer(BaseBrowserManager baseBrowserManager, MenuItemModel menuItem, ManagedBrowser managed) {
        super(baseBrowserManager, menuItem, managed);
    }

    @Override
    protected void onAfterFirstAttach() {
        super.onAfterFirstAttach();

        setWidget(managed);
        setHeading(getCaption());
    }

    public void exit() {
        getBaseBrowserManager().removeBrowser(this);
    }

}
