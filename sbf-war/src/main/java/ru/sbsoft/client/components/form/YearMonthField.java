package ru.sbsoft.client.components.form;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.model.IntStrComboBoxModel;
import ru.sbsoft.shared.consts.Month;
import ru.sbsoft.shared.model.YearMonth;

/**
 *
 * @author Kiselev
 */
public class YearMonthField extends AbstractYearMonthField<YearMonth> {

    public YearMonthField() {
        IntStrComboBox month = getMonthField();
        for (Month e : Month.values()) {
            month.add(new IntStrComboBoxModel(e.getNum(), I18n.get(e.getItemName())));
        }
    }

    @Override
    protected YearMonth createNewValue(int year, int month) {
        return new YearMonth(year, month);
    }

}
