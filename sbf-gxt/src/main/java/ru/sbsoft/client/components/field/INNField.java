package ru.sbsoft.client.components.field;

import ru.sbsoft.common.Strings;
import ru.sbsoft.common.validation.INN;

/**
 * Поле для ввода ИНН.
 * @see ru.sbsoft.common.validation.INN
 * @author balandin
 * @since Jan 30, 2015 5:21:28 PM
 */
//TODO deprecated
public class INNField extends MaskField {

    public INNField() {
        super(INN.PTRN_12, INN.NULL_12);

        setAutoValidate(true);
        setValidationDelay(100);
    }

    @Override
    public boolean isFormatted(String value) {
        return INN.formatted(value, false);
    }

    @Override
    public String[] getErrors(String value) {
        return INN.getErrorMesages(value);
    }

    @Override
    protected String convert(String value) {
        if (value == null) {
            return null;
        }
        final int l = value.length();
        if (l > 0 && l < INN.LENGTH_12 && testNumber(value)) {
            value = value.concat(Strings.repl("_", INN.LENGTH_12 - l));
        }
        return value;
    }

    @Override
    protected String unconvert(String value) {
        if (INN.formatted(value, true)) {
            return value.replaceAll("\\_", "");
        }
        return null;
    }
}
