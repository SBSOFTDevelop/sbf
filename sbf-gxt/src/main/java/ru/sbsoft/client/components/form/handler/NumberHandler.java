package ru.sbsoft.client.components.form.handler;

import com.google.gwt.i18n.client.NumberFormat;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.validator.MaxNumberValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinNumberValidator;
import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 *
 * @author Kiselev
 * @param <V>
 * @param <F>
 * @param <SelfType>
 */
abstract class NumberHandler<F extends NumberField<V>, V extends Number & Comparable<V>, SelfType extends NumberHandler<F, V, SelfType>> extends ValFieldBaseHandler<F, V, SelfType> {

    public NumberHandler(NamedItem paramItem) {
        this(paramItem.getCode(), I18n.get(paramItem.getItemName()));
    }

    public NumberHandler(String name, String label) {
        super(name, label);
    }

    public SelfType setFormat(NumberFormat f) {
        getField().setFormat(f);
        return (SelfType) this;
    }
    
    public SelfType setFormat(String f) {
        getField().setFormat(NumberFormat.getFormat(f));
        return (SelfType) this;
    }

    public SelfType setRange(V minVal, V maxVal) {
        setMin(minVal);
        return setMax(maxVal);
    }

    public SelfType setMin(V minVal) {
        getField().addValidator(new MinNumberValidator<V>(minVal));
        return (SelfType) this;
    }

    public SelfType setMax(V maxVal) {
        getField().addValidator(new MaxNumberValidator<V>(maxVal));
        return (SelfType) this;
    }
}
