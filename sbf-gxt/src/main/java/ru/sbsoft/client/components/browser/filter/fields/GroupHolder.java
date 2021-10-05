package ru.sbsoft.client.components.browser.filter.fields;

import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.meta.Column;

/**
 * Декоративные элементы, (заголовки)
 * @author balandin
 * @since May 27, 2015 5:41:49 PM
 */
public abstract class GroupHolder extends CustomHolder {

    private List<CustomHolder> childs = new ArrayList<CustomHolder>();

    public GroupHolder(int level) {
        super(level);
    }

    public List<CustomHolder> getChilds() {
        return childs;
    }

    public void setChilds(List<CustomHolder> childs) {
        this.childs = childs;
    }

    @Override
    public boolean mathes(String query, Column opposite) {
        query = Strings.clean(query, false);
        for (CustomHolder f : childs) {
            if (f.mathes(query, opposite)) {
                return true;
            }
        }
        return false;
    }
}
