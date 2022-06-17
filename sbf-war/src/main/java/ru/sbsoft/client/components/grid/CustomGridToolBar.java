package ru.sbsoft.client.components.grid;

import ru.sbsoft.client.components.browser.Browser;

/**
 * Базовый класс панелей инструментов таблиц приложения.
 * @author balandin
 * @since Oct 7, 2013 7:10:30 PM
 */
public abstract class CustomGridToolBar extends CustomToolBar {

    protected final Browser browser;

    public CustomGridToolBar(final Browser browser, boolean smallIcons) {
        super(browser.getActionManager(), smallIcons);
        this.browser = browser;
    }

    protected final BaseGrid getGrid() {
        return browser.getGrid();
    }
}
