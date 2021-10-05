package ru.sbsoft.client.components.form.handler;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.sencha.gxt.widget.core.client.Component;

/**
 *
 * @author Kiselev
 * @param <V>
 * @param <F>
 */
abstract class ChangeAwareHandler<F extends Component & HasValueChangeHandlers<V>, V, SelfType extends ChangeAwareHandler<F, V, SelfType>> extends BaseHandler<F, V, SelfType> {

    protected ChangeAwareHandler(String name, String label) {
        super(name, label);
    }

    public SelfType addValueChangeHandler(ValueChangeHandler<V> h) {
        getField().addValueChangeHandler(h);
        return (SelfType) this;
    }
}
