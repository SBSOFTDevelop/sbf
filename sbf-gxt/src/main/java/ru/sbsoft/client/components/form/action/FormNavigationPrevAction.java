package ru.sbsoft.client.components.form.action;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.BaseForm;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;
/**
 * Отображает в форме данные предыдущей строки связанной таблицы.
 */
public class FormNavigationPrevAction extends FormAction {

    public FormNavigationPrevAction(BaseForm form) {
        super(form);
        setCaption(I18n.get(SBFBrowserStr.menuNavPrev));
        setToolTip(I18n.get(SBFBrowserStr.hintNavPrev));
        setIcon16(SBFResources.BROWSER_ICONS.NavPrev16());
        setIcon24(SBFResources.BROWSER_ICONS.NavPrev());
    }

    @Override
    public boolean checkEnabled() {
        return null != getForm().getOwnerGrid();
    }

    @Override
    protected void onExecute() {
        getForm().getOwnerGrid().gotoPrevRecord();
    }
}
