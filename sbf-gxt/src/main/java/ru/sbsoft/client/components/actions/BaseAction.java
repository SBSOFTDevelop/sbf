package ru.sbsoft.client.components.actions;

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import ru.sbsoft.client.components.SBSelectHandler;

/**
 * @deprecated NO USAGES FOUND
 * @author balandin
 * @since Jun 19, 2013 12:24:18 PM
 */
public abstract class BaseAction {

    private final String caption;
    private final String tooltip;
    private final ImageResource smallIcon;
    private final ImageResource icon;
    //
    private MenuItem menuItem;
    private TextButton button;
    //
    private SBSelectHandler handler;

    private BaseAction(String caption, String tooltip, ImageResource smallIcon, ImageResource icon) {
        super();

        this.caption = caption;
        this.tooltip = tooltip;
        this.smallIcon = smallIcon;
        this.icon = icon;

        handler = createSelectHandler();
    }

    public MenuItem getMenuItem() {
        if (menuItem == null) {
            menuItem = new MenuItem();
            menuItem.setIcon(smallIcon);
            menuItem.setText(caption);
            menuItem.setToolTip(tooltip);
            menuItem.addSelectionHandler(handler);
        }
        return menuItem;
    }

    public TextButton getButton() {
        if (button == null) {
            button = new TextButton();
            button.setIcon(icon);
            button.setToolTip(tooltip);
            button.addSelectHandler(handler);
        }
        return button;
    }

    public void setEnabled(boolean value) {
        if (menuItem != null) {
            menuItem.setEnabled(value);
        }
        if (button != null) {
            button.setEnabled(value);
        }
    }

    private SBSelectHandler createSelectHandler() {
        return new SBSelectHandler() {
            @Override
            public void onSelectEvent() {
                onExecute();
            }
        };
    }

    public abstract void onExecute();
}
