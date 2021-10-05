package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;
import ru.sbsoft.shared.model.ApplicationMenuTreeModel;
import ru.sbsoft.shared.model.MenuItemModel;

/**
 * Сервис получения структуры главного меню приложения.
 * @deprecated используйте {@link ru.sbsoft.client.SBFEntryPoint#fillAppMenu(ru.sbsoft.client.appmenu.AppStruct)}
 */
@RemoteServiceRelativePath(ServiceConst.DB_STRUCT_SERVICE_SHORT)
public interface IDBStructService extends SBFRemoteService {

    public List<ApplicationMenuTreeModel> getApplicationList(List<String> appNames);

    public List<MenuItemModel> getChildsMenuItem(MenuItemModel folder);

}
