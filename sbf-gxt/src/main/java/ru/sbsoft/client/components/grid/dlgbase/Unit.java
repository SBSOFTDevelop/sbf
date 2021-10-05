package ru.sbsoft.client.components.grid.dlgbase;

import ru.sbsoft.client.components.grid.dlgbase.event.HasUnitClickListeners;
import ru.sbsoft.client.components.grid.dlgbase.event.UnitLeaderEvent;
import ru.sbsoft.client.components.grid.dlgbase.event.UnitClickEvent;
import ru.sbsoft.client.components.grid.dlgbase.event.UnitLeaderListener;
import ru.sbsoft.client.components.grid.dlgbase.event.UnitClickListener;
import ru.sbsoft.client.components.grid.dlgbase.event.HasHolderListeners;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.components.browser.filter.FocusController;
import ru.sbsoft.client.components.grid.dlgbase.event.HasItemFocusListeners;
import ru.sbsoft.client.components.grid.dlgbase.event.UnitDeleteEvent;
import ru.sbsoft.client.components.grid.dlgbase.event.UnitDeleteListener;
import ru.sbsoft.client.components.grid.dlgbase.event.UnitFocusEvent;
import ru.sbsoft.client.components.grid.dlgbase.event.UnitFocusListener;
import ru.sbsoft.client.components.grid.dlgbase.event.UnitListenersSupport;
import ru.sbsoft.client.utils.HLD;
import ru.sbsoft.client.utils.VLC;

/**
 *
 * @author Kiselev
 */
public abstract class Unit extends HorizontalLayoutContainer implements HasHolderListeners, HasUnitClickListeners, HasItemFocusListeners, FocusEvent.FocusHandler {

    private static final int HOLDER_INDEX = 0;
    private final List<UnitLeaderListener> leaderListeners = new ArrayList<UnitLeaderListener>();
    private final ClickSupport click;
    private final FocusSupport focus;
    private final ClickDispatcher leaderClickDispatcher;
    private final DelSupport delSupport;
    private Label caption = null;

    public Unit() {
        setLayoutData(VLC.CONST);
        click = new ClickSupport(this, this);
        focus = new FocusSupport(this);
        leaderClickDispatcher = new ClickDispatcher(click);
        delSupport = new DelSupport(this);
    }

    @Override
    protected void onInsert(int index, Widget child) {
        super.onInsert(index, child);
        if (child instanceof Component) {
            FocusController.applyFocusListener((Component) child, this);
        }
    }

    @Override
    protected void onRemove(Widget child) {
        super.onRemove(child);
        FocusController.removeFocusListener(child);
    }

    public boolean isFixed() {
        return getLeader() == null;
    }

    public boolean validate() {
        return true;
    }

    public Group getParentGroup() {
        Widget w = null;
        int deep = 5;
        while ((w = (w != null) ? w.getParent() : getParent()) != null && deep-- > 0) {
            if (w instanceof Group) {
                return (Group) w;
            }
        }
        return null;
    }

    public boolean isRootContainer() {
        return getRootContainer() == this;
    }

    public Group getRootContainer() {
        if (!isAttached()) {
            throw new IllegalStateException(getClass().getName() + " is not attached to UI");
        }
        Group r = this instanceof Group ? (Group) this : getParentGroup();
        while (r.getParentGroup() != null) {
            r = r.getParentGroup();
        }
        return r;
    }

    protected int getLevel() {
        final Group p = getParentGroup();
        if (p == null) {
            return 0;
        } else {
            return p.getLevel() + 1;
        }
    }

    public int getHeight() {
        return getOffsetHeight(false);
    }

    protected final IUnitLeader getLeader() {
        return getWidgetCount() > HOLDER_INDEX && (getWidget(HOLDER_INDEX) instanceof Leader) ? (Leader) getWidget(HOLDER_INDEX) : null;
    }

    protected Leader createLeader() {
        return new Leader(this);
    }

    public final void addLeader() {
        if (getLeader() == null) {
            final Leader h = createLeader();
            h.addAttachHandler(new AttachEvent.Handler() {
                @Override
                public void onAttachOrDetach(AttachEvent event) {
                    if (event.isAttached()) {
                        h.addUnitClickListener(leaderClickDispatcher);
                    } else {
                        h.removeUnitClickListener(leaderClickDispatcher);
                    }
                }
            });
            insert(h, HOLDER_INDEX);
            forceLayout();
            fireLeaderAdded(h);
        }
    }

    public final void removeLeader() {
        IUnitLeader h = getLeader();
        if (h != null) {
            remove(HOLDER_INDEX);
            forceLayout();
            fireLeaderRemoved(h);
        }
    }

    private Label makeCaption(String caption) {
        Label l = new Label(caption);
        //Label l = new Label(caption + ":"); // colon is not sutable because of label is used as immutable value too
        l.setWidth(283 + "px");
        l.getElement().getStyle().setProperty("textAlign", "right");
        l.getElement().getStyle().setOverflow(Style.Overflow.HIDDEN);
        l.getElement().getStyle().setProperty("whiteSpice", "nowrap");
        l.getElement().getStyle().setProperty("textOverflow", "ellipsis");
        l.setLayoutData(new HLD(-1, 1, new Margins(4, 2, 0, 1)));
        return l;
    }

    public final void setCaption(String s) {
        setCaption(s != null && !(s = s.trim()).isEmpty() ? makeCaption(s) : (Label)null);
    }

    public final void setCaption(Label l) {
        if (l != caption) {
            removeCaption();
            if (l != null) {
                int insIndex = getLeader() != null ? 1 : 0;
                insert(l, insIndex);
                caption = l;
            }
        }
    }

    public Label getCaption() {
        return caption;
    }

    private void removeCaption() {
        if (caption != null) {
            remove(caption);
            caption = null;
        }
    }

    @Override
    public void add(IsWidget child, HorizontalLayoutData layoutData) {

        // Чтобы компоненты не мельтишили где попало,
        // реальное местоположение будет задано при doLayout
        Component c = (Component) child;
        c.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
        c.getElement().getStyle().setLeft(-10000, Style.Unit.PX);
        c.getElement().getStyle().setTop(-10000, Style.Unit.PX);

        super.add(child, layoutData);
    }

    private void fireLeaderRemoved(IUnitLeader h) {
        UnitLeaderEvent e = new UnitLeaderEvent(this, h);
        for (UnitLeaderListener l : leaderListeners) {
            l.leaderRemoved(e);
        }
    }

    private void fireLeaderAdded(Leader h) {
        UnitLeaderEvent e = new UnitLeaderEvent(this, h);
        for (UnitLeaderListener l : leaderListeners) {
            l.leaderAdded(e);
        }
    }

    @Override
    public void addHolderListener(UnitLeaderListener l) {
        if (!leaderListeners.contains(l)) {
            leaderListeners.add(l);
        }
    }

    @Override
    public void removeHolderListener(UnitLeaderListener l) {
        leaderListeners.remove(l);
    }

    @Override
    public void addUnitClickListener(UnitClickListener l) {
        click.addListener(l);
    }

    @Override
    public void removeUnitClickListener(UnitClickListener l) {
        click.removeListener(l);
    }

    @Override
    public void addUnitFocusListener(UnitFocusListener l) {
        focus.addListener(l);
    }

    @Override
    public void removeUnitFocusListener(UnitFocusListener l) {
        focus.removeListener(l);
    }

    @Override
    public void onFocus(FocusEvent event) {
        focus.fire();
    }

    public void addUnitDeleteListener(UnitDeleteListener l) {
        delSupport.addListener(l);
    }

    public void removeUnitDeleteListener(UnitDeleteListener l) {
        delSupport.removeListener(l);
    }

    public void delete() {
        delSupport.fire();
        delSupport.clear();
        removeFromParent();
    }

    public void clearValue() {
    }

    private static class ClickDispatcher implements UnitClickListener {

        private final ClickSupport destination;

        public ClickDispatcher(ClickSupport destination) {
            this.destination = destination;
        }

        @Override
        public void unitClicked(UnitClickEvent e) {
            destination.fire();
        }

    }

    protected static class Leader extends SimpleContainer implements IUnitLeader {

        public static final int HOLDER_WIDTH = 33;
        private final ClickSupport click;

        Leader(Unit unit) {
            click = new ClickSupport(unit, this);
            Style style = getElement().getStyle();
            style.setCursor(Style.Cursor.POINTER);
            style.setBorderStyle(Style.BorderStyle.DOTTED);
            style.setBorderWidth(1, Style.Unit.PX);
            setWidth(HOLDER_WIDTH);

            setLayoutData(new HLD(-1, 1, new Margins(0, 2, 0, 1)));
        }

        @Override
        public void addUnitClickListener(UnitClickListener l) {
            click.addListener(l);
        }

        @Override
        public void removeUnitClickListener(UnitClickListener l) {
            click.removeListener(l);
        }
    }

    private static class FocusSupport extends UnitListenersSupport<UnitFocusListener> {

        FocusSupport(Unit unit) {
            super(unit);
        }

        void fire() {
            UnitFocusEvent e = new UnitFocusEvent(unit);
            for (UnitFocusListener l : listeners) {
                l.unitFocused(e);
            }
        }
    }

    private static class DelSupport extends UnitListenersSupport<UnitDeleteListener> {

        DelSupport(Unit unit) {
            super(unit);
        }

        void fire() {
            UnitDeleteEvent e = new UnitDeleteEvent(unit);
            for (UnitDeleteListener l : new ArrayList<UnitDeleteListener>(listeners)) {
                l.onUnitDelete(e);
            }
        }
    }

    private static class ClickSupport extends UnitListenersSupport<UnitClickListener> implements ClickHandler {

        private final Widget widget;
        private HandlerRegistration clickHandlerRegistration = null;

        ClickSupport(Unit unit, Widget widget) {
            super(unit);
            this.widget = widget;
        }

        @Override
        public boolean addListener(UnitClickListener l) {
            boolean res;
            if (res = super.addListener(l)) {
                if (clickHandlerRegistration == null) {
                    clickHandlerRegistration = widget.addDomHandler(this, ClickEvent.getType());
                }
            }
            return res;
        }

        @Override
        public boolean removeListener(UnitClickListener l) {
            boolean res;
            if (res = super.removeListener(l)) {
                if (listeners.isEmpty() && clickHandlerRegistration != null) {
                    clickHandlerRegistration.removeHandler();
                    clickHandlerRegistration = null;
                }
            }
            return res;
        }

        @Override
        public void onClick(ClickEvent event) {
            fire();
        }

        public void fire() {
            UnitClickEvent e = new UnitClickEvent(unit);
            for (UnitClickListener l : listeners) {
                l.unitClicked(e);
            }
        }
    }
}
