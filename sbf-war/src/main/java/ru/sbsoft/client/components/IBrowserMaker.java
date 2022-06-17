package ru.sbsoft.client.components;

import ru.sbsoft.shared.interfaces.SecurityItem;

/**
 * Предназначен для создания браузеров через менеджер браузеров.
 * @see ru.sbsoft.client.components.browser.BaseBrowserManager
 * @author Sokoloff
 */
public interface IBrowserMaker extends SecurityItem {

    String getIdBrowser();
    
    String getTitleBrowser();

    ManagedBrowser createBrowser(final String idBrowser, final String titleBrowser);
}
