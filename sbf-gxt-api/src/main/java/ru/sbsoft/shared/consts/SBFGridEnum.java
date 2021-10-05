package ru.sbsoft.shared.consts;

import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.LocalizedString;
import ru.sbsoft.shared.interfaces.NamedGridType;
import ru.sbsoft.shared.api.i18n.NonLocalizedString;

/**
 * Перечисление, элементы которого используются для индентификации сеток,
 * используемых в специализированнх браузерах шедулера и отложенных операций.
 *
 * @author balandin
 * @since Nov 13, 2014 6:59:02 PM
 */
public enum SBFGridEnum implements NamedGridType {

    SR_MULTI_OPERATION_STATUS,
    SR_MULTI_OPERATION_LOG,
    SR_SCHEDULER,
    SR_GRIDLIST,
    SR_CUSTOMREPORT;
    

    private final ILocalizedString itemName;
    private final String securityId;

    private SBFGridEnum() {
        this(null);
    }

    private SBFGridEnum(String itemName) {
        this(itemName, (String) null);
    }

    private SBFGridEnum(String itemName, SBFGridEnum table) {
        this(itemName, table.getSecurityId());
    }

    private SBFGridEnum(String itemName, String tableName) {
        this.itemName = itemName != null ? new NonLocalizedString(itemName) : new NonLocalizedString(name());
        this.securityId = tableName != null ? tableName : name();
    }

    private SBFGridEnum(I18nResourceInfo itemName, String tableName) {
        this.itemName = itemName != null ? new LocalizedString(itemName) : new NonLocalizedString(name());
        this.securityId = tableName != null ? tableName : name();
    }

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public ILocalizedString getItemName() {
        return itemName;
    }

    @Override
    public String getSecurityId() {
        return securityId;
    }
}
