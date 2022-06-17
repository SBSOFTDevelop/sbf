package ru.sbsoft.client.components.actions.event;

import com.google.gwt.event.shared.EventHandler;

/**
 *
 * @author vlki
 */
public interface CheckStateHandler extends EventHandler {

    void updateState(CheckStateEvent ev);
}
