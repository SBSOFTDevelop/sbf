package ru.sbsoft.shared;

/**
 *
 * @author vk
 */
public enum WeekDay {
    /**
     * The singleton instance for the day-of-week of Monday.
     * This has the numeric value of {@code 1}.
     */
    MONDAY,
    /**
     * The singleton instance for the day-of-week of Tuesday.
     * This has the numeric value of {@code 2}.
     */
    TUESDAY,
    /**
     * The singleton instance for the day-of-week of Wednesday.
     * This has the numeric value of {@code 3}.
     */
    WEDNESDAY,
    /**
     * The singleton instance for the day-of-week of Thursday.
     * This has the numeric value of {@code 4}.
     */
    THURSDAY,
    /**
     * The singleton instance for the day-of-week of Friday.
     * This has the numeric value of {@code 5}.
     */
    FRIDAY,
    /**
     * The singleton instance for the day-of-week of Saturday.
     * This has the numeric value of {@code 6}.
     */
    SATURDAY,
    /**
     * The singleton instance for the day-of-week of Sunday.
     * This has the numeric value of {@code 7}.
     */
    SUNDAY;
    /**
     * Private cache of all the constants.
     */
    private static final WeekDay[] ENUMS = WeekDay.values();

    //-----------------------------------------------------------------------
    /**
     * Obtains an instance of {@code WeekDay} from an {@code int} value.
     * <p>
     * {@code WeekDay} is an enum representing the 7 days of the week.
     * This factory allows the enum to be obtained from the {@code int} value.
     * The {@code int} value follows the ISO-8601 standard, from 1 (Monday) to 7 (Sunday).
     *
     * @param weekday  the day-of-week to represent, from 1 (Monday) to 7 (Sunday)
     * @return the day-of-week singleton, not null
     * @throws IllegalArgumentException if the day-of-week is invalid
     */
    public static WeekDay of(int weekday) {
        if (weekday < 1 || weekday > 7) {
            throw new IllegalArgumentException("Invalid value for WeekDay: " + weekday);
        }
        return ENUMS[weekday - 1];
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the day-of-week {@code int} value.
     * <p>
     * The values are numbered following the ISO-8601 standard, from 1 (Monday) to 7 (Sunday).
     * See {@link java.time.temporal.WeekFields#dayOfWeek()} for localized week-numbering.
     *
     * @return the day-of-week, from 1 (Monday) to 7 (Sunday)
     */
    public int getValue() {
        return ordinal() + 1;
    }
}
