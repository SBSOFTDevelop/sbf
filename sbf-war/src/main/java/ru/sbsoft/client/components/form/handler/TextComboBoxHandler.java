package ru.sbsoft.client.components.form.handler;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import ru.sbsoft.svc.cell.core.client.form.ComboBoxCell;
import ru.sbsoft.svc.data.shared.ListStore;
import ru.sbsoft.svc.widget.core.client.form.SimpleComboBox;
import ru.sbsoft.svc.widget.core.client.form.validator.MaxLengthValidator;
import ru.sbsoft.svc.widget.core.client.form.validator.MinLengthValidator;
import java.util.Arrays;
import java.util.Collection;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.SimpleLabelProvider;
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
public class TextComboBoxHandler<SelfType extends TextComboBoxHandler<SelfType>> extends BaseHandler<SimpleComboBox<String>, String, SelfType> {

    public TextComboBoxHandler(NamedItem it) {
        this(it.getCode(), I18n.get(it.getItemName()));
    }

    public TextComboBoxHandler(String name, String label) {
        super(name, label);
    }

    public SelfType cleanItems() {
        SimpleComboBox f = getField();
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
            SimpleComboBox f = getField();
            for (String s : items) {
                f.add(s);
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
            SimpleComboBox f = getField();
            for (E e : items) {
                f.add(I18n.get(e.getItemName()));
            }
        }
        return (SelfType) this;
    }

    public SelfType addValueChangeHandler(ValueChangeHandler<String> h) {
        getField().addValueChangeHandler(h);
        return (SelfType) this;
    }

    public SelfType addSelectionHandler(SelectionHandler<String> h) {
        getField().addSelectionHandler(h);
        return (SelfType) this;
    }

    @Override
    protected SimpleComboBox<String> createField() {
        SimpleComboBox<String> box = new SimpleComboBox<>(new NoRestrictStringComboBoxCell());
        box.setForceSelection(false);
        box.setEditable(true);
        box.setTypeAhead(false);
        box.setAutoValidate(true);
        return box;
    }

    @Override
    public SelfType setVal(String val) {
        getField().setValue(val);
        return (SelfType) this;
    }

    @Override
    public String getVal() {
        return getField().getValue();
    }

    @Override
    protected FilterInfo createFilter() {
        return new StringFilterInfo(null, getVal());
    }

    @Override
    protected ParamInfo createParamInfo() {
        return new StringParamInfo(null, getVal());
    }
    
    public SelfType setMaxLen(int len) {
        getField().addValidator(new MaxLengthValidator(len));
        return (SelfType) this;
    }
    
    public SelfType setMinLen(int len) {
        getField().addValidator(new MinLengthValidator(len));
        return (SelfType) this;
    }
    
    public SelfType setLenRange(int minLen, int maxLen) {
        setMinLen(minLen);
        return setMaxLen(maxLen);
    }

    public SelfType setFixLen(int len) {
        return setLenRange(len, len);
    }

    private static class NoRestrictStringComboBoxCell extends ComboBoxCell<String> {

        public NoRestrictStringComboBoxCell() {
            super(new ListStore<String>((String item) -> item), new SimpleLabelProvider<String>());
        }

        @Override
        protected String selectByValue(String value) {
            return value;
        }
    }
}
