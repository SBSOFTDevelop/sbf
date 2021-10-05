package ru.sbsoft.sbf.app.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import ru.sbsoft.sbf.app.Registration;
import ru.sbsoft.sbf.app.utils.RegistrationUtils;
import static ru.sbsoft.sbf.app.utils.CollectionUtils.wrapList;

/**
 *
 * @author Fedor Resnyanskiy
 * @param <H>
 * @param <EVENT>
 */
abstract class BaseHandlerHolder<H extends IDummyHandler<EVENT>, EVENT extends IEvent> {

    private List<H> handlers = null;

    public boolean isInit() {
        return handlers != null;
    }

    public Collection<H> getHandlers() {
        if (handlers == null) {
            return Collections.emptyList();
        }
        return wrapList(handlers);
    }

    public Registration addHandler(H handler) {
        if (handlers == null) {
            handlers = new ArrayList<>();
        }
        return RegistrationUtils.register(handlers, handler);
    }
}
