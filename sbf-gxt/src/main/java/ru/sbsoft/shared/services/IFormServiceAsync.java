package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.math.BigDecimal;
import java.util.List;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FormContext;
import ru.sbsoft.sbf.app.model.IFormModel;

/**
 * @see IFormService
 */
public interface IFormServiceAsync extends ISBFServiceAsync {

    void getRecord(FormContext type, BigDecimal Id, List<FilterInfo> parentFilters, AsyncCallback<? extends IFormModel> callback);

    void newRecord(FormContext type, List<FilterInfo> parentFilters, BigDecimal clonableRecordID, AsyncCallback<? extends IFormModel> callback);

    void putRecord(FormContext type, IFormModel model, List<FilterInfo> parentFilters, AsyncCallback<? extends IFormModel> callback);

    void delRecord(FormContext type, BigDecimal Id, AsyncCallback<BigDecimal> callback);
}
