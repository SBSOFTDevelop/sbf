package ru.sbsoft.system.common;

import java.sql.Date;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import ru.sbsoft.sbf.app.model.YearMonthDay;
import ru.sbsoft.server.utils.YearMonthDayConverter;

/**
 *
 * @author vk
 */
@Converter(autoApply = true)
public class YMDjpaConverter implements AttributeConverter<YearMonthDay, Date> {

    @Override
    public Date convertToDatabaseColumn(YearMonthDay attribute) {
        if (attribute == null) {
            return null;
        }
        java.util.Date d = YearMonthDayConverter.convert(attribute);
        return new Date(d.getTime());
    }

    @Override
    public YearMonthDay convertToEntityAttribute(Date dbData) {
        if (dbData == null) {
            return null;
        }
        return YearMonthDayConverter.convert(dbData);
    }
}
