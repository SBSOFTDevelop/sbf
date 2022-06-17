package ru.sbsoft.client.components.form.action;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.BaseForm;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Отображает в форме данные следующей строки связанной таблицы.
 */
public class FormNavigationNextAction extends FormAction {

    public FormNavigationNextAction(BaseForm form) {
        super(form);
        setCaption(I18n.get(SBFBrowserStr.menuNavNext));
        setToolTip(I18n.get(SBFBrowserStr.hintNavNext));
        setIcon16(SBFResources.BROWSER_ICONS.NavNext16());
        setIcon24(SBFResources.BROWSER_ICONS.NavNext());
    }

    @Override
    public boolean checkEnabled() {
        return null != getForm().getOwnerGrid();
    }

    @Override
    protected void onExecute() {
        getForm().getOwnerGrid().gotoNextRecord();
    }
}
