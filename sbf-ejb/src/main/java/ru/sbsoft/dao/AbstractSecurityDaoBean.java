package ru.sbsoft.dao;

import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.exceptions.ApplicationException;
import ru.sbsoft.shared.model.user.Group;
import ru.sbsoft.shared.model.user.PasswordPolicy;
import ru.sbsoft.shared.model.user.UserEnv;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Базовый абстрактный класс для работы с ролями пользователей приложения.
 *
 * @author kiselev
 */
@PermitAll
public abstract class AbstractSecurityDaoBean implements ISecurityDao {

    @EJB
    private IServiceDao servDao;

    @Override
    public Set<String> getUserRoles() {

        return getUserRoles(null);
    }

    @Override
    public Set<String> getUserRoles(final String userName) {

        return getDSPrivileges(userName);

    }

    @Override
    public boolean isAdmin(final String userName) {

        return getDSPrivileges(userName).contains("admin");

    }

    @Override
    public UserEnv getUserEnv() {
        final UserEnv ue = new UserEnv();
        ue.setGroups(getUserGroups());
        ue.setRoles(getUserRoles());
        ue.setPpolicy(getUserPasswordPolicy());
        ue.setUserName(servDao.getCurrentUserName());
        ue.setAppInf(servDao.getApplicationInfo());
        return ue;
    }

    @Override
    public boolean isUserRole(String name) {
        return servDao.isUserRole(name);

    }

    @Override
    public Set<Group> getUserGroups() {

        return getDSGroups();
    }

    @Override
    public PasswordPolicy getUserPasswordPolicy() {
        final PasswordPolicy pp = new PasswordPolicy();

        Date d = getDSPasswordDateExpired();

        pp.setDateExpired(d);

        pp.setPolicy(getDSPasswordPolicy());
        pp.setExpired(d != null && pp.getDateExpired().before(new Date()));

        return pp;

    }

    @Override
    public String hashPassword(final String input) {
        if (input == null || "".equals(getHashAlgorithm()) || getHashAlgorithm() == null) {
            return input;
        }
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance(getHashAlgorithm());
            byte[] array = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AbstractSecurityDaoBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    @Override
    public void setPassword(final String newPass) {
        final String dbP = getDSPassword();
        final String hop = hashPassword(newPass);

        if (hop == null) {
            throw new ApplicationException(SBFGeneralStr.msgWrongPassword);
        }
        if (dbP != null && dbP.equals(hop)) {
            throw new ApplicationException(SBFGeneralStr.msgOldPasswordNotEqNewPassword);
        }

        setDSPassword(hop);

    }

    @Override
    public void updatePassword(final String oldPass, final String newPass) {

        final String dbP = getDSPassword();
        final String hop = hashPassword(oldPass);

        if (dbP != null && !dbP.equals(hop)) {
            throw new ApplicationException(SBFGeneralStr.msgWrongPassword);
        }

        setDSPassword(hashPassword(newPass));

    }

    protected abstract String getDSPassword();

    protected abstract int getDSPasswordPolicy();

    protected abstract void setDSPassword(final String pswd);

    protected abstract Date getDSPasswordDateExpired();

    protected abstract String getHashAlgorithm();

    protected abstract Set<String> getDSPrivileges(final String userName);

    protected abstract Set<Group> getDSGroups();

}
