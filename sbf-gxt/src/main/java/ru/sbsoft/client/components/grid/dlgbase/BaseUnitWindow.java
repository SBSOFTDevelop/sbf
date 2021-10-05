package ru.sbsoft.client.components.grid.dlgbase;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Point;
import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.CommonServiceContainer;
import ru.sbsoft.client.components.CommonServiceWindow;
import ru.sbsoft.client.components.actions.AbstractAction;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.client.utils.VLC;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 *
 * @author Kiselev
 * @param <RootGroupType>
 */
public abstract class BaseUnitWindow<RootGroupType extends Group> extends CommonServiceWindow {

    protected static enum StandardUnitAction {

        ADD_ITEM, ADD_GROUP, DEL
    };

    protected static StandardUnitAction ADD_ITEM = StandardUnitAction.ADD_ITEM;
    protected static StandardUnitAction ADD_GROUP = StandardUnitAction.ADD_GROUP;
    protected static StandardUnitAction DEL = StandardUnitAction.DEL;

    private static final int MAX_HEIGHT = 900;
    //
    protected final RootGroupType rootGroup;
    protected final SelectionManager selectionManager;
    protected final DragManager dragManager;
    private final List<ItemPlugin> itemPlugins = new ArrayList<ItemPlugin>();

    private Widget windowParent = null;

    protected BaseUnitWindow(RootGroupType rootGroup) {
        this(rootGroup, null, null);
    }

    protected BaseUnitWindow(RootGroupType rootGroup, SelectionManager selectionManager) {
        this(rootGroup, selectionManager, null);
    }

    protected BaseUnitWindow(RootGroupType rootGroup, DragManager dragManager) {
        this(rootGroup, null, dragManager);
    }

    protected BaseUnitWindow(RootGroupType rootGroup, SelectionManager selectionManager, DragManager dragManager) {
        super(new CommonServiceContainer());
        this.rootGroup = rootGroup;
        this.selectionManager = selectionManager != null ? selectionManager : new SelectionManager();
        this.dragManager = dragManager != null ? dragManager : new DragManager();
        this.selectionManager.attach(actionManager);

        functionalContainer.add(rootGroup, VLC.FILL);

        toolBar.addButton(new ClearAction());
        toolBar.addSeparator();
    }

    protected final void addStandardActions(StandardUnitAction... actions) {
        if (actions != null) {
            for (StandardUnitAction a : actions) {
                switch (a) {
                    case ADD_GROUP:
                        toolBar.addButton(new AddGroupAction());
                        break;
                    case ADD_ITEM:
                        toolBar.addButton(new AddItemAction());
                        break;
                    case DEL:
                        toolBar.addButton(new DeleteAction());
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown standard action: " + a);
                }
            }
        }
    }

    protected void addPlugin(ItemPlugin plugin) {
        if (plugin != null && !itemPlugins.contains(plugin)) {
            itemPlugins.add(plugin);
        }
    }

    @Override
    protected void onKeyPress(Event we) {
        super.onKeyPress(we);
        boolean t = getElement().isOrHasChild(we.getEventTarget().<com.google.gwt.dom.client.Element>cast());
        if (we.getCtrlKey() && we.getKeyCode() == KeyCodes.KEY_ENTER && t) {
            getApplyAction().perform();
        }
    }

    protected final Widget getWindowParent() {
        return windowParent;
    }

    protected void show(Widget parent) {
        this.show(parent, null);
    }

    protected void show(Widget parent, final Point pos) {
        this.windowParent = parent;
        show();
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                if (!rootGroup.hasAnyItems()) {
                    Unit u = rootGroup.addUnit(createItem());
                    if (u instanceof Item) {
                        ((Item) u).initFocus();
                    }
                }
                functionalContainer.forceLayout();
                actionManager.updateState();
            }
        });
    }

    @Override
    protected boolean validate() {
        return super.validate() && rootGroup.validate();
    }

    protected <T extends Item> T tuneItem(T item) {
        return tuneItem(item, true);
    }

    protected <T extends Item> T tuneItem(T item, boolean applyPlugins) {
        selectionManager.addSelectionSupport(item);
        if (item.getLeader() != null) {
            dragManager.addDragSupport(item);
        }
        if (applyPlugins) {
            for (ItemPlugin p : itemPlugins) {
                p.apply(item);
            }
        }
        return item;
    }

    protected final Unit getCurrentUnit() {
        return selectionManager.getSelectedUnit();
    }

    protected final Group getCurrentGroup() {
        Unit unit = getCurrentUnit();
        Group g = unit != null ? unit.getParentGroup() : rootGroup;
        return g != null ? g : rootGroup;
    }

    protected final <U extends Unit> U addUnit(U unit) {
        return addUnit(unit, null);
    }

    protected <U extends Unit> U addUnit(U unit, Unit afterUnit) {
        if (unit == null) {
            throw new IllegalArgumentException("Can't add NULL unit");
        }
        Group g = null;
        if (afterUnit != null) {
            g = afterUnit.getParentGroup();
        }
        if (g == null) {
            afterUnit = null;
            g = getCurrentGroup();
        }
        if (afterUnit == null || afterUnit.isFixed()) {
            g.addUnit(unit);
        } else {
            g.addUnit(unit, afterUnit);
        }
        selectionManager.setSelectedUnit(unit);
        rootGroup.forceLayout();
        if (unit instanceof Item) {
            ((Item) unit).initFocus();
        }
        return unit;
    }

    protected abstract <T extends Item> T createItem();

    protected <G extends Group> G createGroup() {
        return (G) new Group();
    }

    protected void doClearAction() {
        rootGroup.clearValue();
        rootGroup.forceLayout();
    }

    // не используется "прибитие" к левому краю: юзабилити - отображение в центре экрана по умолчанию
    private void tuneWindowsHeight(final Widget parent, Point pos) {
        final int delta = rootGroup.getHeight() - functionalContainer.getOffsetHeight();
        if (delta > 0) {
            setHeight(Math.min(getOffsetHeight() + delta + 4, MAX_HEIGHT));
        }
        if (pos == null) {
            setPosition(parent.getAbsoluteLeft() + 75, parent.getAbsoluteTop() + 50);
        } else {
            setPosition(pos.getX(), pos.getY());
        }
    }

    //++++++ ACTIONS ++++++++
    protected class ClearAction extends AbstractAction {

        public ClearAction() {
            setToolTip(I18n.get(SBFBrowserStr.labelClearAllConditions));
            setIcon16(SBFResources.BROWSER_ICONS.BrowserFilterDelete16());
            setIcon24(SBFResources.BROWSER_ICONS.BrowserFilterDelete());
        }

        @Override
        protected void onExecute() {
            ClientUtils.confirm(BaseUnitWindow.this, SBFBrowserStr.msgClearFilter, new Command() {
                @Override
                public void execute() {
                    doClearAction();
                }
            });
        }
    }

    protected class AddItemAction extends AbstractAction {

        public AddItemAction() {
            setCaption(SBFBrowserStr.menuAddCondition);
            setToolTip(SBFBrowserStr.hintAddCondition);
            setIcon16(SBFResources.BROWSER_ICONS.RowInsert16());
            setIcon24(SBFResources.BROWSER_ICONS.RowInsert());
        }

        @Override
        protected void onExecute() {
            addUnit(createItem(), getCurrentUnit());
        }
    }

    protected class AddGroupAction extends AbstractAction {

        public AddGroupAction() {
            setCaption(SBFBrowserStr.menuAddGroupContitions);
            setToolTip(SBFBrowserStr.hintAddGroupContitions);
            setIcon16(SBFResources.BROWSER_ICONS.RowMassInsert16());
            setIcon24(SBFResources.BROWSER_ICONS.RowMassInsert());
        }

        @Override
        protected void onExecute() {
            addUnit(createGroup(), getCurrentUnit());
        }
    }

    protected class DeleteAction extends AbstractAction {

        public DeleteAction() {
            setCaption(SBFBrowserStr.menuDeleteItemFilter);
            setToolTip(SBFBrowserStr.hintDeleteItemFilter);
            setIcon16(SBFResources.BROWSER_ICONS.RowDelete16());
            setIcon24(SBFResources.BROWSER_ICONS.RowDelete());
        }

        @Override
        public boolean checkEnabled() {
            Unit unit = getCurrentUnit();
            return unit != null && unit != rootGroup && !unit.isFixed();
        }

        @Override
        protected void onExecute() {
            if (checkEnabled()) {
                ClientUtils.confirm(BaseUnitWindow.this, SBFBrowserStr.msgDeleteSelectedItem, new Command() {
                    @Override
                    public void execute() {
                        getCurrentUnit().delete();
                        rootGroup.forceLayout();
                    }
                });
            }
        }
    }

}
