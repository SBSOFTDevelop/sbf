package ru.sbsoft.dao;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.OptimisticLockException;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FormContext;
import ru.sbsoft.sbf.app.model.IFormModel;

/**
 * CRUD для данных визуальной формы
 */
public interface IFormDao {

    IFormModel getRecord(final FormContext type, final BigDecimal Id, final List<FilterInfo> parentFilters);

    IFormModel newRecord(final FormContext type, final List<FilterInfo> parentFilters, final BigDecimal clonableRecordID);

    IFormModel putRecord(final FormContext type, final IFormModel model, final List<FilterInfo> parentFilters) throws OptimisticLockException;

    void delRecord(final FormContext type, final BigDecimal Id) throws OptimisticLockException;
}
