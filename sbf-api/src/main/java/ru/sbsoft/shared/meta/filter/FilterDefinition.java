package ru.sbsoft.shared.meta.filter;

import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.param.DTO;

/**
 * Базовый класс определений пользовательских фильтров
 * @author balandin
 * @since Nov 2, 2015
 */
public abstract class FilterDefinition implements DTO {

    private String alias;
    private ILocalizedString caption;
    private ILocalizedString description;
    //
    private boolean hidden;
    //
    private transient String clause;
    private transient boolean noSql;

    public FilterDefinition() {
    }

    public String getClause() {
        return clause;
    }

    public void setClause(String clause) {
        this.clause = clause;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public ILocalizedString getCaption() {
        return caption;
    }

    public void setCaption(ILocalizedString caption) {
        this.caption = caption;
    }

    public ILocalizedString getDescription() {
        return description;
    }

    public void setDescription(ILocalizedString description) {
        this.description = description;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isNoSql() {
        return noSql;
    }

    public void setNoSql(boolean noSql) {
        this.noSql = noSql;
    }

}
