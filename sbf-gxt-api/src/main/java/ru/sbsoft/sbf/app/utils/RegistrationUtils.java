package ru.sbsoft.sbf.app.utils;

import java.util.List;
import ru.sbsoft.sbf.app.Registration;

/**
 *
 * @author Fedor Resnyanskiy
 */
public class RegistrationUtils {

    private RegistrationUtils() {
    }

    public static <T> Registration register(final List<T> handlerList, final T handler) {
        handlerList.add(handler);
        return new Registration() {
            @Override
            public void remove() {
                handlerList.remove(handler);
            }
        };
    }
}
