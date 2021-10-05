package ru.sbsoft.shared;

import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.LocalizedString;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.interfaces.OperationType;

/**
 * Перечисление содержит типы операций .
 * <p>
 * используется при создании экземпляра построителя операций
 * {@link ru.sbsoft.client.components.operation.OperationMaker} в конструкторе
 * {@code  public OperationMaker(OperationType type)}.
 *
 * @author rfa
 */
public enum CommonOperationEnum implements OperationType {

    EXPORT(SBFGeneralStr.enumExportData),
    CUSTOM_REPORT(SBFGeneralStr.enumCustomReport),
    KLADR_IMPORT(SBFGeneralStr.enumLoadKLADR)
    ;
    //
    private ILocalizedString title;

    private CommonOperationEnum(I18nResourceInfo title) {
        this.title = new LocalizedString(title);
    }

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public ILocalizedString getTitle() {
        return title;
    }

    @Override
    public String getSecurityRole() {
        return name();
    }

}
