package ru.sbsoft.client.components.grid.dlgbase;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.components.grid.dlgbase.event.ListenersSupport;
import ru.sbsoft.client.components.grid.dlgbase.event.UnitAddEvent;
import ru.sbsoft.client.components.grid.dlgbase.event.UnitAddListener;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.client.utils.HLC;
import ru.sbsoft.client.utils.HLD;

/**
 *
 * @author Kiselev
 */
public class Group extends Unit {

    protected final VerticalLayoutContainer unitContainer = new VerticalLayoutContainer();

    private final UnitAddSupport addSupport = new UnitAddSupport();

    public Group() {
        setAdjustForScroll(false);
        setScrollMode(ScrollSupport.ScrollMode.NONE);

        unitContainer.setScrollMode(ScrollSupport.ScrollMode.NONE);
        unitContainer.setLayoutData(HLC.FILL);

        add(unitContainer);
    }

    @Override
    protected Unit.Leader createLeader() {
        Unit.Leader h = super.createLeader();
        h.setLayoutData(new HLD(-1, 1, new Margins(1, 1, 1, 1)));
        return h;
    }

    public <U extends Unit> U addUnit(U unit, Unit unitAfter) {
        if (unitAfter != null) {
            final int n = getUnitIndex(unitAfter);
            if (n != -1) {
                return addUnit(unit, n + 1);
            }
        }
        return addUnit(unit);
    }

    public <U extends Unit> int getUnitIndex(U unit) {
        return unitContainer.getWidgetIndex(unit);
    }

    public <U extends Unit> U addUnit(U unit) {
        return addUnit(unit, unitContainer.getWidgetCount());
    }

    public <U extends Unit> U addUnit(U unit, int indexBefore) {
        unitContainer.insert(unit, indexBefore, (VerticalLayoutContainer.VerticalLayoutData) unit.getLayoutData());
        addSupport.fire(unit);
        return unit;
    }

    public <U extends Unit> List<U> getUnits(Class<U> clazz, boolean deep) {
        return collectUnits(clazz, new ArrayList<U>());
    }

    private <U extends Unit> List<U> collectUnits(Class<U> clazz, List<U> l) {
        if (l == null) {
            l = new ArrayList<U>();
        }
        for (Widget w : unitContainer) {
            if (ClientUtils.isAssignableFrom(clazz, w.getClass())) {
                l.add((U) w);
            }
            if (w instanceof Group) {
                ((Group) w).collectUnits(clazz, l);
            }
        }
        return l;
    }
    
    public boolean hasAnyItems() {
        for (int i = 0; i < unitContainer.getWidgetCount(); i++) {
            Widget w = unitContainer.getWidget(i);
            if ((w instanceof Item) || ((w instanceof Group) && ((Group) w).hasAnyItems())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasSystemItems() {
        for (int i = 0; i < unitContainer.getWidgetCount(); i++) {
            Widget w = unitContainer.getWidget(i);
            if (((w instanceof Item) && ((Item) w).isFixed()) || ((w instanceof Group) && ((Group) w).hasSystemItems())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasCustomItems() {
        for (int i = 0; i < unitContainer.getWidgetCount(); i++) {
            Widget w = unitContainer.getWidget(i);
            if (((w instanceof Item) && !((Item) w).isFixed()) || ((w instanceof Group) && ((Group) w).hasCustomItems())) {
                return true;
            }
        }
        return false;
    }

    public Widget getLastWidget() {
        if (unitContainer.getWidgetCount() > 0) {
            return unitContainer.getWidget(unitContainer.getWidgetCount() - 1);
        }
        return null;
    }

    public boolean isEmpty() {
        for (int i = 0; i < unitContainer.getWidgetCount(); i++) {
            Widget w = unitContainer.getWidget(i);
            if ((w instanceof Item) || ((w instanceof Group) && !((Group) w).isEmpty())) {
                return false;
            }
        }
        return true;
    }

    public int getDepth() {
        int maxChildDepth = 0;
        for (int i = 0; i < unitContainer.getWidgetCount(); i++) {
            final Widget w = unitContainer.getWidget(i);
            if (w instanceof Group) {
                maxChildDepth = Math.max(maxChildDepth, ((Group) w).getDepth());
            }
        }
        return maxChildDepth + 1;
    }

    protected void updateUnits() {
        forceLayout();
    }

    @Override
    public boolean validate() {
        boolean result = true;
        for (int i = 0; i < unitContainer.getWidgetCount(); i++) {
            final Widget w = unitContainer.getWidget(i);
            if (w instanceof Unit) {
                result &= ((Unit) w).validate();
            }
        }
        return result;
    }

    @Override
    public void delete() {
        clearValue();
        if (getParentGroup() != null && isEmpty()) { // assume null means root group. root group can't be deleted
            super.delete();
        }
    }

    @Override
    public void clearValue() {
        for (int i = unitContainer.getWidgetCount() - 1; i >= 0; i--) {
            if (i < unitContainer.getWidgetCount()) {
                Widget w = unitContainer.getWidget(i);
                if (w instanceof Unit) {
                    Unit u = (Unit) w;
                    if (u.isFixed()) {
                        u.clearValue();
                    } else {
                        u.delete();
                    }
                } else {
                    unitContainer.remove(i);
                }
            }
        }
    }

    @Override
    public void forceLayout() {
        super.forceLayout();
        int h = 0;
        for (int i = 0; i < unitContainer.getWidgetCount(); i++) {
            h += ((Unit) unitContainer.getWidget(i)).getHeight();
        }
        if (isRootContainer()) {
            h = Math.max(3 * 24, h);
        }
        setHeight(h);
    }

    public void addUnitAddListener(UnitAddListener l) {
        addSupport.addListener(l);
    }

    public void removeUnitAddListener(UnitAddListener l) {
        addSupport.removeListener(l);
    }

    private static class UnitAddSupport extends ListenersSupport<UnitAddListener> {

        void fire(Unit unit) {
            UnitAddEvent e = new UnitAddEvent(unit);
            for (UnitAddListener l : listeners) {
                l.onUnitAdd(e);
            }
        }
    }

}
