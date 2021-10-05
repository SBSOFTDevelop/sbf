package ru.sbsoft.client.components.grid.aggregate.result;

import com.google.gwt.i18n.client.DateTimeFormat;
import java.util.Date;

/**
 *
 * @author Kiselev
 */
public class TemporalHandler implements IResultHandler<Date> {
    private final DateTimeFormat fmt;

    public TemporalHandler(DateTimeFormat fmt) {
        this.fmt = fmt;
    }

    @Override
    public String formatResultVal(Date o) {
        return fmt != null ? fmt.format(o) : String.valueOf(o);
    }
}
