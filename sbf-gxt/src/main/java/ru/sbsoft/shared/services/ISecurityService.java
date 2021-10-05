package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.Set;
import ru.sbsoft.shared.model.user.Group;
import ru.sbsoft.shared.model.user.PasswordPolicy;
import ru.sbsoft.shared.model.user.UserEnv;

/**
 * Сервисы безопасности.
 */
@RemoteServiceRelativePath(ServiceConst.SEC_SERVICE_SHORT)
public interface ISecurityService extends SBFRemoteService {

    /**
     * Прекращает сеанс пользователя.
     */
    public void logout();

    /**
     * @return список ролей текущего пользователя
     */
    public Set<String> getUserRoles();

    /**
     * @return список групп текущего пользователя
     */
    public Set<Group> getUserGroups();

    public UserEnv getUserEnv();

    public PasswordPolicy getPassworPolicy();

    public void setNewPassword(final String newPass);

    public void changePassword(final String oldPass, final String newPass);
}
