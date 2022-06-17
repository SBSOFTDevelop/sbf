package ru.sbsoft.client.utils;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.shared.ApplicationInfo;
import ru.sbsoft.shared.FormContext;
import ru.sbsoft.shared.model.BrowserSecurityType;
import ru.sbsoft.shared.interfaces.OperationType;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.interfaces.SecurityItem;
import ru.sbsoft.shared.model.SecurityHelper;
import ru.sbsoft.shared.model.user.Group;

import ru.sbsoft.shared.model.user.PasswordPolicy;
import ru.sbsoft.shared.model.user.UserEnv;

/**
 * Кэширует секурити окружение пользователя на клиенте и предоставляет
 * информационный сервис по конфигурации безопасности на их основе.
 *
 * @author panarin
 */
public class RoleCheker {

    private final static RoleCheker INSTANCE = new RoleCheker();

    private PasswordPolicy passwordPolicy;

    private String userName;

    private ApplicationInfo applicationInfo;

    public static RoleCheker getInstance() {
        return INSTANCE;
    }
    private Set<String> roles = null;
    private Set<Group> groups = null;

    public static void reload(AsyncCallback<UserEnv> callback) {
        SBFConst.SECURUTY_SERVICE.getUserEnv(new AsyncCallback<UserEnv>() {
            @Override
            public void onFailure(Throwable caught) {
                if (callback == null) {
                    ClientUtils.alertException(caught);
                } else {
                    callback.onFailure(caught);
                }
            }

            @Override
            public void onSuccess(UserEnv result) {
                RoleCheker.getInstance().init(result);
                if (callback != null) {
                    callback.onSuccess(result);
                }
            }
        });
    }
    
    private RoleCheker(){
    }

    private void init(UserEnv uenv) {
        init(uenv.getRoles(), uenv.getGroups());
        applicationInfo = uenv.getAppInf();
        userName = uenv.getUserName();
        passwordPolicy = uenv.getPpolicy();
    }

    private void init(Set<String> roles, Set<Group> groups) {
        this.roles = new HashSet<>(roles);
        this.roles.add(getBrowserSecurityCode(SecurityItem.PERMIT_ALL_READ_SECURITY_ID, BrowserSecurityType.VIEW));
        this.roles.add(getBrowserSecurityCode(SecurityItem.PERMIT_ALL_WRITE_SECURITY_ID, BrowserSecurityType.MODIFY));
        this.groups = Collections.unmodifiableSet(groups);
    }

    public String getUserName() {
        return userName;
    }

    public ApplicationInfo getApplicationInfo() {
        return applicationInfo;
    }

    public PasswordPolicy getPasswordPolicy() {
        if (passwordPolicy == null) {
            throw new IllegalStateException(I18n.get(SBFGeneralStr.msgPasswordPolicyStoreNotInit));
        }
        return passwordPolicy;
    }

    public boolean isInint() {
        return null != roles && null != groups;
    }

    public Set<Group> getGroups() {
        if (groups == null) {
            throw new IllegalStateException(I18n.get(SBFGeneralStr.msgRoleStoreNotInit));
        }
        return groups;
    }

    private Set<String> getRoles() {
        if (roles == null) {
            throw new IllegalStateException(I18n.get(SBFGeneralStr.msgRoleStoreNotInit));
        }
        return roles;
    }
    
    public boolean isAdmin(){
        return isInint() && isUserInRole("admin");
    }

    public boolean isUserInRole(final String role) {
        return getRoles().contains(role);
    }

    public boolean canUserModify(final String tableName) {
        return isTableReadOnly(tableName);
    }

    public boolean isTableReadOnly(final String tableName) {
        return !isUserInRole(getBrowserSecurityCode(tableName, BrowserSecurityType.MODIFY));
    }

    public boolean isTableDirectReadOnly(final String tableName) {
        return !isUserInRole(getBrowserSecurityCode(tableName, BrowserSecurityType.DIRECT_MODIFY));
    }

    public boolean isOperationGranted(final OperationType type) {
        return hasAnyOperationSecurity(type.getSecurityRole());
    }

    public boolean hasAnyBrowserSecurity(String browserCode) {
        for (BrowserSecurityType t : BrowserSecurityType.values()) {
            if (isUserInRole(getBrowserSecurityCode(browserCode, t))) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAnyOperationSecurity(String operationCode) {
        return isUserInRole(getOperationSecurityCode(operationCode));
    }

    public boolean hasAnyAppSecurity(String applicationCode) {
        return isUserInRole(getAppSecurityCode(applicationCode));
    }

    public static boolean isModifiableContext(FormContext formContext) {
        return RoleCheker.getInstance().isUserInRole(getBrowserSecurityCode(formContext.getFormType().getRights(), BrowserSecurityType.MODIFY));
    }

    private static String getBrowserSecurityCode(String browserCode, BrowserSecurityType securityType) {
        return SecurityHelper.getBrowserSecurityCode(browserCode, securityType);
    }

    private static String getOperationSecurityCode(String operationCode) {
        return SecurityHelper.getOperationSecurityCode(operationCode);
    }

    private static String getAppSecurityCode(String applicationCode) {
        return SecurityHelper.getAppSecurityCode(applicationCode);
    }
}
