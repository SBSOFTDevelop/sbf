
package ru.sbsoft.dao.entity;

import java.util.Date;


public interface CreatableEntity {

    void setCreateUser(String createUser);

    void setCreateDate(Date createDate);
}
