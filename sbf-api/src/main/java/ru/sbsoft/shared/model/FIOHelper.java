package ru.sbsoft.shared.model;

import ru.sbsoft.shared.SbStringBuilder;

public class FIOHelper {

    public static String getFullName(final String surName, final String firstName, final String middleName) {
        final SbStringBuilder sb = new SbStringBuilder();
        sb.setSeparator(" ");
        sb.joinNotEmptyValue(surName);
        sb.joinNotEmptyValue(firstName);
        sb.joinNotEmptyValue(middleName);
        return sb.toString();
    }
}
