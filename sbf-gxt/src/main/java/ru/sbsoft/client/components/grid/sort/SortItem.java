package ru.sbsoft.client.components.grid.sort;

import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.SortInfoBean;
import ru.sbsoft.client.components.browser.filter.fields.CustomHolder;
import ru.sbsoft.client.components.browser.filter.fields.FieldHolder;
import ru.sbsoft.client.components.browser.filter.fields.HoldersList;
import ru.sbsoft.client.components.grid.dlgbase.HoldersStore;
import ru.sbsoft.client.components.grid.dlgbase.Item;
import ru.sbsoft.client.utils.HLC;

/**
 *
 * @author Kiselev
 */
public class SortItem extends Item {

    private final SortOrderControl sortOrderControl;
    private final HoldersList holdersList;

    public SortItem(HoldersList holdersList) {
        super(new HoldersStore(holdersList));
        this.holdersList = holdersList;
        add(sortOrderControl = new SortOrderControl(), HLC.CONST);
    }

    public void setSortInfo(SortInfo s) throws ColumnNotFoundException {
        String alias = s.getSortField();
        CustomHolder h = null;
        if (alias != null) {
            h = holdersList.findHolder(alias);
        }
        if (h == null) {
            setCaption(alias);
            fields.setVisible(false);
        } else {
            setCaption((String)null);
            fields.setVisible(true);
            fields.setValue(h, false, true);
        }
        sortOrderControl.setDir(s.getSortDir());
    }

    public SortInfo getSortInfo() {
        String alias = null;
        if (getCaption() != null) {
            alias = getCaption().getText();
        } else {
            FieldHolder h = (FieldHolder) fields.getValue();
            if (h != null) {
                alias = h.getColumn().getColumn().getAlias();
            }
        }
        return alias != null ? new SortInfoBean(alias, sortOrderControl.getDir()) : null;
    }

}
