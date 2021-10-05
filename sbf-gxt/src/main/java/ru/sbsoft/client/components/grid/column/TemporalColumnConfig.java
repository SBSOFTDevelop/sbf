package ru.sbsoft.client.components.grid.column;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import java.util.Date;
import ru.sbsoft.client.components.grid.column.cell.DateCell;
import ru.sbsoft.client.utils.PredefinedFormatHelper;
import ru.sbsoft.shared.meta.Column;

/**
 * Базовый класс для параметров колонок таблицы, представляемых типом {@link Date}.
 */
public abstract class TemporalColumnConfig extends CustomColumnConfig<Date> {

    private final DateTimeFormat format;

    public TemporalColumnConfig(Column column, int index, PredefinedFormat defaultFormat) {
        super(new CustomValueProvider<Date>(index), column);

        final String s = column.getFormat();
        DateTimeFormat.PredefinedFormat predefinedFormat = (s != null) ? PredefinedFormatHelper.find(s) : defaultFormat;
        format = (predefinedFormat != null) ? DateTimeFormat.getFormat(predefinedFormat) : DateTimeFormat.getFormat(s);

        setCell(new DateCell(format));
        setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    }

    public DateTimeFormat getFormat() {
        return format;
    }
}
