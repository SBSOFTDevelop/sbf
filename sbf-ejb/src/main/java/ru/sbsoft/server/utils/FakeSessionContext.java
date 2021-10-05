package ru.sbsoft.server.utils;

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
 * Реализация SessionContext только для передачи имени пользователя.
 *
 */
public class FakeSessionContext implements SessionContext {

    private final Principal principal;

    public FakeSessionContext(final String userName) {
        this.principal = () -> userName;
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
}
