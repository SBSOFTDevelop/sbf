package ru.sbsoft.client.components.field;

import ru.sbsoft.common.Strings;
import ru.sbsoft.common.validation.Account;

/**
 It seems to be NSI only class.
*/
//TODO deprecated
public class AccountField extends MaskField {

    public AccountField() {
        super(Account.PTRN_20, Account.NULL_20);

        setAutoValidate(true);
        setValidationDelay(100);
    }

    @Override
    public boolean isFormatted(String value) {
        return Account.formatted(value, false);
    }

    @Override
    public String[] getErrors(String value) {
        return Account.getErrorMesages(value);
    }

    @Override
    protected String convert(String value) {
        if (value == null) {
            return null;
        }
        final int l = value.length();
        if (l > 0 && l < Account.LENGTH_20 && testNumber(value)) {
            value = value.concat(Strings.repl("_", Account.LENGTH_20 - l));
        }
        return value;
    }

    @Override
    protected String unconvert(String value) {
        if (Account.formatted(value, true)) {
            return value.replaceAll("\\_", "");
        }
        return null;
    }
}
