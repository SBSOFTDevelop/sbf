package ru.sbsoft.shared.exceptions;

import java.io.Serializable;

/**
 *
 * @author Fedor Resnyanskiy
 */
public class OptimisticLockException extends Exception implements Serializable{

    public OptimisticLockException() {
    }

    public OptimisticLockException(String message) {
        super(message);
    }

    public OptimisticLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public OptimisticLockException(Throwable cause) {
        super(cause);
    }

}
