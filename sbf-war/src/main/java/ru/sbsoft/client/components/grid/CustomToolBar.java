package ru.sbsoft.client.components.grid;

import ru.sbsoft.svc.widget.core.client.toolbar.SeparatorToolItem;
import ru.sbsoft.client.components.actions.ActionManager;
import ru.sbsoft.client.components.actions.ActionToolBar;

/**
 * @author balandin
 * @since Jun 8, 2015 4:53:59 PM
 */
public class CustomToolBar extends ActionToolBar {

    public CustomToolBar(ActionManager actionManager, boolean smallIcons) {
        super(actionManager, smallIcons);
    }

    public void addSeparator() {
        if (getWidgetCount() > 0) {
            if (!(getWidget(getWidgetCount() - 1) instanceof SeparatorToolItem)) {
                add(new SeparatorToolItem());
            }
        }
    }
}
