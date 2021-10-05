package ru.sbsoft.client.components.form.handler.form;

import com.sencha.gxt.core.client.ValueProvider;
import java.math.BigDecimal;
import ru.sbsoft.sbf.app.model.IFormModel;

/**
 *
 * @author Kiselev
 * @param <M>
 */
public class BigDecimalHandler<M extends IFormModel> extends ru.sbsoft.client.components.form.handler.BigDecimalHandler<BigDecimalHandler<M>> implements IFormFieldHandler<M> {

    private final ValueProvider<? super M, BigDecimal> valueProvider;

    public BigDecimalHandler(String label, ValueProvider<? super M, BigDecimal> valueProvider) {
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
