package ru.sbsoft.client.components.form.action;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.BaseForm;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFEditorStr;

/**
 * Сохраняет данные формы без закрытия.
 */
public class FormSaveRefreshAction extends FormAction {

    public FormSaveRefreshAction(BaseForm form) {
        super(form);
        setCaption(I18n.get(SBFEditorStr.menuSaveRefresh));
        setToolTip(I18n.get(SBFEditorStr.hintSaveRefresh));
        setIcon16(SBFResources.GENERAL_ICONS.SaveRefresh16());
        setIcon24(SBFResources.GENERAL_ICONS.SaveRefresh());
    }

    @Override
    public boolean checkEnabled() {
        return !getForm().isReadOnly() && getForm().isChanged();
    }

    @Override
    protected void onExecute() {
        getForm().saveRefresh();
    }
}
