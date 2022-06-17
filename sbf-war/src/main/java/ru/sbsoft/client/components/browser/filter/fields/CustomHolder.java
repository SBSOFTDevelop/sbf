package ru.sbsoft.client.components.browser.filter.fields;

import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.meta.IColumn;
import ru.sbsoft.shared.meta.ColumnGroup;

/**
 * Базовый класс элементов комбо бокса FieldsComboBox
 * @author balandin
 * @since Oct 8, 2014 2:20:33 PM
 */
public abstract class CustomHolder {

    public static final String INDENT = "&nbsp;&nbsp;&nbsp;";
    //
    private final int level;
    private String key;

    public CustomHolder(int level) {
        this.level = level;
    }

    protected static int calculate(ColumnGroup g) {
        int result = 0;
        if (g != null) {
            do {
                result++;
            } while ((g = g.getParent()) != null);
        }
        return result;
    }

    public final String getComboItemTitle() {
        return Strings.repl(INDENT, level) + getTitle();
    }

    public abstract String getTitle();

    public abstract String getFullTitle();

    public abstract String getShortTitle();

    public abstract boolean mathes(String query, IColumn parent);

    protected String getGroupKey(ColumnGroup group, boolean full) {
        List<ColumnGroup> tmp = new ArrayList<ColumnGroup>();
        ColumnGroup xxx = group;
        do {
            tmp.add(0, xxx);
        } while ((xxx = xxx.getParent()) != null);
        List<String> tmp2 = new ArrayList<String>();
        for (ColumnGroup g : tmp) {
            tmp2.add(getTitle(g, full));
        }
        return Strings.join(tmp2.toArray(), " / ");
    }

    private String getTitle(ColumnGroup g, boolean full) {
        if (full) {
            return I18n.get(g.getTitle());
        }
        if (g.getAbbr() != null) {
            return g.getAbbr().replaceAll("\\.\\.\\.", "…");
        }
        String s = I18n.get(g.getTitle());
        s = s.length() > 18 ? s.substring(0, 10) + " …" : s;
        return s;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
