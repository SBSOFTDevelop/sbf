package ru.sbsoft.shared.meta;

import ru.sbsoft.shared.meta.ColumnType;

/**
 * Типы обобщений для задания в шаблонах таблиц.
 *
 * @see ru.sbsoft.meta.columns.ColumnInfo#setFooter(String expression)
 * @author balandin
 * @since May 16, 2014 3:19:49 PM
 */
public enum Aggregate {

    SUM("Сумма"),
    MIN("Минимальное"),
    MAX("Максимальное"),
    COUNT("Количество", "0"),
    AVG("Среднее", "0.0000");
    //
    private final String title;
    private final String format;

    Aggregate(String title) {
        this(title, null);
    }

    Aggregate(String title, String format) {
        this.title = title;
        this.format = format;
    }

    public String getTitle() {
        return title;
    }

    public String getFormat() {
        return format;
    }

    public boolean isApplicable(ColumnType colType) {
        switch (colType.getProtoType()) {
            case NUMERIC:
                return true;
            case DATETIME:
                return in(MIN, MAX, COUNT);
            default:
                return this == COUNT;
        }
    }

    public void checkApplicable(ColumnType colType) {
        if (!isApplicable(colType)) {
            throw new IllegalArgumentException("Aggregate function '" + this + "' is not applicable for column type '" + colType + "'");
        }
    }

    private boolean in(Aggregate... ags) {
        if (ags != null && ags.length > 0) {
            for (Aggregate a : ags) {
                if (a == this) {
                    return true;
                }
            }
        }
        return false;
    }
}
