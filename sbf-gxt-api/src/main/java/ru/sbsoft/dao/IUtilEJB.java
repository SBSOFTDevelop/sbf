package ru.sbsoft.dao;

import javax.ejb.Local;

/**
 *
 * @author sychugin
 */
@Local//Only!!!
//@Remote
public interface IUtilEJB<T extends Runnable, E extends Exception> {

    public static interface RunnableThrow<E extends Exception> {
        void run() throws E;
    }

    public void runInNewTransaction(T runner);

    public void runInTransaction(T runner);

    public void runNotInTransaction(T runner);

    public void runInTransactionAsync(T runner);

    public  <T extends RunnableThrow<E>> void runInNewTransactionE(T runner);

}
