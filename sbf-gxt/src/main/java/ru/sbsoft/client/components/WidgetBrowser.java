package ru.sbsoft.client.components;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author sychugin
 */
public class WidgetBrowser implements ManagedBrowser {

    private final Widget widget;
    private String idBrowser;
    private String shortName;
    private String caption;

    public WidgetBrowser(IsWidget isWidget) {
        this.widget = isWidget.asWidget();
    }

    @Override
    public String getIdBrowser() {
        return idBrowser;
    }

    @Override
    public void setIdBrowser(String idBrowser) {
        this.idBrowser = idBrowser;
    }

    @Override
    public String getShortName() {
        return shortName;
    }

    @Override
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public Widget asWidget() {
        return widget;
    }
}
