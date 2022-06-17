package ru.sbsoft.client.components;

import ru.sbsoft.svc.cell.core.client.form.ComboBoxCell;
import ru.sbsoft.svc.data.shared.LabelProvider;
import ru.sbsoft.svc.data.shared.ListStore;
import ru.sbsoft.svc.data.shared.ModelKeyProvider;
import ru.sbsoft.client.components.form.LookupItem;

public class NoRestrictStringComboBoxCell extends ComboBoxCell<LookupItem> {

    public NoRestrictStringComboBoxCell(LabelProvider<LookupItem> labelProvider) {
        super(new ListStore<LookupItem>(new ModelKeyProvider<LookupItem>() {
            @Override
            public String getKey(LookupItem item) {
                return item.getValue();
            }
        }), labelProvider);
    }

    @Override
    protected LookupItem selectByValue(String value) {
        LookupItem item = findByValue(value);
        if (item != null) {
            select(item);
        } else {
            selectNullValue(value);
        }
        return item;
    }

    protected LookupItem findByValue(String value) {
        if (value != null && !value.isEmpty()) {
            int count = store.size();
            for (int i = 0; i < count; i++) {
                LookupItem item = store.get(i);
                String v = getPropertyEditor().render(item); // String v = getRenderedValue(item);
                if (v != null && v.equals(value)) {
                    return item;
                }
            }
        }
        return null;
    }

    protected void selectNullValue(String value) {
    }
}
