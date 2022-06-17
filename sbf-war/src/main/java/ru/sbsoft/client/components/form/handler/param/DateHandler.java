package ru.sbsoft.client.components.form.handler.param;

import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 *
 * @author Kiselev
 */
public class DateHandler extends ru.sbsoft.client.components.form.handler.DateHandler<DateHandler> {

    public DateHandler(NamedItem param) {
        this(param.getCode(), I18n.get(param.getItemName()));
    }

    public DateHandler(String name, String label) {
        this(name, label, null);
    }

    public DateHandler(NamedItem param, String nullText) {
        this(param.getCode(), I18n.get(param.getItemName()), nullText);
    }

    public DateHandler(String name, String label, String nullText) {
        super(name, label, nullText);
    }

}
