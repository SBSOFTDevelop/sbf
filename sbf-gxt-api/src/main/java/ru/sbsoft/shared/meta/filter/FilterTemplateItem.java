package ru.sbsoft.shared.meta.filter;

import ru.sbsoft.shared.Condition;
import ru.sbsoft.shared.param.DTO;

/**
 * @author balandin
 * @since Oct 29, 2015
 */
public class FilterTemplateItem<SELF extends FilterTemplateItem<?>> implements DTO, FilterTemplateConfig<SELF> {

    private String alias;
    private Condition condition;
    private boolean strict;
    private boolean required;
    //
    private transient Object object;

    public FilterTemplateItem() {
        this(null);
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public FilterTemplateItem(String alias) {
        this.alias = alias;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public SELF op(Condition condition) {
        this.condition = condition;
        return (SELF) this;
    }

    public boolean getStrict() {
        return strict;
    }

    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    public SELF strict() {
        setStrict(true);
        return (SELF) this;
    }

    public boolean getRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public SELF req() {
        setRequired(true);
        return (SELF) this;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public FilterTemplateItem assign(Object object) {
        setObject(object);
        return this;
    }
}
