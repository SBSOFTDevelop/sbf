package ru.sbsoft.client.appmenu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.IBrowserMaker;
import ru.sbsoft.client.components.form.IParamFormFactory;
import ru.sbsoft.client.components.operation.IOperationMaker;
import ru.sbsoft.client.components.operation.OperationMaker;
import ru.sbsoft.client.components.operation.SimpleOperationMaker;
import ru.sbsoft.shared.MenuTypeEnum;
import ru.sbsoft.shared.interfaces.OperationType;
import ru.sbsoft.shared.model.MenuItemModel;
import ru.sbsoft.client.components.treemenu.IDynamicMenuLoader;

/**
 * Класс <code>MenuItem</code> представляет подпункт меню. <br>
 * Может представлять папку, браузер или операцию. Фактически является
 * конструктором модели {@link MenuItemModel}
 *
 * @author Kiselev
 */
public class MenuItem implements IMenuItemProvider {

    private final MenuItemModelEx model;

    private static long ID_GEN = 0;

    static BigDecimal getNextId() {
        return BigDecimal.valueOf(++ID_GEN);
    }

    /**
     * Создает подраздел меню (папку).
     *
     * @param folderName имя папки
     */
    public MenuItem(String folderName) {
        this(MenuTypeEnum.Folder, folderName);
    }

    public MenuItem(String folderName, IDynamicMenuLoader l) {
        this(MenuTypeEnum.DynamicFolder, folderName);
        model.setLoader(l);
    }

    private MenuItem(MenuTypeEnum menuType, String name) {
        model = fillBase(new MenuItemModelEx(), menuType, name);
    }

    /**
     * Создает пункт меню типа браузер.
     *
     * @param b фабрика браузера
     */
    public MenuItem(IBrowserMaker b) {
        this(MenuTypeEnum.Browser, b.getTitleBrowser());
        model.setSecurityID(b.getSecurityId());
        model.setBrowserMaker(b);
    }

    MenuItem(OperationType opt) {
        this(new SimpleOperationMaker(opt));
    }

    MenuItem(OperationType opt, IParamFormFactory param) {
        this(new SimpleOperationMaker(opt, param));
    }

    /**
     * Создает пункт меню типа операция.
     *
     * @param op фабрика операции
     */
    public MenuItem(OperationMaker op) {
        this(op, I18n.get(op.getType().getTitle()), op.getType().getSecurityRole());
    }

    /**
     * Создает пункт меню типа операция.
     *
     * @param op фабрика операции
     * @param name наименование операции для отображения пользователю
     */
    public MenuItem(IOperationMaker op, String name) {
        this(op, name, op.getOperationCode());
    }

    private MenuItem(IOperationMaker op, String name, String securityId) {
        this(MenuTypeEnum.Operation, name);
        model.setOperationMaker(op);
        model.setSecurityID(securityId);
    }

    private static <M extends MenuItemModel> M fillBase(M model, MenuTypeEnum menuType, String name) {
        model.setID(getNextId());
        model.setMenuType(menuType);
        model.setMenuName(name);
        model.setDescription(name);
        return model;
    }

    /**
     * Добавляет дочерний пункт меню типа операция
     *
     * @param opt идентификатор операции
     * @return this
     */
    public MenuItem add(OperationType opt) {
        return add(new MenuItem(opt));
    }

    /**
     * Добавляет дочерний пункт меню типа операция
     *
     * @param opt идентификатор операции
     * @param param фабрика диалога параметров операции
     * @return this
     */
    public MenuItem add(OperationType opt, IParamFormFactory param) {
        return add(new MenuItem(opt, param));
    }

    /**
     * Добавляет дочерний пункт меню типа операция
     *
     * @param oper фабрика операции
     * @return this
     */
    public MenuItem add(OperationMaker oper) {
        return add(new MenuItem(oper));
    }

    /**
     * Добавляет дочерний пункт меню типа браузер
     *
     * @param browser фабрика браузера
     * @return this
     */
    public MenuItem add(IBrowserMaker browser) {
        return add(new MenuItem(browser));
    }

    private void setParentID(BigDecimal parentId) {
        model.setParentID(parentId);
    }

    /**
     * Добавляет дочерний пункт меню
     *
     * @param child
     * @return this
     */
    public MenuItem add(MenuItem child) {
        addChild(child);
        return this;
    }

    private void addChild(MenuItem child) {
        child.setParentID(model.getID());
        List<MenuItemModel> children = model.getChildren();
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child.getMenuItemModel());
        model.setChildren(children);
    }

    /**
     * @return сконструированную модель пункта меню
     */
    @Override
    public MenuItemModel getMenuItemModel() {
        return model;
    }

}
