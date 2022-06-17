package ru.sbsoft.shared.model.user;

import java.util.Set;
import ru.sbsoft.shared.ApplicationInfo;
import ru.sbsoft.shared.param.DTO;

/**
 *
 * @author sychugin
 */
public class UserEnv implements DTO {

    private Set<Group> groups;
    private Set<String> roles;
    private PasswordPolicy ppolicy;
    private String userName;
    private ApplicationInfo appInf;

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public PasswordPolicy getPpolicy() {
        return ppolicy;
    }

    public void setPpolicy(PasswordPolicy ppolicy) {
        this.ppolicy = ppolicy;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ApplicationInfo getAppInf() {
        return appInf;
    }

    public void setAppInf(ApplicationInfo appInf) {
        this.appInf = appInf;
    }

    
    
    
}
