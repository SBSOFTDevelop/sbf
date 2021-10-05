package ru.sbsoft.client.components;

import com.google.gwt.safehtml.shared.SafeHtml;
import ru.sbsoft.sbf.app.utils.RegistrationUtils;
import ru.sbsoft.sbf.app.Registration;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.form.*;
import java.util.*;

/**
 * Базовый класс всех окон приложения.
 *
 * @author Sokoloff
 */
public class BaseWindow implements BaseWindowInterface {

    /**
     * На текущей реализации библиотеки окно ищется через dom-дерево GWT. При
     * переходе на интерфейс IWindow таким оббразом не получится доставать
     * объект IWindow, поскольку он не GWT. Для этого создаем реестр с
     * созданными окнами.
     */
    private final static Map<Window, BaseWindow> windows = new HashMap<Window, BaseWindow>();

    private final HackedWindow window = new HackedWindow();
    private final IElementContainer elementContainer = new DefaultElementContainer();

    private VerticalLayoutContainer mainContainer;
    private boolean firstShow = true;

    private final List<WindowHideHandler> windowHideHandlers = new ArrayList<WindowHideHandler>();
    private final List<WindowShowHandler> windowShowHandlers = new ArrayList<WindowShowHandler>();

    public BaseWindow() {
        this(new VerticalLayoutContainer());
    }

    public BaseWindow(VerticalLayoutContainer mainContainer) {
        super();
        windows.put(window, this);

        this.mainContainer = mainContainer;

        window.setDeferHeight(false);
        window.setWidget(mainContainer);

        window.setMaximizable(true);
//		window.setMonitorWindowResize(true);

        window.addShowHandler(new ShowEvent.ShowHandler() {
            @Override
            public void onShow(ShowEvent event) {
                onWindowShow(event);
            }
        });

        window.addHideHandler(new HideEvent.HideHandler() {
            @Override
            public void onHide(HideEvent event) {
                onWindowHide(event);
            }
        });

        window.addNotifyShowListener(new NotifyShowHandler() {
            @Override
            public void onNotifyShow() {
                notifyShow();
            }
        });

        window.addAfterFirstAttachListener(new AfterFirstAttachHandler() {
            @Override
            public void onAfterFirstAttach() {
                BaseWindow.this.onAfterFirstAttach();
            }
        });
    }

    @Override
    public void forceLayout() {
        window.forceLayout();
    }

    /**
     * @return @deprecated Нежелательно обращаться к системному GXT window
     */
    @Override
    public Window getWindow() {
        return window;
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            show();
        } else {
            hide();
        }
    }

    @Override
    public void show() {
        window.show();
        onShow();
    }

    @Override
    public void hide() {
        if (window.isMaximized()) {
            window.restore();
            window.maximize();
        }
        window.hide();
    }

    protected void onShow() {
    }

    @Override
    public void setModal(boolean modal) {
        window.setModal(modal);
    }

    @Override
    public void setClosable(boolean closable) {
        window.setClosable(closable);
    }

    private void notifyShow() {
        if (firstShow) {
            if (window.isResize()) {
                final int h = window.getOffsetHeight();
                final int clientHeight = com.google.gwt.user.client.Window.getClientHeight() - 100;
                if (h > clientHeight) {
                    setHeight(clientHeight);
                    window.getElement().setTop(50);
                }
            }
            firstShow = false;
        }
    }

    protected void onAfterFirstAttach() {
    }

    @Override
    public void setCollapsible(boolean collapsible) {
        window.setCollapsible(collapsible);
    }

    @Override
    public void setMaximizable(boolean maximizable) {
        window.setMaximizable(maximizable);
    }

    private void onWindowHide(HideEvent event) {
        final WindowHideEvent e = new WindowHideEvent();
        e.setSourceEvent(event);
        e.setSourceWindow(this);

        for (WindowHideHandler handler : windowHideHandlers) {
            handler.onHide(e);
        }
    }

    private void onWindowShow(ShowEvent event) {
        final WindowShowEvent e = new WindowShowEvent();
        e.setSourceEvent(event);
        e.setSourceWindow(this);

        for (WindowShowHandler handler : windowShowHandlers) {
            handler.onShow(e);
        }
    }

    @Override
    public Registration addHideHandler(final WindowHideHandler handler) {
        windowHideHandlers.add(handler);
        return new Registration() {
            @Override
            public void remove() {
                windowHideHandlers.remove(handler);
            }
        };
    }

    @Override
    public Registration addShowHandler(final WindowShowHandler handler) {
        windowShowHandlers.add(handler);
        return new Registration() {
            @Override
            public void remove() {
                windowShowHandlers.remove(handler);
            }
        };
    }

    @Override
    public void addRegion(Component component) {
        mainContainer.add(component);
    }

    @Override
    public void addRegion(Component component, VerticalLayoutContainer.VerticalLayoutData layout) {
        mainContainer.add(component, layout);
    }

    protected List<Field> getFieldList() {
        List<Field> result = new ArrayList<Field>();
        for (IsField<?> f : FormPanelHelper.getFields(mainContainer)) {
            if (f instanceof Field && !((Field) f).getCell().isReadOnly()) {
                result.add((Field) f);
            }
        }
        return result;
    }

    @Override
    public void mask(final String message) {
        if (!GXT.isIE()) {
            window.mask(message);
        }
    }

    @Override
    public void setHeading(SafeHtml header) {
        window.setHeading(header);
    }

    @Override
    public void setHeading(String header) {
        window.setHeading(header);
    }

    @Override
    public void setHeight(int height) {
        window.setHeight(height);
    }

    @Override
    public void setMinHeight(int minHeight) {
        window.setMinHeight(minHeight);
    }

    @Override
    public void setWidth(int width) {
        window.setWidth(width);
    }

    @Override
    public void setMinWidth(int minWidth) {
        window.setMinWidth(minWidth);
    }

    @Override
    public void setResizable(boolean resisable) {
        window.setResizable(resisable);
    }

    @Override
    public void mask() {
        window.mask();
    }

    @Override
    public void unmask() {
        window.unmask();
    }

    @Override
    public int getLeft() {
        return window.getElement().getLeft();
    }

    @Override
    public int getTop() {
        return window.getElement().getTop();
    }

    @Override
    public boolean isVisible() {
        return window.isVisible(true);
    }

    @Override
    public void setPixelSize(int width, int height) {
        window.setPixelSize(width, height);
    }

    @Override
    public List<? extends IElement> getElements() {
        return elementContainer.getElements();
    }

    @Override
    public boolean hasElement(IElement element) {
        return elementContainer.hasElement(element);
    }

    @Override
    public boolean hasChild(IElement child) {
        return elementContainer.hasChild(child);
    }

    public static BaseWindow forGxtWindow(Window w) {
        return windows.get(w);
    }

    @Override
    public boolean isModal() {
        return window.isModal();
    }

    public Registration addNotifyShowListener(NotifyShowHandler notifyShowHandler) {
        return window.addNotifyShowListener(notifyShowHandler);
    }

    public Registration addAfterFirstAttachListener(AfterFirstAttachHandler afterFirstAttachHandler) {
        return window.addAfterFirstAttachListener(afterFirstAttachHandler);
    }

    public static interface NotifyShowHandler {

        void onNotifyShow();
    }

    public static interface AfterFirstAttachHandler {

        void onAfterFirstAttach();
    }

    public static final class HackedWindow extends Window {

        private List<NotifyShowHandler> notifyShowHandlers = new ArrayList<NotifyShowHandler>();
        private List<AfterFirstAttachHandler> afterFirstAttachHandlers = new ArrayList<AfterFirstAttachHandler>();

        public Registration addNotifyShowListener(NotifyShowHandler notifyShowHandler) {
            return RegistrationUtils.register(notifyShowHandlers, notifyShowHandler);
        }

        public Registration addAfterFirstAttachListener(AfterFirstAttachHandler afterFirstAttachHandler) {
            return RegistrationUtils.register(afterFirstAttachHandlers, afterFirstAttachHandler);
        }

        @Override
        protected void onAfterFirstAttach() {
            super.onAfterFirstAttach();
            for (AfterFirstAttachHandler afterFirstAttachHandler : afterFirstAttachHandlers) {
                afterFirstAttachHandler.onAfterFirstAttach();
            }
        }

        @Override
        protected void notifyShow() {
            super.notifyShow();
            for (NotifyShowHandler notifyShowHandler : notifyShowHandlers) {
                notifyShowHandler.onNotifyShow();
            }
        }

    }
}
