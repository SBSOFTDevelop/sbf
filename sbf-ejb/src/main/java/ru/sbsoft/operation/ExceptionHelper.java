package ru.sbsoft.operation;

/**
 *
 * @author panarin
 */
public class ExceptionHelper {


	public static String getExceptionMessage(final Throwable exception) {
		if (null == exception.getCause()) {
			if (null == exception.getMessage()) {
				return exception.getClass().getCanonicalName();
			} else {
				return exception.getLocalizedMessage();//  getMessage();
			}
		} else {
			return getExceptionMessage(exception.getCause());
		}
	}
}
