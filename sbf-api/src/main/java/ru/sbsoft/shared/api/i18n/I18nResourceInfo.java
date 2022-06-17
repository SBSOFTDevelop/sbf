package ru.sbsoft.shared.api.i18n;

import ru.sbsoft.shared.consts.I18nSection;

/**
 * Универсальный (не зависящий от локали) путь строкового ресурса.
 *
 * @author Kiselev
 */
public interface I18nResourceInfo extends java.io.Serializable {

    /**
     * @return идентификатор библиотеки строкового ресурса
     */
    I18nSection getLib();

    /**
     * @return ключ строкового ресурса
     */
    String getKey();
}
