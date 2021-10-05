package ru.sbsoft.shared.model.operation;

public class NoSuchOperationCodeException extends OperationException {

    public NoSuchOperationCodeException() {
    }

    public NoSuchOperationCodeException(String message) {
        super(message);
    }

    public NoSuchOperationCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchOperationCodeException(Throwable cause) {
        super(cause);
    }

}
