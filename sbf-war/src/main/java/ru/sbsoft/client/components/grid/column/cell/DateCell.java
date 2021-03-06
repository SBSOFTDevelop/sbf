package ru.sbsoft.client.components.grid.column.cell;

import com.google.gwt.i18n.client.DateTimeFormat;
import java.util.Date;

/**
 * Ячейка таблицы для отображения значений {@link Date}
 *
 * @author balandin
 * @since Oct 23, 2014 2:48:17 PM
 */
public class DateCell extends CustomCell<Date> {

    //private static String europeMoscow = "{\"id\": \"Europe/Moscow\", \"transitions\": [98589, 60, 102980, 0, 107349, 60, 111740, 0, 116109, 60, 120500, 0, 124893, 60, 129263, 0, 133631, 60, 137999, 0, 142367, 60, 146735, 0, 151103, 60, 155471, 0, 159839, 60, 164207, 0, 168575, 60, 172943, 0, 177311, 60, 181847, 0, 186215, 60, 190584, 0, 194948, 60, 199315, 0, 203687, 60, 208055, 0, 212423, 60, 216791, 0, 221159, 60, 225527, 0, 230063, 60, 235103, 0, 238799, 60, 243839, 0, 247535, 60, 252575, 0, 256271, 60, 261479, 0, 265007, 60, 270215, 0, 273743, 60, 278951, 0, 282647, 60, 287687, 0, 291383, 60, 296423, 0, 300119, 60, 305327, 0, 308855, 60, 314063, 0, 317591, 60, 322799, 0, 326327, 60, 331535, 0, 335231, 60, 340271, 0, 343967, 60, 349007, 0, 352703, 60, 357911, 0], \"names\": [\"MSK\", \"Moscow Standard Time\", \"MSKS\", \"Moscow Summer Time\"], \"std_offset\": 240}";
    private final DateTimeFormat format;

    public DateCell(DateTimeFormat format) {
        this.format = format;
    }

    @Override
    public String format(Date value) {
        //europeMoscow      

       // return format.format(value, com.google.gwt.i18n.client.TimeZone.createTimeZone(europeMoscow));
        return format.format(value);
    }
}
