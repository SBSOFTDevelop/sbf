package ru.sbsoft.client.components.field;

import ru.sbsoft.common.Strings;
import ru.sbsoft.common.validation.KPP;

//TODO deprecated
public class KPPLegalField extends MaskField {

    public KPPLegalField() {
        super(KPP.PTRN_9, KPP.NULL_9);

        setAutoValidate(true);
        setValidationDelay(100);
    }

    @Override
    public boolean isFormatted(String value) {
        return KPP.formatted(value, false);
    }

    @Override
    public String[] getErrors(String value) {
        return KPP.getErrorMesages(value);
    }

    @Override
    protected String convert(String value) {
        if (value == null) {
            return null;
        }
        final int l = value.length();
        if (l > 0 && l < KPP.LENGTH_9 && testNumber(value)) {
            value = value.concat(Strings.repl("_", KPP.LENGTH_9 - l));
        }
        return value;
    }

    @Override
    protected String unconvert(String value) {
        if (KPP.formatted(value, true)) {
            return value.replaceAll("\\_", "");
        }
        return null;
    }
}
