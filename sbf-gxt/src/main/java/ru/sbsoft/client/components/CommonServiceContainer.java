package ru.sbsoft.client.components;

import com.sencha.gxt.core.client.dom.FormScrollSupport;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

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
