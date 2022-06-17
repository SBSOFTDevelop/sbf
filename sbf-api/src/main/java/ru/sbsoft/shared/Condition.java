package ru.sbsoft.shared;

import java.io.Serializable;
import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.consts.I18nSection;
import ru.sbsoft.shared.consts.I18nType;

/**
 * Перечисление содержит экземпляры условий в пользовательских фильтрах.
 *
 * @author balandin
 * @since Jan 9, 2013 12:11:15 PM
 */
public enum Condition implements Serializable, I18nResourceInfo {

    EMPTY(ComparisonEnum.is_null, false, "X"),
    EQUAL(ComparisonEnum.eq, true, "="),
    EQUAL_ALT(ComparisonEnum.eq_text, true, "="),
    //
    LIKE(ComparisonEnum.like, false, "="),
    CONTAINS(ComparisonEnum.contains, false, "*?*"),
    STARTS_WITH(ComparisonEnum.startswith, false, "*?"),
    ENDS_WITH(ComparisonEnum.endswith, false, "?*"),
    //
    GREATER(ComparisonEnum.gt, true, ">"),
    GREATER_OR_EQUAL(ComparisonEnum.gteq, true, ">="),
    LESS(ComparisonEnum.lt, true, "<"),
    LESS_OR_EQUAL(ComparisonEnum.lteq, true, "<="),
    //
    IN_RANGE(ComparisonEnum.in_range, false, "[?, ?]"),
    IN_BOUND(ComparisonEnum.in_bound, false, "(?, ?)");
    //
    private final ComparisonEnum comparison;
    private final boolean canCompareFields;
    private final String label;

    Condition(ComparisonEnum comparison, boolean canCompareFields, String label) {
        this.comparison = comparison;
        this.canCompareFields = canCompareFields;
        this.label = label;
    }

    @Override
    public String getKey() {
        return "cond" + name();
    }

    public ComparisonEnum getComparison() {
        return comparison;
    }

    public boolean isCanCompareFields() {
        return canCompareFields;
    }

    public String getLabel() {
        return label;
    }

    static {
        for (Condition c : values()) {
            c.getComparison().setCondition(c);
        }
    }

    @Override
    public I18nSection getLib() {
        return I18nType.BROWSER;
    }
}
