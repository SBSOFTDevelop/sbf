package ru.sbsoft.meta.context;

import ru.sbsoft.common.ServerConfig;
import java.util.Date;
import javax.persistence.TemporalType;
import ru.sbsoft.common.jdbc.QueryParam;
import ru.sbsoft.common.jdbc.QueryParamImpl;
import ru.sbsoft.dao.IApplicationDao;
import ru.sbsoft.shared.filter.FilterConsts;

/**
 * Класс представляет глобальный контекст (окружение) приложения. Добавляет
 * параметры
 * <code>БCURRENT_USERNAME_PARAMETER, CURRENT_APP_CODE_PARAMETER</code>
 * <p>
 * используемые в дальнейшем парсером построителя запросов
 * {@link ru.sbsoft.dao.AbstractBuilder}
 * <p>
 * для подстановки значений в предикаты оператора SQL <code>where</code>. Методы
 * {@link #getCurrentAppCode()}, {@link #getCurrentUsername()} переопределяются
 * в классе наследнике для возврата конкретных значений (код приложения, имя
 * сеансового пользователя)
 *
 * @author rfa
 */
public class GlobalQueryContext extends AbstractQueryContext {

    public static final String CURRENT_USERNAME_PARAMETER = "CURRENT_USERNAME_PARAMETER";
    public static final String CURRENT_APP_CODE_PARAMETER = "CURRENT_APP_CODE_PARAMETER";
    public static final String CURRENT_IS_ADMIN_PARAMETER = "CURRENT_IS_ADMIN_PARAMETER";
    public static final String JMS_PARAMETER = "JMS_PARAMETER";

    private final String username;
    private final IApplicationDao appDao;

    private final Boolean isAdmin;

    public GlobalQueryContext(String username, boolean isAdmin, IApplicationDao appDao) {
        this.username = username;
        this.appDao = appDao;
        this.isAdmin = isAdmin;
    }

    @Override
    public QueryParam getParam(String name) {
        if (CURRENT_IS_ADMIN_PARAMETER.equals(name)) {
            return new QueryParamImpl(isAdmin);
        }

        if (CURRENT_USERNAME_PARAMETER.equals(name)) {
            return new QueryParamImpl(getCurrentUsername());
        }

        if (JMS_PARAMETER.equals(name)) {
            if (appDao != null) {
                if (appDao.getAppCode() != null) {
                    return new QueryParamImpl(ServerConfig.getServerPrefix() + appDao.getAppCode() + ru.sbsoft.common.jdbc.Const.JMS_OPER_PREF);
                } else {
                    throw new IllegalStateException("Application code is null in application dao");
                }
            } else {
                throw new IllegalStateException("Application dao is not set in global sql template context");
            }

        }

        if (CURRENT_APP_CODE_PARAMETER.equals(name)) {
            if (appDao != null) {
                if (appDao.getAppCode() != null) {
                    return new QueryParamImpl(ServerConfig.getServerPrefix() + appDao.getAppCode());
                } else {
                    throw new IllegalStateException("Application code is null in application dao");
                }
            } else {
                throw new IllegalStateException("Application dao is not set in global sql template context");
            }
        }
        if (FilterConsts.CURRENT_DATE_FIELD.equals(name)) {
            return new QueryParamImpl(getCurrentDate(), TemporalType.DATE);
        }
        if (FilterConsts.USER_FIELD.equals(name)) {
            return new QueryParamImpl(getCurrentUsername());
        }
        return super.getParam(name);
    }

    protected String getCurrentUsername() {
        return username;
    }

    protected Date getCurrentDate() {
        return new Date();
    }
}
