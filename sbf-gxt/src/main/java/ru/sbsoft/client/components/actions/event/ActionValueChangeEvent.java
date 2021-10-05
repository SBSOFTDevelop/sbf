package ru.sbsoft.client.components.actions.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 *
 * @author vlki
 * @param <H>
 * @param <V>
 */
public abstract class ActionValueChangeEvent<H extends ActionValueChangeHandler, V> extends GwtEvent<H> {

    private final Type<H> type;
    private final V value;

    public ActionValueChangeEvent(Type<H> type, V value) {
        this.value = value;
        this.type = type;
    }

    public final V getValue() {
        return value;
    }

    @Override
    public final Type<H> getAssociatedType() {
        return type;
    }

    @Override
    protected final void dispatch(H handler) {
        handler.onValueChanged(this);
    }
}
