package ru.sbsoft.client.components.form.handler;

import java.util.Date;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.YearMonthField;
import ru.sbsoft.client.utils.CliUtil;
import ru.sbsoft.shared.interfaces.NamedItem;
import ru.sbsoft.shared.model.YearMonth;

/**
 *
 * @author Kiselev
 * @param <SelfType>
 */
public class YearMonthHandler<SelfType extends YearMonthHandler<SelfType>> extends AbstractYearMonthHandler<YearMonth, YearMonthField, SelfType> {

    public YearMonthHandler(NamedItem paramItem) {
        this(paramItem.getCode(), I18n.get(paramItem.getItemName()));
    }

    public YearMonthHandler(String name, String label) {
        super(name, label);
    }

    @Override
    protected YearMonthField createField() {
        return new YearMonthField();
    }

    @Override
    protected Date toDate(YearMonth v) {
        return CliUtil.toDate(v);
    }

    @Override
    protected YearMonth fromDate(Date d) {
        return CliUtil.getYearMonth(d);
    }
}
