package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.math.BigDecimal;
import java.util.List;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FormContext;
import ru.sbsoft.shared.exceptions.OptimisticLockException;
import ru.sbsoft.sbf.app.model.IFormModel;

/**
 * CRUD для работы с сущностью. 
 * Т.к. работа с сущностью пользователем обычно происходит в форме, этот сервис, в основном, используется кодом формы.
 */
@RemoteServiceRelativePath(ServiceConst.FORM_SERVICE_SHORT)
public interface IFormService extends SBFRemoteService {

    IFormModel getRecord(final FormContext type, final BigDecimal Id, final List<FilterInfo> parentFilters);

    IFormModel newRecord(final FormContext type, final List<FilterInfo> parentFilters, final BigDecimal clonableRecordID);

    IFormModel putRecord(final FormContext type, final IFormModel model, final List<FilterInfo> parentFilters) throws OptimisticLockException;

    BigDecimal delRecord(final FormContext type, final BigDecimal Id) throws OptimisticLockException;
}
