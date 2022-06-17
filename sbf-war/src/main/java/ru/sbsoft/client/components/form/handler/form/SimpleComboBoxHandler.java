package ru.sbsoft.client.components.form.handler.form;

import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.svc.data.shared.LabelProvider;
import ru.sbsoft.sbf.app.model.IFormModel;

/**
 *
 * @author Kiselev
 * @param <M>
 * @param <V>
 */
public class SimpleComboBoxHandler<M extends IFormModel, V> extends ru.sbsoft.client.components.form.handler.SimpleComboBoxHandler<V, SimpleComboBoxHandler<M, V>> implements IFormFieldHandler<M> {

    private final ValueProvider<? super M, V> valueProvider;

    public SimpleComboBoxHandler(String label, ValueProvider<? super M, V> valueProvider, LabelProvider<? super V> valueLabelProvider) {
        super(null, label, valueLabelProvider);
        this.valueProvider = valueProvider;
    }

    @Override
    public void fromModel(M model) {
        if (valueProvider != null) {
            setVal(valueProvider.getValue(model));
        }
    }

    @Override
    public void toModel(M model) {
        if (valueProvider != null) {
            valueProvider.setValue(model, getVal());
        }
    }
}
