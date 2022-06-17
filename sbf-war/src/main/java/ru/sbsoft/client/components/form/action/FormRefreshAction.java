package ru.sbsoft.client.components.form.action;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.BaseForm;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Обновляет данные формы с сервера.
 */
public class FormRefreshAction extends FormAction {

    public FormRefreshAction(BaseForm form) {
        super(form);
        setCaption(I18n.get(SBFBrowserStr.menuFileRefresh));
        setToolTip(I18n.get(SBFBrowserStr.hintFileRefresh));
        setIcon16(SBFResources.BROWSER_ICONS.Refresh16());
        setIcon24(SBFResources.BROWSER_ICONS.Refresh());
    }

    @Override
    protected void onExecute() {
        getForm().refresh();
    }
}
