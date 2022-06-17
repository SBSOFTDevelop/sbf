package ru.sbsoft.shared.api.i18n;

import ru.sbsoft.shared.consts.I18nSection;

/**
 * Интерфейс определяет ресурс использования строк локализации. Используется в Render.
 *
 * @author Sokoloff
 */
public interface I18nResource {

    String i18n(I18nSection library, String key);
    
    String i18n(ILocalizedString item);
}
