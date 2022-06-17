package ru.sbsoft.shared.model.enums;

import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.LocalizedString;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 * Перечисление для представления флагов. Используется в генераторе полей типа CHECKBOX.
 *
 * @author rfa
 */
public enum YesNoEnum implements NamedItem {

    NO(false, SBFGeneralStr.labelNo),
    YES(true, SBFGeneralStr.labelYes);

    private final boolean value;
    private final ILocalizedString name;

    YesNoEnum(boolean value, I18nResourceInfo resourceInfo) {
        this.value = value;
        this.name = new LocalizedString(resourceInfo);
    }

    @Override
    public String getCode() {
        return Boolean.toString(value);
    }

    @Override
    public ILocalizedString getItemName() {
        return name;
    }

    public static YesNoEnum forBoolean(boolean val) {
        return val ? YES : NO;
    }

    public static YesNoEnum forString(String val) {
        return forBoolean(Boolean.parseBoolean(val));
    }
}
