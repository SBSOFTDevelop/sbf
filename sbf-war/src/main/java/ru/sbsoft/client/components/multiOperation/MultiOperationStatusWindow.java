package ru.sbsoft.client.components.multiOperation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import ru.sbsoft.svc.widget.core.client.button.TextButton;
import ru.sbsoft.svc.widget.core.client.event.SelectEvent;
import ru.sbsoft.svc.widget.core.client.form.CheckBox;
import ru.sbsoft.svc.widget.core.client.form.ComboBox;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.BaseWindow;
import ru.sbsoft.client.components.actions.Action;
import ru.sbsoft.client.components.actions.ActionMenu;
import ru.sbsoft.client.components.browser.actions.GridAction;
import ru.sbsoft.client.components.browser.actions.GridRefreshAction;
import ru.sbsoft.client.components.form.ComboBoxUtils;
import ru.sbsoft.client.components.form.FormGridView;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.components.grid.ContextGrid;
import ru.sbsoft.client.components.grid.CustomGridToolBar;
import ru.sbsoft.client.components.grid.GridMode;
import ru.sbsoft.client.components.grid.menu.GridContextMenu;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.DefaultAsyncCallback;
import ru.sbsoft.shared.consts.SBFGridEnum;
import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.shared.model.enums.MultiOperationStatus;
import ru.sbsoft.shared.model.operation.OperationInfo;
import static ru.sbsoft.shared.model.enums.MultiOperationStatus.*;
import ru.sbsoft.client.components.multiOperation.MultiOperationMonitor.AbstractMultiOperationMonitorHandler;
import ru.sbsoft.client.consts.SBFConfig;
import ru.sbsoft.client.consts.SBFVariable;
import ru.sbsoft.model.PageFilterInfo;
import ru.sbsoft.shared.ComparisonEnum;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.TemplateParameterConst;
import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.LocalizedString;
import ru.sbsoft.shared.filter.DateFilterInfo;
import ru.sbsoft.shared.filter.StringFilterInfo;
import ru.sbsoft.shared.services.FetchParams;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.interfaces.NamedItem;
import ru.sbsoft.shared.meta.IColumn;

/**
 * Отображает журнал операций пользователя. Синглетон; для получения используйте
 * {@link getInstance()}
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public final class MultiOperationStatusWindow extends BaseWindow {

    private final MultiOperationStatusGrid multiOperationStatusGrid;
    private final FormGridView multiOperationView;
    private final Long dayLong = 1000L * 60 * 60 * 24;

    private final TextButton exitButton = new TextButton();
    private static MultiOperationStatusWindow INSTANCE;
    private CustomGridToolBar toolbar;
    private boolean isAuto = false;

    private MultiOperationStatusWindow() {
        super();
        multiOperationStatusGrid = new MultiOperationStatusGrid();
        final ComboBox<OperationsPeriod> periodBox = createPeriodCombo();
        multiOperationView = createView(multiOperationStatusGrid, periodBox);

        getWindow().setWidget(multiOperationView);
        setHeading(I18n.get(SBFGeneralStr.labelMonitorOperations));
        setWidth(700);
        setHeight(400);

        MultiOperationMonitor.getInstance().addHandler(new AbstractMultiOperationMonitorHandler() {
            @Override
            public void onUpdate(List<OperationInfo> operationList) {
                if (isAuto) {
                    multiOperationStatusGrid.tryReload();
                }
            }
        });

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                multiOperationStatusGrid.setParentFilters(createPeriodFilters(periodBox.getCurrentValue()));
            }
        });
    }

    private ComboBox<OperationsPeriod> createPeriodCombo() {
        final ComboBox<OperationsPeriod> periodBox = ComboBoxUtils.createNamedComboBox(OperationsPeriod.class);
        periodBox.setEditable(false);
        final ComboHandler handler = new ComboHandler(periodBox, new PeriodChangeListener() {

            @Override
            public void onChange(OperationsPeriod period) {
                final List<FilterInfo> filter = createPeriodFilters(period);
                multiOperationStatusGrid.setParentFilters(filter);
                MultiOperationMonitor.getInstance().forceUpdate();
            }
        });

        periodBox.setValue(OperationsPeriod.DAY);
        return periodBox;
    }

    private List<FilterInfo> createPeriodFilters(OperationsPeriod selectedPeriod) {
        long periodLength;
        switch (selectedPeriod) {
            case MONTH:
                periodLength = dayLong * 30;
                break;
            case WEEK:
                periodLength = dayLong * 6;
                break;
            case ALL:
                periodLength = -1;
                break;
            default:
                periodLength = 0L;
        }

        List<FilterInfo> result = new ArrayList<FilterInfo>();

        if (periodLength >= 0) {
            Date start = new Date(new Date().getTime() - periodLength);
            result.add(new DateFilterInfo("DATE_FROM", start));
        }

        return result;
    }

    @Override
    protected void onAfterFirstAttach() {
        super.onAfterFirstAttach();

        exitButton.setIcon(SBFResources.GENERAL_ICONS.Exit());
        exitButton.setToolTip(I18n.get(SBFGeneralStr.labelCloseWindow));
        exitButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                hide();
            }
        });

        toolbar.insert(exitButton, 0);
    }

    /**
     * Возвращает экземпляр журнала операций в этом приложении.
     *
     * @return экземпляр журнала операций в этом приложении.
     */
    public static MultiOperationStatusWindow getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MultiOperationStatusWindow();
        }
        return INSTANCE;
    }

    private FormGridView createView(MultiOperationStatusGrid grid, ComboBox periodBox) {
        final FormGridView view = new FormGridView(grid, false, true, GridMode.HIDE_INSERT, GridMode.HIDE_DELETE, GridMode.HIDE_UPDATE) {
            private MultiOperationStatusMenu menu;

            @Override
            public GridContextMenu getGridContextMenu() {
                if (menu == null) {
                    menu = new MultiOperationStatusMenu();
                }
                return menu;
            }

        };
        view.setHeaderVisible(false);
        toolbar = view.getGridToolBar();

        addPeriodCombo(view, periodBox);

        return view;
    }

    //TODO +add enable update checkbox
    private void addPeriodCombo(FormGridView view, ComboBox periodsCB) {
        final CustomGridToolBar tb = view.getGridToolBar();

        final CheckBox chb = new CheckBox();
        chb.setValue(isAuto);

        chb.setBoxLabel("Авто обнов.");
        chb.addChangeHandler((ChangeEvent event) -> {
            isAuto = chb.getValue();
        });

        tb.addSeparator();
        tb.add(periodsCB);
        tb.addSeparator();
        tb.add(chb);
    }

    @Override
    public void show() {
        super.show();
        MultiOperationMonitor.getInstance().update();
    }

    class MultiOperationStatusMenu extends GridContextMenu {

        private ActionMenu menu;

        public MultiOperationStatusMenu() {
            super(null);
        }

        @Override
        public ActionMenu getMenu() {
            if (menu != null) {
                return menu;
            }

            menu = new ActionMenu(multiOperationView.getActionManager());
            menu.addAction(new GridRefreshAction(multiOperationStatusGrid));
            menu.addAction(new StartOperationAction(multiOperationStatusGrid));
            menu.addAction(new CancelOperationAction(multiOperationStatusGrid));
            menu.addAction(new ShowLogOperationAction(multiOperationStatusGrid));
            menu.addAction(new RemoveOperationAction(multiOperationStatusGrid));

            return menu;
        }
    }

}

class MultiOperationStatusGrid extends ContextGrid {

    public MultiOperationStatusGrid() {
        super(SBFGridEnum.SR_MULTI_OPERATION_STATUS);
        //getGrid().setLoadMask(false);

        disableContextMenu(false);
        /*((LiveGridView) getGrid().getView()).setCacheSize(50);
        ((PagingLoader) getGrid().getLoader()).setLimit(50);
         */
    }

    @Override
    public Action getDblClickAction() {
        return new OperationSelectionEvent(this);
    }

    @Override
    public PageFilterInfo getFetchParams(FetchParams bean) {
        final PageFilterInfo result = super.getFetchParams(bean);
        List<FilterInfo> tmp = new ArrayList<>(result.getParentFilters());
        tmp.add(new StringFilterInfo(TemplateParameterConst.CURRENT_MODULE_CODE_PARAMETER, ComparisonEnum.eq, GWT.getModuleName()));
        result.setParentFilters(tmp);
        return result;
    }

}

class OperationSelectionEvent extends GridAction {

    public OperationSelectionEvent(BaseGrid grid) {
        super(grid);
    }

    @Override
    public boolean checkEnabled() {
        return isSingeleSelection();
    }

    @Override
    protected void onExecute() {
        final List selectedRecords = getGrid().getSelectedRecords();
        if (selectedRecords.size() != 1) {
            return;
        }
        Row row = (Row) selectedRecords.get(0);
        final MultiOperationLogWindow window = MultiOperationLogWindow.forOperation(getRowId(row));
        window.show(false);
        window.getWindow().toFront();
    }

    private Long getRowId(Row row) {
        String nameId = SBFConfig.readString(SBFVariable.LOG_OPERATION_GRID_NAME_ID);
        if (nameId != null) {
            IColumn column = row.getColumns().getColumnForAlias(nameId);
            if (column != null) {
                return ((BigDecimal) row.getValues().get(column.getIndex())).longValue();
            }
        }
        return row.getRECORD_ID().longValue();
    }
}

abstract class RowGridAction extends GridAction {

    public RowGridAction(BaseGrid grid) {
        super(grid);
    }

    private List<Row> getCurrentRows() {
        return getGrid().getSelectedRecords();
    }

    @Override
    public final boolean checkEnabled() {
        final List<Row> rows = getCurrentRows();
        if (rows == null || rows.size() < 1) {
            return false;
        }

        for (Row row : rows) {
            if (!checkEnabledForRow(row)) {
                return false;
            }
        }

        return true;
    }

    abstract protected boolean checkEnabledForRow(Row row);

    @Override
    protected final void onExecute() {
        final List<Row> rows = getCurrentRows();

        if (rows == null || rows.isEmpty()) {
            return;
        }

        for (Row row : rows) {
            onExecuteRow(row);
        }
    }

    abstract protected void onExecuteRow(Row row);

}

abstract class OperationRowGridAction extends RowGridAction {

    public OperationRowGridAction(BaseGrid grid) {
        super(grid);
    }

    @Override
    protected boolean checkEnabledForRow(Row row) {
        final Long id = row.getRECORD_ID().longValue();
        MultiOperationStatus status = MultiOperationStatus.valueOf(row.getString("STATUS"));
        return checkEnabledForOperation(id, status);
    }

    abstract protected boolean checkEnabledForOperation(Long id, MultiOperationStatus status);

    @Override
    protected void onExecuteRow(Row row) {
        final Long id = row.getRECORD_ID().longValue();
        MultiOperationStatus status = MultiOperationStatus.valueOf(row.getString("STATUS"));
        executeForOperation(id, status);
    }

    abstract protected void executeForOperation(Long id, MultiOperationStatus status);

}

class StartOperationAction extends OperationRowGridAction {

    public StartOperationAction(BaseGrid grid) {
        super(grid);
        setCaption(I18n.get(SBFGeneralStr.labelRun));
        setToolTip(I18n.get(SBFGeneralStr.hintRun));
        setIcon16(SBFResources.GENERAL_ICONS.Run16());
        setIcon24(SBFResources.GENERAL_ICONS.Run());
    }

    @Override
    protected boolean checkEnabledForOperation(Long id, MultiOperationStatus status) {
        return status == CREATED;
    }

    @Override
    protected void executeForOperation(Long id, MultiOperationStatus status) {
        SBFConst.MULTI_OPERATION_SERVICE.start(id, new DefaultAsyncCallback<Void>() {

            @Override
            public void onResult(Void result) {
                MultiOperationMonitor.getInstance().forceUpdate();
            }
        });
    }
}

class CancelOperationAction extends OperationRowGridAction {

    public CancelOperationAction(BaseGrid grid) {
        super(grid);
        setCaption(I18n.get(SBFGeneralStr.labelCancelOperation));
        setToolTip(I18n.get(SBFGeneralStr.hintCancelOperation));
        setIcon16(SBFResources.GENERAL_ICONS.Stop16());
        setIcon24(SBFResources.GENERAL_ICONS.Stop());
    }

    @Override
    protected boolean checkEnabledForOperation(Long id, MultiOperationStatus status) {
        return status == STARTED || status == READY_TO_START || status == CREATED;
    }

    @Override
    protected void executeForOperation(Long id, MultiOperationStatus status) {
        SBFConst.MULTI_OPERATION_SERVICE.cancel(id, new DefaultAsyncCallback<Void>() {

            @Override
            public void onResult(Void result) {
                MultiOperationMonitor.getInstance().forceUpdate();
            }
        });
    }
}

class ShowLogOperationAction extends OperationRowGridAction {

    public ShowLogOperationAction(BaseGrid grid) {
        super(grid);

        //показать лог (окно с прогресс-баром)операции
        setCaption(I18n.get(SBFGeneralStr.labelShowLogOperation));
        setToolTip(I18n.get(SBFGeneralStr.hintShowLogOperation));
        setIcon16(SBFResources.BROWSER_ICONS.Spec16());
        setIcon24(SBFResources.BROWSER_ICONS.Spec());
    }

    @Override
    protected boolean checkEnabledForOperation(Long id, MultiOperationStatus status) {
        return true;
    }

    @Override
    protected void executeForOperation(Long id, MultiOperationStatus status) {
        final List selectedRecords = getGrid().getSelectedRecords();
        if (selectedRecords.size() != 1) {
            return;
        }
        Row row = (Row) selectedRecords.get(0);
        Long operationId = row.getRECORD_ID().longValue();
        final MultiOperationLogWindow window = MultiOperationLogWindow.forOperation(operationId);
        window.show(false);
        window.getWindow().toFront();
    }
}

class RemoveOperationAction extends OperationRowGridAction {

    public RemoveOperationAction(BaseGrid grid) {
        super(grid);
        setCaption(I18n.get(SBFGeneralStr.labelRemoveOperation));
        setToolTip(I18n.get(SBFGeneralStr.hintRemoveOperation));
        setIcon16(SBFResources.GENERAL_ICONS.OK16());
        setIcon24(SBFResources.GENERAL_ICONS.OK());
    }

    @Override
    protected boolean checkEnabledForOperation(Long id, MultiOperationStatus status) {
        return status == CANCELED || status == DONE || status == ERROR;
    }

    @Override
    protected void executeForOperation(final Long id, MultiOperationStatus status) {
        SBFConst.MULTI_OPERATION_SERVICE.setVisible(id, false, new DefaultAsyncCallback<Void>() {

            @Override
            public void onResult(Void result) {
                if (MultiOperationLogWindow.isWindowOpened(id)) {
                    final MultiOperationLogWindow window = MultiOperationLogWindow.forOperation(id);
                    window.hide();
                }
                getGrid().tryReload();
            }
        });
    }
}

interface PeriodChangeListener {

    void onChange(OperationsPeriod period);
}

class ComboHandler implements ChangeHandler, SelectionHandler<OperationsPeriod> {

    ComboBox<OperationsPeriod> combo;
    PeriodChangeListener changeListener;

    ComboHandler(ComboBox<OperationsPeriod> combo, PeriodChangeListener changeListener) {
        this.changeListener = changeListener;
        this.combo = combo;
        this.combo.addSelectionHandler(this);
        this.combo.addChangeHandler(this);
    }

    @Override
    public void onChange(ChangeEvent event) {
        changeListener.onChange(combo.getCurrentValue());
    }

    @Override
    public void onSelection(SelectionEvent<OperationsPeriod> event) {
        changeListener.onChange(combo.getCurrentValue());
    }

}

enum OperationsPeriod implements NamedItem {

    DAY(SBFGeneralStr.labelDay),
    WEEK(SBFGeneralStr.labelWeek),
    MONTH(SBFGeneralStr.labelMonth),
    ALL(SBFGeneralStr.labelAll);

    private final ILocalizedString itemName;

    OperationsPeriod(I18nResourceInfo resourceInfo) {
        this.itemName = new LocalizedString(resourceInfo);
    }

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public ILocalizedString getItemName() {
        return itemName;
    }

}
