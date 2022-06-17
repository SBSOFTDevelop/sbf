package ru.sbsoft.client.components.grid.dlgbase;

import ru.sbsoft.client.components.grid.dlgbase.event.DragListener;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.core.client.dom.XDOM;
import ru.sbsoft.svc.core.client.util.Margins;
import ru.sbsoft.svc.fx.client.DragCancelEvent;
import ru.sbsoft.svc.fx.client.DragEndEvent;
import ru.sbsoft.svc.fx.client.DragHandler;
import ru.sbsoft.svc.fx.client.DragMoveEvent;
import ru.sbsoft.svc.fx.client.DragStartEvent;
import ru.sbsoft.svc.fx.client.Draggable;
import ru.sbsoft.svc.widget.core.client.Component;
import ru.sbsoft.svc.widget.core.client.container.SimpleContainer;
import ru.sbsoft.svc.widget.core.client.container.VerticalLayoutContainer;
import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.components.grid.dlgbase.event.UnitDeleteEvent;
import ru.sbsoft.client.components.grid.dlgbase.event.UnitDeleteListener;
import ru.sbsoft.client.utils.HLC;
import ru.sbsoft.client.utils.VLD;

/**
 *
 * @author Kiselev
 */
public class DragManager implements IDragManager {

    private static final String DND_KEY = "svc.draggable";
    //
    private Dummy dummy;
    private ArrayList<Unit> cache;
    //
    private int deltax, deltay;
    private Group startCnt;
    private int startIndex;
    //
    private Unit cargo;
    //
    private Group rootGroup = null;

    private final List<DragListener> dragListeners = new ArrayList<DragListener>();

    private final UnitDragHandler dragHandler = new UnitDragHandler();

    private final UnregistrListener unregistrListener = new UnregistrListener();

    @Override
    public void addDragSupport(Unit unit) {
        if (unit.getLeader() != null) {
            registr(unit, unit.getLeader().asWidget());
        } else {
            throw new IllegalArgumentException("Can't add drag support to " + unit + ": there is no holder.");
        }
    }

    @Override
    public void removeDragSupport(Unit unit) {
        unregistr(unit);
    }

    private void registr(Component widget, Widget holder) {
        Draggable d = widget.getData(DND_KEY);
        if (d == null) {
            d = new Draggable(widget, holder);
            d.setConstrainClient(false);
            d.setUseProxy(true);
            d.addDragHandler(dragHandler);
            d.setMoveAfterProxyDrag(false);
            d.setSizeProxyToSource(true);
            widget.setData(DND_KEY, d);
            if (widget instanceof Unit) {
                ((Unit) widget).addUnitDeleteListener(unregistrListener);
            }
        }
    }

    private void unregistr(Component widget) {
        Draggable d = widget.getData(DND_KEY);
        if (d != null) {
            d.release();
        }
        widget.setData(DND_KEY, null);
        if (widget instanceof Unit) {
            ((Unit) widget).removeUnitDeleteListener(unregistrListener);
        }
    }

    public void addDragListener(DragListener l) {
        if (!dragListeners.contains(l)) {
            dragListeners.add(l);
        }
    }

    private void fireDragComplete(Unit e, Group from, Group to) {
        for (DragListener l : dragListeners) {
            l.dragComplete(e, from, to);
        }
    }

    private Dummy getDummy() {
        if (dummy == null) {
            dummy = new Dummy();
            dummy.getElement().getStyle().setBorderStyle(Style.BorderStyle.DOTTED);
            dummy.getElement().getStyle().setBorderWidth(1, Style.Unit.PX);
            dummy.getElement().getStyle().setBorderColor("blue");
        }
        return dummy;
    }

    private void showDummy(Unit row, boolean after) {
        final VerticalLayoutContainer vcnt = row.getParentGroup().unitContainer;
        final int index = vcnt.getWidgetIndex(row) + (after ? 1 : 0);
        final int oldIndex = vcnt.getWidgetIndex(getDummy());
        if (oldIndex != index) {
            row.getParentGroup().addUnit(getDummy(), index);
            rootGroup.forceLayout();
        }
    }

    private Group findConditionsByHolder(Group cnt, int x, int y) {
        if (cnt.getLeader() != null) {
            final Widget h = cnt.getLeader().asWidget();
            if (h.getAbsoluteTop() <= y && y <= h.getAbsoluteTop() + h.getOffsetHeight()
                    && h.getAbsoluteLeft() <= x && x <= h.getAbsoluteLeft() + h.getOffsetWidth()) {
                return cnt;
            }
        }
        for (int i = 0; i < cnt.unitContainer.getWidgetCount(); i++) {
            final Widget w = cnt.unitContainer.getWidget(i);
            if (w instanceof Group) {
                Group item = findConditionsByHolder((Group) w, x, y);
                if (item != null) {
                    return item;
                }
            }
        }
        return null;
    }

    private void processTreeItemByHorizontalPosition(int y) {
        Unit last = null;
        for (Unit panel : cache) {
            if (panel == cargo) {
                continue;
            }
            int b = panel.getAbsoluteTop();
            int t = b + panel.getOffsetHeight();
            if (y < t) {
                showDummy(panel, (y >= b + panel.getOffsetHeight() / 2));
                return;
            }
            last = panel;
        }
        if (last != null) {
            showDummy(last, true);
        }
    }

    private void listConditionPanels(Group cnt, List<Unit> result) {
        for (int i = 0; i < cnt.unitContainer.getWidgetCount(); i++) {
            final Widget w = cnt.unitContainer.getWidget(i);
            if (w != cargo) {
                if (w instanceof Item) {
                    final Item item = (Item) w;
                    if (item.getLeader() != null) {
                        result.add((Item) w);
                    }
                } else if (w instanceof Group) {
                    listConditionPanels((Group) w, result);
                }
            }
        }
    }

    private void removeEmpty(Group g) {
        if (g.isEmpty()) {
            g.delete();
        }
    }

    /**
     * Пустая заглушка посвечивающая местоположение перетаскиваемого объекта,
     *
     * @author balandin
     */
    private static class Dummy extends Unit {

        public Dummy() {
            add(new SimpleContainer(), HLC.FILL);
            setLayoutData(new VLD(1, -1, new Margins(4)));
        }

        @Override
        public int getHeight() {
            return super.getHeight() + 8;
        }
    }

    private class UnitDragHandler implements DragHandler {

        @Override
        public void onDragStart(DragStartEvent de) {
            cargo = (Unit) de.getTarget();
            rootGroup = cargo.getRootContainer();
            listConditionPanels(rootGroup, cache = new ArrayList<Unit>());

            final Widget h = cargo.getLeader().asWidget();
            deltax = de.getNativeEvent().getClientX() + XDOM.getBodyScrollLeft() - h.getAbsoluteLeft();
            deltay = de.getNativeEvent().getClientY() + XDOM.getBodyScrollTop() - h.getAbsoluteTop();
            startCnt = cargo.getParentGroup();
            startIndex = startCnt.unitContainer.getWidgetIndex(cargo);

            getDummy().setHeight(cargo.getOffsetHeight(false) - 8);
            getDummy().getElement().getStyle().setProperty("padding", cargo.getElement().getStyle().getPadding());

            final Group cnt = cargo.getParentGroup();
            final int index = cnt.unitContainer.getWidgetIndex(cargo);
            cnt.addUnit(getDummy(), index);
            cargo.removeFromParent();
            cnt.forceLayout();
        }

        @Override
        public void onDragMove(DragMoveEvent de) {
            final int x = de.getNativeEvent().getClientX() + XDOM.getBodyScrollLeft() - deltax;
            final int y = de.getNativeEvent().getClientY() + XDOM.getBodyScrollTop() - deltay;
            final Group item = findConditionsByHolder(rootGroup, x, y);
            if (item != null && !item.isRootContainer()) {
                int b = item.getAbsoluteTop();
                showDummy(item, (y >= b + item.getOffsetHeight() / 2));
            } else {
                processTreeItemByHorizontalPosition(y);
            }
        }

        @Override
        public void onDragEnd(DragEndEvent de) {
            final Group cnt = getDummy().getParentGroup();
            final int index = cnt.unitContainer.getWidgetIndex(getDummy());
            cnt.addUnit(cargo, index);
            getDummy().removeFromParent();
            rootGroup.forceLayout();

            fireDragComplete(cargo, startCnt, cnt);
            removeEmpty(startCnt);

            cache = null;
            rootGroup.updateUnits();
        }

        @Override
        public void onDragCancel(DragCancelEvent event) {
            getDummy().removeFromParent();
            startCnt.addUnit(cargo, startIndex);
            rootGroup.forceLayout();
        }

    }

    private class UnregistrListener implements UnitDeleteListener {

        @Override
        public void onUnitDelete(UnitDeleteEvent e) {
            unregistr(e.getUnit());
        }
    }
}
