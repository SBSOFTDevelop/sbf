package ru.sbsoft.client.components.actions.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 *
 * @author vlki
 */
public class CheckStateEvent extends GwtEvent<CheckStateHandler> {

    public static final Type<CheckStateHandler> TYPE = new Type<CheckStateHandler>();

    @Override
    public Type<CheckStateHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(CheckStateHandler handler) {
        handler.updateState(this);
    }

}
