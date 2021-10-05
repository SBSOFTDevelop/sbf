package ru.sbsoft.client.components.form.handler.param;

import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 *
 * @author Kiselev
 */
public class LongHandler extends ru.sbsoft.client.components.form.handler.LongHandler<LongHandler>{

    public LongHandler(NamedItem param) {
        this(param.getCode(), I18n.get(param.getItemName()));
    }
    
    public LongHandler(String name, String label) {
        super(name, label);
    }
    
}
