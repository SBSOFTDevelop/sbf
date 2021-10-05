package ru.sbsoft.shared.model.enums;

import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.LocalizedString;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 *
 * @author sokolov
 */
public enum GridTypeEnum implements NamedItem {
    
    NORM(SBFGeneralStr.labelGridNorm), 
    DYN(SBFGeneralStr.labelGridDyn);
    
    private final ILocalizedString itemName;

    private GridTypeEnum(I18nResourceInfo resourceInfo) {
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
