package ru.sbsoft.client.components.form.handler.form;

import ru.sbsoft.svc.core.client.ValueProvider;
import java.util.Date;
import ru.sbsoft.client.components.field.DateConsts;
import ru.sbsoft.sbf.app.model.IFormModel;

/**
 *
 * @author Kiselev
 * @param <M>
 */
public class DateHandler<M extends IFormModel> extends ru.sbsoft.client.components.form.handler.DateHandler<DateHandler<M>> implements IFormFieldHandler<M> {

    private final ValueProvider<? super M, Date> valueProvider;

    public DateHandler(String label, ValueProvider<? super M, Date> valueProvider) {
        this(label, valueProvider, (String) null);
    }

    public DateHandler(String label, ValueProvider<? super M, Date> valueProvider, String nullText) {
        this(label, null, valueProvider, nullText);
    }

    public DateHandler(String label, DateConsts format, ValueProvider<? super M, Date> valueProvider) {
        this(label, format, valueProvider, null);
    }

    public DateHandler(String label, DateConsts format, ValueProvider<? super M, Date> valueProvider, String nullText) {
        super(null, label, format, nullText);
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
