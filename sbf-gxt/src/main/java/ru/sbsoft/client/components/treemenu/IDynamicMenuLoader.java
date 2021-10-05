package ru.sbsoft.client.components.treemenu;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import ru.sbsoft.shared.model.MenuItemModel;

/**
 *
 * @author sergo
 */
public interface IDynamicMenuLoader {

    void loadChildren(MenuItemModel folder, AsyncCallback<List<MenuItemModel>> callback);
}
