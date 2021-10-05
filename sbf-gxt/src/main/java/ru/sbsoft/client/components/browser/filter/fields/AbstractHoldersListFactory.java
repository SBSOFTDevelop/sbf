package ru.sbsoft.client.components.browser.filter.fields;

import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import ru.sbsoft.client.components.grid.SystemGrid;
import ru.sbsoft.client.components.grid.column.CustomColumnConfig;
import ru.sbsoft.shared.meta.ColumnGroup;
import ru.sbsoft.shared.meta.ColumnType;

/**
 *
 * @author Kiselev
 */
public abstract class AbstractHoldersListFactory<H extends HoldersList> {

    public H create(SystemGrid grid) {
        H hold = createInstance();
        HashMap<ColumnGroup, GroupHolder> groups = new HashMap<ColumnGroup, GroupHolder>();
        // добовляем колонки
        ColumnModel cm = grid.getGrid().getColumnModel();
        for (int i = 0; i < cm.getColumnCount(); i++) {
            ColumnConfig column = cm.getColumn(i);
            if (column instanceof CustomColumnConfig) {
                CustomColumnConfig c = (CustomColumnConfig) column;
                if (isColumnMatch(c)) {
                    // регистрируем группы колонок
                    ColumnGroup g = c.getColumn().getGroup();
                    if (g != null) {
                        do {
                            if (groups.containsKey(g)) {
                                break;
                            }
                            groups.put(g, new ColumnGroupHolder(g));
                        } while ((g = (g.getParent())) != null);
                    }
                    // привязываем группу колонок к подчненным, для последующего поиска и фильтрации
                    FieldHolder f = new FieldHolder(c);
                    g = c.getColumn().getGroup();
                    if (g != null) {
                        groups.get(g).getChilds().add(f);
                    }
                    // добавляем колонку
                    hold.add(f);
                }
            }
        }

        // добовляем группы колонок
        Iterator<ColumnGroup> it = groups.keySet().iterator();
        while (it.hasNext()) {
            ColumnGroup g = it.next();
            GroupHolder h = groups.get(g);
            if (g.getParent() != null) {
                groups.get(g.getParent()).getChilds().add(h);
            }
            hold.add(h);
        }

        // сортируем все что получилось
        Collections.sort(hold, new Comparator<CustomHolder>() {
            @Override
            public int compare(CustomHolder o1, CustomHolder o2) {
                FieldHolder f1 = o1 instanceof FieldHolder ? (FieldHolder) o1 : null;
                FieldHolder f2 = o2 instanceof FieldHolder ? (FieldHolder) o2 : null;
                boolean ng1 = f1 != null ? f1.getColumn().getColumn().getGroup() == null : false;
                boolean ng2 = f2 != null ? f2.getColumn().getColumn().getGroup() == null : false;
                if (ng1 && !ng2) {
                    return -1;
                } else if (ng2 && !ng1) {
                    return 1;
                }
                return o1.getFullTitle().compareTo(o2.getFullTitle());
            }
        });
        return hold;
    }

    public final boolean hasColumnMatch(SystemGrid grid) {
        Grid g;
        ColumnModel cm;
        if (grid != null && (g = grid.getGrid()) != null && (cm = g.getColumnModel()) != null) {
            for (int i = 0; i < cm.getColumnCount(); i++) {
                ColumnConfig column = cm.getColumn(i);
                if(isColumnMatch(column)){
                    return true;
                }
            }
        }
        return false;
    }

    public final boolean isColumnMatch(ColumnConfig c) {
        if (c instanceof CustomColumnConfig) {
            return isColumnMatch((CustomColumnConfig) c);
        }
        return false;
    }

    protected boolean isColumnMatch(CustomColumnConfig c) {
        ColumnType type = c.getColumn().getType();
        return c.isEnabled() && !(type == ColumnType.KEY || type == ColumnType.TEMPORAL_KEY);
    }

    protected abstract H createInstance();
}
