package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import ru.sbsoft.sbf.app.model.FormModel;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FormContext;
import ru.sbsoft.shared.grid.condition.GridEditVal;

public interface IEditableGridUpdateAsync {

    void updateRows(String table, Map<BigDecimal, Map<String, GridEditVal>> rows,
            AsyncCallback<List<BigDecimal>> callback);

    void insertRow(final FormContext type, final List<FilterInfo> parentFilters,
            AsyncCallback<FormModel> callback);
}
