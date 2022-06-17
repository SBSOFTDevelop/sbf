package ru.sbsoft.client.components.form.handler;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.StrStrComboBox;
import ru.sbsoft.client.components.form.model.StrStrComboBoxModel;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.filter.StringFilterInfo;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.param.StringParamInfo;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 *
 * @author Kiselev
 * @param <V>
 * @param <SelfType>
 */
public class EnumHandler<V extends Enum<V> & NamedItem, SelfType extends EnumHandler<V, SelfType>> extends BaseHandler<StrStrComboBox, V, SelfType> {

    private final Map<String, V> valMap = new HashMap<>();

    public EnumHandler(NamedItem it) {
        this(it.getCode(), I18n.get(it.getItemName()));
    }

    public EnumHandler(String name, String label) {
        super(name, label);
    }

    public SelfType setItems(V... items) {
        if (items != null && items.length > 0) {
            return setItems(EnumSet.copyOf(Arrays.asList(items)));
        }
        return (SelfType) this;
    }

    public SelfType clearItems() {
        StrStrComboBox f = getField();
        f.getStore().clear();
        valMap.clear();
        return (SelfType) this;
    }

    public SelfType setItems(Set<V> items) {
        if (items != null && !items.isEmpty()) {
            StrStrComboBox f = getField();
            for (V e : items) {
                String key = e.name();
                valMap.put(key, e);
                f.add(new StrStrComboBoxModel(key, I18n.get(e.getItemName())));
            }
        }
        return (SelfType) this;
    }

    public SelfType addValueChangeHandler(final ValueChangeHandler<V> h) {
        getField().addValueChangeHandler(new ValueChangeHandler<StrStrComboBoxModel>() {
            @Override
            public void onValueChange(ValueChangeEvent<StrStrComboBoxModel> event) {
                h.onValueChange(new ValueChangeEvent<V>(toEnum(event.getValue())) {
                });
            }
        });
        return (SelfType) this;
    }
    
    public SelfType addSelectionHandler(final SelectionHandler<V> h) {
        getField().addSelectionHandler(new SelectionHandler<StrStrComboBoxModel>() {
            @Override
            public void onSelection(SelectionEvent<StrStrComboBoxModel> event) {
                h.onSelection(new SelectionEvent<V>(toEnum(event.getSelectedItem())){
                });
            }
        });
        return (SelfType) this;
    }

    @Override
    protected StrStrComboBox createField() {
        StrStrComboBox box = new StrStrComboBox();
        box.setAutoValidate(true);
        return box;
    }

    @Override
    public SelfType setVal(V val) {
        StrStrComboBoxModel mod = null;
        if (val != null) {
            for (StrStrComboBoxModel mod2 : getField().getStore().getAll()) {
                if (val.name().equals(mod2.getId())) {
                    mod = mod2;
                    break;
                }
            }
        }
        getField().setValue(mod);
        return (SelfType) this;
    }

    @Override
    public V getVal() {
        StrStrComboBoxModel mod = getField().getValue();
        return toEnum(mod);
    }

    private V toEnum(StrStrComboBoxModel mod) {
        return mod != null ? valMap.get(mod.getId()) : null;
    }

    @Override
    protected FilterInfo createFilter() {
        return new StringFilterInfo(null, getVal().name());
    }

    @Override
    protected ParamInfo createParamInfo() {
        return new StringParamInfo(null, getVal().name());
    }
}
