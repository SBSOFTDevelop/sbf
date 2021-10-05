package ru.sbsoft.dao;

import java.util.Collection;
import java.util.Set;
import ru.sbsoft.shared.model.user.Group;
import ru.sbsoft.shared.model.user.PasswordPolicy;
import ru.sbsoft.shared.model.user.UserEnv;

/**
 *
 * @author panarin
 */
public interface ISecurityDao {

    Set<String> getUserRoles();

    Set<String> getUserRoles(final String userName);
    
    boolean isAdmin(final String userName);
    
    Set<Group> getUserGroups();

    PasswordPolicy getUserPasswordPolicy();

    UserEnv getUserEnv();

    void setPassword(final String newPass);

    String hashPassword(final String newPass);

    boolean isUserRole(String name);

    public void updatePassword(final String oldPass, final String newPass);
}
