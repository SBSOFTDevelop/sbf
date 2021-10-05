package com.sencha.gxt.cell.core.client.form;

import com.google.gwt.core.client.JsDate;
import java.util.Date;
import ru.sbsoft.sbf.app.model.YearMonthDay;

/**
 *
 * @author Fedor Resnyanskiy
 */
public class GxtYearMonthDayConverter {

    public static Date convert(final YearMonthDay sbDate) {
        if (null == sbDate) {
            return null;
        }
        final JsDate jsDate = JsDate.create(sbDate.getYear(), sbDate.getMonth() - 1, sbDate.getDay());
        return new Date((long) jsDate.getTime());
    }

    public static YearMonthDay convert(final Date date) {
        if (null == date) {
            return null;
        }
        final JsDate jsDate = JsDate.create(date.getTime());
        return new YearMonthDay(jsDate.getFullYear(), jsDate.getMonth() + 1, jsDate.getDate());
    }
}
