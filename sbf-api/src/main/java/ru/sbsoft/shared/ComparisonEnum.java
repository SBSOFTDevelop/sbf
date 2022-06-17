package ru.sbsoft.shared;

import java.io.Serializable;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.consts.I18nSection;
import ru.sbsoft.shared.consts.I18nType;

/**
 * Перечисление содержит экземпляры логических операторов для построения фильтров (условий where)
 * в SQL запросах.
 * <p>Используется экземплярами перечисления {@link ru.sbsoft.shared.Condition}. 
 * @author panarin
 */
public enum ComparisonEnum implements Serializable, I18nResourceInfo {

    is_null(null),
    eq("="),
    eq_text("="),
    //
    like("LIKE", "="),
    contains("LIKE", "compContains"),
    startswith("LIKE", "compStartsWith"),
    endswith("LIKE", "compEndsWith"),
    //
    gt(">"),
    gteq(">="),
    lt("<"),
    lteq("<="),
    //
    in_range(null, "="), // [] в диапазоне, включая граничные значения
    in_bound(null, "=")  // () в диапазоне, НЕ включая граничные значения
    ;
    //
    private final String sql;
    private final String key;
    private Condition condition;

    ComparisonEnum(String sql) {
        this(sql, null);
    }

    ComparisonEnum(String sql, String key) {
        this.sql = Strings.coalesce(sql);
        this.key = key;
    }

    public String getSql() {
        return sql;
    }

    @Override
    public String getKey() {
        return key;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Condition getCondition() {
        return condition;
    }

    @Override
    public I18nSection getLib() {
        return I18nType.BROWSER;
    }
}
