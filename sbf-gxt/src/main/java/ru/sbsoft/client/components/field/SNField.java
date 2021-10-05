package ru.sbsoft.client.components.field;

import ru.sbsoft.common.validation.NumClassifiers;

/**
 *
 * @author sychugin
 */
public class SNField extends MaskField {

    private static class SN extends NumClassifiers {

        public static final String NULLVAL = "__.___-___";
        public static final String PATTERN = "99.999-999";
        public static final int SIGNIFICANT_LENGTH = 8;

        private static boolean formatted(String value, boolean isFullComplete) {
            return valid(value, NULLVAL, isFullComplete);
        }

        public static String getErrorMesage(String value) {
            if (value == null) {
                return EMPTY;
            }
            if (!formatted(value, true)) {
                return INVALID_FORMAT;
            }

            return null;
        }
    }

    public SNField() {
        super(SN.PATTERN, SN.NULLVAL);
    }

    @Override
    public boolean isFormatted(String value) {
        return SN.formatted(value, false);
    }

    @Override
    public String[] getErrors(String value) {
        final String message = SN.getErrorMesage(value);
        return (message == null) ? null : new String[]{message};
    }

}
