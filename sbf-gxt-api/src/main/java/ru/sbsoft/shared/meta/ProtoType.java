package ru.sbsoft.shared.meta;

import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.LocalizedString;

/**
 * @author balandin
 * @since Jun 3, 2015 6:44:15 PM
 */
public enum ProtoType {

    NUMERIC(SBFGeneralStr.enumNumeric),
    DATETIME(SBFGeneralStr.enumDateTime),
    TEXT(SBFGeneralStr.enumText),
    LOGICAL(SBFGeneralStr.enumLogical),
    NONE(SBFGeneralStr.enumUnknown);
    //
    private final ILocalizedString caption;

    ProtoType(I18nResourceInfo caption) {
        this.caption = new LocalizedString(caption);
    }

    public ILocalizedString getCaption() {
        return caption;
    }
}
