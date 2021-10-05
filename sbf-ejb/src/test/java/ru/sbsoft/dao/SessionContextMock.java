package ru.sbsoft.dao;

import java.security.Identity;
import java.security.Principal;
import java.util.HashMap;
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
 *
 * @author imd
 */
public class SessionContextMock implements SessionContext {

    private final Principal principal;
    private final Map<String, Object> contextData;

    public SessionContextMock(final String userName) {
        this.principal = new PrincipalImpl(userName);
        this.contextData = new HashMap<String, Object>();
    }

    @Override
    public EJBLocalObject getEJBLocalObject() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EJBObject getEJBObject() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MessageContext getMessageContext() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T getBusinessObject(Class<T> arg0) throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Class getInvokedBusinessInterface() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean wasCancelCalled() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EJBHome getEJBHome() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EJBLocalHome getEJBLocalHome() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Properties getEnvironment() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Deprecated
    public Identity getCallerIdentity() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Principal getCallerPrincipal() {
        return principal;
    }

    @Override
    public boolean isCallerInRole(Identity role) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isCallerInRole(String roleName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public UserTransaction getUserTransaction() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRollbackOnly() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean getRollbackOnly() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TimerService getTimerService() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object lookup(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, Object> getContextData() {
        return contextData;
    }

    private static class PrincipalImpl implements Principal {

        private final String user;

        public PrincipalImpl(String user) {
            this.user = user;
        }

        @Override
        public String getName() {
            return user;
        }
    }
}
