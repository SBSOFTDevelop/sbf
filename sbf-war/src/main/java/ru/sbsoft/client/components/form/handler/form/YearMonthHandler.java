package ru.sbsoft.client.components.form.handler.form;

import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.model.YearMonth;

/**
 *
 * @author Kiselev
 * @param <M>
 */
public class YearMonthHandler<M extends IFormModel> extends ru.sbsoft.client.components.form.handler.YearMonthHandler<YearMonthHandler<M>> implements IFormFieldHandler<M> {

    private final ValueProvider<? super M, YearMonth> valueProvider;

    public YearMonthHandler(String label, ValueProvider<? super M, YearMonth> valueProvider) {
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
