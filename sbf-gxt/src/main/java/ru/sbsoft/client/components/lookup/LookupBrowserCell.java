package ru.sbsoft.client.components.lookup;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.TriggerFieldCell;
import com.sencha.gxt.core.client.Style.Anchor;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.DelayedTask;
import com.sencha.gxt.data.shared.loader.LoadEvent;
import com.sencha.gxt.data.shared.loader.LoadHandler;
import com.sencha.gxt.messages.client.DefaultMessages;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.XEvent;
import ru.sbsoft.client.components.actions.AbstractAction;
import ru.sbsoft.client.components.actions.Action;
import ru.sbsoft.client.components.form.LookupField;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.components.grid.GridEvent;
import ru.sbsoft.client.components.grid.SBLiveGridView;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.services.FetchResult;
import ru.sbsoft.shared.model.LookupCellType;
import ru.sbsoft.shared.model.MarkModel;
import ru.sbsoft.shared.services.FetchParams;

/**
 * @author balandin
 * @since Jul 11, 2013 1:42:21 PM
 */
public class LookupBrowserCell extends TriggerFieldCell<String> {

    private final LookupContext shared;
    //
    private final LookupField lookupField;
    private LookupGridMenu menu;
    private BaseGrid grid;
    private LoadHandler<FetchParams, FetchResult<MarkModel>> loadHandler;
    private final LookupCellType lookupCellType;
    //
    private final DelayedTask expandTask;
    private String lastQuery;
    private boolean hideOnBlur;

    public LookupBrowserCell(LookupField lookupField, LookupContext state, LookupCellType cellType) {
        super();

        this.lookupField = lookupField;
        this.shared = state;
        this.lookupCellType = cellType;

        expandTask = new DelayedTask() {
            @Override
            public void onExecute() {
                showMenu(false);
            }
        };
    }

    public void collapse() {
        if (!shared.isExpanded()) {
            return;
        }
        shared.setExpanded(false);
        menu.hide();

        final LookupComboBox f = lookupCellType == LookupCellType.KEY ? lookupField.getFieldKey() : lookupField.getFieldName();
        getInputElement(f.getElement()).focus();

        final int n = f.getText().length();
        f.setSelectionRange(n, 0);

        setFormClosable(true);
    }

    public static Window findParentWindow(Widget w) {
        Widget widget = w;
        while ((widget = widget.getParent()) != null) {
            if (widget instanceof Window) {
                return (Window) widget;
            }
        }
        return null;
    }

    private void setFormClosable(final boolean value) {
        Scheduler.get().scheduleDeferred(new Command() {

            @Override
            public void execute() {
                Window w = findParentWindow(lookupField);
                if (w != null) {
                    w.setOnEsc(value);
                }
            }
        });
    }

    public void expand(final XElement parent, boolean userQuery) {
        lookupField.onExpand();
        shared.setUserQuery(userQuery);
        if (shared.isExpanded()) {
            if (shared.isUserQuery()) {
                reloadGrid(parent);
            }
            return;
        }

        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                if (menu == null) {

                    menu = lookupField.getGridMenu();
                    menu.addHideHandler(new HideHandler() {
                        @Override
                        public void onHide(HideEvent event) {
                            collapse();
                        }
                    });

                    grid = lookupField.getSelectBrowser(true).getGrid();

                    Action doubleClickAction = new AbstractAction() {
                        @Override
                        protected void onExecute() {
                            selectValue();
                        }
                    };
                    grid.setData("doubleClickAction", doubleClickAction);

                    loadHandler = new LoadHandler<FetchParams, FetchResult<MarkModel>>() {
                        @Override
                        public void onLoad(LoadEvent<FetchParams, FetchResult<MarkModel>> event) {
                            if (shared.isUserQuery()) {
                                final FetchResult<MarkModel> result = event.getLoadResult();
                                shared.setLastLoadResult(result);
                                if (result.getTotalLength() == 1) {
                                    grid.getGrid().getSelectionModel().select(event.getLoadResult().getData().get(0), false);
                                    selectValue();
                                    expandTask.cancel();
                                } else {
                                    showMenu(false);
                                }
                            }
                        }
                    };
                }

                final Action action = (Action) grid.getData("doubleClickAction");
                grid.bindEvent(GridEvent.DOUBLE_CLICK, action);
                grid.bindEvent(GridEvent.ENTER_KEY_PRESS, action);
                grid.bindEvent(GridEvent.INSERT_KEY_PRESS, null);
                grid.bindEvent(GridEvent.DELETE_KEY_PRESS, null);
                grid.setLoadHandler(loadHandler);
                grid.setDefferedContextMenu(null);

                grid.setHeight(301);
                grid.setWidth(lookupField.getElement().getWidth(true) - 6);

                if (menu.getWidgetIndex(grid) == -1) {
                    menu.add(grid);
                }
                menu.setWidth(lookupField.getElement().getWidth(true));

                lookupField.getSelectBrowser(true);

                if (shared.isUserQuery()) {
                    final boolean valid = reloadGrid(parent);
                    if (valid) {
                        updateParentValues(parent);
                        expandTask.delay(500);
                    }
                } else {
                    grid.setNeedReload(false);
                    grid.checkInitialized();
                    showMenu(true);
                }
            }
        });
    }

    private void showMenu(boolean focus) {
        if (!menu.isVisible()) {
            menu.show(lookupField.getElement(), new AnchorAlignment(Anchor.TOP_LEFT, Anchor.BOTTOM_LEFT, true), 0, 3);
            shared.setExpanded(true);
            setFormClosable(false);

            Scheduler.get().scheduleDeferred(new Command() {

                @Override
                public void execute() {
                    if (grid.isMasked()) {
                        grid.getGrid().mask(DefaultMessages.getMessages().loadMask_msg());
                    }
                }
            });
        }

        grid.setMarkedLookup(lookupField.getMultiValues());

        this.hideOnBlur = !focus;
        if (focus) {
            menu.focus();
        }

        Scheduler.get().scheduleDeferred(new Command() {

            @Override
            public void execute() {
                final FetchResult<MarkModel> result = shared.getLastLoadResult();
                if (result != null) {
                    shared.setLastLoadResult(null);
                    grid.getGrid().getStore().replaceAll(result.getData());
                    final SBLiveGridView v = (SBLiveGridView) grid.getGrid().getView();
                    v.getCacheStore().replaceAll(result.getData());
                    // must be auto -- v.updateVScroll();
                }

                if (grid.getSelectedRecords().isEmpty()) {
                    if (grid.getTotalRecordsCount() > 0) {
                        grid.gotoFirstRecord();
                    }
                }
            }
        });

    }

    private boolean reloadGrid(final XElement parent) {
        final String s = Strings.trim(getInputElement(parent).getValue());
        if (Strings.equals(s, lastQuery)) {
            expand(parent, false);
            return false;
        }
        final boolean result = lookupField.prepareForLookup(lookupCellType, s);
        if (result) {
            lastQuery = s;
        }
        return result;
    }

    private void updateParentValues(XElement parent) {
        lookupField.internalClearValue(true);
        if (parent == lookupField.getFieldKey().getElement()) {
            getInputElement(lookupField.getFieldName().getElement()).setValue(Strings.EMPTY);
        } else {
            getInputElement(lookupField.getFieldKey().getElement()).setValue(Strings.EMPTY);
        }
    }

    private void selectValue() {
        lookupField.selectValue(new Runnable() {

            @Override
            public void run() {
                Scheduler.get().scheduleDeferred(new Command() {

                    @Override
                    public void execute() {
                        collapse();
                    }
                });
            }
        });
    }

    public boolean isExpanded() {
        return shared.isExpanded();
    }

    @Override
    protected void onNavigationKey(Cell.Context context, Element parent, String value, NativeEvent event, ValueUpdater<String> valueUpdater) {
        if (event.getKeyCode() == KeyCodes.KEY_DOWN) {
            event.stopPropagation();
            event.preventDefault();
            if (shared.isExpanded()) {
                showMenu(true);
            } else {
                onTriggerClick(context, parent.<XElement>cast(), event, value, valueUpdater);
            }
        }
    }

    @Override
    protected void onTriggerClick(Cell.Context context, XElement parent, NativeEvent event, String value, ValueUpdater<String> updater) {
        super.onTriggerClick(context, parent, event, value, updater);
        if (shared.isExpanded()) {
            collapse();
        } else {
            if (!isReadOnly() && !isDisabled() /*&& !state.isPreventEvents()*/) {
                expand(parent, false);
            }
        }
    }

    @Override
    protected boolean isFocusedWithTarget(Element parent, Element target) {
        boolean result = super.isFocusedWithTarget(parent, target)
                || (menu != null && (menu.getElement().isOrHasChild(target)))
                || (grid != null && (grid.getElement().isOrHasChild(target)));
//            || lookupField.getFieldKey().getCell().getAppearance().triggerIsOrHasChild(lookupField.getFieldKey().getElement(), target)
//            || lookupField.getFieldName().getCell().getAppearance().triggerIsOrHasChild(lookupField.getFieldName().getElement(), target);
        return result;
    }

    private int lastKeyDown;

    @Override
    public void onBrowserEvent(Cell.Context context, Element parent, String value, NativeEvent event, ValueUpdater<String> valueUpdater) {
        if ("keydown".equals(event.getType()) && event.getKeyCode() == KeyCodes.KEY_ENTER) {
            event.preventDefault();
            lastKeyDown = event.getKeyCode();
            return;
        }
        super.onBrowserEvent(context, parent, value, event, valueUpdater);
    }

    @Override
    protected void onBlur(Cell.Context context, XElement parent, String value, NativeEvent event, ValueUpdater<String> valueUpdater) {
        super.onBlur(context, parent, value, event, valueUpdater);
        if (hideOnBlur) {
            collapse();
        }
    }

    @Override
    protected void onKeyDown(Context context, Element parent, String value, NativeEvent event, ValueUpdater<String> valueUpdater) {
        super.onKeyDown(context, parent, value, event, valueUpdater);
    }

    @Override
    protected void onKeyUp(Cell.Context context, Element parent, String value, NativeEvent event, ValueUpdater<String> valueUpdater) {
        super.onKeyUp(context, parent, value, event, valueUpdater);

        final int kc = event.getKeyCode();
        final boolean wasEnter = kc == KeyCodes.KEY_ENTER && lastKeyDown == KeyCodes.KEY_ENTER;
        if (!isReadOnly() && isEditable() && (!event.<XEvent>cast().isSpecialKey() || kc == KeyCodes.KEY_BACKSPACE || kc == KeyCodes.KEY_DELETE || wasEnter)) {
            lastParent = parent.cast();
            if (kc == KeyCodes.KEY_ENTER) {
                expand(lastParent, true);
            }
        }
        lastKeyDown = 0;
    }

    public LookupField getLookupField() {
        return lookupField;
    }

    public LookupCellType getLookupCellType() {
        return lookupCellType;
    }

    public void initSearchHistory() {
        final LookupComboBox cb = lookupCellType == LookupCellType.KEY ? lookupField.getFieldKey() : lookupField.getFieldName();
        this.lastQuery = Strings.clean(getInputElement(cb.getElement()).getValue());
    }
}
