package ru.sbsoft.client.appmenu;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.math.BigDecimal;
import java.util.List;
import ru.sbsoft.shared.model.ApplicationMenuTreeModel;
import ru.sbsoft.shared.model.MenuItemModel;

/**
 * Главное меню приложения.
 *
 * @see ru.sbsoft.client.appmenu.AppStruct
 *
 * @author Kiselev
 */
public interface IAppStruct {

    /**
     * Возвращает список разделов (корневых пунктов меню), доступных
     * пользователю
     *
     * @param applications Список кодов разделов, которые требуется отобразить.
     * Если null - выдаются все доступные пользователю разделы приложения.
     * @return Список разделов
     */
    List<ApplicationMenuTreeModel> getApplicationList(final List<String> applications);

    /**
     * Возвращает доступные пользователю пункты меню.
     *
     * @param parentMenu родительское меню
     * @param callback объект, принимающий писок пунктов меню (возможно загруженных)
     */
    void loadChildren(MenuItemModel parentMenu, AsyncCallback<List<MenuItemModel>> callback);
    
    /**
     * @return true - если меню не содержит ни одного приложения (корневого пункта)
     */
    boolean isEmpty();
}
