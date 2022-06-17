package ru.sbsoft.dao;

import javax.ejb.Local;

/**
 *
 * @author sychugin
 */
@Local//Only!!!
//@Remote
public interface IUtilEJB<T extends Runnable, E extends Exception> {

    interface RunnableThrow<E extends Exception> {
        void run() throws E;
    }

    void runInNewTransaction(T runner);

    void runInTransaction(T runner);

    void runNotInTransaction(T runner);

    void runInTransactionAsync(T runner);

    <TT extends RunnableThrow<E>> void runInNewTransactionE(TT runner);

}
