package ru.sbsoft.server.services;

import java.util.Set;
import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import ru.sbsoft.dao.ISecurityDao;
import ru.sbsoft.shared.model.user.Group;
import ru.sbsoft.shared.model.user.PasswordPolicy;
import ru.sbsoft.shared.model.user.UserEnv;
import ru.sbsoft.shared.services.ISecurityService;
import ru.sbsoft.shared.services.ServiceConst;

@WebServlet(urlPatterns = {ServiceConst.SEC_SERVICE_LONG})
public class SecurityService extends SBFRemoteServiceServlet implements ISecurityService {

    @EJB
    private ISecurityDao securityDao;

    @Override
    public void logout() {
        final HttpSession session = getThreadLocalRequest().getSession(false);

        if (session != null) {
            session.invalidate();
        }
    }

    @Override
    public Set<String> getUserRoles() {
        return securityDao.getUserRoles();
    }

    @Override
    public Set<Group> getUserGroups() {
        return securityDao.getUserGroups();
    }

    @Override
    public PasswordPolicy getPassworPolicy() {
        return securityDao.getUserPasswordPolicy();
    }

    @Override
    public void setNewPassword(String newPass) {
        securityDao.setPassword(newPass);
    }

    public void changePassword(final String oldPass, final String newPass) {

        securityDao.updatePassword(oldPass, newPass);
    }

    
    
    @Override
    public UserEnv getUserEnv() {
        return securityDao.getUserEnv();
    }
}
