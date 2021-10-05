package ru.sbsoft.session;

import java.security.Principal;
import javax.ejb.SessionContext;
import ru.sbsoft.common.Strings;

/**
 *
 * @author balandin
 */
public class SessionUtils {

    /**
     * Статический метод класса возращает имя пользователя из текущего контекста сессии.
     * 
     * @param sessionContext контекст сессии
     * @return имя пользователя сессионого контекста или пустая строка.
     */
    public static String getCurrentUserName(final SessionContext sessionContext) {
        if (sessionContext == null) {
            return Strings.EMPTY;
        }
        final Principal p = sessionContext.getCallerPrincipal();
        if (p == null) {
            return Strings.EMPTY;
        } 
        return Strings.coalesce(p.getName());
    }
}
