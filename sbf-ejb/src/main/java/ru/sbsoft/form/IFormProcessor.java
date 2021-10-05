package ru.sbsoft.form;

import java.math.BigDecimal;
import java.util.List;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.FilterInfo;

/**
 * Обработчик запросов из конкретной формы. Для связи с формой используется
 * анотация {@link ru.sbsoft.generator.api.FormProcessor}.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 * @param <T>
 */
public interface IFormProcessor<T extends IFormModel> {

    T newRecord(final List<FilterInfo> parentFilters, final BigDecimal clonableRecordID);

    T putRecord(final T model);

    void delRecord(final BigDecimal id);

    T getRecord(final BigDecimal id);
    
    IFormProcessor<T> setParentFilters(List<FilterInfo> filters);
}
