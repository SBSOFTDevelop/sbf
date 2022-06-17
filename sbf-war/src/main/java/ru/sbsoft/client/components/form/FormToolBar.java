package ru.sbsoft.client.components.form;

import ru.sbsoft.svc.widget.core.client.button.TextButton;
import ru.sbsoft.svc.widget.core.client.toolbar.SeparatorToolItem;
import ru.sbsoft.client.components.actions.ActionToolBar;
import ru.sbsoft.client.components.form.action.*;

/**
 * Базовый класс панелей инструментов окон приложения.
 *
 * @author balandin
 * @since Jun 10, 2013 11:28:55 AM
 */
public class FormToolBar extends ActionToolBar {

    private final static int DEFAULT_SPACING = 2;
    private final BaseForm form;
    private TextButton closeButton;
    private TextButton refreshButton;
    private TextButton saveButton;
    private TextButton saveRefreshButton;

    public FormToolBar(BaseForm form) {
        super(form.getActionManager(), false);
        this.form = form;
        setSpacing(DEFAULT_SPACING);

        init();
    }

    public TextButton getCloseButton() {
        return closeButton;
    }

    public TextButton getSaveButton() {
        return saveButton;
    }

    public TextButton getRefreshButton() {
        return refreshButton;
    }

    public TextButton getSaveRefreshButton() {
        return saveRefreshButton;
    }

    public BaseForm getForm() {
        return form;
    }

    protected void init() {
        addCloseButton();
        refreshButton = addButton(new FormRefreshAction(getForm()));

        add(createSeparator());

        addSaveButton();
        saveRefreshButton = addButton(new FormSaveRefreshAction(getForm()));


    }

    protected final void addCloseButton() {
        closeButton = addButton(new FormCloseAction(getForm()));
    }

    protected final void addSaveButton() {
        saveButton = addButton(new FormSaveAction(getForm()));
    }

    protected SeparatorToolItem createSeparator() {
        return new SeparatorToolItem();
    }
}
