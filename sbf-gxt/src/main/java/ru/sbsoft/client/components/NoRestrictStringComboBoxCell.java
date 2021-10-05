package ru.sbsoft.client.components;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
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
