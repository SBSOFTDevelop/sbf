package ru.sbsoft.client.components.actions.event;

/**
 *
 * @author vlki
 */
public class EnabledChangeEvent extends ActionValueChangeEvent<EnabledChangeHandler, Boolean> {

    public static final Type<EnabledChangeHandler> TYPE = new Type<EnabledChangeHandler>();

    public EnabledChangeEvent(Boolean enabled) {
        super(TYPE, enabled == null ? Boolean.FALSE : enabled);
    }
}
