package ru.sbsoft.client.components.form.handler;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.IntStrComboBox;
import ru.sbsoft.client.components.form.model.IntStrComboBoxModel;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.filter.IntegerFilterInfo;
import ru.sbsoft.shared.param.IntegerParamInfo;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.interfaces.NamedItem;
import ru.sbsoft.shared.interfaces.NumeratedItem;

/**
 *
 * @author Kiselev
 * @param <SelfType>
 */
public class IntStrComboBoxHandler<SelfType extends IntStrComboBoxHandler<SelfType>> extends BaseHandler<IntStrComboBox, Integer, SelfType> {

    public IntStrComboBoxHandler(NamedItem it) {
        this(it.getCode(), I18n.get(it.getItemName()));
    }

    public IntStrComboBoxHandler(String name, String label) {
        super(name, label);
    }

    public SelfType setItems(IntStrComboBoxModel... items) {
        IntStrComboBox f = getField();
        for (IntStrComboBoxModel e : items) {
            f.add(e);
        }
        return (SelfType) this;
    }

    public <E extends Enum<E> & NumeratedItem> SelfType setItems(E[] items) {
        IntStrComboBox f = getField();
        for (E e : items) {
            f.add(new IntStrComboBoxModel(e.getNum(), I18n.get(e.getItemName())));
        }
        return (SelfType) this;
    }

    public SelfType setEditable(boolean editable) {
        ((IntStrComboBox) getField()).setEditable(editable);
        return (SelfType) this;
    }

    public SelfType addValueChangeHandler(ValueChangeHandler<IntStrComboBoxModel> h) {
        getField().addValueChangeHandler(h);
        return (SelfType) this;
    }

    public SelfType addSelectionHandler(SelectionHandler<IntStrComboBoxModel> h) {
        getField().addSelectionHandler(h);
        return (SelfType) this;
    }

    @Override
    protected IntStrComboBox createField() {
        IntStrComboBox box = new IntStrComboBox();
        box.setAutoValidate(true);
        return box;
    }

    @Override
    public SelfType setVal(Integer val) {
        IntStrComboBoxModel mod = null;
        if (val != null) {
            for (IntStrComboBoxModel mod2 : getField().getStore().getAll()) {
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
    public Integer getVal() {
        IntStrComboBoxModel modVal = getField().getValue();
        return modVal != null ? modVal.getId() : null;
    }

    @Override
    protected FilterInfo createFilter() {
        return new IntegerFilterInfo(null, getVal());
    }

    @Override
    protected ParamInfo createParamInfo() {
        return new IntegerParamInfo(null, getVal());
    }
}
