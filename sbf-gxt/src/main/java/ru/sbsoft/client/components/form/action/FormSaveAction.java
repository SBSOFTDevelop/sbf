package ru.sbsoft.client.components.form.action;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.BaseForm;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFEditorStr;

/**
 * Сохраняет данные формы и закрывает ее.
 */
public class FormSaveAction extends FormAction {

    public FormSaveAction(BaseForm form) {
        super(form);
        setCaption(I18n.get(SBFEditorStr.menuSave));
        setToolTip(I18n.get(SBFEditorStr.hintSave));
        setIcon16(SBFResources.GENERAL_ICONS.Save16());
        setIcon24(SBFResources.GENERAL_ICONS.Save());
    }

    @Override
    public boolean checkEnabled() {
        return !getForm().isReadOnly() && getForm().isChanged();
    }

    @Override
    protected void onExecute() {
        getForm().saveClose();
    }
}
