package ru.sbsoft.client.components.form.handler.param;

import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 *
 * @author Kiselev
 */
public class TextHandler extends ru.sbsoft.client.components.form.handler.TextHandler<TextHandler>{

    public TextHandler(NamedItem param) {
        this(param.getCode(), I18n.get(param.getItemName()));
    }
    
    public TextHandler(String name, String label) {
        super(name, label);
    }
    
}
