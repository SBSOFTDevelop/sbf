package ru.sbsoft.client.components.browser;

import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * Представляет визуальный элемент, содержащий браузер (контейнер).
 * Используется для обращения к нему из браузера
 * @see BaseBrowser#setContainer(ru.sbsoft.client.components.browser.IBaseBrowserContainer) 
 * @author balandin
 * @since Oct 1, 2013 4:58:29 PM
 */
public interface IBaseBrowserContainer {

    void setHeading(SafeHtml header, SafeHtml tooltip);
    
    void exit();
}
