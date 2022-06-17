package ru.sbsoft.client.components.exception;

/**
 * Исключение, возникающее при регистрации, создании или работе браузера.
 *
 * @author Sokoloff
 */
public class BrowserException extends Exception {

    public BrowserException(Throwable cause) {
        super(cause);
    }

    public BrowserException(String message, Throwable cause) {
        super(message, cause);
    }

    public BrowserException(String message) {
        super(message);
    }

    public BrowserException(String message, Object[] params) {
        super(makeMessage(message, params));
    }

    public BrowserException(String message, Object[] params, Throwable cause) {
        super(makeMessage(message, params), cause);
    }

    private static String makeMessage(String message, Object[] params) {
        if (null != params) {
            for (Object obj : params) {
                message = message.replaceFirst("&", obj.toString());
            }
        }
        return message;
    }

    public BrowserException() {
    }
}
