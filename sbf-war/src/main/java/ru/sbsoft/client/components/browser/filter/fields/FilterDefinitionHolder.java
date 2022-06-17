package ru.sbsoft.client.components.browser.filter.fields;

import ru.sbsoft.client.I18n;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.meta.IColumn;
import ru.sbsoft.shared.meta.filter.FilterDefinition;

/**
 * Пользовательский или лукап элемент
 * @author balandin
 * @since Nov 6, 2015
 */
public class FilterDefinitionHolder extends ItemHolder {

    private final FilterDefinition definition;

    public FilterDefinitionHolder(FilterDefinition definition) {
        super(1);
        this.definition = definition;
    }

    public FilterDefinition getDefinition() {
        return definition;
    }

    @Override
    public String getTitle() {
        return I18n.get(getDefinition().getCaption());
    }

    @Override
    public String getFullTitle() {
        return I18n.get(getDefinition().getCaption());
    }

    @Override
    public String getShortTitle() {
        return Strings.coalesce(I18n.get(getDefinition().getCaption()), getDefinition().getAlias());
    }

    @Override
    public boolean mathes(String query, IColumn parent) {
        if (parent != null) {
            return false;
        }
        if (query == null) {
            return true;
        }
        return getTitle().toLowerCase().contains(query.toLowerCase());
    }
}
