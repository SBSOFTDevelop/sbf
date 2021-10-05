package ru.sbsoft.client.components.field;

import ru.sbsoft.common.Strings;
import ru.sbsoft.common.validation.OKPO;

/**
 *
 * @author frolikov
 */
//TODO deprecated
public class OKPOLegalField extends MaskField {

    public OKPOLegalField() {
        super(OKPO.PTRN_10, OKPO.NULL_10);
        
        setAutoValidate(true);
        setValidationDelay(100);
    }

    @Override
    public boolean isFormatted(String value) {
          return OKPO.formatted(value, false);
    }

    @Override
    public String[] getErrors(String value) {
        return OKPO.getErrorMesages(value);
    }
 
    @Override
    protected String convert(String value) {
        if (value == null) {
            return null;
        }
        final int l = value.length();
        if (l > 0 && l < OKPO.LENGTH_10 && testNumber(value)) {
            value = value.concat(Strings.repl("_", OKPO.LENGTH_10 - l));
        }
        return value;
    }
    
}
