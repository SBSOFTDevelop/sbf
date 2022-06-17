package ru.sbsoft.shared.model.enums;

import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.LocalizedString;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 * Статус планировщика.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public enum SchedulerStatus implements NamedItem {

    READY(SBFGeneralStr.labelReadyStart),
    PROCESS(SBFGeneralStr.labelProcessed),
    ERROR(SBFGeneralStr.labelErrorRunning);

    private final ILocalizedString label;

    SchedulerStatus(I18nResourceInfo resourceInfo) {
        this.label = new LocalizedString(resourceInfo);
    }

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public ILocalizedString getItemName() {
        return label;
    }

}
