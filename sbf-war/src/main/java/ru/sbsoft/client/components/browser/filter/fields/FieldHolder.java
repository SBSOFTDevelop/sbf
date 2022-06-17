package ru.sbsoft.client.components.browser.filter.fields;

import ru.sbsoft.client.components.grid.column.CustomColumnConfig;
import ru.sbsoft.shared.meta.IColumn;

/**
 * Колоночный фильтр
 * @author balandin
 * @since May 27, 2015 5:43:05 PM
 */
public class FieldHolder extends ItemHolder {

    private final CustomColumnConfig columnConfig;

    public FieldHolder(CustomColumnConfig columnConfig) {
        super((columnConfig != null) ? calculate(columnConfig.getColumn().getGroup()) : 0);
        this.columnConfig = columnConfig;
    }

    @Override
    public String getTitle() {
        return columnConfig.getTitle();
    }

    public CustomColumnConfig getColumn() {
        return columnConfig;
    }

    @Override
    public String getShortTitle() {
        if (columnConfig.getColumn().getGroup() != null) {
            return getGroupKey(columnConfig.getColumn().getGroup(), false) + " / " + columnConfig.getTitle();
        } else {
            return columnConfig.getTitle();
        }
    }

    @Override
    public String getFullTitle() {
        if (columnConfig.getColumn().getGroup() != null) {
            return getGroupKey(columnConfig.getColumn().getGroup(), true) + " / " + columnConfig.getTitle();
        } else {
            return columnConfig.getTitle();
        }
    }

    @Override
    public boolean mathes(String query, IColumn opposite) {
        if (opposite != null) {
            if (!opposite.isComparable(columnConfig.getColumn())) {
                return false;
            }
        }
        if (query == null) {
            return true;
        }
        return getTitle().toLowerCase().contains(query.toLowerCase());
    }
}
