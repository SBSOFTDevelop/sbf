package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.Set;
import ru.sbsoft.shared.model.user.Group;
import ru.sbsoft.shared.model.user.PasswordPolicy;
import ru.sbsoft.shared.model.user.UserEnv;

/**
 * @see ISecService
 */
public interface ISecurityServiceAsync extends ISBFServiceAsync {

    void logout(AsyncCallback<Void> callback);

    void getUserRoles(AsyncCallback<Set<String>> callback);

    void getUserGroups(AsyncCallback<Set<Group>> callback);

    void getPassworPolicy(AsyncCallback<PasswordPolicy> callback);

    void setNewPassword(String newPass, AsyncCallback<Void> callback);

    void changePassword(String oldPass, String newPass, AsyncCallback<Void> callback);

    void getUserEnv(AsyncCallback<UserEnv> callback);

}
