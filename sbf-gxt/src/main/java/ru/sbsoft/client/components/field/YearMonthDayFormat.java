package ru.sbsoft.client.components.field;

import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.api.i18n.consts.SBFEditorStr;

/**
 * Задает формат даты для {@link YearMonthDayMaskField}
 *
 * @author balandin
 * @since Sep 18, 2014 1:03:31 PM
 */
public enum YearMonthDayFormat {

    DATE_SHORT(
            "dd.MM.yyyy",
            "99.99.9999",
            "__.__.____",
            I18n.get(SBFEditorStr.formatDateShow)// "ДД.ММ.ГГГГ"
    );
    //
    public final String format, pattern, nullValue, emptyText;

    private YearMonthDayFormat(String format, String pattern, String nullValue, String emptyText) {
        this.format = format;
        this.pattern = pattern;
        this.nullValue = nullValue;
        this.emptyText = emptyText;
    }

    public String getFormat() {
        return format;
    }

    public String getPattern() {
        return pattern;
    }

    public String getNullValue() {
        return nullValue;
    }

    public String getEmptyText() {
        return emptyText;
    }
}
