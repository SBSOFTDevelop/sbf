package ru.sbsoft.client.components.grid.sort;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.SortInfo;
import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.components.grid.dlgbase.Group;

/**
 *
 * @author Kiselev
 */
public class SortGroup extends Group {
    public List<SortInfo> getSort(){
        List<SortInfo> sort = new ArrayList<SortInfo>();
        for (Widget w : unitContainer) {
            if (w instanceof SortItem) {
                sort.add(((SortItem) w).getSortInfo());
            }
        }
        return sort;
    }
}
