package ru.sbsoft.server.services;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbsoft.common.Strings;
import ru.sbsoft.dao.errors.ISqlErrors;
import ru.sbsoft.model.SQLRetCode;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.exceptions.ApplicationException;
import ru.sbsoft.shared.exceptions.ThrowableWrapper;
import ru.sbsoft.shared.param.DTO;
import ru.sbsoft.shared.services.SBFRemoteService;

/**
 * @author balandin
 * @since May 17, 2013 6:44:58 PM
 */
public class SBFRemoteServiceServlet extends RemoteServiceServlet implements SBFRemoteService {

    protected static final String APPLICATION_PREFIX_PARAM_NAME = "APPLICATION_PREFIX";
    protected static final String LAST_ERROR = "LAST_ERROR";
    //
    protected static final Logger LOGGER = LoggerFactory.getLogger(SBFRemoteServiceServlet.class);

    /**
     * Однобуквенный префикс приложения задается в web.xml
     *
     *
     *
     * <web-app>
     * ...
     * <context-param>
     * <param-name>APPLICATION_PREFIX</param-name>
     * <param-value>W</param-value>
     * </context-param>
     * ...
     * </web-app>
     *
     */
    private String appPrefix;
    static final String LOGGED_USER_ATTR = "loged_user";
    
    @EJB
    private ISqlErrors sqlErrors;

    public SBFRemoteServiceServlet() {
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
        HttpSession session = req.getSession(false);
        if (req.getUserPrincipal() != null && session != null && session.getAttribute(LOGGED_USER_ATTR) == null) {
            session.setAttribute(LOGGED_USER_ATTR, req.getUserPrincipal().getName());
        }

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // не работает
        // appPrefix = Strings.clean(config.getInitParameter(APPLICATION_PREFIX_PARAM_NAME), false);
        //
        // так работает
        appPrefix = Strings.clean(config.getServletContext().getInitParameter(APPLICATION_PREFIX_PARAM_NAME), false);
    }

    @Override
    public List<DTO> dummy(List<DTO> dto) {
        throw new UnsupportedOperationException("Method for type declaration only");
    }

    @Override
    protected void doUnexpectedFailure(Throwable e) {
        try {
            final SQLException sqlException = extractSqlException(e);
            if (sqlException != null) {
                //Заменим исключение более читабельным текстом
                final ApplicationException replacedSqlException = replaceSqlException(sqlException);
                if (replacedSqlException != null) {
                    e = replacedSqlException;
                    LOGGER.warn("SQLException detected ", e);
                } else {
                    LOGGER.warn("SQLException detected ", e);
                }
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot replace Throwable " + ex.toString());
        }
        perThreadRequest.get().getSession().setAttribute(LAST_ERROR, new ThrowableWrapper(e));
    }

    protected String getAppPrefix() {
        return appPrefix;
    }

    protected String getCurrentModuleCode() {
        return perThreadRequest.get().getContextPath().replaceAll("^/", "");
    }

    private ApplicationException replaceSqlException(final SQLException e) throws SQLException {

        ILocalizedString newErrorMessage = sqlErrors.getMessage(new SQLRetCode(e.getSQLState(), e.getErrorCode()), e.getMessage());
        LOGGER.warn("SQLException detected (SQLSTATE:" + e.getSQLState() + " ERRORCODE:" + e.getErrorCode() + ") " + e.getMessage());

        if (newErrorMessage == null) {
            return null;
        }
        return new ApplicationException(newErrorMessage);
    }

    private SQLException extractSqlException(Throwable e) {
        do {
            if (e == null || (e instanceof SQLException)) {
                return (SQLException) e;
            }
            e = e.getCause();
        } while (true);
    }
}
