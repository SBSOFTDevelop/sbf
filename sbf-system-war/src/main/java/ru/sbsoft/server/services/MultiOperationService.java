package ru.sbsoft.server.services;

import ru.sbsoft.shared.meta.StringWrapper;
import ru.sbsoft.shared.meta.BigDecimalListWrapper;
import ru.sbsoft.shared.meta.BigDecimalWrapper;
import ru.sbsoft.shared.meta.IntegerWrapper;
import ru.sbsoft.shared.meta.DateWrapper;
import ru.sbsoft.shared.meta.BooleanWrapper;
import ru.sbsoft.shared.meta.LookupListWrapper;
import ru.sbsoft.shared.meta.LongWrapper;
import ru.sbsoft.shared.meta.Wrapper;
import ru.sbsoft.shared.meta.StringListWrapper;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import ru.sbsoft.dao.IConfigDao;
import ru.sbsoft.dao.IGridDao;
import ru.sbsoft.dao.operations.IMultiOperationDao;
import ru.sbsoft.sbf.app.model.YearMonthDay;
import ru.sbsoft.shared.GridOperationParamConst;
import ru.sbsoft.shared.OperationObject;
import ru.sbsoft.shared.api.i18n.consts.SBFExceptionStr;
import ru.sbsoft.shared.exceptions.ApplicationException;
import ru.sbsoft.shared.meta.YearMonthDayWrapper;
import ru.sbsoft.shared.model.GridParamsBean;
import ru.sbsoft.shared.model.LookupInfoModel;
import ru.sbsoft.shared.model.operation.OperationCommand;
import ru.sbsoft.shared.model.operation.CreateOperationRequest;
import ru.sbsoft.shared.model.operation.OperationException;
import ru.sbsoft.shared.model.operation.OperationInfo;
import ru.sbsoft.shared.model.operation.OperationsSelectFilter;
import ru.sbsoft.shared.param.DTO;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.param.ParamTypeEnum;
import ru.sbsoft.shared.param.StringParamInfo;
import ru.sbsoft.shared.services.IMultiOpearationService;
import ru.sbsoft.shared.services.ServiceConst;

/**
 * Реализация сервиса для взаимодействия клиента с серверными механизмами
 * управления операциями.
 *
 * @see IMultiOpearationService
 * @author Fedor Resnyanskiy, SBSOFT
 */
@WebServlet(urlPatterns = {ServiceConst.MULTI_OPERATION_SERVICE_LONG})
public class MultiOperationService extends SBFRemoteServiceServlet implements IMultiOpearationService {

    @EJB
    private IMultiOperationDao multiOperationDao;

    @EJB
    private IGridDao gridDao;

    @EJB
    private IConfigDao configDao;

    @Override
    public Long createOperation(CreateOperationRequest request) {
        try {
            final Long operationId = multiOperationDao.createOperation(request.getOperationType(), request.getLocale(), request.getModuleName());
            return operationId;
        } catch (OperationException ex) {
            throw new ApplicationException(SBFExceptionStr.operCreateError, ex);
        }
    }

    @Override
    public void setOperationParameters(Long operationId, List<OperationObject> parameters) {
        try {
            multiOperationDao.setOperationParameters(operationId, parameters);
        } catch (OperationException ex) {
            throw new ApplicationException(SBFExceptionStr.operSetParamsError, ex);
        }
    }

    @Override
    public List<OperationInfo> listCurrentUserOperations(OperationsSelectFilter filter) {
        try {
            return multiOperationDao.listCurrentUserOperationsJdbc(filter);
        } catch (SQLException ex) {
            throw new ApplicationException(SBFExceptionStr.operListError, ex);
        }
    }

    @Override
    public OperationInfo getOperationInfo(Long operationId) {
        try {
            return multiOperationDao.getOperationInfo(operationId);
        } catch (OperationException ex) {
            throw new ApplicationException(SBFExceptionStr.operInfoError, ex);
        }
    }

    
    
    @Override
    public void start(Long operationId) {
        try {
            multiOperationDao.startOperation(operationId);
        } catch (OperationException ex) {
            throw new ApplicationException(SBFExceptionStr.operStartError, ex);
        }
    }

    @Override
    public void cancel(Long operationId) {
        try {
            multiOperationDao.cancelOperation(operationId);
        } catch (OperationException ex) {
            throw new ApplicationException(SBFExceptionStr.operCancelError, ex);
        }
    }

    @Override
    public void setVisible(Long operationId, boolean visible) {
        try {
            multiOperationDao.setOperationVisible(operationId, visible);
        } catch (OperationException ex) {
            throw new ApplicationException(SBFExceptionStr.operMarkError, ex);
        }
    }

    @Override
    public Long startOperation(OperationCommand command) {
        try {
            command.getParams().add(new StringParamInfo(APPLICATION_PREFIX_PARAM_NAME, getAppPrefix()));

            final GridParamsBean gridParams = command.getGridContext();
            if (null != gridParams && gridParams.getGridContext() != null) {
                //добиваем настройками фильтров
                gridParams.getPageFilterInfo().setFilters(configDao.getCurrentFilter(getAppPrefix(), gridParams.getGridContext()).getFilter());
            }

            setGridMarks(command);

            final String currentModuleCode = command.getModuleName();
            //1
            final Long operationId = multiOperationDao.createOperation(command.getOperationType(), command.getLocale(), currentModuleCode, command.isNeedNotify());

            final List<OperationObject> operationParameters = convertParamters(command);
            final OperationObject gridParameter = convertGridParameters(command);
            if (gridParameter != null) {
                operationParameters.add(gridParameter);
            }

            multiOperationDao.setOperationParameters(operationId, operationParameters);

            multiOperationDao.startOperation(operationId);
            return operationId;
        } catch (OperationException ex) {
            throw new ApplicationException(SBFExceptionStr.operStartErrorP, ex, command.getCode());
        }
    }

    private static List<OperationObject> convertParamters(OperationCommand command) throws OperationException {
        final List<ParamInfo> params = command.getParams();
        final List<OperationObject> operationParameters = new ArrayList<>();
        if (params != null) {
            for (ParamInfo param : params) {
                operationParameters.add(convertParameter(param));
            }
        }

        return operationParameters;
    }

    private static OperationObject convertParameter(ParamInfo param) throws OperationException {
        final OperationObject parameter = new OperationObject();
        parameter.setName(param.getName());
        try {
            parameter.setDTO(convertParameterValue(param.getType(), param.getValue()));
        } catch (OperationException ex) {
            throw new ApplicationException(SBFExceptionStr.operConvertParamError, ex, param.getName());
        }
        return parameter;
    }

    private static DTO convertParameterValue(ParamTypeEnum type, Object value) throws OperationException {
        switch (type) {
            case BIGDECIMAL:
                return new BigDecimalWrapper((BigDecimal) value);
            case BOOLEAN:
                return new BooleanWrapper((Boolean) value);
            case DATE:
                if (value instanceof Date) {
                    return new DateWrapper((Date) value);
                } else {
                    return new Wrapper(value);
                }
            case YMD:
                return new YearMonthDayWrapper((YearMonthDay) value);
            case INTEGER:
                return new IntegerWrapper((Integer) value);
            case LIST:
                return new StringListWrapper((List<String>) value);
            case LONG:
                return new LongWrapper((Long) value);
            case LOOKUP:
                return new LookupListWrapper((List<LookupInfoModel>) value);
            case STRING:
                return new StringWrapper((String) value);

            default:
                throw new ApplicationException(SBFExceptionStr.operConvertParamTypeError, type.name());
        }
    }

    private OperationObject convertGridParameters(OperationCommand command) {
        final GridParamsBean gridParams = command.getGridContext();
        if (gridParams == null) {
            return null;
        }

        OperationObject gridParameters = new OperationObject();
        gridParameters.setName(OperationInfo.OPERATION_GRID_PARAMETERS);
        gridParameters.setDTO(gridParams);

        return gridParameters;
    }

    @Override
    public BigDecimalListWrapper getOperationParameter(Long operationId, String parameterName) {
        try {
            final OperationObject parameter = multiOperationDao.getOperationParameter(operationId, parameterName);
            return parameter == null ? null : (BigDecimalListWrapper) parameter.getDTO();
        } catch (OperationException ex) {
            throw new ApplicationException(SBFExceptionStr.operRetParamError, ex, parameterName, String.valueOf(operationId));
        }

    }

    /**
     * Если операция выполняется по всем записям таблицы, выгружаем целевые
     * строки и добавляем их в отмеченные.
     *
     * @param command команда запуска операции.
     */
    private void setGridMarks(OperationCommand command) {
        final ParamInfo selectType = getParameter(command.getParams(), GridOperationParamConst.GRID_SELECT_TYPE_PARAM);
        final ParamInfo idColumn = getParameter(command.getParams(), GridOperationParamConst.GRID_ID_COLUMN_PARAM);
        final GridParamsBean gridParams = command.getGridContext();
        if (gridParams != null && idColumn != null && selectType != null && GridOperationParamConst.GRID_SELECT_TYPE_ALL.equals(selectType.getValue())) {
            final List<BigDecimal> idList = gridDao.getOnlyIdsForBrowser(gridParams.getGridContext(), gridParams.getPageFilterInfo(), (String) idColumn.getValue());
            gridParams.setMarks(idList);
            //Эти параметры больше не нужны
            command.getParams().remove(selectType);
            command.getParams().remove(idColumn);
        }
    }

    private ParamInfo getParameter(List<ParamInfo> operationParameters, String operationName) {
        for (ParamInfo p : operationParameters) {
            if (p.getName().equals(operationName)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public void setUserNotified(Long operationId, Boolean notified) {
        try {
            multiOperationDao.setOperationNotified(operationId, notified);
        } catch (OperationException ex) {
            throw new ApplicationException(SBFExceptionStr.failedChangeStatus);
        }
    }

}
