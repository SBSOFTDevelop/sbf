package ru.sbsoft.client.components.actions.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

/**
 *
 * @author vk
 */
public interface HasChangedStateHandlers extends HasHandlers {

    HandlerRegistration addChangedStateHandler(ChangedStateHandler handler);
}
