package ru.sbsoft.server.services;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;

import ru.sbsoft.dao.IFormDao;
import ru.sbsoft.dao.IGridDao;
import ru.sbsoft.model.PageFilterInfo;
import ru.sbsoft.shared.services.IEditableGridUpdate;
import ru.sbsoft.dao.IGridUpdateDao;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FilterTypeEnum;
import ru.sbsoft.shared.FormContext;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.exceptions.ApplicationException;
import ru.sbsoft.shared.filter.StringFilterInfo;
import ru.sbsoft.shared.grid.condition.GridEditVal;
import ru.sbsoft.shared.meta.BigDecimalWrapper;
import ru.sbsoft.shared.meta.IdNameLongWrapper;
import ru.sbsoft.shared.meta.LongWrapper;
import ru.sbsoft.shared.meta.LookupGridInfo;
import ru.sbsoft.shared.meta.LookupKeyType;
import ru.sbsoft.shared.meta.Wrapper;
import ru.sbsoft.shared.services.ServiceConst;

@WebServlet(urlPatterns = {ServiceConst.EDITABLE_GRID_UPDATE_SERVICE_LONG})
public class EditableGridUpdateImpl extends SBFRemoteServiceServlet implements IEditableGridUpdate {

    @EJB
    private IGridUpdateDao gridUpdate;
    @EJB
    private IGridDao gridDao;
    @EJB
    private IFormDao formDao;

    @Override
    public List<BigDecimal> updateRows(String table, Map<BigDecimal, Map<String, GridEditVal>> rows) {
        Map<BigDecimal, Map<String, Wrapper>> newRows = new HashMap<>();
        rows.forEach((dataRowId, cols) -> {
            cols.forEach((colName, edVal) -> {
                Wrapper wr = edVal.getVal();
                if (edVal.getCol().getLookupGridInfo() != null) {
                    wr = getIdForLookup(edVal.getCol().getLookupGridInfo(), wr);
                }
                newRows.computeIfAbsent(dataRowId, rid -> new HashMap<>()).put(colName, wr);
            });
        });
        return gridUpdate.updateRows(table, newRows);
    }

    private Wrapper getIdForLookup(LookupGridInfo gridInfo, Wrapper lookupValueWrapper) {
        if (lookupValueWrapper == null || lookupValueWrapper.getValue() == null) {
            return new BigDecimalWrapper();
        }
        final FilterInfo filterInfo = new StringFilterInfo(
                null, lookupValueWrapper.getValue().toString());
        if (LookupKeyType.KEY_CODE == gridInfo.getKeyType()) {
            filterInfo.setType(FilterTypeEnum.LOOKUP_CODE);
        } else {
            filterInfo.setType(FilterTypeEnum.LOOKUP_NAME);
        }
        final PageFilterInfo pageFilterInfo = new PageFilterInfo();
        pageFilterInfo.setOffset(0);
        pageFilterInfo.setLimit(1);
        pageFilterInfo.setTempFilter(filterInfo);
        final GridContext context = new GridContext(gridInfo.getGridType(), null);
        final List<BigDecimal> result = gridDao.getOnlyIdsForBrowser(context, pageFilterInfo);
        if (result.isEmpty()) {
            throw new ApplicationException("Lookup value not found for value  = '"
                    + filterInfo.getValue().toString() + "'");
        }
        return new BigDecimalWrapper(result.get(0));
    }

    @Override
    public IFormModel insertRow(FormContext type, List<FilterInfo> parentFilters) {
        final IFormModel insertModel = formDao.newRecord(type, parentFilters, null);
        return formDao.putRecord(type, insertModel, parentFilters);
    }
}
