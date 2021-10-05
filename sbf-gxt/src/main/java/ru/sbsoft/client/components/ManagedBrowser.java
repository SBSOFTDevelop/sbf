package ru.sbsoft.client.components;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Минимально необходимый функционал браузера для использования его в библиотеке.
 * Обычно для создания браузеров используется {@link ru.sbsoft.client.components.browser.BaseBrowser} как реализация по умолчанию.
 * @see IBrowserMaker
 * @see ru.sbsoft.client.components.browser.BaseBrowserManager#addInstance(ru.sbsoft.client.components.IBrowserMaker) 
 */
public interface ManagedBrowser extends IsWidget {

    public void setIdBrowser(String value);

    public String getIdBrowser();

    public String getShortName();

    public void setShortName(String value);

    public String getCaption();

    public void setCaption(String value);
}
