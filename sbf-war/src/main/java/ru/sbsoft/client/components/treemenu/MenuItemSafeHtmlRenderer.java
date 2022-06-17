package ru.sbsoft.client.components.treemenu;

import ru.sbsoft.shared.model.MenuItemModel;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;

/**
 * Формирует строку для отображения пунктов меню.
 * Используется в {@link ApplicationContentPanel} при создании дерева меню.
 * @author Sokoloff
 */
class MenuItemSafeHtmlRenderer implements SafeHtmlRenderer<MenuItemModel> {

    private static MenuItemSafeHtmlRenderer instance;

    public static MenuItemSafeHtmlRenderer getInstance() {
        if (instance == null) {
            instance = new MenuItemSafeHtmlRenderer();
        }
        return instance;
    }

    private MenuItemSafeHtmlRenderer() {
    }

    @Override
    public SafeHtml render(MenuItemModel object) {
        return (object == null) ? SafeHtmlUtils.EMPTY_SAFE_HTML : SafeHtmlUtils.fromString(object.getMenuName());
    }

    @Override
    public void render(MenuItemModel object, SafeHtmlBuilder appendable) {
        appendable.append(SafeHtmlUtils.fromString(object.getMenuName()));
    }
}
