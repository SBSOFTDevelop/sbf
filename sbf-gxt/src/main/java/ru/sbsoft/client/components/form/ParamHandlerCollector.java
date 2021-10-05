package ru.sbsoft.client.components.form;

import java.util.List;
import ru.sbsoft.client.components.form.handler.IFieldHandler;

/**
 *
 * @author Kiselev
 */
public class ParamHandlerCollector {

    private final List<IFieldHandler<?, ?, ?>> handlers;
    private Boolean defaultReq = Boolean.TRUE;

    public ParamHandlerCollector(List<IFieldHandler<?, ?, ?>> handlers) {
        this.handlers = handlers;
    }

    public void setDefaultReq(Boolean defaultReq) {
        this.defaultReq = defaultReq;
    }

    public IFieldHandler<?, ?, ?> add(IFieldHandler<?, ?, ?> h) {
        if(defaultReq != null){
            h.setReq(defaultReq);
        }
        handlers.add(h);
        return h;
    }
    
    public IFieldHandler<?, ?, ?> get(int i){
        return handlers.get(i);
    }
}
