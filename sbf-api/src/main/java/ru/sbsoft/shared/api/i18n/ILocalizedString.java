package ru.sbsoft.shared.api.i18n;

import ru.sbsoft.shared.param.DTO;

/**
 * Представляет интерфейс локализованного ресурса I18nResourceInfo. Если локализованный ресурс не указан, то
 * представляется не локлизованное строковое значение DefaultName.
 * @author Sokoloff
 */
public interface ILocalizedString extends DTO {

    I18nResourceInfo getResourceInfo();

    String getDefaultName();
    
}
