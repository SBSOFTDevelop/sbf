package ru.sbsoft.client.components.actions.event;

import com.google.gwt.event.shared.EventHandler;

/**
 *
 * @author vlki
 * @param <E>
 */
public interface ActionValueChangeHandler<E extends ActionValueChangeEvent> extends EventHandler {

    void onValueChanged(E event);
}
