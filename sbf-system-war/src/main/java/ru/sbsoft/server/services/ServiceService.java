package ru.sbsoft.server.services;

import ru.sbsoft.server.services.SBFRemoteServiceServlet;
import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import ru.sbsoft.dao.IServiceDao;
import ru.sbsoft.shared.ApplicationInfo;
import ru.sbsoft.shared.exceptions.ThrowableWrapper;
import ru.sbsoft.shared.services.IServiceService;
import ru.sbsoft.shared.services.ServiceConst;

@WebServlet(urlPatterns = {ServiceConst.SERVICE_SERVICE_LONG})
public class ServiceService extends SBFRemoteServiceServlet implements IServiceService {

    @EJB
    private IServiceDao serviceDao;

    @Override
    public ThrowableWrapper getLastError() {
        final ThrowableWrapper throwableWrapper = (ThrowableWrapper) perThreadRequest.get().getSession().getAttribute(LAST_ERROR);
        perThreadRequest.get().getSession().setAttribute(LAST_ERROR, null);
        return throwableWrapper;
    }

    @Override
    public String getCurrentUserName() {
        return serviceDao.getCurrentUserName();
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        return serviceDao.getApplicationInfo();
    }
}
