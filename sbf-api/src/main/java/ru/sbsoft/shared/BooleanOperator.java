package ru.sbsoft.shared;

import java.io.Serializable;
import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.consts.I18nSection;
import ru.sbsoft.shared.consts.I18nType;

/**
 * Перечисление содержит экземпляры логических операторов использующихся в SQL запросе.
 * Используется экземпляром класса {@link ru.sbsoft.meta.sql.SQLBuilder} для построения условий в операторе {@code where}.
 * @author balandin
 * @since Dec 4, 2014 1:54:48 PM
 */
public enum BooleanOperator implements Serializable, I18nResourceInfo {

    AND("&"),
    OR("||"),
    NOT("!");
    //
    private final String icon;

    BooleanOperator(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public String getKey() {
        return "bool" + name();
    }

    public BooleanOperator getReverseOperator() {
        switch (this) {
            case AND:
                return BooleanOperator.OR;
            case OR:
                return BooleanOperator.AND;
        }
        throw new IllegalStateException();
    }

    @Override
    public I18nSection getLib() {
        return I18nType.BROWSER;
    }
}
