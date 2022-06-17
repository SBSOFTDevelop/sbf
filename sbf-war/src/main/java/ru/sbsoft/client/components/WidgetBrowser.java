package ru.sbsoft.client.components;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author sychugin
 */
public class WidgetBrowser extends AbstractBrowser {

    private final Widget widget;

    public WidgetBrowser(IsWidget isWidget) {
        this.widget = isWidget.asWidget();
    }
    
    public WidgetBrowser forMaker(IBrowserMaker maker){
        setIdBrowser(maker.getIdBrowser());
        setCaption(maker.getTitleBrowser());
        setShortName(maker.getTitleBrowser());
        return this;
    }

    @Override
    public Widget asWidget() {
        return widget;
    }
}
