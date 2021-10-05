package ru.sbsoft.meta.columns;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import ru.sbsoft.sbf.app.model.YearMonthDay;
import ru.sbsoft.server.utils.YearMonthDayConverter;
import ru.sbsoft.shared.meta.ColumnType;

/**
 *
 * @author sychugin
 */
public class YearMonthDayColumnInfo extends ColumnInfo<YearMonthDay>{

    public YearMonthDayColumnInfo() {
        super(ColumnType.YMDAY);
    }

    @Override
    public YearMonthDay read(ResultSet resultSet) throws SQLException {
        final Date value = resultSet.getDate(alias);
	return YearMonthDayConverter.convert(value);
    }
    

}
