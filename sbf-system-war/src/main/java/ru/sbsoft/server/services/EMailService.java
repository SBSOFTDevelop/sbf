package ru.sbsoft.server.services;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.servlet.annotation.WebServlet;
import ru.sbsoft.dao.IEMailDao;
import ru.sbsoft.shared.EMailMessage;
import ru.sbsoft.shared.EMailResult;
import ru.sbsoft.shared.services.IEMailService;
import ru.sbsoft.shared.services.ServiceConst;

/**
 *
 * @author Sokoloff
 */
@WebServlet(urlPatterns = {ServiceConst.EMAIL_SERVICE_LONG})
public class EMailService extends SBFRemoteServiceServlet implements IEMailService {

    @EJB
    private IEMailDao mailDao;

    public EMailService() {
    }

    @Override
    public EMailResult sendMessage(final String errLocale, EMailMessage message) {
        try {
            return mailDao.sendMessage(errLocale, message);
        } catch (EJBException ex) {
            return new EMailResult(EMailResult.ResultEnum.ERROR, "Initialization error EMail service on the application server.");
        }
    }

}
