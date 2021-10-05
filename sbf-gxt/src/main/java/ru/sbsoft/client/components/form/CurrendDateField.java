package ru.sbsoft.client.components.form;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.field.DateField;
import ru.sbsoft.shared.api.i18n.consts.SBFFormStr;

/**
 * Поле для задания текущей даты.
 * @author panarin
 */
public class CurrendDateField extends DateField {

    public CurrendDateField() {
        super();
        setToolTip(I18n.get(SBFFormStr.labelCurrentWorkDate));
        setAutoValidate(true);
        setAllowBlank(false);
    }
}
