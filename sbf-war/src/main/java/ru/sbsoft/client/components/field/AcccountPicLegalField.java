package ru.sbsoft.client.components.field;

import ru.sbsoft.common.Strings;
import ru.sbsoft.common.validation.AccountPic;

/*
 It seems to be NSI only class.
*/
//TODO deprecated
public class AcccountPicLegalField extends MaskField {

    public AcccountPicLegalField() {
        super(AccountPic.PTRN_20, AccountPic.NULL_20);

        setAutoValidate(true);
        setValidationDelay(100);
    }

    @Override
    public boolean isFormatted(String value) {
        return AccountPic.formatted(value, false);
    }

    @Override
    public String[] getErrors(String value) {
        return AccountPic.getErrorMesages(value);
    }

    @Override
    protected String convert(String value) {
        if (value == null) {
            return null;
        }
        final int l = value.length();
        if (l > 0 && l < AccountPic.LENGTH_20 && testNumber(value)) {
            value = value.concat(Strings.repl("_", AccountPic.LENGTH_20 - l));
        }
        return value;
    }

    @Override
    protected String unconvert(String value) {
        if (AccountPic.formatted(value, true)) {
            return value.replaceAll("\\_", "");
        }
        return null;
    }
}
