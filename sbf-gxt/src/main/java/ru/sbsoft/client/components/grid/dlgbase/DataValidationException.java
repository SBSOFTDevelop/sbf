package ru.sbsoft.client.components.grid.dlgbase;

/**
 *
 * @author kiselev
 */
public class DataValidationException extends Exception {

    public DataValidationException() {
    }

    public DataValidationException(String message) {
        super(message);
    }

    public DataValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataValidationException(Throwable cause) {
        super(cause);
    }

}
