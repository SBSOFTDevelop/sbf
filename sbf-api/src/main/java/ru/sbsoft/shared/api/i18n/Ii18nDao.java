package ru.sbsoft.shared.api.i18n;

import ru.sbsoft.shared.consts.I18nSection;

/**
 * Интерфейс серверной интернационализации.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public interface Ii18nDao extends java.io.Serializable{

    String I18N_PROTOCOL = "i18n://";
    String I18N_VALUE_REGEXP = I18N_PROTOCOL + "([^\\?]+)(|\\?.+)$";
    String I18N_LIBRARY_REGEXP = ".*library=([^&]+).*";
    String I18N_LOCALE_REGEXP = ".*locale=([^&]+).*";

    /**
     * Формирует информацию по локализации в соотвтетствии с указанной локалью.
     *
     * @param locale название локали.
     * @return информацию по локализации.
     */
    i18nModuleInfo geTi18n(String locale, I18nSection[] files);

    /**
     * Локализует строку вида "i18n://XXX" в соответствующее значение. Строка может содержать параметр ?library=ZZZ, где
     * ZZZ- название файла локализации.
     *
     * @param i18nString строка локализации.
     * @return локализованное значение, если таковое найдено, иначе содержимое параметра i18nString без протокола.
     */
    String i18n(String i18nString);

    /**
     * Осуществляет перевод строкового ресурса.
     *
     * @param locale название локали.
     * @param I18nResourceInfo информация о строковом ресурсе.
     * @param parameters список параметров ILocalizedString
     * @return переведенную строку, если перевод найден, иначе - параметр key.
     */
    String get(String locale, I18nResourceInfo resourceInfo, ILocalizedString... parameters);

    /**
     * Осуществляет перевод строкового ресурса.
     *
     * @param locale название локали.
     * @param I18nResourceInfo информация о строковом ресурсе.
     * @param parameters список строковых параметров
     * @return переведенную строку, если перевод найден, иначе - параметр key.
     */
    String get(String locale, I18nResourceInfo resourceInfo, String... parameters);
    
    /**
     * Осуществляет перевод строкового ресурса.
     *
     * @param locale название локали.
     * @param I18nResourceInfo информация о строковом ресурсе.
     * @return переведенную строку, если перевод найден, иначе - параметр key.
     */
    String get(String locale, I18nResourceInfo resourceInfo);
    
    /**
     * Осуществляет перевод ILocalizedString.
     * @param locale название локали
     * @param item объект, реализующий интерфейс ILocalizedString
     * @return 
     */
    String get(String locale, ILocalizedString item);

    /**
     * Осуществляет перевод указанной строки.
     *
     * @param locale название локали.
     * @param library файл перевода.
     * @param key ключ для перевода.
     * @param parameters список строковых параметров
     * @return переведенную строку, если перевод найден, иначе - параметр key.
     */
    String get(String locale, I18nSection library, String key, ILocalizedString... parameters);

}
