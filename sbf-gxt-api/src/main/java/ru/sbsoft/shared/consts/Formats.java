package ru.sbsoft.shared.consts;

import ru.sbsoft.shared.meta.Column;

/**
 * Константы-идентификаторы встроенных форматов колонки браузера.
 * @see Column#setFormat(java.lang.String) 
 * @see ru.sbsoft.dao.AbstractTemplate
 * @author balandin
 * @since May 12, 2014 2:55:06 PM
 */
public interface Formats {

    public static final String DATE_SHORT = "DATE_SHORT"; // dd.MM.yyyy
    public static final String DATE_TIME_SHORT = "DATE_TIME_SHORT";  // dd.MM.yyyy HH.mm
    public static final String DATE_TIME_MEDIUM = "DATE_TIME_MEDIUM";  // dd.MM.yyyy HH.mm.ss
    public static final String HOUR24_MINUTE = "HOUR24_MINUTE"; // HH.mm
    //
    public static final String BOOL_YN = "BOOL_YN"; // Да / Нет
    public static final String BOOL_Y = "BOOL_Y"; // Да 
    public static final String BOOL_PM = "BOOL_PM"; // + / -
    public static final String BOOL_GENDER = "BOOL_GENDER"; // Жен / Муж
    public static final String BOOL_DEFAULT = BOOL_YN; // default boolean renderer
    //
    public static final String MONTHS = "MONTHS"; // Месяццццыы
    public static final String RESIDENT = "RESIDENT"; // Украинццццы
}
