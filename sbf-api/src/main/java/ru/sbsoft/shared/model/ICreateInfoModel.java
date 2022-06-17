package ru.sbsoft.shared.model;

import java.util.Date;

/**
 *
 * @author vk
 */
public interface ICreateInfoModel extends ICreateInfo {

    void setCreateDate(Date createDate);

    void setCreateUser(String createUser);
}
