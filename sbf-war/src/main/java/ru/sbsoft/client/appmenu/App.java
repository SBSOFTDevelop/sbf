package ru.sbsoft.client.appmenu;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.IBrowserMaker;
import ru.sbsoft.client.components.form.IParamFormFactory;
import ru.sbsoft.client.components.operation.OperationMaker;
import ru.sbsoft.shared.interfaces.NamedItem;
import ru.sbsoft.shared.interfaces.OperationType;
import ru.sbsoft.shared.model.ApplicationMenuTreeModel;
import ru.sbsoft.shared.model.MenuItemModel;

/**
 * Класс <code>App</code> представляет самостоятельный раздел в структуре главного меню, т.н. приложения. Содержит методы для
 * добавления подпунктов меню приложения. Фактически является конструктором модели {@link ApplicationMenuTreeModel}
 *
 * @author Kiselev
 */
public class App implements IMainMenuSectionProvider {

    private final ApplicationMenuTreeModel model = new ApplicationMenuTreeModel();

    /**
     * В качестве кода приложения используется заданный {@code appName}
     * @param appName имя приложения для отображения пользователю
     */
    public App(String appName) {
        this(appName, appName);
    }

    /**
     * Код приложения определяется как 
     * @param appItem идентификатор, определяющий код и имя приложения
     */
    public App(NamedItem appItem) {
        this(appItem.getCode(), I18n.get(appItem.getItemName()));
    }

    /**
     * @param appCode код приложения
     * @param appTitle имя приложения для отображения пользователю
     */
    public App(String appCode, String appTitle) {
        model.setAPPLICATION_CODE(appCode);
        model.setAPPLICATION_TITLE(appTitle);
    }

    /**
     * Добавляет пункт меню типа операция
     *
     * @param opt идентификатор операции
     * @return this
     */
    public App add(OperationType opt) {
        return add(new MenuItem(opt));
    }

    /**
     * Добавляет пункт меню типа операция
     *
     * @param opt идентификатор операции
     * @param param фабрика диалога параметров операции
     * @return this
     */
    public App add(OperationType opt, IParamFormFactory param) {
        return add(new MenuItem(opt, param));
    }

    /**
     * Добавляет пункт меню типа операция
     *
     * @param oper фабрика операции
     * @return this
     */
    public App add(OperationMaker oper) {
        return add(new MenuItem(oper));
    }

    /**
     * Добавляет пункт меню типа браузер
     *
     * @param browser фабрика браузера
     * @return this
     */
    public App add(IBrowserMaker browser) {
        return add(new MenuItem(browser));
    }
    
    /**
     * Добавляет пункт меню
     *
     * @param item подпункт
     * @return this
     */
    public App add(IMenuItemProvider item) {
        return add(item.getMenuItemModel());
    }

    private App add(MenuItemModel item) {
        item.setParentID(null);
        model.getRootMenuList().add(item);
        return this;
    }

    /**
     * @return Сконструированная модель раздела главного меню
     */
    @Override
    public ApplicationMenuTreeModel getMenuSectionModel() {
        return model;
    }

}
