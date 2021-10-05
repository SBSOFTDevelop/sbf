package ru.sbsoft.shared.meta.filter;

import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.LocalizedString;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 *
 * @author Kiselev
 */
public enum StoredFilterType implements NamedItem {

    GLOBAL(SBFGeneralStr.labelSystems),
    GROUP(SBFGeneralStr.labelGroups),
    PERSONAL(SBFGeneralStr.labelPersonals);

    private final ILocalizedString resourceInfo;

    private StoredFilterType(I18nResourceInfo resourceInfo) {
        this.resourceInfo = new LocalizedString(resourceInfo);
    }

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public ILocalizedString getItemName() {
        return resourceInfo;
    }

}
