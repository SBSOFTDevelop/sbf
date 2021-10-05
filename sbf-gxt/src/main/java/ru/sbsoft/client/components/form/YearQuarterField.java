package ru.sbsoft.client.components.form;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.model.IntStrComboBoxModel;
import ru.sbsoft.shared.consts.Month;
import ru.sbsoft.shared.consts.Quarter;
import ru.sbsoft.shared.model.YearQuarter;

/**
 *
 * @author Kiselev
 */
public class YearQuarterField extends AbstractYearMonthField<YearQuarter> {

    public YearQuarterField() {
        IntStrComboBox month = getMonthField();
        for (Quarter e : Quarter.values()) {
            month.add(new IntStrComboBoxModel(e.getMonthNum(), I18n.get(e.getItemName())));
        }
    }

    @Override
    protected YearQuarter createNewValue(int year, int month) {
        return new YearQuarter(year, Month.getQuarterNum(month));
    }

}
