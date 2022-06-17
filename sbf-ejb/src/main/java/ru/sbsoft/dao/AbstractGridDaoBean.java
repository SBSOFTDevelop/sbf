package ru.sbsoft.dao;

import ru.sbsoft.common.Defs;
import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.model.PageFilterInfo;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.PageList;
import ru.sbsoft.shared.consts.TemplateStateMode;
import ru.sbsoft.shared.exceptions.FilterRequireException;
import ru.sbsoft.shared.interfaces.DynamicGridType;
import ru.sbsoft.shared.interfaces.GridType;
import ru.sbsoft.shared.interfaces.ObjectType;
import ru.sbsoft.shared.meta.IColumns;
import ru.sbsoft.shared.meta.aggregate.IAggregateDef;
import ru.sbsoft.shared.model.CustomReportFilterInfo;
import ru.sbsoft.shared.model.LookupInfoModel;
import ru.sbsoft.shared.model.MarkModel;
import ru.sbsoft.shared.model.enums.GridTypeEnum;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Kiselev
 */
@PermitAll
public abstract class AbstractGridDaoBean implements IGridDao, GridSupport {

    @Resource
    private SessionContext sessionContext;
    @PersistenceContext(unitName = ru.sbsoft.common.jdbc.Const.DEFAULT_PERSISTENCE_CTX)
    private EntityManager entityManager;
    @EJB
    private IApplicationDao appDao;
    private final ITemplateContext defaultTemplateContext = new BaseTemplateContext();
    private IJdbcWorkExecutor jdbcWorkExecutor;

    @PostConstruct
    private void init() {
        jdbcWorkExecutor = new JdbcWorkExecutor(entityManager);
    }

    protected final ITemplateContext getDefaultTemplateContext() {
        return defaultTemplateContext;
    }

    @Override
    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    protected IApplicationDao getAppDao() {
        return appDao;
    }

    public void setJdbcWorkExecutor(IJdbcWorkExecutor jdbcWorkExecutor) {
        if (this.jdbcWorkExecutor != null) {
            throw new IllegalStateException("Value is managed by ejb container");
        }
        this.jdbcWorkExecutor = jdbcWorkExecutor;
    }

    public void setEntityManager(EntityManager entityManager) {
        if (this.entityManager != null) {
            throw new IllegalStateException("Value is managed by ejb container");
        }
        this.entityManager = entityManager;
    }

    public void setAppDao(IApplicationDao appDao) {
        if (this.appDao != null) {
            throw new IllegalStateException("Value is managed by ejb container");
        }
        this.appDao = appDao;
    }

    protected abstract ITemplateManager getTemplateManager();

    protected AbstractTemplate getTemplate(GridContext context) {
        GridType type = context.getGridType();
        try {
            AbstractTemplate template = getTemplateManager().initTemplate(type);
            if (template != null) {
                template.setModifiers(Defs.coalesce(context.getModifiers().getItems(), Collections.EMPTY_SET));
                template.setParentFilters(context.getParentFilters());
                return template;
            } else {
                throw new IllegalArgumentException("Unknown type browser: " + type.getCode());
            }
        } catch (Exception ex) {
            throw new EJBException("Template initializze error", ex);
        }
    }

    @Override
    public IColumns getMeta(GridContext context) {
        DefaultTemplateBuilder builder = getTemplateBuilder(context);
        builder.setTemplateState(TemplateStateMode.META);
        return builder.getMeta();
    }

    @Override
    public PageList<? extends MarkModel> getDataForBrowser(GridContext context, PageFilterInfo pageFilterInfo) throws FilterRequireException {
        DefaultTemplateBuilder builder = getTemplateBuilder(context);
        builder.setTemplateState(TemplateStateMode.DATA);
        return builder.getDataForBrowser(pageFilterInfo);
    }

    @Override
    public List<BigDecimal> getOnlyIdsForBrowser(GridContext context, PageFilterInfo pageFilterInfo) {
        return getOnlyIdsForBrowser(context, pageFilterInfo, null);
    }

    @Override
    public List<BigDecimal> getOnlyIdsForBrowser(GridContext context, PageFilterInfo pageFilterInfo, String idColumn) {
        DefaultTemplateBuilder b = getTemplateBuilder(context);
        b.setTemplateState(TemplateStateMode.ONLY_IDS);
        if (idColumn == null) {
            ColumnInfo keyCol = b.getColumnsInfo().getKeyColumn();
            if (keyCol != null) {
                idColumn = keyCol.getAlias();
            } else {
                throw new EJBException("Can't get ids for browser " + context.getGridType().getCode() + ": idColumn is not defined and key column is not found");
            }
        }
        return b.getOnlyIdsForBrowser(pageFilterInfo, idColumn);
    }

    @Override
    public MarkModel getRow(GridContext context, PageFilterInfo pageFilterInfo, BigDecimal recordUQ) {
        DefaultTemplateBuilder builder = getTemplateBuilder(context);
        builder.setTemplateState(TemplateStateMode.ROW);
        return builder.getRow(pageFilterInfo, recordUQ);
    }

    @Override
    public List<? extends MarkModel> getRows(GridContext context, PageFilterInfo pageFilterInfo, List<BigDecimal> recordUQs) {
        DefaultTemplateBuilder builder = getTemplateBuilder(context);
        builder.setTemplateState(TemplateStateMode.ROWS);
        return builder.getRows(pageFilterInfo, recordUQs);
    }

    @Override
    public Map<String, ?> getAggregates(GridContext context, PageFilterInfo filterInfo, List<BigDecimal> ids, List<IAggregateDef> defs) {
        DefaultTemplateBuilder builder = getTemplateBuilder(context);
        builder.setTemplateState(TemplateStateMode.AGGR);
        return builder.getAggregates(filterInfo, ids, defs);
    }

    @Override
    public List<LookupInfoModel> lookup(GridContext context, PageFilterInfo filterInfo, List<BigDecimal> recordUQ) {
        DefaultTemplateBuilder builder = getTemplateBuilder(context);
        builder.setTemplateState(TemplateStateMode.LOOKUP);
        return builder.lookup(filterInfo, recordUQ);
    }
        
    @Override
    public List<CustomReportFilterInfo> getReportFilters(String gridCode, GridTypeEnum gridTypeEnum) {
        GridType gridType;
        switch (gridTypeEnum) {
            case NORM:
                gridType = () -> gridCode;
                break;
            case DYN:
                gridType = new DynamicGridType((ObjectType) () -> gridCode, null, "");
                break;
            default:
            throw new IllegalArgumentException("Unknown grid type: " + gridTypeEnum.name());    
        }
        return getReportFilters(gridType);
    }
    
    private List<CustomReportFilterInfo> getReportFilters(GridType type) {
        try {
            AbstractTemplate template = getTemplateManager().initTemplate(type);
            if (template != null) {
                return template.getReportFilters();
            } else {
                throw new IllegalArgumentException("Unknown type browser: " + type.getCode());
            }
        } catch (Exception ex) {
            throw new EJBException("Template initializze error", ex);
        }
    }

    protected class BaseTemplateContext implements ITemplateContext {

        @Override
        public EntityManager getEntityManager() {
            return entityManager;
        }

        @Override
        public SessionContext getSessionContext() {
            return sessionContext;
        }

        @Override
        public IJdbcWorkExecutor getJdbcWorkExecutor() {
            return jdbcWorkExecutor;
        }

    }

}
