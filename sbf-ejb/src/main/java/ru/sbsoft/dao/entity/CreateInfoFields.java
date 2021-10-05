package ru.sbsoft.dao.entity;

import java.io.Serializable;
import java.util.Date;
import javax.ejb.SessionContext;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import ru.sbsoft.session.SessionUtils;
import ru.sbsoft.shared.model.ICreateInfo;

/**
 * Базовый класс предназначен для заполнения технологических полей:
 *
 * CREATE_DATE, CREATE_USER,
 *
 * @author Sokoloff
 */
@Embeddable
public class CreateInfoFields implements ICreateInfo, Serializable {

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false)
    private Date createDate = null;
    @Column(name = "create_user", nullable = false)
    private String createUser = null;

    @Override
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public void fillFrom(final SessionContext scontext) {
        if (createUser == null || createUser.trim().isEmpty() || createDate == null) {
            createUser = SessionUtils.getCurrentUserName(scontext);
            createDate = new Date();
        }
    }
    
    public void fillFrom(final String runUser) {
        if (createUser == null || createUser.trim().isEmpty() || createDate == null) {
            createUser = runUser;
            createDate = new Date();
        }
    }
    
}
