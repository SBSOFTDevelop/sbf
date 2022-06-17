package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FormContext;
import ru.sbsoft.shared.grid.condition.GridEditVal;
import ru.sbsoft.shared.meta.Wrapper;

@RemoteServiceRelativePath(ServiceConst.EDITABLE_GRID_UPDATE_SERVICE_SHORT)
public interface IEditableGridUpdate extends RemoteService {

    List<BigDecimal> updateRows(String table, Map<BigDecimal, Map<String, GridEditVal>> rows);

    IFormModel insertRow(final FormContext type, final List<FilterInfo> parentFilters);
}
