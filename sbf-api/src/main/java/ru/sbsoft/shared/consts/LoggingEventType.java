/*
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */
package ru.sbsoft.shared.consts;

import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.LocalizedString;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 * Тип события журналирования
 * 
 * @author sokolov
 */
public enum LoggingEventType implements NamedItem {

    LOGIN(SBFGeneralStr.eventLogin),
    LOGOUT(SBFGeneralStr.eventLogout),
    CREATE(SBFGeneralStr.eventCreate),
    UPDATE(SBFGeneralStr.eventUpdate),
    DELETE(SBFGeneralStr.eventDelete);
    
    private final ILocalizedString itemName;

    private LoggingEventType(I18nResourceInfo resourceInfo) {
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
