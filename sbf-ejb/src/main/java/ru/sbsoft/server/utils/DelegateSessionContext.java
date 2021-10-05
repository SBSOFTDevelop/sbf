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
 *
 * @author vlki
 */
public class DelegateSessionContext implements SessionContext {

    private final SessionContext delegate;
    private final Principal principal;

    public DelegateSessionContext(SessionContext delegate, String userName) {
        this.delegate = delegate;
        if (userName != null && !(userName = userName.trim()).isEmpty()) {
            final String un = userName;
            this.principal = () -> un;
        } else {
            this.principal = null;
        }
    }

    @Override
    public EJBLocalObject getEJBLocalObject() throws IllegalStateException {
        return delegate.getEJBLocalObject();
    }

    @Override
    public EJBObject getEJBObject() throws IllegalStateException {
        return delegate.getEJBObject();
    }

    @Override
    public MessageContext getMessageContext() throws IllegalStateException {
        return delegate.getMessageContext();
    }

    @Override
    public <T> T getBusinessObject(Class<T> businessInterface) throws IllegalStateException {
        return delegate.getBusinessObject(businessInterface);
    }

    @Override
    public Class getInvokedBusinessInterface() throws IllegalStateException {
        return delegate.getInvokedBusinessInterface();
    }

    @Override
    public boolean wasCancelCalled() throws IllegalStateException {
        return delegate.wasCancelCalled();
    }

    @Override
    public EJBHome getEJBHome() throws IllegalStateException {
        return delegate.getEJBHome();
    }

    @Override
    public EJBLocalHome getEJBLocalHome() throws IllegalStateException {
        return delegate.getEJBLocalHome();
    }

    @SuppressWarnings("deprecation")
    @Override
    public Properties getEnvironment() {
        return delegate.getEnvironment();
    }

    @SuppressWarnings("deprecation")
    @Override
    public Identity getCallerIdentity() {
        return delegate.getCallerIdentity();
    }

    @Override
    public Principal getCallerPrincipal() throws IllegalStateException {
        return principal != null ? principal : delegate.getCallerPrincipal();
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isCallerInRole(Identity role) {
        return delegate.isCallerInRole(role);
    }

    @Override
    public boolean isCallerInRole(String roleName) throws IllegalStateException {
        return delegate.isCallerInRole(roleName);
    }

    @Override
    public UserTransaction getUserTransaction() throws IllegalStateException {
        return delegate.getUserTransaction();
    }

    @Override
    public void setRollbackOnly() throws IllegalStateException {
        delegate.setRollbackOnly();
    }

    @Override
    public boolean getRollbackOnly() throws IllegalStateException {
        return delegate.getRollbackOnly();
    }

    @Override
    public TimerService getTimerService() throws IllegalStateException {
        return delegate.getTimerService();
    }

    @Override
    public Object lookup(String name) throws IllegalArgumentException {
        return delegate.lookup(name);
    }

    @Override
    public Map<String, Object> getContextData() {
        return delegate.getContextData();
    }

}
