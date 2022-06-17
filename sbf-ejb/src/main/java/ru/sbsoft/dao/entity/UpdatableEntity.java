package ru.sbsoft.dao.entity;

import java.util.Date;

public interface UpdatableEntity {

    void setUpdateUser(String updateUser);

    void setUpdateDate(Date updateDate);
}
