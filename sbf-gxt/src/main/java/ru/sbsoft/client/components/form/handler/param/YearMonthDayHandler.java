package ru.sbsoft.client.components.form.handler.param;

import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 *
 * @author sokolov
 */
public class YearMonthDayHandler extends ru.sbsoft.client.components.form.handler.YearMonthDayHandler<YearMonthDayHandler> {

    public YearMonthDayHandler(NamedItem param) {
        this(param.getCode(), I18n.get(param.getItemName()));
    }

    public YearMonthDayHandler(String name, String label) {
        super(name, label);
    }

}
