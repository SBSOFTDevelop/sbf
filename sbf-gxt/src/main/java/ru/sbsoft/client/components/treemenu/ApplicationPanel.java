package ru.sbsoft.client.components.treemenu;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.ContentPanel;
import ru.sbsoft.client.I18n;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;

/**
 * Корневая панель приложения. Содержит {@link ApplicationContainer} и заголовки
 * приложения.
 *
 * @author balandin
 * @since Mar 20, 2013 2:40:43 PM
 */
public class ApplicationPanel extends ContentPanel {
    
    private final ApplicationContainer applicationContainer;
    private String caption;
    //
    private String contextCaption;
    private String user;
    
    public ApplicationPanel(ApplicationContainer applicationContainer, String caption) {
        super();
        
        this.applicationContainer = applicationContainer;
        this.caption = caption;
        
        setWidget(applicationContainer);
        
        updateCaption();
    }
    
    public String getCaption() {
        return caption;
    }
    
    public void setCaption(String value) {
        // если caption еще не устанвлен, то устанавливаем сначала его
        if (null == this.caption) {
            this.caption = value;
        } else {
            this.contextCaption = value;
        }
        updateCaption();
    }
    
    public void setUser(String user) {
        this.user = user;
        updateRightCaption();
    }
    
    public ApplicationContainer getApplicationContainer() {
        return applicationContainer;
    }
    
    private void updateRightCaption() {
        SafeHtmlBuilder b = new SafeHtmlBuilder();
        b.appendHtmlConstant("<span style='font-weight:normal;'>" + I18n.get(SBFGeneralStr.labelUser)
                + ":&nbsp;&nbsp;&nbsp;&nbsp;</span>");
        b.appendEscaped(user == null ? Strings.EMPTY : user);
        b.appendHtmlConstant("&nbsp;&nbsp;&nbsp;&nbsp;");
        
        final XElement barElem = getHeader().getAppearance().getBarElem(getHeader().getElement());
        barElem.removeChildren();
        XElement elem = XElement.createElement("div");
        
        barElem.appendChild(elem);
        
        barElem.appendChild(new HTML(b.toSafeHtml()).getElement());
        
    }
    
    
    
    private void updateCaption() {
        SafeHtmlBuilder builder = new SafeHtmlBuilder();
        builder.appendEscaped(null == caption ? "" : caption);
//        if (contextCaption != null) {
//            builder.appendEscaped(" \\ ");
//            builder.appendEscaped(contextCaption);
//        }
        setHeading(builder.toSafeHtml());
    }
}
