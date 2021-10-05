package ru.sbsoft.client.components.browser.filter.fields;

import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.meta.ColumnGroup;

/**
 * @author balandin
 * @since Nov 6, 2015
 */
public class ColumnGroupHolder extends GroupHolder {

    private final ColumnGroup columnGroup;
    private String fullTitle;
    private String key;

    public ColumnGroupHolder(ColumnGroup columnGroup) {
        super(calculate(columnGroup) - 1);
        this.columnGroup = columnGroup;
    }

    @Override
    public String getTitle() {
        return I18n.get(getColumnGroup().getTitle());
    }

    public ColumnGroup getColumnGroup() {
        return columnGroup;
    }

    @Override
    public String getShortTitle() {
        if (key == null) {
            key = getGroupKey(columnGroup, false);
        }
        return key;
    }

    @Override
    public String getFullTitle() {
        if (fullTitle == null) {
            fullTitle = getGroupKey(columnGroup, true);
        }
        return fullTitle;
    }
}
