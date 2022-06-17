package ru.sbsoft.client.components.form;

import ru.sbsoft.svc.cell.core.client.form.ComboBoxCell;
import ru.sbsoft.svc.data.shared.LabelProvider;
import ru.sbsoft.svc.data.shared.ListStore;
import ru.sbsoft.svc.widget.core.client.form.SimpleComboBox;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ru.sbsoft.client.components.browser.filter.LookupSuccessHandler;
import ru.sbsoft.client.utils.ClientUtils;

public class LookupComboBox extends SimpleComboBox<LookupItem> implements LookupSuccessHandler {

    public LookupComboBox(LabelProvider<? super LookupItem> labelProvider) {
        super(labelProvider);
    }

    public LookupComboBox(ComboBoxCell<LookupItem> cell) {
        super(cell);
    }

    @Override
    public void onLookupLoad(LookupItemsList items) {
        getStore().clear();
        if (items != null) {
            List<LookupItem> addItems = new ArrayList<LookupItem>(items);
            Collections.sort(addItems, new LookupKeyComparator());
            final ListStore<LookupItem> store = getStore();
            store.addAll(addItems);
            LookupItem val = getValue();
            if (val != null && val.isUserInput() && !addItems.isEmpty()) {
                val = findLookupItem(val.getKey());
                if (val != null) {
                    setValue(val, false);
                }
            }
        }
    }

    private LookupItem findLookupItem(Object value) {
        final List<LookupItem> items = getStore().getAll();
        for (LookupItem item : items) {
            if (ClientUtils.equals(item.getKey(), value)) {
                return item;
            }
        }
        return null;
    }
}
