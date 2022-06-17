package ru.sbsoft.client.components.actions;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.widget.core.client.button.SplitButton;
import ru.sbsoft.svc.widget.core.client.button.TextButton;
import ru.sbsoft.svc.widget.core.client.event.SelectEvent;
import ru.sbsoft.svc.widget.core.client.menu.Item;
import ru.sbsoft.svc.widget.core.client.menu.Menu;
import ru.sbsoft.svc.widget.core.client.menu.MenuItem;
import ru.sbsoft.svc.widget.core.client.toolbar.ToolBar;

/**
 * Базовый класс панелей инструментов окон и таблиц приложения.
 *
 * @author balandin
 * @since Oct 3, 2013 12:48:01 PM
 */
public abstract class ActionToolBar extends ToolBar {

    private final ActionManager actionManager;
    private final boolean smallIcons;

    public ActionToolBar(final ActionManager actionManager, final boolean smallIcons) {
        super();

        this.actionManager = actionManager;
        this.smallIcons = smallIcons;

        getElement().getStyle().setProperty("borderBottom", "none");
    }

    public boolean isSmallIcons() {
        return smallIcons;
    }

    public TextButton addActionsButton(Action action, Action... acts) {
        return addActionsButton(getWidgetCount(), action, acts);
    }
    
    public TextButton addActionsButton(int beforeIndex, Action action, Action... acts) {
        ActionMenu menu = new ActionMenu(actionManager, isSmallIcons());
        boolean notEmpty = false;
        if (acts != null && acts.length > 0) {
            for (Action act : acts) {
                notEmpty |= menu.addAction(act) != null;
            }
        }
        if (notEmpty) {
            SplitButton btn = addSplitButton(action, beforeIndex);
            if (btn != null) {
                btn.setMenu(menu);
            }
            return btn;
        } else {
            return addButton(action, beforeIndex);
        }
    }

    public TextButton addButton(Action action) {
        return addButton(action, getWidgetCount());
    }

    public SplitButton addSplitButton(Action action) {
        return addSplitButton(action, getWidgetCount());
    }

    public SplitButton addSplitButton(Action action, int beforeIndex) {
        if (action == null) {
            return null;
        }

        final SplitButton button = new SplitButton() {
            @Override
            public void onBrowserEvent(Event event) {
                actionManager.wasCtrlKey(event.getCtrlKey());
                actionManager.wasAltKey(event.getAltKey());
                actionManager.wasShiftKey(event.getShiftKey());
                super.onBrowserEvent(event);
            }
        };
        actionManager.bindButtonWithAction(button, action, smallIcons ? ButtonIconSize.small : ButtonIconSize.large);
        insert(button, beforeIndex);
        return button;
    }

    public TextButton addButton(Action action, int beforeIndex) {
        final TextButton button = createButton(action);
        if (button != null) {
            insert(button, beforeIndex);
        }
        return button;
    }

    public TextButton createButton(Action action) {
        return actionManager.createButton(action, smallIcons ? ButtonIconSize.small : ButtonIconSize.large);
    }

    public TextButton createModalButton(Action action) {
        final TextButton b = createButton(action);
        b.setText(action.getCaption());
        b.setIcon(action.getIcon16());
        return b;
    }

    @Override
    protected void addWidgetToMenu(Menu menu, Widget w) {
        if (w instanceof TextButton) {
            final TextButton b = (TextButton) w;
            final Object tmp = b.getData("action");
            if (tmp instanceof Action) {
                final Action action = (Action) tmp;
                MenuItem item = new MenuItem(b.getText(), action.getIcon16());
                item.setItemId(b.getItemId());
                if (b.getMenu() != null) {
                    item.setHideOnClick(false);
                    item.setSubMenu(b.getMenu());
                }
                item.setText(action.getCaption());
                item.setEnabled(b.isEnabled());
                item.addSelectionHandler(new SelectionHandler<Item>() {

                    @Override
                    public void onSelection(SelectionEvent<Item> event) {
                        b.fireEvent(new SelectEvent());
                    }
                });
                menu.add(item);
            }
            return;
        }
        super.addWidgetToMenu(menu, w);
    }
}
