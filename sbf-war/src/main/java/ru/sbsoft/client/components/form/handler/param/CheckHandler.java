package ru.sbsoft.client.components.form.handler.param;

import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 *
 * @author Kiselev
 */
public class CheckHandler extends ru.sbsoft.client.components.form.handler.CheckHandler<CheckHandler>{

    public CheckHandler(NamedItem param) {
        this(param.getCode(), I18n.get(param.getItemName()));
    }
    
    public CheckHandler(String name, String label) {
        super(name, label);
    }
    
}
