package ru.sbsoft.client.components;

import com.google.gwt.safehtml.shared.SafeHtml;
import ru.sbsoft.sbf.app.Registration;
import ru.sbsoft.svc.widget.core.client.Component;
import ru.sbsoft.svc.widget.core.client.Window;
import ru.sbsoft.svc.widget.core.client.container.VerticalLayoutContainer;
import java.util.List;

/**
 *
 * @author Fedor Resnyanskiy
 */
@Deprecated
public interface BaseWindowInterface extends IElementContainer, IWindow {

    Registration addHideHandler(final WindowHideHandler handler);

    void addRegion(Component component);

    void addRegion(Component component, VerticalLayoutContainer.VerticalLayoutData layout);

    Registration addShowHandler(final WindowShowHandler handler);

    void forceLayout();

    List<? extends IElement> getElements();

    int getLeft();

    int getTop();

    /**
     * @return @deprecated Нежелательно обращаться к системному SVC window
     */
    @Deprecated
    Window getWindow();

    boolean hasChild(IElement child);

    boolean hasElement(IElement element);

    void hide();

    boolean isModal();

    boolean isVisible();

    void mask(final String message);

    void mask();

    void setClosable(boolean closable);

    void setCollapsible(boolean collapsible);

    void setHeading(SafeHtml header);
    
    void setHeading(String header);

    void setHeight(int height);

    void setMaximizable(boolean maximizable);

    void setMinHeight(int minHeight);

    void setMinWidth(int minWidth);

    void setModal(boolean modal);

    void setPixelSize(int width, int height);

    void setResizable(boolean resisable);

    void setVisible(boolean visible);

    void setWidth(int width);

    void show();

    void unmask();
    
}
