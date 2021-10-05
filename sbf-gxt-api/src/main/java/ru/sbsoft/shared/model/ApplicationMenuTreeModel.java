package ru.sbsoft.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Описывает структуру раздела главного меню приложения.
 */
public class ApplicationMenuTreeModel implements Serializable {

    private String APPLICATION_CODE;
    private String APPLICATION_TITLE;
    private List<MenuItemModel> rootMenuList;

    public ApplicationMenuTreeModel() {
    }

    public String getAPPLICATION_CODE() {
        return APPLICATION_CODE;
    }

    public void setAPPLICATION_CODE(String APPLICATION_CODE) {
        this.APPLICATION_CODE = APPLICATION_CODE;
    }

    public String getAPPLICATION_TITLE() {
        return APPLICATION_TITLE;
    }

    public void setAPPLICATION_TITLE(String APPLICATION_TITLE) {
        this.APPLICATION_TITLE = APPLICATION_TITLE;
    }

    public List<MenuItemModel> getRootMenuList() {
        if (null == rootMenuList) {
            rootMenuList = new ArrayList<MenuItemModel>();
        }
        return rootMenuList;
    }

    public void setRootMenuList(List<MenuItemModel> rootMenuList) {
        getRootMenuList().clear();
        getRootMenuList().addAll(rootMenuList);
    }
}
