package ru.sbsoft.common;

import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Пареметры сервера приложений
 *
 * ru.sbsoft.schedulers.isolate_multioperation_prefix - префикс севера для
 * разделения мультиопераций по серверам в случае работы с одной СУБД по
 * умолчанию ="" ru.sbsoft.schedulers.startdelay - задержка для старта
 * планировщика мультиопераций по умолчанию =1000
 *
 * @author sychugin
 */
public class ServerConfig {

    private static final SystemProperty ISOLATE_MULTIOPERATION_PREF = new SystemProperty("ru.sbsoft.schedulers.isolate_multioperation_prefix", "");
    private static final SystemProperty START_SCHEDULER_DELAY = new SystemProperty("ru.sbsoft.schedulers.startdelay", "1");
    private static final SystemProperty MAIL_FULL_JNDI_NAME = new SystemProperty("ru.sbsoft.mail.full_jndi-name", null);

    public static String getServerPrefix() {

        return ISOLATE_MULTIOPERATION_PREF.getParameterValue();

    }

    public static long getStartSchedulerDelay() {

        return START_SCHEDULER_DELAY.getParameterLongValue() * 1000;
    }

    public static javax.mail.Session getDefaultMailSession() throws ResourceNotFoundException {
        String parId = MAIL_FULL_JNDI_NAME.getParameterIdentifier();
        String jndi_name = MAIL_FULL_JNDI_NAME.getParameterValue();
        if (jndi_name == null) {
            throw new ResourceNotFoundException("Default mail session not found. Parameter '" + parId + "' is not defined for this environment. "
                    + "Administractor can do it, as examlpe, by application server config.");
        }
        Object lookupedObj;
        try {
            lookupedObj = InitialContext.doLookup(jndi_name);
        } catch (NamingException ex) {
            throw new ResourceNotFoundException("Default mail session not found. Lookup exception for '" + parId + "' is occured.", ex);
        }
        if (lookupedObj instanceof javax.mail.Session) {
            return (javax.mail.Session) lookupedObj;
        } else {
            throw new ResourceNotFoundException("Default mail session not found. Lookuped for '" + parId + "' value is not javax.mail.Session, but " + (lookupedObj == null ? "NULL" : lookupedObj.getClass().getName()));
        }
    }

}
