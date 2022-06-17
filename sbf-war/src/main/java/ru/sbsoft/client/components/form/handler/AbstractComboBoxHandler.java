package ru.sbsoft.client.components.form.handler;

import com.google.gwt.event.logical.shared.SelectionHandler;
import ru.sbsoft.svc.data.shared.LabelProvider;
import ru.sbsoft.svc.widget.core.client.form.ComboBox;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 *
 * @author Kiselev
 * @param <V>
 * @param <SelfType>
 */
abstract class AbstractComboBoxHandler<CB extends ComboBox<V>, V, SelfType extends AbstractComboBoxHandler<CB, V, SelfType>> extends ValFieldBaseHandler<CB, V, SelfType> {

    protected final LabelProvider<? super V> labelProvider;

    public AbstractComboBoxHandler(NamedItem it, LabelProvider<? super V> labelProvider) {
        this(it.getCode(), I18n.get(it.getItemName()), labelProvider);
    }

    public AbstractComboBoxHandler(String name, String label, LabelProvider<? super V> labelProvider) {
        super(name, label);
        this.labelProvider = labelProvider;
    }

    public SelfType addItems(List<V> items) {
        if(items != null){
            getField().getStore().addAll(items);
        }
        return (SelfType) this;
    }
    
    public SelfType setItems(List<V> items) {
        getField().getStore().clear();
        return addItems(items);
    }

    public SelfType setEditable(boolean editable) {
        getField().setEditable(editable);
        return (SelfType) this;
    }

    public SelfType addSelectionHandler(SelectionHandler<V> h) {
        getField().addSelectionHandler(h);
        return (SelfType) this;
    }
}
