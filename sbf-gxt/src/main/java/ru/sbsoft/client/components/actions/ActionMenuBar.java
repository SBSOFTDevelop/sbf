package ru.sbsoft.client.components.actions;

import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.widget.core.client.menu.Menu;
import ru.sbsoft.client.components.SBMenuBar;

/**
 * Базовый класс строки меню окон приложения с {@link  ActionManager}
 * @author balandin
 * @since Oct 3, 2013 12:47:53 PM
 */
public class ActionMenuBar extends SBMenuBar {

    private final ActionManager actionManager;

    public ActionMenuBar(ActionManager actionManager) {
        super();
        this.actionManager = actionManager;
        addStyleName(ThemeStyles.get().style().borderBottom());
    }

    public ActionManager getActionManager() {
        return actionManager;
    }

    public void addMenuBar(String caption, Menu menu, int beforeIndex) {
        if (menu != null) {
            insert(new MBI(caption, menu), beforeIndex);
        }
    }
}
