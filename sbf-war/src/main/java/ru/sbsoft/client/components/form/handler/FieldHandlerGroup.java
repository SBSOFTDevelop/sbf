package ru.sbsoft.client.components.form.handler;

import java.util.function.Consumer;

/**
 *
 * @author vk
 */
public class FieldHandlerGroup {

    private final IFieldHandler[] g;

    public FieldHandlerGroup(IFieldHandler... g) {
        this.g = g;
    }

    public FieldHandlerGroup setReadonly(final boolean b) {
        return handleHandlers(h -> h.setRO(b));
    }

    public FieldHandlerGroup setReq(final boolean b) {
        return handleHandlers(h -> h.setReq(b));
    }

    public FieldHandlerGroup setVisible(final boolean b) {
        return handleHandlers(h -> h.setVisible(b));
    }

    private FieldHandlerGroup handleHandlers(Consumer<IFieldHandler> hh) {
        if (g != null) {
            for (IFieldHandler h : g) {
                hh.accept(h);
            }
        }
        return this;
    }
}
