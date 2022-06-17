package ru.sbsoft.client.components.actions.event;

import com.google.gwt.event.shared.EventHandler;

/**
 *
 * @author vlki
 */
public interface ChangedStateHandler extends EventHandler {

    void updateState(ChangedStateEvent ev);
}
