package ru.sbsoft.dao;

import ru.sbsoft.shared.ApplicationInfo;

/**
 * @author balandin
 * @since Apr 11, 2013 4:39:42 PM
 */
public interface IServiceDao {

    String getCurrentUserName();

    ApplicationInfo getApplicationInfo();
    
    void saveApplicationInfo(ApplicationInfo appInfo);

    boolean isUserRole(String name);
}
