package ru.sbsoft.common;

/**
 *
 * @author balandin
 * @since Oct 15, 2013 7:36:07 PM
 */
public class ConfigException extends Exception {

	public ConfigException() {
	}

	public ConfigException(String message) {
		super(message);
	}

	public ConfigException(Throwable cause) {
		super(cause);
	}

	public ConfigException(String message, Throwable cause) {
		super(message, cause);
	}
}
