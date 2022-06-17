package ru.sbsoft.client.components.browser.actions;

import ru.sbsoft.client.components.actions.AbstractAction;
import ru.sbsoft.client.components.browser.Browser;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.Modifiers;
import ru.sbsoft.shared.interfaces.GridType;

/**
 * Отображает программно-техническую информацию о таблице. Доступно только из контекстного меню таблицы.
 * @see ru.sbsoft.client.components.grid.menu.GridContextMenu
 * @author balandin
 * @since Apr 25, 2014 12:28:56 PM
 */
public class GridInfoAction extends AbstractAction {

    private static final String BR = "<br>";
    private final Browser browser;

    public GridInfoAction(Browser browser) {
        super();

        setCaption("?");
        setIcon16(SBFResources.GENERAL_ICONS.About16());
        setIcon24(SBFResources.GENERAL_ICONS.About());

        this.browser = browser;
    }

    @Override
    protected void onExecute() {
        final BaseGrid grid = browser.getGrid();
        final GridType type = grid.__getGridContext().getGridType();
        final Modifiers modifiers = grid.__getGridContext().getModifiers();

        StringBuilder s = new StringBuilder();
        s.append(BR);
        s.append(BR);
        s.append(BR);
        s.append(BR);
        s.append(grid.getMetaInfo().getTemplateClassName()).append(BR);
        s.append(grid.getClass().getName()).append(BR);
        s.append(grid.__getTableName()).append(BR);
        s.append(BR);
        s.append("context:").append(BR);
        s.append(type.getClass().getName()).append('.').append(type.getCode()).append(BR);
        s.append(Strings.coalesce(grid.__getGridContext().getContext())).append(BR);
        s.append(modifiers.toString()).append(BR);
        s.append(BR);

        ClientUtils.message("Grid system information", s.toString());
    }
}
