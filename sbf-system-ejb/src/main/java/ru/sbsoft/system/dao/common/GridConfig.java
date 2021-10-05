package ru.sbsoft.system.dao.common;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import ru.sbsoft.common.Strings;
import ru.sbsoft.dao.IConfigDao;
import ru.sbsoft.dao.ISecurityDao;
import ru.sbsoft.session.SessionUtils;
import ru.sbsoft.shared.FiltersBean;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.meta.config.ColumnCfg;
import ru.sbsoft.shared.meta.filter.FilterBox;
import ru.sbsoft.shared.meta.filter.IStoredFilterList;
import ru.sbsoft.shared.meta.filter.StoredFilterPath;
import ru.sbsoft.shared.model.user.Group;
import ru.sbsoft.system.cfg.DeleteFilterConfigCommand;
import ru.sbsoft.system.cfg.GetDefaultFilterCommand;
import ru.sbsoft.system.cfg.IConfigCommandContext;
import ru.sbsoft.system.cfg.LoadColumnConfigCommand;
import ru.sbsoft.system.cfg.LoadFilterConfigCommand;
import ru.sbsoft.system.cfg.LoadStoredFilterCommand;
import ru.sbsoft.system.cfg.SaveColumnConfigCommand;
import ru.sbsoft.system.cfg.SaveFilterConfigCommand;
import ru.sbsoft.system.cfg.SetDefaultFilterCommand;

/**
 * Сервис сохранениея насроек браузеров (колонок, фильтров)
 *
 * @author balandin
 * @since Mar 2, 2015 5:44:44 PM
 */
@Stateless
@Remote(IConfigDao.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@PermitAll
public class GridConfig implements IConfigDao {

    @Resource
    private SessionContext sessionContext;
    @PersistenceContext(unitName = ru.sbsoft.common.jdbc.Const.DEFAULT_PERSISTENCE_CTX)
    private EntityManager entityManager;
    @EJB
    private ISecurityDao securityDao;

    private final IConfigCommandContext cmdCtx;

    public GridConfig() {
        this.cmdCtx = new CommandContext();
    }

    public void setSessionContext(SessionContext sessionContext) {
        if (this.sessionContext == null) {
            this.sessionContext = sessionContext;
        } else {
            throw new IllegalStateException("SessionContext is already set.");
        }
    }

    public void setEntityManager(EntityManager entityManager) {
        if (this.entityManager == null) {
            this.entityManager = entityManager;
        } else {
            throw new IllegalStateException("EntityManager is already set.");
        }
    }

    public void setSecurityDao(ISecurityDao securityDao) {
        if (this.securityDao == null) {
            this.securityDao = securityDao;
        } else {
            throw new IllegalStateException("SecurityDao is already set.");
        }
    }

    @Override
    public void saveConfiguration(String appPrefix, GridContext context, List<ColumnCfg> columns) {
        new SaveColumnConfigCommand(cmdCtx, appPrefix, context).exec(columns);
    }

    @Override
    public List<ColumnCfg> loadConfiguration(String appPrefix, GridContext context) {
        return new LoadColumnConfigCommand(cmdCtx, appPrefix, context).exec();
    }

    @Override
    public FiltersBean getFilter(String appPrefix, GridContext context, StoredFilterPath filterPath) {
        return new LoadFilterConfigCommand(cmdCtx, appPrefix, context).exec(filterPath);
    }

    @Override
    public void saveFilter(String appPrefix, GridContext context, FiltersBean filters, StoredFilterPath filterPath) {
        new SaveFilterConfigCommand(cmdCtx, appPrefix, context).exec(filters, filterPath);
    }

    @Override
    public IStoredFilterList getStoredFilterList(String appPrefix, GridContext context) {
        return new LoadStoredFilterCommand(cmdCtx, appPrefix, context).exec();
    }

    @Override
    public Map<StoredFilterPath, Exception> deleteFilters(String appPrefix, GridContext context, Collection<StoredFilterPath> filterPaths) {
        return new DeleteFilterConfigCommand(cmdCtx, appPrefix, context).exec(filterPaths);
    }

    @Override
    public FilterBox getCurrentFilter(String appPrefix, GridContext context) {
        return new GetDefaultFilterCommand(cmdCtx, appPrefix, context).exec();
    }

    @Override
    public void setCurrentFilter(String appPrefix, GridContext context, StoredFilterPath filterPath) {
        new SetDefaultFilterCommand(cmdCtx, appPrefix, context).exec(filterPath);
    }

    private class CommandContext implements IConfigCommandContext {

        @Override
        public EntityManager getEm() {
            return entityManager;
        }

        @Override
        public String getUserName() {
            final String user = Strings.clean(SessionUtils.getCurrentUserName(sessionContext));
            if (user == null) {
                throw new IllegalStateException("Session not authorized");
            }
            return user;
        }

        @Override
        public Set<Group> getUserGroups() {
            return securityDao.getUserGroups();
        }

    }
}
