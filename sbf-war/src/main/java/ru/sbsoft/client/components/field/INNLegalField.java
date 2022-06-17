package ru.sbsoft.client.components.field;

import ru.sbsoft.common.Strings;
import ru.sbsoft.common.validation.INN;

//TODO deprecated
public class INNLegalField extends MaskField {

    public INNLegalField() {
        super(INN.PTRN_10, INN.NULL_10);

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
        if (l > 0 && l < INN.LENGTH_10 && testNumber(value)) {
            value = value.concat(Strings.repl("_", INN.LENGTH_10 - l));
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
