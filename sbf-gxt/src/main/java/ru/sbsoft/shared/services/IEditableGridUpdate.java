package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FormContext;
import ru.sbsoft.shared.meta.Column;
import ru.sbsoft.shared.meta.Wrapper;

@RemoteServiceRelativePath(ServiceConst.EDITABLE_GRID_UPDATE_SERVICE_SHORT)
public interface IEditableGridUpdate extends RemoteService {

    BigDecimal updateColumn(BigDecimal primaryKey, Column column, Integer cellPrecision, Wrapper value);

    List<BigDecimal> updateColumns(List<Column> columns, Integer cellPrecision, Wrapper value);

    List<BigDecimal> updateRows(Map<BigDecimal, Map<String, Wrapper>> rows, String table);

    IFormModel insertRow(final FormContext type, final List<FilterInfo> parentFilters);
}
