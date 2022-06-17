package ru.sbsoft.client.components.field;

import ru.sbsoft.client.components.AllowBlankControl;
import ru.sbsoft.common.Strings;
import ru.sbsoft.common.validation.SNILS;

/**
 * @author balandin
 * @since Sep 11, 2014 5:44:23 PM
 */
//TODO deprecated
public class SnilsField extends MaskField implements AllowBlankControl {

    public SnilsField() {
        super(SNILS.PATTERN, SNILS.NULLVAL);
    }

    @Override
    public boolean isFormatted(String value) {
        return SNILS.formatted(value, false);
    }

    @Override
    public String[] getErrors(String value) {
        final String message = SNILS.getErrorMesage(value);
        return (message == null) ? null : new String[]{message};
    }

    @Override
    protected String convert(String value) {
        if (value == null) {
            return null;
        }
        String s = format(value);
        if (s != null) {
            return s;
        }
        final int length = value.length();
        if (testNumber(value) && length < SNILS.SIGNIFICANT_LENGTH) {
            value = value.concat(Strings.repl("_", SNILS.SIGNIFICANT_LENGTH - length));
            s = format(value);
            if (s != null) {
                return s;
            }
        }

        return value;
    }

    public String format(String value) {
        if (testNumber(value, SNILS.SIGNIFICANT_LENGTH)) {
            StringBuilder s = new StringBuilder();
            s.append(value, 0, 3).append("-");
            s.append(value,3, 6).append("-");
            s.append(value,6, 9).append(" ");
            s.append(value,9, 11);
            return s.toString();
        }
        return null;
    }

    @Override
    protected String unconvert(String value) {
        if (SNILS.valid(value)) {
            StringBuilder s = new StringBuilder();
            s.append(value,0, 3);
            s.append(value,4, 7);
            s.append(value,8, 11);
            s.append(value,12, 14);
            return s.toString();
        }
        return null;
    }
}
