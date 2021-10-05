package ru.sbsoft.shared;

import ru.sbsoft.common.Strings;

/**
 * Склеивает строки вставляя заданный разделитель.
 *
 * @author panarin
 */
public class SbStringBuilder {

    private final StringBuilder sb;
    private String separator;

    public SbStringBuilder() {
        this.sb = new StringBuilder();
    }

    public SbStringBuilder(final String initValue) {
        if (null == initValue) {
            this.sb = new StringBuilder();
        } else {
            this.sb = new StringBuilder(initValue);
        }
    }

    public String getSeparator() {
        if (null == separator) {
            separator = ", ";
        }
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public void joinNotEmptyValue(final String prefix, final String value) {
        if (Strings.isEmpty(value)) {
            return;
        }
        if (0 < sb.length()) {
            sb.append(getSeparator());
        }
        sb.append(prefix).append(value);
    }

    public void joinNotEmptyValue(final String value) {
        joinNotEmptyValue("", value);
    }

    @Override
    public String toString() {
        return sb.toString();
    }

    public int length() {
        return sb.length();
    }
}
