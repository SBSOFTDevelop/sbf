package ru.sbsoft.client.components.grid.dlgbase;

import com.google.gwt.dom.client.Style;

/**
 *
 * @author Kiselev
 */
public class ItemBase extends Unit {

    public ItemBase() {
        getElement().getStyle().setPaddingTop(1, Style.Unit.PX);
        getElement().getStyle().setPaddingBottom(1, Style.Unit.PX);
        setHeight(24);
    }
    
}
