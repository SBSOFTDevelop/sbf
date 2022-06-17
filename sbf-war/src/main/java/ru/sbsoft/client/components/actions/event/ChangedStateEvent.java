package ru.sbsoft.client.components.actions.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 *
 * @author vlki
 */
public class ChangedStateEvent extends GwtEvent<ChangedStateHandler> {

    public static final Type<ChangedStateHandler> TYPE = new Type<ChangedStateHandler>();

    public static Type<ChangedStateHandler> getType() {
        return TYPE;
    }

    @Override
    public Type<ChangedStateHandler> getAssociatedType() {
        return TYPE;
    }

    private final boolean changed;

    public ChangedStateEvent(boolean changed) {
        this.changed = changed;
    }

    public boolean isChanged() {
        return changed;
    }

    @Override
    protected void dispatch(ChangedStateHandler handler) {
        handler.updateState(this);
    }
}
