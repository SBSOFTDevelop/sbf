package ru.sbsoft.client.components.field;

import ru.sbsoft.common.Strings;
import ru.sbsoft.common.validation.OGRN;

//TODO deprecated
public class OGRNLegalField extends MaskField {

    public OGRNLegalField() {
        super(OGRN.PTRN_13, OGRN.NULL_13);

        setAutoValidate(true);
        setValidationDelay(100);
    }

    @Override
    public boolean isFormatted(String value) {
        return OGRN.formatted(value, false);
    }

    @Override
    public String[] getErrors(String value) {
        return OGRN.getErrorMesages(value);
    }

    @Override
    protected String convert(String value) {
        if (value == null) {
            return null;
        }
        final int l = value.length();
        if (l > 0 && l < OGRN.LENGTH_13 && testNumber(value)) {
            value = value.concat(Strings.repl("_", OGRN.LENGTH_13 - l));
        }
        return value;
    }

    @Override
    protected String unconvert(String value) {
        if (OGRN.formatted(value, true)) {
            return value.replaceAll("\\_", "");
        }
        return null;
    }
}
