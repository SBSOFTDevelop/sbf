package ru.sbsoft.client.components;

import ru.sbsoft.svc.core.client.dom.FormScrollSupport;
import ru.sbsoft.svc.core.client.dom.ScrollSupport;
import ru.sbsoft.svc.widget.core.client.container.VerticalLayoutContainer;

/**
 *
 * @author Kiselev
 */
public class CommonServiceContainer extends VerticalLayoutContainer {

    public CommonServiceContainer() {
        setAdjustForScroll(true);
        setScrollSupport(new FormScrollSupport(getElement()));
        setScrollMode(ScrollSupport.ScrollMode.AUTOY);
        getElement().getStyle().setBackgroundColor("#f0f0ff");
    }
    
    public boolean validate() {
        return true;
    }

}
