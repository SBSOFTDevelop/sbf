package ru.sbsoft.dao.entity;

import java.util.Date;

public interface UpdatableEntity {

    public void setUpdateUser(String updateUser);

    public void setUpdateDate(Date updateDate);
}
