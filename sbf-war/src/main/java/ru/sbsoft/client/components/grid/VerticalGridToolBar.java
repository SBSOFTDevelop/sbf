package ru.sbsoft.client.components.grid;

import com.google.gwt.user.client.ui.FlexTable;
import ru.sbsoft.svc.widget.core.client.button.TextButton;
import ru.sbsoft.svc.widget.core.client.container.HBoxLayoutContainer.HBoxLayoutAlign;
import ru.sbsoft.client.components.actions.Action;
import ru.sbsoft.client.components.browser.Browser;

/**
 * Панель инструментов с вертикальным расположением для таблицы. Обычно
 * используется в таблицах встроенных в форму
 * {@link ru.sbsoft.client.components.form.FormGridView}.
 */
public final class VerticalGridToolBar extends CustomGridToolBar {

    private final FlexTable table = new FlexTable();

    public VerticalGridToolBar(final Browser browser, boolean smallIcons) {
        super(browser, smallIcons);

        setHBoxLayoutAlign(HBoxLayoutAlign.TOP);
        add(table);

        addButton(getGrid().getSaveAction());
        addButton(getGrid().getRefreshAction());
        addButton(getGrid().getGridRloadAction());
        addButton(getGrid().getUpdateAction());
        addActionsButton(getGrid().getInsertAction(), getGrid().getCloneAction());
        addButton(getGrid().getDeleteAction());
    }

    @Override
    public TextButton addButton(Action action) {
        final TextButton button = createButton(action);
        if (button != null) {
            table.setWidget(table.getRowCount(), 0, button);
        }
        return button;
    }
}
