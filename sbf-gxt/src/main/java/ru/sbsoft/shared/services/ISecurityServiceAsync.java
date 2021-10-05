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

    public void logout(AsyncCallback<Void> callback);

    public void getUserRoles(AsyncCallback<Set<String>> callback);

    public void getUserGroups(AsyncCallback<Set<Group>> callback);

    public void getPassworPolicy(AsyncCallback<PasswordPolicy> callback);

    public void setNewPassword(String newPass, AsyncCallback<Void> callback);

    public void changePassword(String oldPass, String newPass, AsyncCallback<Void> callback);

    public void getUserEnv(AsyncCallback<UserEnv> callback);

}
