package ru.sbsoft.shared.consts;

/**
 * Константы-идентификаторы встроенных форматов колонки браузера.
 * @see Column#setFormat(java.lang.String) 
 * @see ru.sbsoft.dao.AbstractTemplate
 * @author balandin
 * @since May 12, 2014 2:55:06 PM
 */
public interface Formats {

    String DATE_SHORT = "DATE_SHORT"; // dd.MM.yyyy
    String DATE_TIME_SHORT = "DATE_TIME_SHORT";  // dd.MM.yyyy HH.mm
    String DATE_TIME_MEDIUM = "DATE_TIME_MEDIUM";  // dd.MM.yyyy HH.mm.ss
    String HOUR24_MINUTE = "HOUR24_MINUTE"; // HH.mm
    //
    String BOOL_YN = "BOOL_YN"; // Да / Нет
    String BOOL_Y = "BOOL_Y"; // Да
    String BOOL_PM = "BOOL_PM"; // + / -
    String BOOL_GENDER = "BOOL_GENDER"; // Жен / Муж
    String BOOL_DEFAULT = BOOL_YN; // default boolean renderer
    //
    String MONTHS = "MONTHS"; // Месяццццыы
    String RESIDENT = "RESIDENT"; // Украинццццы
}
