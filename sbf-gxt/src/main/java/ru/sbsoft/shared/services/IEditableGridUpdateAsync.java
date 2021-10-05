package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import ru.sbsoft.sbf.app.model.FormModel;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FormContext;
import ru.sbsoft.shared.meta.Column;
import ru.sbsoft.shared.meta.Wrapper;

public interface IEditableGridUpdateAsync {

    void updateColumn(BigDecimal primaryKey, Column column, Integer cellPrecision, Wrapper value,
            AsyncCallback<BigDecimal> callback);

    void updateColumns(List<Column> columns, Integer cellPrecision, Wrapper value,
            AsyncCallback<List<BigDecimal>> callback);

    void updateRows(Map<BigDecimal, Map<String, Wrapper>> rows, String table,
            AsyncCallback<List<BigDecimal>> callback);

    void insertRow(final FormContext type, final List<FilterInfo> parentFilters,
            AsyncCallback<FormModel> callback);
}
