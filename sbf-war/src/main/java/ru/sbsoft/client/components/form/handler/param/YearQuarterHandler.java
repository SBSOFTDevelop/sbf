package ru.sbsoft.client.components.form.handler.param;

import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 *
 * @author Kiselev
 */
public class YearQuarterHandler extends ru.sbsoft.client.components.form.handler.YearQuarterHandler<YearQuarterHandler>{

    public YearQuarterHandler(NamedItem param) {
        this(param.getCode(), I18n.get(param.getItemName()));
    }
    
    public YearQuarterHandler(String name, String label) {
        super(name, label);
    }
    
}
