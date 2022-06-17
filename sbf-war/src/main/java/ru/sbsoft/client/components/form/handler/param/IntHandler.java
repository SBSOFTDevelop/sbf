package ru.sbsoft.client.components.form.handler.param;

import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 *
 * @author Kiselev
 */
public class IntHandler extends ru.sbsoft.client.components.form.handler.IntHandler<IntHandler>{

    public IntHandler(NamedItem param) {
        this(param.getCode(), I18n.get(param.getItemName()));
    }
    
    public IntHandler(String name, String label) {
        super(name, label);
    }
    
}
