package ru.sbsoft.client.components;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import ru.sbsoft.svc.widget.core.client.menu.Menu;
import ru.sbsoft.svc.widget.core.client.menu.MenuBar;
import ru.sbsoft.svc.widget.core.client.menu.MenuBarItem;

/**
 * Базовый класс строки меню окон приложения
 * @author balandin
 * @since Oct 30, 2013 3:16:30 PM
 */
public class SBMenuBar extends MenuBar {

    public SBMenuBar() {
        super();
        sinkEvents(Event.ONCLICK | Event.ONFOCUS | Event.ONBLUR | Event.ONMOUSEOVER);
    }

    /**
     * Добавляет выпадающее меню.
     * @param caption заголовок
     * @param menu выпадающее меню
     */
    public void addMenuBar(String caption, Menu menu) {
        if (menu != null) {
            add(new MBI(caption, menu));
        }
    }

    protected static class MBI extends MenuBarItem {

        public MBI(String text, Menu menu) {
            super(text, menu);
        }

        public boolean isExpanded() {
            return expanded;
        }
    }

    /**
     * Событие от интернет браузера, связанное в данной строкой меню.
     * @param event 
     */
    @Override
    public void onBrowserEvent(Event event) {
        if (event.getTypeInt() == Event.ONMOUSEOVER) {
            if (active != null && ((MBI) active).isExpanded()) {
                event.stopPropagation();
                event.preventDefault();
                MenuBarItem item = (MenuBarItem) findWidget(event.getEventTarget().<Element>cast());
                if (item != null && active != item) {
                    setActiveItem(item, true);
                }
                return;
            }
        }
        super.onBrowserEvent(event);
    }
}
