package ru.sbsoft.client.components.form.handler.form;

import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.sbf.app.model.YearMonthDay;

/**
 *
 * @author Kiselev
 * @param <M>
 */
public class YearMonthDayHandler<M extends IFormModel> extends ru.sbsoft.client.components.form.handler.YearMonthDayHandler<YearMonthDayHandler<M>> implements IFormFieldHandler<M> {

    private final ValueProvider<? super M, YearMonthDay> valueProvider;

    public YearMonthDayHandler(String label, ValueProvider<? super M, YearMonthDay> valueProvider) {
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
