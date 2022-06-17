package ru.sbsoft.client.components.grid.dlgbase;

import ru.sbsoft.svc.data.shared.ListStore;
import ru.sbsoft.client.components.browser.filter.fields.CustomHolder;
import ru.sbsoft.client.components.browser.filter.fields.FieldAccess;
import ru.sbsoft.client.components.browser.filter.fields.HoldersList;

/**
 *
 * @author Kiselev
 */
public class HoldersStore extends ListStore<CustomHolder> {

    public HoldersStore() {
        super(new FieldAccess());
    }
    
    public HoldersStore(HoldersList holdersList) {
        this();
        addAll(holdersList);
    }
    
}
