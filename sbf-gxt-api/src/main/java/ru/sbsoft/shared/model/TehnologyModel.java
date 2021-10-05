package ru.sbsoft.shared.model;

import ru.sbsoft.sbf.app.model.FormModel;
import java.util.Date;

public class TehnologyModel extends FormModel implements ICreateInfoModel, IUpdateInfoModel {

    private Date createDate;
    private String createUser;
    private Date updateDate;
    private String updateUser;

    public TehnologyModel(TehnologyModel m) {
        super(m);
        if (m != null) {
            this.createDate = m.createDate;
            this.createUser = m.createUser;
            this.updateDate = m.updateDate;
            this.updateUser = m.updateUser;
        }
    }

    public TehnologyModel() {
    }

    @Override
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    @Override
    public Date getCreateDate() {
        return createDate;
    }

    @Override
    public String getCreateUser() {
        return createUser;
    }

    @Override
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    @Override
    public Date getUpdateDate() {
        return updateDate;
    }

    @Override
    public String getUpdateUser() {
        return updateUser;
    }
}
