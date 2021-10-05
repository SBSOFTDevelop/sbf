package ru.sbsoft.shared.model.provider;

import ru.sbsoft.sbf.app.model.FormModel;
import ru.sbsoft.shared.model.enums.YesNoEnum;

/**
 * Провайдер для логических полей. Позволяет использовать модели с разными типами (String, Integer,
 * Boolean).
 *
 * @author rfa
 * @param <VALUE> тип данных, передаваемых в модели
 * @param <MODEL> тип модели
 */
public abstract class AbstractCheckboxValueProvider<VALUE, MODEL extends FormModel> extends AbstractFormModelValueProvider<YesNoEnum, MODEL> {

    @Override
    public YesNoEnum getValue(MODEL model) {
        VALUE value = getCheckboxValue(model);
        if (value instanceof Boolean) {
            return YesNoEnum.forBoolean((Boolean) value);
        }
        if (value instanceof Integer) {
            return YesNoEnum.forBoolean((Integer) value == 1);
        }
        if (value instanceof String) {
            return YesNoEnum.forBoolean(Boolean.parseBoolean((String) value));
        }
        return null;
    }

    @Override
    public void setValue(YesNoEnum value, MODEL model) {
        VALUE v = getCheckboxValue(model);
        if (v instanceof Boolean) {
            setCheckboxValue((VALUE) new Boolean(value == YesNoEnum.YES), model);
        }
        if (v instanceof Integer) {
            setCheckboxValue((VALUE) (value == YesNoEnum.YES ? new Integer(1) : new Integer(0)), model);
        }
        if (v instanceof String) {
            setCheckboxValue((VALUE) value.getCode(), model);
        }
    }

    public abstract VALUE getCheckboxValue(MODEL model);

    public abstract void setCheckboxValue(VALUE value, MODEL model);

}
