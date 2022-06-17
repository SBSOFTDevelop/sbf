package ru.sbsoft.dao;

import ru.sbsoft.shared.model.user.Group;
import ru.sbsoft.shared.model.user.PasswordPolicy;
import ru.sbsoft.shared.model.user.UserEnv;

import java.util.Set;

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

    void updatePassword(final String oldPass, final String newPass);
}
