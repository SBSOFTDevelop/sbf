package ru.sbsoft.client.components;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Контейнер для сохранения обработчика события и объекта его регистрации вместе.
 * @author balandin
 * @since Jul 16, 2013 7:38:34 PM
 */
public class HandlerHolder {

    private EventHandler handler;
    private HandlerRegistration registration;

    public HandlerHolder() {
    }

    public EventHandler getHandler() {
        return handler;
    }

    public void removeHandler() {
        if (this.registration != null) {
            this.registration.removeHandler();
        }
        this.handler = null;
        this.registration = null;
    }

    public void hold(EventHandler handler, HandlerRegistration registration) {
        this.handler = handler;
        this.registration = registration;
    }
}
