package ru.sbsoft.dao.entity;

import java.io.Serializable;
import java.util.Date;
import javax.ejb.SessionContext;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import ru.sbsoft.session.SessionUtils;
import ru.sbsoft.shared.model.IUpdateInfo;

/**
 * Базовый класс предназначен для заполнения технологических полей:
 *
 * UPDATE_DATE, UPDATE_USER,
 *
 * @author Sokoloff
 */
@Embeddable
public class UpdateInfoFields implements IUpdateInfo, Serializable {

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "update_date", nullable = false)
    private Date updateDate = null;
    @Column(name = "update_user", nullable = false)
    private String updateUser = null;

    @Override
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public void fillFrom(final SessionContext scontext) {
        updateUser = SessionUtils.getCurrentUserName(scontext);
        updateDate = new Date();
    }
    
    public void fillFrom(final String runUser) {
        updateUser = runUser;
        updateDate = new Date();
    }
    
}
