package ru.sbsoft.client.components.form.handler;

import com.sencha.gxt.widget.core.client.form.ValueBaseField;

/**
 *
 * @author Kiselev
 * @param <V>
 * @param <F>
 * @param <SelfType>
 */
public abstract class ValFieldBaseHandler<F extends ValueBaseField<V>, V, SelfType extends ValFieldBaseHandler<F, V, SelfType>> extends ChangeAwareHandler<F, V, SelfType> {

    protected ValFieldBaseHandler(String name, String label) {
        super(name, label);
    }

    @Override
    public SelfType setVal(V val) {
        getField().setValue(val, true);
        return (SelfType)this;
    }

    @Override
    public V getVal() {
        return getField().getCurrentValue();
    }
}
