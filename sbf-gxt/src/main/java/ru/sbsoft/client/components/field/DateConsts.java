package ru.sbsoft.client.components.field;

import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.api.i18n.consts.SBFEditorStr;

/**
 * Задает тип (дата или дата-время) для {@link DateField}
 *
 * @author balandin
 * @since Sep 18, 2014 1:03:31 PM
 */
public enum DateConsts {

    DATE(
            "dd.MM.yyyy",
            "99.99.9999",
            "__.__.____",
            I18n.get(SBFEditorStr.formatDateShow)// "ДД.ММ.ГГГГ"
    ),
    DATE_TIME(
            "dd.MM.yyyy HH:mm:ss",
            "99.99.9999 99:99:99",
            "__.__.____ __:__:__",
            I18n.get(SBFEditorStr.formatDateTimeShow) //"ДД.ММ.ГГГГ чч:мм:сс"
    ),
    TIME(
            "HH:mm:ss",
            "99:99:99",
            "__:__:__",
            I18n.get(SBFEditorStr.formatTimeShow) //"чч:мм:сс"
    );
    //
    
    public final String FORMAT, PATTERN, NULL_VALUE, EMPTY_TEXT;


private DateConsts(String format, String pattern, String nullValue, String emptyText) {
        this.FORMAT = format;
        this.PATTERN = pattern;
        this.NULL_VALUE = nullValue;
        this.EMPTY_TEXT = emptyText;
    }
}
