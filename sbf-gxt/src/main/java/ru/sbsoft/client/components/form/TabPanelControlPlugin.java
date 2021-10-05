package ru.sbsoft.client.components.form;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.common.Strings;

/**
 * Создает в панели инструментов формы выпадающий список ее вкладок, который позволяет переключаться на выбранную вкладку.
 * @author balandin
 * @since Jul 4, 2013 1:11:48 PM
 */
public class TabPanelControlPlugin {

    private final static String WIDGET_MARK_KEY = "w";
    private final TabPanel mainTab;
    private final TextButton menuButton;
    private final Menu scrollMenu;

    public static TabPanelControlPlugin bind(TabPanel mainTab, FormToolBar toolBar) {
        return new TabPanelControlPlugin(mainTab, toolBar);
    }

    public TabPanelControlPlugin(final TabPanel mainTab, final ToolBar toolbar) {
        super();

        this.mainTab = mainTab;

        menuButton = new TextButton(Strings.EMPTY, SBFResources.APP_ICONS.Tabs());
        scrollMenu = new Menu();
        // scrollMenu.setMaxHeight(300);

        for (int i = 0; i < mainTab.getWidgetCount(); i++) {
            final TabItemConfig config = mainTab.getConfig(mainTab.getWidget(i));
            final MenuItem menuItem = new MenuItem(config.getText());
            menuItem.setData(WIDGET_MARK_KEY, mainTab.getWidget(i));
            scrollMenu.add(menuItem);
        }
        menuButton.setMenu(scrollMenu);

        scrollMenu.addSelectionHandler(new SelectionHandler<Item>() {
            @Override
            public void onSelection(SelectionEvent<Item> event) {
                final Widget w = (Widget) event.getSelectedItem().getData(WIDGET_MARK_KEY);
                mainTab.scrollToTab(w, false);
                mainTab.setActiveWidget(w);
            }
        });

        mainTab.addSelectionHandler(new SelectionHandler<Widget>() {
            @Override
            public void onSelection(SelectionEvent<Widget> event) {
                selectMenuItem(event.getSelectedItem());
            }
        });

        if (mainTab.getWidgetCount() > 0) {
            selectMenuItem(mainTab.getWidget(0));
        }

        if (toolbar != null) {
            toolbar.add(new SeparatorToolItem());
            toolbar.add(menuButton);
        }
    }

    private void selectMenuItem(Widget widget) {
        for (int i = 0; i < scrollMenu.getWidgetCount(); i++) {
            final MenuItem menuItem = (MenuItem) scrollMenu.getWidget(i);
            final boolean selected = menuItem.getData(WIDGET_MARK_KEY) == widget;
            menuItem.setIcon(selected ? SBFResources.APP_ICONS.TabRight() : null);
            menuItem.getElement().getStyle().setFontWeight(selected ? Style.FontWeight.BOLD : Style.FontWeight.NORMAL);
        }
        menuButton.setText(mainTab.getConfig(widget).getText());
    }

    public TextButton getMenuButton() {
        return menuButton;
    }
}
