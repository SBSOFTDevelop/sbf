package ru.sbsoft.sbf.app.form;

/**
 *
 * @author Fedor Resnyanskiy
 * @param <EVENT>
 */
public class DefaultHandlerHolder<EVENT extends IEvent> extends BaseHandlerHolder<IHandler<EVENT>, EVENT> {

    public void onHandler(EVENT e) {
        getHandlers().forEach(h -> onHandle(h, e));
    }
    
    protected void onHandle(IHandler<EVENT> h, EVENT e){
        h.onHandle(e);
    }
}
