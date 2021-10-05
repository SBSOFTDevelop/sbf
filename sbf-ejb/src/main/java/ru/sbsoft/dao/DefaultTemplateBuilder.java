package ru.sbsoft.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.SessionContext;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbsoft.common.Defs;
import ru.sbsoft.common.jdbc.QueryContext;
import ru.sbsoft.meta.ColumnsInfo;
import ru.sbsoft.meta.context.DefaultSQLBuilderFactory;
import ru.sbsoft.meta.context.FiltersContext;
import ru.sbsoft.meta.context.IGlobalQueryContextFactory;
import ru.sbsoft.meta.context.ISQLBuilderFactory;
import ru.sbsoft.meta.context.TemplateContext;
import ru.sbsoft.meta.context.VarContext;
import ru.sbsoft.meta.lookup.LookupValueProvider;
import ru.sbsoft.meta.sql.SQLBuilder;
import ru.sbsoft.model.PageFilterInfo;
import ru.sbsoft.session.SessionUtils;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FiltersBean;
import ru.sbsoft.shared.PageList;
import ru.sbsoft.shared.api.i18n.consts.SBFExceptionStr;
import ru.sbsoft.shared.consts.TemplateStateMode;
import ru.sbsoft.shared.exceptions.ApplicationException;
import ru.sbsoft.shared.exceptions.FilterRequireException;
import ru.sbsoft.shared.filter.StringFilterInfo;
import ru.sbsoft.shared.meta.Columns;
import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.shared.model.LookupInfoModel;

/**
 * Наследник (единственный) класса <code>AbstractBuilder</code> расширяет
 * базовый класс функционалом работы с шаблонами браузеров - классов наследников
 * {@link ru.sbsoft.dao.AbstractTemplate}.
 *
 * @author balandin
 * @since Jun 25, 2014 5:02:12 PM
 */
public class DefaultTemplateBuilder extends AbstractBuilder {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    protected final AbstractTemplate template;
    private LookupValueProvider lookupProvider;
    protected final ITemplateContext templateContext;
    private final IGlobalQueryContextFactory globalQueryContextFactory;
    private final ISQLBuilderFactory sqlBuilderFactory;

    public DefaultTemplateBuilder(AbstractTemplate template, ITemplateContext templateContext, IGlobalQueryContextFactory globalQueryContextFactory) {
        this(template, templateContext, globalQueryContextFactory, null);
    }

    public DefaultTemplateBuilder(AbstractTemplate template, ITemplateContext templateContext, IGlobalQueryContextFactory globalQueryContextFactory, ISQLBuilderFactory sqlBuilderFactory) {
        super(templateContext.getJdbcWorkExecutor());
        this.template = template;
        this.globalQueryContextFactory = globalQueryContextFactory;
        this.templateContext = templateContext;
        this.sqlBuilderFactory = sqlBuilderFactory != null ? sqlBuilderFactory : new DefaultSQLBuilderFactory(template);
    }

    /**
     * Возвращает экземпляр {@link SQLBuilder} c {@link DefaultSqlTemplateInfo},
     * ссылающимся на этот класс.
     *
     * @return конструктор по-умолчанию
     */
    @Override
    protected SQLBuilder newSQLBuilder() {
        return sqlBuilderFactory.createSQLBuilder();
    }

    @Override
    protected void checkFilters(FiltersBean filters) throws FilterRequireException {
        template.checkFilters(filters);
    }

    protected final QueryContext createContext(PageFilterInfo filterInfo) {

        final SessionContext sc = templateContext.getSessionContext();
        String userName = SessionUtils.getCurrentUserName(sc);

       
        return createContext(userName, sc.isCallerInRole("admin"), filterInfo);
    }

    @Override
    protected final QueryContext createContext(String username, boolean isAdmin, PageFilterInfo filterInfo) {
        final QueryContext globalContext = globalQueryContextFactory.createGlobalQueryContext(username, isAdmin);
        final QueryContext templateContext = new TemplateContext(template).setParent(globalContext);
        final QueryContext filtersContext = filterInfo != null ? new FiltersContext(filterInfo).setParent(templateContext) : templateContext;
        final QueryContext variableContext = new VarContext().setParent(filtersContext);
        return variableContext;
    }

    @Override
    protected ColumnsInfo createColumns() {
        // Do not use caches of ColumnsInfo and Columns from AbstractTemplate because of already cached
        return null;
    }

    @Override
    public Columns getColumns() {
        return getColumnsInfo().getColumns();
    }

    @Override
    public ColumnsInfo getColumnsInfo() {
        return template.getColumnsInfo();
    }

    @Override
    public Columns getMeta() {
        return MetaDataBuilder.getMeta(template);
    }

    @Override
    public PageList<Row> getDataForBrowser(PageFilterInfo filterInfo) throws FilterRequireException {
        if (log.isDebugEnabled()) {
            _logDebugInfo(filterInfo);
        }
        return super.getDataForBrowser(filterInfo);
    }
    
    public void setTemplateState(TemplateStateMode stateMode) {
        template.addMode(stateMode);
    }
    
    @Override
    protected boolean isSkipWhereOnGetRow() {
        return template.isSkipWhereOnGetRow();
    }
    
    protected LookupValueProvider getLookupProvider() {
        if (lookupProvider == null) {
            lookupProvider = template.createLookupProvider();

            if (lookupProvider == null) {
                final Columns c = getColumns();
                if (c.getColumnForAlias(LookupValueProvider.CODE_VALUE) != null && c.getColumnForAlias(LookupValueProvider.NAME_VALUE) != null) {
                    lookupProvider = LookupValueProvider.DEFAULT;
                }
            }

            if (lookupProvider == null) {
                throw new ApplicationException(SBFExceptionStr.notSetLookupProvider);
            }
        }

        return lookupProvider;
    }

    @Override
    protected FilterInfo convertLookupFilterItem(StringFilterInfo lookupInfo) {
        return getLookupProvider().convertLookupFilterItem(lookupInfo);
    }

    @Override
    public List<LookupInfoModel> lookup(PageFilterInfo filterInfo, List<BigDecimal> recordsUQs) {
        final List<Row> rows = getRows(filterInfo, recordsUQs);
        List<LookupInfoModel> res = new ArrayList<>();
        if (!rows.isEmpty()) {
            Columns columns = getColumns();
            LookupValueProvider pro = getLookupProvider();
            EntityManager em = templateContext.getEntityManager();
            for (Row r : rows) {
                r.setColumns(columns);
                res.add(pro.createLookupModel(r, em));
            }
        }
        return res;
    }

    private void _logDebugInfo(PageFilterInfo filterInfo) {
        log.debug("==== Browser" + template.getClass().getName() + "\n");
        log.debug("begin trace getDataForBrowser====\n"
                + "PageFilterInfo\n"
                + "colName=" + filterInfo.getColumnName() + "\n"
                + "Values=" + filterInfo.toString()
        );
        if (filterInfo.getFilters() != null) {
            log.debug("system filters:\n");
            for (FilterInfo fi : filterInfo.getFilters().getSystemFilters()) {
                _debugParentFilterInfo(fi);
            }
            log.debug("user filters:\n");
            for (FilterInfo fi : filterInfo.getFilters().getUserFilters().getChildFilters()) {
                _debugParentFilterInfo(fi);
            }
        }
        log.debug("ParentFilterInfo\n");
        for (FilterInfo fi : filterInfo.getParentFilters()) {
            _debugParentFilterInfo(fi);
        }
        log.debug("Temp filter\n");
        _debugParentFilterInfo(filterInfo.getTempFilter());
    }

    private void _debugParentFilterInfo(FilterInfo fi) {
        if (null != fi) {
            log.debug("colName=" + Defs.coalesce(fi.getColumnName(), "null")
                    + "\n SqlParName=" + null == fi.getSQLParameterName() ? "null" : Defs.coalesce(fi.getSQLParameterName(), "null")
                    + "\n Values=" + Defs.coalesce(fi.getValue(), "null")
            );
        } else {
            log.debug("FilterInfo is null");
        }
    }

}
