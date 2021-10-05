package ru.sbsoft.shared.consts;

import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.LocalizedString;
import ru.sbsoft.shared.api.i18n.consts.SBFReportStr;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 *
 * @author sokolov
 */
public enum CustomReportParamType implements NamedItem {
    
    STRING(SBFReportStr.nameStringParameter),
    LONG(SBFReportStr.nameLongParameter),
    DATE(SBFReportStr.nameDateParameter)
    ;
    
    private final ILocalizedString itemName;

    private CustomReportParamType(I18nResourceInfo resourceInfo) {
        this.itemName = new LocalizedString(resourceInfo);
    }

    @Override
    public ILocalizedString getItemName() {
        return itemName;
    }

    @Override
    public String getCode() {
        return name();
    }
    
}
