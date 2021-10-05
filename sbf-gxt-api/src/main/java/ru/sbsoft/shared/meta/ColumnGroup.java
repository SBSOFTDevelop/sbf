package ru.sbsoft.shared.meta;

import java.io.Serializable;
import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.LocalizedString;
import ru.sbsoft.shared.api.i18n.NonLocalizedString;

/**
 * @author balandin
 * @since Apr 23, 2014 4:51:03 PM
 */
public class ColumnGroup implements Serializable {

    private ILocalizedString title;
    private ColumnGroup parent;
    private String abbr;
    private int rows = 1;

    public ColumnGroup() {
    }

    public static ColumnGroup create(ILocalizedString title) {
        return create(title, 1, null);
    }

    public static ColumnGroup create(I18nResourceInfo title) {
        return create(title, 1, null);
    }

    public static ColumnGroup create(String title) {
        return create(title, 1, null);
    }

    public static ColumnGroup create(ILocalizedString title, ColumnGroup parent) {
        return create(title, 1, parent);
    }

    public static ColumnGroup create(I18nResourceInfo title, ColumnGroup parent) {
        return create(title, 1, parent);
    }

    public static ColumnGroup create(String title, ColumnGroup parent) {
        return create(title, 1, parent);
    }

    public static ColumnGroup create(ILocalizedString title, int rows) {
        return create(title, rows, null);
    }

    public static ColumnGroup create(I18nResourceInfo title, int rows) {
        return create(title, rows, null);
    }

    public static ColumnGroup create(String title, int rows) {
        return create(title, rows, null);
    }

    public static ColumnGroup create(ILocalizedString title, int rows, ColumnGroup parent) {
        ColumnGroup g = new ColumnGroup();
        g.setTitle(title);
        g.setParent(parent);
        g.setRows(rows);
        return g;
    }
    
    public static ColumnGroup create(I18nResourceInfo title, int rows, ColumnGroup parent) {
        return create(new LocalizedString(title), rows, parent);
    }

    public static ColumnGroup create(String title, int rows, ColumnGroup parent) {
        return create(new NonLocalizedString(title), rows, parent);
    }


    public ILocalizedString getTitle() {
        return title;
    }

    public void setTitle(ILocalizedString title) {
        this.title = title;
    }

    public ColumnGroup getParent() {
        return parent;
    }

    public void setParent(ColumnGroup parent) {
        this.parent = parent;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public ColumnGroup abbr(String abbr) {
        this.abbr = abbr;
        return this;
    }

}
