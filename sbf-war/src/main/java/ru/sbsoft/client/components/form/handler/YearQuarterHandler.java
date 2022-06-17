package ru.sbsoft.client.components.form.handler;

import java.util.Date;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.YearQuarterField;
import ru.sbsoft.client.utils.CliUtil;
import ru.sbsoft.shared.interfaces.NamedItem;
import ru.sbsoft.shared.model.YearQuarter;

/**
 *
 * @author Kiselev
 * @param <SelfType>
 */
public class YearQuarterHandler<SelfType extends YearQuarterHandler<SelfType>> extends AbstractYearMonthHandler<YearQuarter, YearQuarterField, SelfType> {

    public YearQuarterHandler(NamedItem paramItem) {
        this(paramItem.getCode(), I18n.get(paramItem.getItemName()));
    }

    public YearQuarterHandler(String name, String label) {
        super(name, label);
    }

    @Override
    protected YearQuarterField createField() {
        return new YearQuarterField();
    }

    @Override
    protected Date toDate(YearQuarter v) {
        return CliUtil.toDate(v.toMonth());
    }

    @Override
    protected YearQuarter fromDate(Date d) {
        return CliUtil.getYearMonth(d).toQuarter();
    }
}
