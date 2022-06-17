package ru.sbsoft.client.components.form.action;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.BaseForm;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Действие закрытия формы.
 * Выполняется как скрытие (hide).
 */
public class FormCloseAction extends FormAction {

    public FormCloseAction(BaseForm form) {
        super(form);
        setCaption(I18n.get(SBFBrowserStr.menuFileExit));
        setToolTip(I18n.get(SBFBrowserStr.hintFileExit));
        setIcon16(SBFResources.GENERAL_ICONS.Exit16());
        setIcon24(SBFResources.GENERAL_ICONS.Exit());
    }

    @Override
    protected void onExecute() {
        getForm().hide();
    }
}
