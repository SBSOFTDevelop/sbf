package ru.sbsoft.shared.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import ru.sbsoft.shared.MenuTypeEnum;

/**
 * Описывает структуру пунктов главного меню приложения.
 * <ul>
 * <li>ID - id пункта </li>
 * <li> parentID - id родительского узла (для посроения иерархии)</li>
 * <li> menuOrder - порядковый номер пункта для сортировки</li>
 * <li> menuName - название пункта</li>
 * <li> menuType - тип пункта {@link ru.sbsoft.shared.MenuTypeEnum}</li>
 * <li> browserID - id браузера</li>
 * <li> operationID -id операции</li>
 * <li> securityID -id доступа</li>
 * <li> children - список дочерних узлов</li>
 * <li> description -описание пункта</li>
 * </ul>
 *
 * @author Sokoloff
 */
public class MenuItemModel implements Serializable {

    private BigDecimal ID;
    private BigDecimal parentID;
    private BigDecimal menuOrder;
    private String menuName;
    private MenuTypeEnum menuType;
    private String browserID;
    private String operationID;
    private String securityID;
    private List<MenuItemModel> children;
    private String description;

    public MenuItemModel() {
    }

    public String getBrowserID() {
        return browserID;
    }

    public void setBrowserID(String browserID) {
        this.browserID = browserID;
    }

    public BigDecimal getID() {
        return ID;
    }

    public void setID(BigDecimal fieldUQ) {
        this.ID = fieldUQ;
    }

    public BigDecimal getParentID() {
        return parentID;
    }

    public void setParentID(BigDecimal fieldUQUP) {
        this.parentID = fieldUQUP;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public BigDecimal getMenuOrder() {
        return menuOrder;
    }

    public void setMenuOrder(BigDecimal menuOrder) {
        this.menuOrder = menuOrder;
    }

    public MenuTypeEnum getMenuType() {
        return menuType;
    }

    public void setMenuType(MenuTypeEnum menuType) {
        this.menuType = menuType;
    }

    public String getOperationID() {
        return operationID;
    }

    public void setOperationID(String operationID) {
        this.operationID = operationID;
    }

    public List<MenuItemModel> getChildren() {
        return children;
    }

    public void setChildren(final List<MenuItemModel> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return getMenuName() != null ? getMenuName() : super.toString();
    }

    public static boolean isFolder(MenuItemModel item) {
        return item != null && (MenuTypeEnum.Folder.equals(item.getMenuType())
                || MenuTypeEnum.DynamicFolder.equals(item.getMenuType()
                ));
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSecurityID() {
        return securityID;
    }

    public void setSecurityID(String securityID) {
        this.securityID = securityID;
    }
}
