package ru.sbsoft.shared.exceptions;

import java.io.Serializable;

/**
 * @author balandin
 * @since Nov 24, 2015
 */
public class FilterRequireException extends Exception implements Serializable {

    public FilterRequireException() {
    }

    public FilterRequireException(String message) {
        super(message);
    }
}
