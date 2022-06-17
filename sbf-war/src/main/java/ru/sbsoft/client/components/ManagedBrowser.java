package ru.sbsoft.client.components;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Минимально необходимый функционал браузера для использования его в библиотеке.
 * Обычно для создания браузеров используется {@link ru.sbsoft.client.components.browser.BaseBrowser} как реализация по умолчанию.
 * @see IBrowserMaker
 * @see ru.sbsoft.client.components.browser.BaseBrowserManager#addInstance(ru.sbsoft.client.components.IBrowserMaker) 
 */
public interface ManagedBrowser extends IsWidget {

    void setIdBrowser(String value);

    String getIdBrowser();

    String getShortName();

    void setShortName(String value);

    String getCaption();

    void setCaption(String value);
}
