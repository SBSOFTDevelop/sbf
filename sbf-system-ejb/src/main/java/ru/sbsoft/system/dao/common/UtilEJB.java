package ru.sbsoft.system.dao.common;

import javax.annotation.security.PermitAll;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import ru.sbsoft.dao.IUtilEJB;

/**
 *
 * @author sychugin
 */
@Stateless
//@Remote(IUtilEJB.class)
//@LocalBean
@PermitAll
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class UtilEJB<T extends Runnable, E extends Exception> implements IUtilEJB<T, E> {

    @Override
    public void runInNewTransaction(T runner) {
        runner.run();

    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public void runInTransaction(T runner) {
        runner.run();
    }

    @Asynchronous
    @Override
    public void runInTransactionAsync(T runner) {
        runInNewTransaction(runner);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public void runNotInTransaction(T runner) {
        runner.run();
    }

    private <T extends RunnableThrow<E>> void runInNewTransaction(T runner) throws E {
        runner.run();
    }

    @Override
    public <T extends RunnableThrow<E>> void runInNewTransactionE(T runner) {

        try {
            runInNewTransaction(() -> runner.run());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

}
