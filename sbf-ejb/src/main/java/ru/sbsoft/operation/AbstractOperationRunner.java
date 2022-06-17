package ru.sbsoft.operation;

import java.io.File;

import ru.sbsoft.processor.ServerOperationContext;
import ru.sbsoft.processor.IOperationProcessor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.SessionContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbsoft.dao.I18nResourceWrap;
import ru.sbsoft.dao.operations.IMultiOperationDao;
import ru.sbsoft.dao.IStorageDao;
import ru.sbsoft.processor.OperationLogger;
import ru.sbsoft.shared.OperationObject;
import ru.sbsoft.shared.api.i18n.Ii18nDao;
import ru.sbsoft.shared.exceptions.ApplicationException;
import ru.sbsoft.shared.meta.BigDecimalListWrapper;
import ru.sbsoft.shared.meta.BigDecimalWrapper;
import ru.sbsoft.shared.meta.BooleanWrapper;
import ru.sbsoft.shared.meta.DateWrapper;
import ru.sbsoft.shared.meta.IntegerWrapper;
import ru.sbsoft.shared.meta.LongWrapper;
import ru.sbsoft.shared.meta.LookupListWrapper;
import ru.sbsoft.shared.meta.StringListWrapper;
import ru.sbsoft.shared.meta.StringWrapper;
import ru.sbsoft.shared.meta.Wrapper;
import ru.sbsoft.shared.model.GridParamsBean;
import ru.sbsoft.shared.model.operation.OperationCommand;
import ru.sbsoft.shared.model.OperationEventType;
import ru.sbsoft.shared.model.enums.MultiOperationStatus;
import ru.sbsoft.shared.model.operation.OperationException;
import ru.sbsoft.shared.model.operation.OperationInfo;

import static ru.sbsoft.shared.model.operation.OperationInfo.FINISH_MARKS;

import ru.sbsoft.shared.param.BigDecimalParamInfo;
import ru.sbsoft.shared.param.BooleanParamInfo;
import ru.sbsoft.shared.param.DateParamInfo;
import ru.sbsoft.shared.param.IntegerParamInfo;
import ru.sbsoft.shared.param.ListParamInfo;
import ru.sbsoft.shared.param.LongParamInfo;
import ru.sbsoft.shared.param.LookUpParamInfo;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.param.ParamTypeEnum;
import ru.sbsoft.shared.param.StringParamInfo;
import ru.sbsoft.generator.api.Lookup;
import ru.sbsoft.sbf.app.model.YearMonthDay;
import ru.sbsoft.shared.api.i18n.I18nResource;
import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.consts.SBFExceptionStr;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.meta.YearMonthDayWrapper;
import ru.sbsoft.shared.param.YearMonthDayParamInfo;

/**
 * Базовый абстрактный класс запуска /старых/ операций на сервере
 */
public abstract class AbstractOperationRunner implements IOperationProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractOperationRunner.class);
    private MultiOperationSessionContext sessionContext;

    private OperationCommand operationCommand;
    private ServerOperationContext serverOperationContext;
    @Lookup
    protected IStorageDao storageDao;
    @Lookup
    private IMultiOperationDao iMultiOperationDao;
    @Lookup
    protected Ii18nDao i18nDao;
    //
    private BigDecimal progressPercent = BigDecimal.ZERO;
    private long progressTotal = 100;
    private long progressValue = -1;
    private final Map<String, ParamInfo> parameters = new HashMap<>();
    private boolean canceled;
    private I18nResource i18nResource;

    @Override
    public void init(ServerOperationContext serverOperationContext) throws OperationException {
        this.serverOperationContext = serverOperationContext;
        sessionContext = createSessionContext(serverOperationContext.getRunUser());

        operationCommand = createOperationCommandWithContext(serverOperationContext);
        for (ParamInfo paramInfo : operationCommand.getParams()) {
            parameters.put(paramInfo.getName(), paramInfo);
        }
    }

    public OperationLogger getOperationLogger() {
        return serverOperationContext.getOperationLogger();
    }

    public ServerOperationContext getServerOperationContext() {
        return serverOperationContext;
    }

    public String getOperationUsername() {
        return getServerOperationContext().getRunUser();
    }

    protected long getOperationId() {
        return serverOperationContext.getCurrentOperationId();
    }

    /**
     * Контекст пользователя, создавшего операцию (текущий SessionContext будет
     * принадлежать СЕРВЕРУ).
     *
     * @return контекст пользователя, создавшего операцию.
     */
    protected SessionContext getSessionContext() {
        return sessionContext;
    }

    protected OperationCommand getOperationCommand() {
        return operationCommand;
    }

    protected String getCode() {
        return operationCommand.getCode();
    }

    protected String getLocale() {
        return operationCommand.getLocale();
    }

    protected I18nResource getI18nResource() {
        if (null == i18nResource) {
            i18nResource = new I18nResourceWrap(i18nDao, getLocale());
        }
        return i18nResource;
    }

    public final void checkInterruptedLazy() throws InterruptedException {
        if (progressTotal % 1000 == 0) {
            checkInterrupted();
        }
    }

    protected String getLocaleResource(I18nResourceInfo resourceInfo, ILocalizedString... parameters) {
        return i18nDao.get(getLocale(), resourceInfo, parameters);
    }

    protected String getLocaleResource(I18nResourceInfo resourceInfo, String... parameters) {
        return i18nDao.get(getLocale(), resourceInfo, parameters);
    }

    protected String getLocaleResource(I18nResourceInfo resourceInfo) {
        return i18nDao.get(getLocale(), resourceInfo);
    }

    public final void checkInterrupted() throws InterruptedException {
        try {
            if (isCanceled()) {
                throw new InterruptedException(getLocaleResource(SBFGeneralStr.msgOperAborted));
            }
        } catch (OperationException ex) {
            LOGGER.warn("Cannot check operation status", ex);
        }
    }

    public void initProgress(long total) {
        this.progressTotal = total;
        updateProgress(0);
    }

    public void updateProgress(long done) {
        updateProgress(done, null);
    }

    public void updateProgress(long done, String notes) {
        this.progressValue = done;
        updateProgress(notes);
    }

    private void updateProgress(String notes) {
        if (progressValue > progressTotal) {
            progressTotal = progressValue;
        }
        saveProgress(new BigDecimal(100.0 * progressValue / progressTotal).setScale(2, RoundingMode.HALF_EVEN), notes);
    }

    private void saveProgress(final BigDecimal newProgressPercent, String notes) {
        if (progressPercent.compareTo(newProgressPercent) != 0) {
            progressPercent = newProgressPercent;
            try {
                iMultiOperationDao.updateOperationProgress( getOperationId(), progressPercent, notes);
            } catch (OperationException ex) {
                LOGGER.warn("Cannot update operation progess", ex);
            }
        }
    }

    protected void saveReport(File resultFile, String filename) {
        saveFile(OperationEventType.RESULT, filename, resultFile);
    }

    protected void saveExport(File resultFile, String filename) {
        saveFile(OperationEventType.EXPORT, filename, resultFile);
    }

    private void saveFile(OperationEventType type, String filename, File resultFile) throws RuntimeException {
        try {
            long fileId = storageDao.save(getOperationUsername(), getOperationCommand().getOperationType().toString(), null, filename, resultFile);
            final String fileString = "" + fileId + "|" + filename;
            getOperationLogger().log(fileString, null, type);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void error(final String message) {
        getOperationLogger().error(message);
    }

    public void error(final String message, Throwable throwable) {
        if (throwable != null) {
            // НАДО!!! иначе потом пропадет
            throwable.getStackTrace();
        }
        getOperationLogger().error(message, throwable);
    }

    public void warning(final String message) {
        getOperationLogger().warn(message);
    }

    public void info(final String message) {
        getOperationLogger().info(message);
    }

    public void saveMarks(List<BigDecimal> marks) {
        try {
            OperationObject markList = new OperationObject();
            markList.setName(FINISH_MARKS);
            markList.setDTO(new BigDecimalListWrapper(marks));
            iMultiOperationDao.setOperationParameter(getOperationId(), markList);
        } catch (OperationException ex) {
            String message = getLocaleResource(SBFExceptionStr.failedSaveTableLabel);
            error(message, ex);
            throw new ApplicationException(message);
        }
    }

    protected ParamInfo getParam(String name) {
        final ParamInfo param = getParam(name, null);
        if (param == null) {
            throw new IllegalArgumentException(getLocaleResource(SBFExceptionStr.parameterNotDefined, name));
        }
        return param;
    }

    public List<ParamInfo> getParameters() {
        return operationCommand.getParams();
    }

    public Map<String, ParamInfo> getParametersMap() {
        return parameters;
    }

    protected boolean isExistsParam(String name) {
        return getParametersMap().get(name) != null;
    }

    protected ParamInfo getParam(String name, ParamInfo defaultValue) {
        final ParamInfo param = getParametersMap().get(name);
        return param != null ? param : defaultValue;
    }

    protected String getStringParam(String name) {
        final ParamInfo param = getParam(name);
        if (param instanceof StringParamInfo) {
            return ((StringParamInfo) param).getValue();
        }
        throw new IllegalArgumentException(getLocaleResource(SBFExceptionStr.parameterTypeIncorrect, name, param.getType().name(), ParamTypeEnum.STRING.name()));
    }

    protected Integer getIntegerParam(String name) {
        final Integer result = getIntegerParam(name, null);
        if (result == null) {
            throw new IllegalArgumentException(getLocaleResource(SBFExceptionStr.parameterNotDefined, name));
        }
        return result;
    }

    protected Integer getIntegerParam(String name, Integer defaulValue) {
        final ParamInfo param = getParam(name);
        if (param == null) {
            return defaulValue;
        }
        if (param instanceof IntegerParamInfo) {
            return ((IntegerParamInfo) param).getValue();
        }
        throw new IllegalArgumentException(getLocaleResource(SBFExceptionStr.parameterTypeIncorrect, name, param.getType().name(), ParamTypeEnum.INTEGER.name()));
    }

    protected Date getDateParam(String name) {
        final ParamInfo param = getParam(name);
        if (param instanceof DateParamInfo) {
            return ((DateParamInfo) param).getValue();
        }
        throw new IllegalArgumentException(getLocaleResource(SBFExceptionStr.parameterTypeIncorrect, name, param.getType().name(), ParamTypeEnum.DATE.name()));
    }

    protected BigDecimal getBigDecimalParam(String name) {
        final ParamInfo param = getParam(name);
        if (param instanceof BigDecimalParamInfo) {
            return ((BigDecimalParamInfo) param).getValue();
        }
        throw new IllegalArgumentException(getLocaleResource(SBFExceptionStr.parameterTypeIncorrect, name, param.getType().name(), ParamTypeEnum.BIGDECIMAL.name()));
    }

    protected YearMonthDay getYearMonthDayParam(String name) {
        final ParamInfo param = getParam(name);
        if (param instanceof YearMonthDayParamInfo) {
            return ((YearMonthDayParamInfo) param).getValue();
        }
        throw new IllegalArgumentException(getLocaleResource(SBFExceptionStr.parameterTypeIncorrect, name, param.getType().name(), ParamTypeEnum.YMD.name()));
    }

    public long getProgressTotal() {
        return progressTotal;
    }

    public long getProgressValue() {
        return progressValue;
    }

    public long incrementProgressValue(final int delta) {
        long newProgressValue = progressValue + delta;
        updateProgress(newProgressValue);
        return newProgressValue;
    }

    /**
     * Запуск операции на сервере
     *
     * @throws Exception
     */
    public abstract void run() throws Exception;

    @Override
    public void execute() throws OperationException {
        try {
            run();
            updateProgress(progressTotal, getLocaleResource(SBFGeneralStr.executed));
        } catch (Exception ex) {
            throw new OperationException(ex);
        }
    }

    @Override
    public boolean isCanceled() throws OperationException {
        //Вот как я умею :)
        return canceled = canceled || MultiOperationStatus.STARTED != iMultiOperationDao.getOperationStatus(getOperationId());
    }

    //<editor-fold defaultstate="collapsed" desc="Adapt command">
    private static OperationCommand createOperationCommandWithContext(ServerOperationContext serverOperationContext) throws OperationException {
        OperationCommand command = new OperationCommand();
        command.setCurrentOperationId(serverOperationContext.getCurrentOperationId());
        command.putParams(convertParams(serverOperationContext.getParameters()));
        command.setOperationType(serverOperationContext.getOperationType());
        command.setGridContext(createGridContext(serverOperationContext.getParameters()));
        return command;
    }

    private static GridParamsBean createGridContext(List<OperationObject> operationParameters) {
        for (OperationObject parameter : operationParameters) {
            if (parameter.getName().equals(OperationInfo.OPERATION_GRID_PARAMETERS)) {
                return (GridParamsBean) parameter.getDTO();
            }
        }
        return null;
    }

    public static List<ParamInfo> convertParams(List<OperationObject> operationParameters) throws OperationException {
        List<ParamInfo> params = new ArrayList<>();
        if (operationParameters == null) {
            return params;
        }
        for (OperationObject parameter : operationParameters) {
            if (OperationInfo.isSystemParameter(parameter)) {
                continue;
            }
            final ParamInfo param = convertParameter(parameter);
            if (param != null) {
                params.add(param);
            }
        }
        return params;
    }

    public static ParamInfo convertParameter(OperationObject parameter) {
        String name = parameter.getName();

        if (!Wrapper.class.isAssignableFrom(parameter.getDTO().getClass())) {
            return null;
        }

        Wrapper wrapper = (Wrapper) parameter.getDTO();
        final Object value = wrapper.getValue();

        if (wrapper instanceof BigDecimalWrapper) {
            return new BigDecimalParamInfo(name, (BigDecimal) value);
        }
        if (wrapper instanceof BooleanWrapper) {
            return new BooleanParamInfo(name, (Boolean) value);
        }
        if (wrapper instanceof DateWrapper) {
            return new DateParamInfo(name, (Date) value);
        }
        if (wrapper instanceof YearMonthDayWrapper) {
            return new YearMonthDayParamInfo(name, (YearMonthDay) value);
        }
        if (wrapper instanceof IntegerWrapper) {
            return new IntegerParamInfo(name, (Integer) value);
        }
        if (wrapper instanceof StringListWrapper) {
            return new ListParamInfo(name, (List) value);
        }
        if (wrapper instanceof LongWrapper) {
            return new LongParamInfo(name, (Long) value);
        }
        if (wrapper instanceof LookupListWrapper) {
            final LookUpParamInfo lookUpParamInfo = new LookUpParamInfo();
            lookUpParamInfo.setValue((List) value);
            lookUpParamInfo.setName(name);
            return lookUpParamInfo;

        }
        if (wrapper instanceof StringWrapper) {
            return new StringParamInfo(name, (String) value);
        }

        ParamInfo paramInfo = new CustomParamInfo();
        paramInfo.setName(name);
        paramInfo.setValue(value);
        return paramInfo;


        // throw new OperationException("Parameter '" + wrapper.getClass().getName() + "' has unknown type '" + wrapper.getClass().getSimpleName() + "'");
    }
    //</editor-fold>

    private MultiOperationSessionContext createSessionContext(String runUser) {
        MultiOperationSessionContext scontext = new MultiOperationSessionContext();
        scontext.setName(runUser);
        return scontext;
    }

    private static class CustomParamInfo extends ParamInfo {
    }
}
