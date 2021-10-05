package ru.sbsoft.client.components;

/**
 * Фабрика браузеров для использования с {@link ru.sbsoft.client.components.form.LookupField}
 * @author Sokoloff
 */
public interface ISelectBrowserMaker {

    ISelectBrowser createBrowser();
}
