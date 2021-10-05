package ru.sbsoft.client.components.actions;

import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.menu.SeparatorMenuItem;

/**
 * Выпадающее подменю на строке меню окна или таблицы.
 *
 * @author balandin
 * @since Oct 3, 2013 3:08:25 PM
 */
public class ActionMenu extends Menu {

    private final ActionManager actionManager;
    private boolean smallIcons = true;

    public ActionMenu(ActionManager actionManager) {

        this(actionManager, true);

    }

    public ActionMenu(ActionManager actionManager, boolean smallIcons) {
        super();
        this.actionManager = actionManager;
        this.smallIcons = smallIcons;
        addBeforeShowHandler(new BeforeShowEvent.BeforeShowHandler() {
            public void onBeforeShow(BeforeShowEvent event) {
                for (int i = 0; i < getWidgetCount(); i++) {
                    if (getWidget(i) instanceof MenuItem) {
                        MenuItem menuItem = (MenuItem) getWidget(i);
                        final Action action = ActionUtils.findAction(menuItem);
                        if (action != null) {
                            action.checkState();
                            menuItem.setEnabled(action.isEnabled());
                        }
                    }
                }
            }
        });
    }

    /**
     * Добавление пункта меню в конец списка
     *
     * @param action
     * @return созданый пункт меню
     */
    public MenuItem addAction(Action action) {
        return addAction(action, getWidgetCount());
    }

    /**
     * Добавление пункта меню в произвольнов порядке
     *
     * @param action
     * @param beforeIndex порядковый номер пункта меню за которым будет
     * происходить вставка нового
     * @return созданый пункт меню
     */
    public MenuItem addAction(Action action, int beforeIndex) {
        final MenuItem menuItem = actionManager.createMenuItem(action, ActionManager.ButtonSign.IconAndText, smallIcons ? ButtonIconSize.small : ButtonIconSize.large);
        if (menuItem != null) {
            insert(menuItem, beforeIndex);
        }
        return menuItem;
    }

    public void addSeparator() {
        final int count = getWidgetCount();
        if (count == 0) {
            return;
        }
        if (getWidget(count - 1) instanceof SeparatorMenuItem) {
            return;
        }
        add(new SeparatorMenuItem());
    }
}
