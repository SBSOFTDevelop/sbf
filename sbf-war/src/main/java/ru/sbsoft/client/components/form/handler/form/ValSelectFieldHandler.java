package ru.sbsoft.client.components.form.handler.form;

import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.client.components.form.handler.form.IFormFieldHandler;
import ru.sbsoft.sbf.app.model.FormModel;
import ru.sbsoft.client.components.IValSelectHandler;

/**
 *
 * @author sychugin
 */
    public class ValSelectFieldHandler<V, M extends FormModel> extends ru.sbsoft.client.components.form.handler.ValSelectFieldHandler<V, ValSelectFieldHandler<V, M>> implements IFormFieldHandler<M> {

        private final ValueProvider<? super M, V> valueProvider;

        public ValSelectFieldHandler(String label, ValueProvider<? super M, V> valueProvider, IValSelectHandler<V> h) {
            super(label, h);
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
