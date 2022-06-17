package ru.sbsoft.client.components.form;

import ru.sbsoft.svc.core.client.resources.ThemeStyles;
import ru.sbsoft.svc.widget.core.client.event.AddEvent;
import ru.sbsoft.svc.widget.core.client.event.ContainerHandlerAdapter;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.actions.ActionMenu;
import ru.sbsoft.client.components.actions.ActionMenuBar;
import ru.sbsoft.client.components.form.action.*;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Строка меню формы.
 */
public class FormMenu extends ActionMenuBar {

    private final BaseForm form;
    private final ActionMenu operationsMenu;

    public FormMenu(BaseForm form) {
        super(form.getActionManager());
        this.form = form;
        addStyleName(ThemeStyles.get().style().borderBottom());
        createMenu();

        operationsMenu = new ActionMenu(getActionManager());
        operationsMenu.setData("empty", "true");
        operationsMenu.addContainerHandler(new ContainerHandlerAdapter() {

            @Override
            public void onAdd(AddEvent event) {
                if ("true".equals(operationsMenu.getData("empty"))) {
                    operationsMenu.setData("empty", "false");
                    addMenuBar(I18n.get(SBFBrowserStr.menuOperation), operationsMenu, 1);
//                    insert(new MenuBarItem(SBFResources.BROWSER_CONTENT.menuOperation(), operationsMenu), 1);
                }
                super.onAdd(event);
            }
        });

    }

    private void createMenu() {
        addMenuBar(I18n.get(SBFBrowserStr.menuFile), createFileMenu());
    }

    protected ActionMenu createFileMenu() {
        final ActionMenu menu = new ActionMenu(getActionManager());
        menu.addAction(new FormSaveAction(getForm()));
        menu.addSeparator();
        menu.addAction(new FormSaveRefreshAction(getForm()));
        menu.addAction(new FormRefreshAction(getForm()));
        menu.addSeparator();
        menu.addAction(new FormCloseAction(getForm()));
        return menu;
    }


    public BaseForm getForm() {
        return form;
    }

    public ActionMenu getOperationsMenu() {
        return operationsMenu;
    }
}
