package ru.sbsoft.shared.api.i18n;

/**
 * Интерфейс для работы с интернационализацией на клиенте.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public interface i18nUnit {
    
    /**
     * Локализует строку вида "i18n://XXX" в соответствующее значение. Строка может содержать
     * параметр ?library=ZZZ, где ZZZ- название файла локализации.
     *
     * @param i18nString строка локализации.
     * @return локализованное значение, если таковое найдено, иначе содержимое параметра i18nString
     * без протокола.
     */
    public String i18n(String i18nString);

    /**
     * Получает перевод по значению с указанными значениями параметров.
     *
     * @param key значение ключа для перевода.
     * @param parameters значения параметров для заполнения переводимой строки.
     * @return
     */
//    public String get(String key, String... parameters);

    /**
     * Получает перевод по значению из указанного файла.
     *
     * @param inf путь-идентификатор строкового ресурса
     * @param parameters значения параметров для заполнения переводимой строки. 
     * @return локализованное значение
     */
    public String get(I18nResourceInfo inf, ILocalizedString... parameters);
    /**
     * Получает перевод по значению из указанного файла.
     *
     * @param inf путь-идентификатор строкового ресурса
     * @param parameters значения параметров для заполнения переводимой строки. 
     * @return локализованное значение
     */
    public String get(I18nResourceInfo inf, String... parameters);
    /**
     * Получает перевод по значению из указанного файла.
     *
     * @param inf путь-идентификатор строкового ресурса
     * @return локализованное значение
     */
    public String get(I18nResourceInfo inf);
}
