package ru.sbsoft.client.components.operation;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;
import ru.sbsoft.svc.core.client.util.DelayedTask;
import ru.sbsoft.svc.core.client.util.Margins;
import ru.sbsoft.svc.data.shared.loader.PagingLoader;
import ru.sbsoft.svc.widget.core.client.Component;
import ru.sbsoft.svc.widget.core.client.ContentPanel;
import ru.sbsoft.svc.widget.core.client.ProgressBar;
import ru.sbsoft.svc.widget.core.client.button.TextButton;
import ru.sbsoft.svc.widget.core.client.container.BorderLayoutContainer;
import ru.sbsoft.svc.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import ru.sbsoft.svc.widget.core.client.container.MarginData;
import ru.sbsoft.svc.widget.core.client.event.SelectEvent;
import ru.sbsoft.svc.widget.core.client.form.TextArea;
import ru.sbsoft.svc.widget.core.client.grid.ColumnConfig;
import ru.sbsoft.svc.widget.core.client.grid.ColumnModel;
import ru.sbsoft.svc.widget.core.client.grid.LiveGridView;
import ru.sbsoft.svc.widget.core.client.toolbar.ToolBar;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.BaseWindow;
import ru.sbsoft.client.components.browser.actions.GridAction;
import ru.sbsoft.client.components.dialog.ErrorDialog;
import ru.sbsoft.client.components.form.FormGridView;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.components.grid.ContextGrid;
import ru.sbsoft.client.components.grid.GridEvent;
import ru.sbsoft.client.components.grid.GridMode;
import ru.sbsoft.client.components.grid.column.CustomColumnConfig;
import ru.sbsoft.client.components.grid.column.cell.CustomCell;
import ru.sbsoft.client.components.multiOperation.MultiOperationLogWindow;
import ru.sbsoft.client.components.multiOperation.MultiOperationMonitor;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.client.utils.DefaultAsyncCallback;
import ru.sbsoft.client.utils.VLC;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.api.i18n.I18nResource;
import ru.sbsoft.shared.filter.BooleanFilterInfo;
import ru.sbsoft.shared.filter.LongFilterInfo;
import ru.sbsoft.shared.interfaces.GridType;
import ru.sbsoft.shared.meta.IColumns;
import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.shared.model.MarkModel;
import ru.sbsoft.shared.model.OperationEventType;
import static ru.sbsoft.shared.model.OperationEventType.FINISH_OK;
import static ru.sbsoft.shared.model.OperationEventType.FINISH_ERROR;
import ru.sbsoft.shared.model.enums.MultiOperationStatus;
import ru.sbsoft.shared.model.operation.OperationInfo;
import ru.sbsoft.shared.services.ServiceConst;
import ru.sbsoft.client.i18n.GwTi18nUnit;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;

/**
 * Отображает ход выполнения операции. Самостоятельно уже не используется (см.
 * {@link MultiOperationLogWindow}).
 */
public abstract class BaseOperationProgressForm extends BaseWindow {

    private static final NumberFormat PERCENT_FORMAT = NumberFormat.getFormat("0.00 %");
    private final AbstractOperation operation;
    //
    private final TextButton exitButton = new TextButton();
    private final TextButton cancelOperationButton = new TextButton();
    private final ProgressBar progressBar = new ProgressBar();
    //
    protected FormGridView loggingBrowser;
    protected BaseGrid loggingGrid;
    protected final TextArea infoField = new TextArea();
    //
    protected long operationId;
    protected DelayedTask delayedTask;
    protected OperationEventType resultEventType;

    private String startProcessMessage;
    private String endProcessMessage;
    private String endProcessErrorMessage;
    private final I18nResource i18nResource;

    public BaseOperationProgressForm(AbstractOperation operation, String title) {
        super();
        this.operation = operation;
        this.i18nResource = GwTi18nUnit.getInstance();
        setPixelSize(800, 600);
        setHeading(I18n.get(SBFGeneralStr.labelLogOperation));
        setModal(true);
        setClosable(false);
    }

    public long getOperationId() {
        return operationId;
    }

    @Override
    protected void onAfterFirstAttach() {
        super.onAfterFirstAttach();
        fillViewForm();
        MultiOperationMonitor.getInstance().addHandler(new MultiOperationMonitor.SimpleConcreteOperationMonitor(getOperationId()) {

            @Override
            public void onUpdateOperation(OperationInfo operationInfo) {
                final double progress = operationInfo.getProgress().doubleValue() / 100;
                
                progressBar.updateProgress(progress, PERCENT_FORMAT.format(progress) 
                        + (operationInfo.getProcessComment() != null ?  " " + operationInfo.getProcessComment().trim() : ""));
            }
        });
    }

    private void fillViewForm() {
        addRegion(createMainContainer(), VLC.FILL);
        fillToolBar(loggingBrowser.getGridToolBar());
    }

    protected void fillToolBar(final ToolBar toolBar) {
        cancelOperationButton.setIcon(SBFResources.GENERAL_ICONS.Stop());
        cancelOperationButton.setToolTip(I18n.get(SBFGeneralStr.hintInterruptOperation));
        cancelOperationButton.setEnabled(false);
        cancelOperationButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                SBFConst.MULTI_OPERATION_SERVICE.cancel(getOperationId(), new DefaultAsyncCallback<Void>() {

                    @Override
                    public void onResult(Void result) {
                        MultiOperationMonitor.getInstance().forceUpdate();
                    }
                });
            }
        });
        toolBar.insert(cancelOperationButton, 0);
        MultiOperationMonitor.getInstance().addHandler(new MultiOperationMonitor.SimpleConcreteOperationMonitor(getOperationId()) {

            @Override
            public void onUpdateOperation(OperationInfo operationInfo) {
                updateCancelButtonState(operationInfo.getStatus());
            }

        });

        exitButton.setIcon(SBFResources.GENERAL_ICONS.Exit());
        exitButton.setToolTip(I18n.get(SBFGeneralStr.labelCloseWindow));
        exitButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                hide();
            }
        });
        toolBar.insert(exitButton, 0);

    }

    private void updateCancelButtonState(MultiOperationStatus operationStatus) {
        boolean mayCancel = operationStatus == MultiOperationStatus.CREATED || operationStatus == MultiOperationStatus.READY_TO_START || operationStatus == MultiOperationStatus.STARTED;
        cancelOperationButton.setEnabled(mayCancel);
    }

    private BorderLayoutContainer createMainContainer() {
        BorderLayoutData northData = new BorderLayoutContainer.BorderLayoutData();
        northData.setMargins(new Margins(0));
        northData.setCollapsible(false);
        northData.setSize(55);

        BorderLayoutData southData = new BorderLayoutContainer.BorderLayoutData();
        southData.setMargins(new Margins(5));
        southData.setCollapsible(true);
        southData.setCollapseMini(true);
        southData.setSize(150);

        final BorderLayoutContainer container = new BorderLayoutContainer();
        container.setNorthWidget(createProgressComponent(), northData);
        container.setCenterWidget(createLogComponent(), new MarginData(0, 5, 0, 5));
        container.setSouthWidget(createDetailsComponent(), southData);
        return container;
    }

    private Component createProgressComponent() {
        ContentPanel cp = new ContentPanel();
        cp.setHeading(I18n.get(SBFGeneralStr.labelOperationProgress));
        //progressBar.setHeight(25);
        cp.setWidget(progressBar);
        cp.setBodyBorder(false);
        return cp;
    }

    protected abstract GridType getLogGridType();

    protected boolean hasMenu() {
        return true;
    }

    private class LogGrid extends ContextGrid {

        public LogGrid() {
            super(getLogGridType());
            disableContextMenu(false);
        }

        @Override
        protected void onRightClick(Event event) {
        }

        @Override
        protected ColumnModel<Row> createColumnModel(IColumns columns, boolean isMark) {
            final ColumnModel<Row> result = super.createColumnModel(columns, isMark);
            for (int i = 0; i < result.getColumnCount(); i++) {
                final ColumnConfig<Row, Object> c = result.getColumn(i);
                c.setSortable(false);
                if (c instanceof CustomColumnConfig) {
                    CustomColumnConfig columnConfig = (CustomColumnConfig) c;
                    if ("MESSAGE".equals(columnConfig.getColumn().getAlias())) {
                        final CustomCell<String> customCell = new CustomCell<String>() {

                            @Override
                            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                                final Row row = (Row) loggingGrid.getGrid().getStore().get(context.getIndex());
                                final OperationEventType type = OperationEventType.find(row.getString("LTYPE"));
                                applyText(sb, formatMessage((type != null) ? type : OperationEventType.INFO, value == null ? "" : value));
                                applyStyle(context);
                            }
                        };
                        customCell.setStyleChecker(((CustomCell) columnConfig.getCell()).getStyleChecker());
                        customCell.setConfig(columnConfig);
                        columnConfig.setCell(customCell);
                    }
                }
            }
            return result;
        }

        private String formatMessage(OperationEventType type, String value) {
            switch (type) {
                case RESULT:
                case EXPORT:
                    return type.getMessage(i18nResource) + ": " + link(value);
                case START:
                    return Strings.coalesce(value, startProcessMessage, type.getMessage(i18nResource));
                case FINISH_OK:
                    return Strings.coalesce(value, endProcessMessage, type.getMessage(i18nResource));
                case FINISH_ERROR:
                    return Strings.coalesce(value, endProcessErrorMessage, type.getMessage(i18nResource));
            }
            return value;
        }

        private String link(String value) {
            final String[] values = value.split("\\|");
            return "<a href='" + address(values[0]) + "'>" + values[1] + "</a>";
        }

        private String address(String fileId) {
            return ClientUtils.getAppURL() + ServiceConst.DOWNLOAD_OPERATION_SERVICE
                    + "?" + ServiceConst.FILE_ID_PARAM + "=" + fileId
                    + "&" + ServiceConst.BROWSER_PARAM + "=" + ClientUtils.getUserAgent();
        }
    }

    private Component createLogComponent() {
        loggingGrid = new LogGrid();

        ((LiveGridView) loggingGrid.getGrid().getView()).setCacheSize(50);
        ((PagingLoader) loggingGrid.getGrid().getLoader()).setLimit(50);

        loggingGrid.setParentFilter(new LongFilterInfo("OPERATION_RECORD_ID", getOperationId()));
        loggingGrid.getGrid().setLoadMask(false);
        loggingGrid.getGrid().getSelectionModel().addSelectionHandler(new SelectionHandler<Row>() {
            @Override
            public void onSelection(SelectionEvent<Row> event) {
                final Row item = event.getSelectedItem();
                infoField.setValue(item == null ? null : item.getString("MESSAGE"));
            }
        });

        loggingBrowser = new FormGridView(loggingGrid, false, hasMenu(), GridMode.HIDE_INSERT, GridMode.HIDE_DELETE, GridMode.HIDE_UPDATE);
        loggingBrowser.setHeadingText(I18n.get(SBFGeneralStr.labelProtocol));

        loggingGrid.bindEvent(GridEvent.DOUBLE_CLICK, new GridAction(loggingGrid) {

            @Override
            public boolean checkEnabled() {
                return isSingeleSelection() && hasTraceStack((Row) getGrid().getSelectedRecords().get(0));
            }

            private boolean hasTraceStack(Row row) {
                return !Strings.isEmpty(row.getString("TRACE_FLAG"), true);
            }

            @Override
            protected void onExecute() {
                final List selectedRecords = getGrid().getSelectedRecords();
                if (selectedRecords.size() == 1) {
                    Row row = (Row) selectedRecords.get(0);

//                    FetchParams c = new FetchParams();
//                    c.setFilters(FilterInfo.list(new BooleanFilterInfo("EX", Boolean.TRUE)));
//                    final PageFilterInfo fetchParams = SystemGrid.convertLoadConfig(c);
//                    SystemGrid.injectModuleIntoFilter(fetchParams);
//
//                    SBFConst.GRID_SERVICE.getModelRow(loggingGrid.__getGridContext(), fetchParams, row.getPrimaryKeyValue(),
//                            new DefaultAsyncCallback<MarkModel>() {
//
//                                @Override
//                                public void onResult(MarkModel result) {
//                                    Row row = (Row) result;
//                                    row.setColumns(loggingGrid.getMetaInfo());
//
//                                    String message = row.getString("MESSAGE");
//                                    String trace = row.getString("TRACE");
//
//                                    SafeHtmlBuilder s = new SafeHtmlBuilder();
//                                    if (message != null) {
//                                        s.appendEscapedLines(message);
//                                        s.appendHtmlConstant("<br><br>");
//                                    }
//                                    s.appendHtmlConstant(Strings.coalesce(trace));
//                                    new ErrorDialog().show(s.toSafeHtml().asString());
//                                }
//                            }
//                    );
                    loggingGrid.loadSingleRow(row.getPrimaryKeyValue(), FilterInfo.list(new BooleanFilterInfo("EX", Boolean.TRUE)),
                            new DefaultAsyncCallback<MarkModel>() {

                        @Override
                        public void onResult(MarkModel result) {
                            Row row = (Row) result;
                            row.setColumns(loggingGrid.getMetaInfo());

                            String message = row.getString("MESSAGE");
                            String trace = row.getString("TRACE");

                            SafeHtmlBuilder s = new SafeHtmlBuilder();
                            if (message != null) {
                                s.appendEscapedLines(message);
                                s.appendHtmlConstant("<br><br>");
                            }
                            s.appendHtmlConstant(Strings.coalesce(trace));
                            new ErrorDialog().show(s.toSafeHtml().asString());
                        }
                    }
                    );
                }
            }
        });

        ContentPanel cp = new ContentPanel();
        cp.setHeaderVisible(false);
        cp.setHeading(I18n.get(SBFGeneralStr.labelProtocol));
        cp.setWidget(loggingBrowser);
        cp.setBodyBorder(false);

        return cp;
    }

    private Component createDetailsComponent() {
        infoField.setBorders(false);
        ContentPanel cp = new ContentPanel();
        cp.setHeading(I18n.get(SBFGeneralStr.labelMessage));
        cp.setWidget(infoField);
        cp.setBodyBorder(false);
        return cp;
    }

//    public void show(final OperationStatus status) {
//        operationId = status.getOperationId();
//        setStatus(status);
//        delayedTask = new DelayedTask() {
//
//            @Override
//            public void onExecute() {
//                loggingGrid.tryReload();
//                requestStaus();
//                delayedTask.delay(500);
//            }
//        };
//        delayedTask.delay(10);
//        super.show();
//    }
//
//    protected void requestStaus() {
//        SBFConst.OPERATION_SERVICE.getProcessStatus(operationId, null, new AsyncCallback<OperationStatus>() {
//            @Override
//            public void onFailure(final Throwable caught) {
//                ClientUtils.alertException(caught);
//            }
//
//            @Override
//            public void onSuccess(final OperationStatus result) {
//                setStatus(result);
//            }
//        });
//    }
//
//    private void setStatus(final OperationStatus status) {
//        if (status != null) {
//            final double progress = status.getProgressValue();
//            String notes = (status.getNotes() == null) ? Strings.EMPTY : status.getNotes() + " / ";
//            progressBar.updateProgress(progress, notes + PERCENT_FORMAT.format(progress));
//
//            final List<OperationEvent> events = status.getEvents();
//            if (events != null && !events.isEmpty()) {
//                finishProgress(events.get(0).getType());
//            }
//        }
//    }
//    public HandlerRegistration addCompleteHandler(CompleteEvent.CompleteHandler handler) {
//        return addHandler(handler, CompleteEvent.getType());
//    }
    private boolean isCompleted() {
        return resultEventType != null;
    }

    public OperationEventType getResultEventType() {
        return resultEventType;
    }

//    protected void finishProgress(final OperationEventType event) {
//        if (event == FINISH_OK || event == FINISH_ERROR) {
//            delayedTask.cancel();
//            resultEventType = event;
//            exitButton.setToolTip("Закрыть окно");
//            progressBar.updateProgress(1D, PERCENT_FORMAT.format(1D));
//        }
//
//        if (resultEventType == FINISH_OK && operation != null) {
//            operation.onCompleted();
//            //fireEvent(new CompleteEvent(operation));
//        }
//    }

    public String getStartProcessMessage() {
        return startProcessMessage;
    }

    public void setStartProcessMessage(String startProcessMessage) {
        this.startProcessMessage = startProcessMessage;
    }

    public String getEndProcessMessage() {
        return endProcessMessage;
    }

    public void setEndProcessMessage(String endProcessMessage) {
        this.endProcessMessage = endProcessMessage;
    }

    public String getEndProcessErrorMessage() {
        return endProcessErrorMessage;
    }

    public void setEndProcessErrorMessage(String endProcessErrorMessage) {
        this.endProcessErrorMessage = endProcessErrorMessage;
    }
}
