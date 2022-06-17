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
    void logout();

    /**
     * @return список ролей текущего пользователя
     */
    Set<String> getUserRoles();

    /**
     * @return список групп текущего пользователя
     */
    Set<Group> getUserGroups();

    UserEnv getUserEnv();

    PasswordPolicy getPassworPolicy();

    void setNewPassword(final String newPass);

    void changePassword(final String oldPass, final String newPass);
}
