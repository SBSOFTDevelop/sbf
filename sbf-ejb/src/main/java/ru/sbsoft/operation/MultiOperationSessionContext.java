package ru.sbsoft.operation;

import java.security.Identity;
import java.security.Principal;
import java.util.Map;
import java.util.Properties;
import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.SessionContext;
import javax.ejb.TimerService;
import javax.transaction.UserTransaction;
import javax.xml.rpc.handler.MessageContext;

/**
 * Реализация SessionContext для работы некоторых механизмов получения логина (checkTechnologyFields).
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
//TODO отвязаться от использования SessionContext для авторизации.
public class MultiOperationSessionContext implements SessionContext {

    private MultiOperationSessionContextPrincipal principal;

    @Deprecated
    public void setName(String name) {
        if (principal == null) {
            principal = new MultiOperationSessionContextPrincipal();
        }
        principal.setName(name);
    }

    /**
     * Не поддерживается
     * @return
     * @throws IllegalStateException 
     */
    @Override
    public EJBLocalObject getEJBLocalObject() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EJBObject getEJBObject() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MessageContext getMessageContext() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> T getBusinessObject(Class<T> businessInterface) throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Class getInvokedBusinessInterface() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean wasCancelCalled() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EJBHome getEJBHome() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EJBLocalHome getEJBLocalHome() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Properties getEnvironment() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Identity getCallerIdentity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Deprecated
    public Principal getCallerPrincipal() throws IllegalStateException {
        return principal;
    }

    @Override
    public boolean isCallerInRole(Identity role) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isCallerInRole(String roleName) throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserTransaction getUserTransaction() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRollbackOnly() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean getRollbackOnly() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TimerService getTimerService() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object lookup(String name) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, Object> getContextData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Deprecated
    public static class MultiOperationSessionContextPrincipal implements Principal {

        String name;

        @Override
        @Deprecated
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
