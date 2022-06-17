package ru.sbsoft.client.components.form.handler.form;

import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.shared.kladr.AddressModel;
import ru.sbsoft.sbf.app.model.IFormModel;

/**
 *
 * @author Kiselev
 * @param <M>
 */
public class AddressHandler<M extends IFormModel> extends ru.sbsoft.client.components.form.handler.AddressHandler<AddressHandler<M>> implements IFormFieldHandler<M> {

    private final ValueProvider<? super M, AddressModel> valueProvider;

    public AddressHandler(String title, ValueProvider<? super M, AddressModel> valueProvider) {
        super(null, title);
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
