package ru.sbsoft.shared.model;

import java.util.Date;

/**
 *
 * @author vk
 */
public interface IUpdateInfoModel extends IUpdateInfo {

    void setUpdateDate(Date updateDate);

    void setUpdateUser(String updateUser);
 }
