package ru.sbsoft.client.components;

import com.google.gwt.safehtml.shared.SafeHtml;
import ru.sbsoft.sbf.app.Registration;

/**
 *
 * @author Fedor Resnyanskiy
 */
public interface IWindow {

    void setHeading(SafeHtml header);

    void setWidth(int width);

    void setMinWidth(int minWidth);

    void setHeight(int height);

    void setMinHeight(int minHeight);

    void setResizable(boolean resisable);

    int getLeft();

    int getTop();

    void mask(String message);

    void mask();

    void unmask();

    boolean isVisible();

    void setPixelSize(int width, int height);

    void setModal(boolean modal);

    boolean isModal();

    void setClosable(boolean closable);

    void setCollapsible(boolean collapsible);

    void setMaximizable(boolean maximizable);

    void setVisible(boolean visible);

    Registration addHideHandler(final WindowHideHandler handler);

    Registration addShowHandler(final WindowShowHandler handler);

//    public void addRegion(IWindowRegion windowRegion);
//
//    public void addRegion(IWindowRegion windowRegion, Object layoutInfo);
//
//    public void removeRegion(IWindowRegion windowRegion);

    //<editor-fold defaultstate="collapsed" desc="Events">
    static class WindowEvent {

        private Object sourceEvent;
        private IWindow sourceWindow;

        public Object getSourceEvent() {
            return sourceEvent;
        }

        public void setSourceEvent(Object sourceEvent) {
            this.sourceEvent = sourceEvent;
        }

        public IWindow getSourceWindow() {
            return sourceWindow;
        }

        public void setSourceWindow(IWindow sourceWindow) {
            this.sourceWindow = sourceWindow;
        }

    }

    //<editor-fold defaultstate="collapsed" desc="Hide Event">
    static class WindowHideEvent extends WindowEvent {
    }

    static interface WindowHideHandler {

        void onHide(WindowHideEvent event);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Show Event">
    static class WindowShowEvent extends WindowEvent {
    }

    static interface WindowShowHandler {

        void onShow(WindowShowEvent event);
    }
    //</editor-fold>
    //</editor-fold>
}
