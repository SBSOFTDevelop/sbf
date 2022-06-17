package ru.sbsoft.client.components.form.handler.form;

import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 *
 * @author Kiselev
 * @param <M>
 * @param <V>
 */
public class EnumHandler<M extends IFormModel, V extends Enum<V> & NamedItem> extends ru.sbsoft.client.components.form.handler.EnumHandler<V, EnumHandler<M, V>> implements IFormFieldHandler<M> {

    private final ValueProvider<? super M, V> valueProvider;

    public EnumHandler(String label, ValueProvider<? super M, V> valueProvider) {
        super(null, label);
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
