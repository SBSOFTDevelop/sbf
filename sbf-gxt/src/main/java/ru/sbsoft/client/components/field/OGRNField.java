package ru.sbsoft.client.components.field;

import ru.sbsoft.common.Strings;
import ru.sbsoft.common.validation.OGRN;

/**
 * @author balandin
 * @since Feb 2, 2015 4:46:58 PM
 */
//TODO deprecated
public class OGRNField extends MaskField {

    public OGRNField() {
        super(OGRN.PTRN_15, OGRN.NULL_15);

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
        if (l > 0 && l < OGRN.LENGTH_15 && testNumber(value)) {
            value = value.concat(Strings.repl("_", OGRN.LENGTH_15 - l));
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
