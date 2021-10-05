package com.sencha.gxt.cell.core.client.form;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.sencha.gxt.widget.core.client.form.PropertyEditor;
import java.text.ParseException;
import java.util.Date;
import ru.sbsoft.sbf.app.model.YearMonthDay;

/**
 *
 * @author Fedor Resnyanskiy
 */
public class YearMonthDayEditor extends PropertyEditor<YearMonthDay> {

    protected DateTimeFormat format = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT);
    protected boolean parseStrict = true;

    /**
     * Creates a new date time property editor.
     */
    public YearMonthDayEditor() {
    }

    /**
     * Creates a new date time property editor.
     *
     * @param format the date time format
     */
    public YearMonthDayEditor(DateTimeFormat format) {
        this.format = format;
    }

    /**
     * Creates a new date time property editor.
     *
     * @param pattern the pattern used to create a new @link
     * {@link DateTimeFormat}.
     */
    public YearMonthDayEditor(String pattern) {
        this.format = DateTimeFormat.getFormat(pattern);
    }

    /**
     * Returns the date time format.
     *
     * @return the date time format
     */
    public DateTimeFormat getFormat() {
        return format;
    }

    /**
     * Returns true if parsing strictly.
     *
     * @return the parse strict state
     */
    public boolean isParseStrict() {
        return parseStrict;
    }

    @Override
    public YearMonthDay parse(CharSequence text) throws ParseException {
        final Date date;
        final YearMonthDay ymd;
        try {
            if (parseStrict) {
                date = format.parseStrict(text.toString());
            } else {
                date = format.parse(text.toString());
            }

            ymd = GxtYearMonthDayConverter.convert(date);
        } catch (Exception ex) {
            throw new ParseException(ex.getMessage(), -1);
        }

        return ymd;
    }

    @Override
    public String render(YearMonthDay value) {
        return format.format(GxtYearMonthDayConverter.convert(value));
    }

    /**
     * True to parse dates strictly (defaults to true). See @link
     * {@link DateTimeFormat#parseStrict(String)}.
     *
     * @param parseStrict true to parse strictly
     */
    public void setParseStrict(boolean parseStrict) {
        this.parseStrict = parseStrict;
    }

}
