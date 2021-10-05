package ru.sbsoft.client.components.form.handler;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import java.util.Arrays;
import java.util.Collection;
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
 * @param <SelfType>
 */
public class StrStrComboBoxHandler<SelfType extends StrStrComboBoxHandler<SelfType>> extends BaseHandler<StrStrComboBox, String, SelfType> {

    public StrStrComboBoxHandler(NamedItem it) {
        this(it.getCode(), I18n.get(it.getItemName()));
    }

    public StrStrComboBoxHandler(String name, String label) {
        super(name, label);
    }

    public SelfType cleanItems() {
        StrStrComboBox f = getField();
        f.clear();
        f.getStore().clear();
        return (SelfType) this;
    }

    public SelfType setItems(String... items) {
        if (items != null && items.length > 0) {
            return setItems(Arrays.asList(items));
        }
        return (SelfType) this;
    }

    public SelfType setItems(Collection<String> items) {
        if (items != null && !items.isEmpty()) {
            StrStrComboBox f = getField();
            for (String s : items) {
                f.add(new StrStrComboBoxModel(s));
            }
        }
        return (SelfType) this;
    }

    public SelfType setItems(StrStrComboBoxModel... items) {
        if (items != null && items.length > 0) {
            StrStrComboBox f = getField();
            for (StrStrComboBoxModel e : items) {
                f.add(e);
            }
        }
        return (SelfType) this;
    }

    public <E extends NamedItem> SelfType setNamedItems(Collection<E> items) {
        if (items != null && !items.isEmpty()) {
            for (E it : items) {
                setItems(it);
            }
        }
        return (SelfType) this;
    }

    public <E extends NamedItem> SelfType setItems(E... items) {
        if (items != null && items.length > 0) {
            StrStrComboBox f = getField();
            for (E e : items) {
                f.add(new StrStrComboBoxModel(e.getCode(), I18n.get(e.getItemName())));
            }
        }
        return (SelfType) this;
    }

    public SelfType addValueChangeHandler(ValueChangeHandler<StrStrComboBoxModel> h) {
        getField().addValueChangeHandler(h);
        return (SelfType) this;
    }

    public SelfType addSelectionHandler(SelectionHandler<StrStrComboBoxModel> h) {
        getField().addSelectionHandler(h);
        return (SelfType) this;
    }

    @Override
    protected StrStrComboBox createField() {
        StrStrComboBox box = new StrStrComboBox();
        box.setAutoValidate(true);
        return box;
    }

    @Override
    public SelfType setVal(String val) {
        StrStrComboBoxModel mod = null;
        if (val != null) {
            for (StrStrComboBoxModel mod2 : getField().getStore().getAll()) {
                if (val.equals(mod2.getId())) {
                    mod = mod2;
                    break;
                }
            }
        }
        getField().setValue(mod);
        return (SelfType) this;
    }

    @Override
    public String getVal() {
        StrStrComboBoxModel mod = getField().getValue();
        return mod != null ? mod.getId() : null;
    }

    @Override
    protected FilterInfo createFilter() {
        return new StringFilterInfo(null, getVal());
    }

    @Override
    protected ParamInfo createParamInfo() {
        return new StringParamInfo(null, getVal());
    }
}
