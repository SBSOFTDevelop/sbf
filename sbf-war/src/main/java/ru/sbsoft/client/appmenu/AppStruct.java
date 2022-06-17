package ru.sbsoft.client.appmenu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.client.utils.RoleCheker;
import ru.sbsoft.shared.MenuTypeEnum;
import ru.sbsoft.shared.model.ApplicationMenuTreeModel;
import ru.sbsoft.shared.model.MenuItemModel;
import ru.sbsoft.client.components.treemenu.IDynamicMenuLoader;

/**
 * Реализация-контейнер главного меню приложения.
 *
 * @author Kiselev
 */
public class AppStruct implements IAppStruct {

    private final RoleCheker roleChecker = RoleCheker.getInstance();

    private final List<ApplicationMenuTreeModel> appList = new ArrayList<>();
    private final Map<BigDecimal, MenuItemModel> index = new HashMap<>();

    private boolean useAppRoles = false;

    /**
     * Переключает режим видимости разделов главного меню для пользователя. По
     * умолчанию специальные роли разделов не используются (false).
     *
     * @param useAppRoles {@code true} - раздел включается в меню, если у
     * текущего пользователя имеется роль вида APP.код_приложения , где
     * код_приложения - это
     * {@link ApplicationMenuTreeModel#getAPPLICATION_CODE()}, который задается
     * в конструкторах {@link App};
     * {@code false} - раздел включается в меню, если пользователь имеет права
     * на хотя бы один из его подменю
     */
    public void setUseAppRoles(boolean useAppRoles) {
        this.useAppRoles = useAppRoles;
    }

    /**
     * Добавляет новый раздел главного меню.
     *
     * @param app поставщик раздела
     */
    public void addApp(IMainMenuSectionProvider app) {
        ApplicationMenuTreeModel m = app.getMenuSectionModel();
        appList.add(m);
        index(m.getRootMenuList());
    }

    private void index(List<MenuItemModel> models) {
        if (models != null && !models.isEmpty()) {
            for (MenuItemModel m : models) {
                index.put(m.getID(), m);
                index(m.getChildren());
            }
        }
    }

    @Override
    public boolean isEmpty() {
        return appList.isEmpty();
    }

    @Override
    public List<ApplicationMenuTreeModel> getApplicationList(final List<String> applications) {
        List<ApplicationMenuTreeModel> res = new ArrayList<>();
        for (ApplicationMenuTreeModel m : appList) {
            String appCode = m.getAPPLICATION_CODE();
            if ((applications == null || isIn(appCode, applications)) && isAppGranted(m)) {
                ApplicationMenuTreeModel mCopy = new ApplicationMenuTreeModel();
                mCopy.setAPPLICATION_CODE(m.getAPPLICATION_CODE());
                mCopy.setAPPLICATION_TITLE(m.getAPPLICATION_TITLE());
                List<MenuItemModel> rootMenu = new ArrayList<>();
                for (MenuItemModel rm : m.getRootMenuList()) {
                    if (isMenuShow(rm)) {
                        rootMenu.add(rm);
                    }
                }
                mCopy.setRootMenuList(rootMenu);
                res.add(mCopy);
            }
        }
        return res;
    }

    @Override
    public void loadChildren(final MenuItemModel parentMenu, AsyncCallback<List<MenuItemModel>> callback) {
        MenuItemModel m = parentMenu.getMenuType() == MenuTypeEnum.DynamicFolder ? parentMenu : index.get(parentMenu.getID());
        if (m == null) {
            callback.onSuccess(Collections.emptyList());
        } else {
            final List<MenuItemModel> si = m.getChildren() != null ? m.getChildren().stream().filter(it -> isMenuShow(it)).collect(Collectors.toList()) : Collections.emptyList();
            final IDynamicMenuLoader ld = getDynamicLoader(m);
            if (ld == null || !si.isEmpty()) {
                callback.onSuccess(si);
            } else {
                ld.loadChildren(m, new AsyncCallback<List<MenuItemModel>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(List<MenuItemModel> result) {
                        List<MenuItemModel> l = new ArrayList<>(si);
                        l.addAll(getOk(parentMenu, result));
                        callback.onSuccess(l);
                    }

                    private List<MenuItemModel> getOk(final MenuItemModel parent, List<MenuItemModel> l) {
                        List<MenuItemModel> res = new ArrayList<>();
                        if (l != null) {
                            for (MenuItemModel mm : l) {
                                if (isMenuShow(mm)) {
                                    mm.setID(MenuItem.getNextId());
                                    mm.setParentID(parent.getID());
                                    mm.setChildren(getOk(mm, mm.getChildren()));
                                    res.add(mm);
                                }
                            }
                        }
                        return !res.isEmpty() ? res : Collections.emptyList();
                    }
                });
            }
        }
    }

    private static IDynamicMenuLoader getDynamicLoader(MenuItemModel m) {
        return m.getMenuType() == MenuTypeEnum.DynamicFolder && m instanceof MenuItemModelEx ? ((MenuItemModelEx) m).getLoader() : null;
    }

    private boolean isAppGranted(ApplicationMenuTreeModel app) {
        return useAppRoles ? roleChecker.hasAnyAppSecurity(app.getAPPLICATION_CODE()) : hasShowItems(app.getRootMenuList());
    }

    private boolean hasShowItems(List<MenuItemModel> items) {
        if (items != null && !items.isEmpty()) {
            for (MenuItemModel m : items) {
                if (isMenuShow(m)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isMenuShow(MenuItemModel it) {
        try {
            switch (it.getMenuType()) {
                case DynamicFolder:
                    return true;
                case Root:
                case Folder:
                    return hasShowItems(it.getChildren());
                case Operation:
                    return roleChecker.hasAnyOperationSecurity(it.getSecurityID());
                case Browser:
                    return roleChecker.hasAnyBrowserSecurity(it.getSecurityID());
            }
        } catch (Throwable e) {
            String descr = "Menu \"" + it + "\" can't be shown";
            GWT.log(descr, e);
            ClientUtils.showError(descr + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    private boolean isIn(String code, List<String> codes) {
        for (String c : codes) {
            if (code.equals(c)) {
                return true;
            }
        }
        return false;
    }

}
