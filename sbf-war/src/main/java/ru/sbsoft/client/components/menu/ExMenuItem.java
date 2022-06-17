package ru.sbsoft.client.components.menu;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.svc.widget.core.client.menu.MenuItem;

/**
 * Пункт контекстного меню 'быстрого' фильтра браузера {@link ru.sbsoft.client.components.browser.filter.FilterContextMenu}.
 * @author balandin
 * @since Dec 18, 2014 1:19:25 PM
 */
public class ExMenuItem extends MenuItem {

    public ExMenuItem() {
    }

    public ExMenuItem(String text) {
        super(text);
    }

    public ExMenuItem(String text, ImageResource icon) {
        super(text, icon);
    }

    public ExMenuItem(String text, SelectionHandler<MenuItem> handler) {
        super(text, handler);
    }

    public ExMenuItem(String text, ImageResource icon, SelectionHandler<MenuItem> handler) {
        super(text, icon);
        addSelectionHandler((SelectionHandler) handler);
    }
}
