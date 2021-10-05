package ru.sbsoft.server.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import ru.sbsoft.shared.PageList;
import ru.sbsoft.shared.exceptions.ApplicationException;
import ru.sbsoft.shared.exceptions.FilterRequireException;
import ru.sbsoft.shared.filter.StringFilterInfo;
import ru.sbsoft.shared.meta.Column;
import ru.sbsoft.shared.meta.RowsInfo;
import ru.sbsoft.shared.meta.LookupGridInfo;
import ru.sbsoft.shared.meta.LookupKeyType;
import ru.sbsoft.shared.meta.UpdateInfo;
import ru.sbsoft.shared.meta.Wrapper;
import ru.sbsoft.shared.model.MarkModel;
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
    public List<BigDecimal> updateRows(Map<BigDecimal, Map<String, Wrapper>> rows, String table) {

        return gridUpdate.updateRows(rows, table);

    }

    @Override
    public List<BigDecimal> updateColumns(List<Column> columns, Integer cellPrecision, Wrapper value) {
        Object value_ = value.getValue();

        Set<BigDecimal> res = new HashSet<>();
        for (Column column : columns) {
            UpdateInfo updateInfo = column.getUpdateInfo();
            if (cellPrecision != null) {
                updateInfo = new UpdateInfo(updateInfo.getTable(), updateInfo.getColumn(), cellPrecision);
            }
            res.addAll(gridUpdate.updateColumn(((RowsInfo) column.getCustomInfo()).getItems(), updateInfo, value_));
            /*for (BigDecimal rowId : ((RowsInfo) column.getCustomInfo()).getItems()) {
                res.add(updateColumn(rowId, column, cellPrecision, value));

            }
             */
        }

        return new ArrayList<>(res);
    }

    @Override
    public BigDecimal updateColumn(BigDecimal primaryKey, Column column, Integer cellPrecision, Wrapper wrapper) {
        final LookupGridInfo gridInfo = column.getLookupGridInfo();
        final Object value;
        if (null == wrapper) {
            value = null;
        } else if (null == gridInfo) {
            value = wrapper.getValue();
        } else {
            final FilterInfo filterInfo = new StringFilterInfo(
                    null, wrapper.getValue().toString());
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
            try {
                final PageList<? extends MarkModel> result
                        = gridDao.getDataForBrowser(context, pageFilterInfo);
                if (result.isEmpty()) {
                    throw new ApplicationException("Lookup value not found for value  = '"
                            + filterInfo.getValue().toString() + "'");
                }
                value = result.get(0).getRECORD_ID();
            } catch (FilterRequireException ex) {
                LOGGER.error("", ex);
                throw new ApplicationException(ex.getMessage());
            }
        }

        UpdateInfo updateInfo = column.getUpdateInfo();
        if (cellPrecision != null) {
            updateInfo = new UpdateInfo(updateInfo.getTable(), updateInfo.getColumn(), cellPrecision);
        }
        return gridUpdate.updateColumn(primaryKey, updateInfo, value);
    }

    @Override
    public IFormModel insertRow(FormContext type, List<FilterInfo> parentFilters) {
        final IFormModel insertModel = formDao.newRecord(type, parentFilters, null);
        return formDao.putRecord(type, insertModel, parentFilters);
    }
}
