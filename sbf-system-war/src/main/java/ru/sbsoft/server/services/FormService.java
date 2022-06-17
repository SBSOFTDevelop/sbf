package ru.sbsoft.server.services;

import java.math.BigDecimal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.servlet.annotation.WebServlet;
import ru.sbsoft.dao.IFormDao;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FormContext;
import ru.sbsoft.shared.exceptions.OptimisticLockException;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.services.IFormService;
import ru.sbsoft.shared.services.ServiceConst;

@WebServlet(urlPatterns = {ServiceConst.FORM_SERVICE_LONG})
public class FormService extends SBFRemoteServiceServlet implements IFormService {

    @EJB
    private IFormDao formDao;

    @Override
    public IFormModel getRecord(FormContext type, BigDecimal Id, List<FilterInfo> parentFilters) {
        return formDao.getRecord(type, Id, parentFilters);
    }    

    @Override
    public IFormModel putRecord(FormContext type, IFormModel model, final List<FilterInfo> parentFilters) throws OptimisticLockException {
        try {
            return formDao.putRecord(type, model, parentFilters);
        } catch (RuntimeException ex) {
            throw checkOptimisticException(ex);
        }
    }

    @Override
    public BigDecimal delRecord(FormContext type, BigDecimal id) throws OptimisticLockException {
        try {
            formDao.delRecord(type, id);
            return id;
        } catch (RuntimeException ex) {
            throw checkOptimisticException(ex);
        }
    }

    @Override
    public IFormModel newRecord(FormContext type, final List<FilterInfo> parentFilters, final BigDecimal clonableRecordID) {
        return formDao.newRecord(type, parentFilters, clonableRecordID);
    }

    private static RuntimeException checkOptimisticException(RuntimeException ex) throws OptimisticLockException {
        if (ex instanceof EJBException) {
            if (ex.getCause() instanceof javax.persistence.OptimisticLockException) {
                throw new OptimisticLockException(ex.getCause().getMessage());
            }
        }

        if (ex instanceof javax.ejb.EJBTransactionRolledbackException) {
            final Throwable cause1 = ex.getCause();
            if ((cause1 instanceof javax.ejb.TransactionRolledbackLocalException)) {
                final Throwable cause2 = cause1.getCause();
                if ((cause2 instanceof javax.persistence.OptimisticLockException)) {
                    throw new OptimisticLockException(cause2.getMessage());
                }
            }
        }
        return ex;
    }

}
