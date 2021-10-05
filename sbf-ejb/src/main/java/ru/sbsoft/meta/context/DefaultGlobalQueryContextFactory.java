package ru.sbsoft.meta.context;

import ru.sbsoft.dao.IApplicationDao;

/**
 *
 * @author Kiselev
 */
public class DefaultGlobalQueryContextFactory implements IGlobalQueryContextFactory {

    private final IApplicationDao appDao;

    public DefaultGlobalQueryContextFactory(IApplicationDao appDao) {
        this.appDao = appDao;
    }

    @Override
    public GlobalQueryContext createGlobalQueryContext(String userName, boolean isAdmin) {
        return new GlobalQueryContext(userName, isAdmin, appDao);
    }
}
