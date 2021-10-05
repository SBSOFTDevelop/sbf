package ru.sbsoft.shared;

import java.util.List;
import ru.sbsoft.common.Strings;

/**
 * Вспомогательный класс содержащий статические методы (утилиты) для работы с элементами пользовательского фильтра.
 * <ul>
 * <li>{@code public static boolean equalsFilters(final List<FilterInfo> filters1, final List<FilterInfo> filters2)}- проверка на равенство двух экземпляров {@code List<FilterInfo>} </li>
 * <li>{@code public static FilterInfo FilterInfo findItem(final List<FilterInfo> filters, final String columnName)} - поиск в списке List<FilterInfo> экземпляра {@codeList<FilterInfo>} по имени колонки (поля в БД) {@code final String columnName}</li>
 * </ul>
 * @author Sokoloff
 */
public class FilterUtils {

    public static boolean equalsFilters(final List<FilterInfo> filters1, final List<FilterInfo> filters2) {
        if (null == filters1 && null == filters2) {
            return true;
        }
        if (null == filters1 && null != filters2) {
            return false;
        }
        if (null != filters1 && null == filters2) {
            return false;
        }
        if (filters1.size() != filters2.size()) {
            return false;
        }
        for (int i = 0; i < filters1.size(); i++) {
            FilterInfo filterInfo = findItem(filters2, filters1.get(i).getColumnName());
            if (filterInfo == null) {
                return false;
            }
            if (!filters1.get(i).equals(filterInfo)) {
                return false;
            }
        }
        return true;
    }
    
    public static FilterInfo findItem(final List<FilterInfo> filters, final String columnName) {
        if (null == filters || filters.isEmpty() || Strings.isEmpty(columnName)) {
            return null;
        }
        for (int i = 0; i < filters.size(); i++) {
            final FilterInfo filterInfo = filters.get(i);
            if (columnName.equals(filterInfo.getColumnName())) {
                return filterInfo;
            }
        }
        return null;
    }
}
