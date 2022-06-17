package ru.sbsoft.client.appmenu;

import ru.sbsoft.client.components.IBrowserMaker;
import ru.sbsoft.client.components.operation.IOperationMaker;
import ru.sbsoft.shared.model.MenuItemModel;
import ru.sbsoft.client.components.treemenu.IDynamicMenuLoader;

/**
 * Расширенная модель пункта меню. Передназначена для использования на клиенте и
 * предполагает непосредственное связывание браузера или операции с пунктом
 * меню.
 *
 * @author Kiselev
 */
public class MenuItemModelEx extends MenuItemModel {

    private IBrowserMaker browserMaker = null;
    private IOperationMaker operationMaker = null;
    private IDynamicMenuLoader loader = null;

    public MenuItemModelEx() {
    }

    public static MenuItemModelEx shallowCopy(MenuItemModel model) {
        MenuItemModelEx res = new MenuItemModelEx();
        res.setMenuName(model.getMenuName());
        res.setID(model.getID());
        res.setDescription(model.getDescription());
        res.setMenuType(model.getMenuType());
        res.setMenuOrder(model.getMenuOrder());
        res.setParentID(model.getParentID());
        res.setChildren(model.getChildren());
        res.setSecurityID(model.getSecurityID());
        if (model instanceof MenuItemModelEx) {
            MenuItemModelEx ex = (MenuItemModelEx) model;
            res.setBrowserMaker(ex.getBrowserMaker());
            res.setOperationMaker(ex.getOperationMaker());
            res.setLoader(ex.getLoader());
        }
        return res;
    }

    /**
     * @return фабрика браузера
     * @see #setBrowserMaker(ru.sbsoft.client.components.IBrowserMaker)
     */
    public IBrowserMaker getBrowserMaker() {
        return browserMaker;
    }

    /**
     * Устанавливает браузер, связанный с пунктом меню.
     * @param browserMaker фабрика браузера
     */
    public void setBrowserMaker(IBrowserMaker browserMaker) {
        this.browserMaker = browserMaker;
    }

    /**
     * @return фабрика операции
     * @see #setOperationMaker(ru.sbsoft.client.components.operation.IOperationMaker)
     */
    public IOperationMaker getOperationMaker() {
        return operationMaker;
    }

    /**
     * Устанавливает операцию, связанную с пунктом меню.
     * @param operationMaker фабрика операции
     */
    public void setOperationMaker(IOperationMaker operationMaker) {
        this.operationMaker = operationMaker;
    }

    public IDynamicMenuLoader getLoader() {
        return loader;
    }

    public void setLoader(IDynamicMenuLoader loader) {
        this.loader = loader;
    }
    
    @Override
    public String getBrowserID() {
        if (browserMaker == null) {
            throw new IllegalStateException("Browser maker is not set");
        }
        return browserMaker.getIdBrowser();
    }

    @Override
    public String getOperationID() {
        if (operationMaker == null) {
            throw new IllegalStateException("Operation maker is not set");
        }
        return operationMaker.getOperationCode();
    }

    /**
     * Не поддерживается.
     * @throws UnsupportedOperationException
     * @param browserID
     */
    @Override
    public void setBrowserID(String browserID) {
        throw new UnsupportedOperationException("Browser maker id is used");
    }

    /**
     * Не поддерживается.
     * @throws UnsupportedOperationException
     * @param operationID
     */
    @Override
    public void setOperationID(String operationID) {
        throw new UnsupportedOperationException("Operation maker id is used");
    }
}
