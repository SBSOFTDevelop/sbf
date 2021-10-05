package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import ru.sbsoft.shared.model.ApplicationMenuTreeModel;
import ru.sbsoft.shared.model.MenuItemModel;

/**
 * @see IDBStructService
 * @deprecated используйте {@link ru.sbsoft.client.SBFEntryPoint#fillAppMenu(ru.sbsoft.client.appmenu.AppStruct)}
 */
public interface IDBStructServiceAsync extends ISBFServiceAsync {

    void getApplicationList(List<String> appNames, AsyncCallback<List<ApplicationMenuTreeModel>> callback);

    void getChildsMenuItem(MenuItemModel folder, AsyncCallback<List<MenuItemModel>> callback);
}
