package ru.sbsoft.client.components.form.handler.param;

import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 *
 * @author Kiselev
 */
public class YearMonthHandler extends ru.sbsoft.client.components.form.handler.YearMonthHandler<YearMonthHandler>{

    public YearMonthHandler(NamedItem param) {
        this(param.getCode(), I18n.get(param.getItemName()));
    }
    
    public YearMonthHandler(String name, String label) {
        super(name, label);
    }
    
}
