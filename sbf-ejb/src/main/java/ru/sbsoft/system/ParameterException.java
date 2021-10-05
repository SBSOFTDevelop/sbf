package ru.sbsoft.system;

/**
 * Класс исключения, возникающего при преобразовании значения параметров из String в Integer
 * экземпляром {@link ru.sbsoft.system.Parameters}.
 * <p>По существу экземпляр этого класса подменяет стандартное исключение java {@link java.lang.NumberFormatException} в методе
 * strToInt.
 * <pre>
 * try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw new ParameterException("Unexpected integer value " + value);
        }
 * </pre>
 * @author balandin
 * @since Mar 21, 2013 12:53:12 PM
 */
public class ParameterException extends Exception {

    public ParameterException() {
    }

    public ParameterException(String message) {
        super(message);
    }

    public ParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParameterException(Throwable cause) {
        super(cause);
    }
}
