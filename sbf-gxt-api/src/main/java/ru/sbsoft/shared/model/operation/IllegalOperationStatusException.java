package ru.sbsoft.shared.model.operation;

public class IllegalOperationStatusException extends OperationException {

    public IllegalOperationStatusException() {
    }

    public IllegalOperationStatusException(String message) {
        super(message);
    }

    public IllegalOperationStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalOperationStatusException(Throwable cause) {
        super(cause);
    }

}
