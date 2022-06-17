package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import ru.sbsoft.shared.ApplicationInfo;
import ru.sbsoft.shared.exceptions.ThrowableWrapper;

/**
 * "Общие" сервисы приложения.
 */
@RemoteServiceRelativePath(ServiceConst.SERVICE_SERVICE_SHORT)
public interface IServiceService extends SBFRemoteService {

    /**
     * @return наиболее позднюю ошибку, возникшую при вызове какого-либо ссервиса в контексте пользователя
     */
    ThrowableWrapper getLastError();

    /**
     * @return имя текущего пользователя
     */
    String getCurrentUserName();
	
    /**
     * @return информация о запущенном приложении
     */
    ApplicationInfo getApplicationInfo();
    
    void saveApplicationInfo(ApplicationInfo appInfo);
    
}
