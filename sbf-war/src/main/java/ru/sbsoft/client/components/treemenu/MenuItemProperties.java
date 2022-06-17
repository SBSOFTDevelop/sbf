package ru.sbsoft.client.components.treemenu;

import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.svc.data.shared.ModelKeyProvider;
import ru.sbsoft.shared.model.MenuItemModel;

/**
 * Фабрика стандартных объектов GWT для доступа к свойствам {@link MenuItemModel}.
 * Используется в {@link ApplicationContentPanel}.
 * @author panarin
 */
class MenuItemProperties {

    public ModelKeyProvider<MenuItemModel> key() {
        return new ModelKeyProvider<MenuItemModel>() {

            @Override
            public String getKey(MenuItemModel item) {
                return (MenuItemModel.isFolder(item) ? "f-" : "m-") + item.getID().toString();
            }
        };
    }

    public ValueProvider<MenuItemModel, MenuItemModel> menuObject() {
        return new ValueProvider<MenuItemModel, MenuItemModel>() {

            @Override
            public MenuItemModel getValue(MenuItemModel object) {
                return object;
            }

            @Override
            public void setValue(MenuItemModel object, MenuItemModel value) {
            }

            @Override
            public String getPath() {
                return "name";
            }
        };
    }
}
