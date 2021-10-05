package ru.sbsoft.client.components.grid;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.BindingPropertySet;
import com.sencha.gxt.core.client.GXTLogConfiguration;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.util.KeyNav;
import com.sencha.gxt.core.client.util.Size;
import com.sencha.gxt.core.shared.event.GroupingHandlerRegistration;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.SortInfoBean;
import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.data.shared.event.StoreClearEvent;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent;
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.data.shared.event.StoreHandlers;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent;
import com.sencha.gxt.data.shared.event.StoreSortEvent;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent;
import com.sencha.gxt.data.shared.loader.BeforeLoadEvent;
import com.sencha.gxt.data.shared.loader.ListLoader;
import com.sencha.gxt.data.shared.loader.LoadEvent;
import com.sencha.gxt.data.shared.loader.LoadExceptionEvent;
import com.sencha.gxt.data.shared.loader.LoadHandler;
import com.sencha.gxt.data.shared.loader.LoaderHandler;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.event.BodyScrollEvent;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent.CellDoubleClickHandler;
import com.sencha.gxt.widget.core.client.event.ColumnMoveEvent;
import com.sencha.gxt.widget.core.client.event.ColumnWidthChangeEvent;
import com.sencha.gxt.widget.core.client.event.LiveGridViewUpdateEvent.LiveGridViewUpdateHandler;
import com.sencha.gxt.widget.core.client.grid.AggregationRenderer;
import com.sencha.gxt.widget.core.client.grid.AggregationRowConfig;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnHeader;
import com.sencha.gxt.widget.core.client.grid.ColumnHeaderChangeEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnHiddenChangeEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.ColumnModelHandlers;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.StoreSelectionModel;
import com.sencha.gxt.widget.core.client.tips.QuickTip;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import java.util.logging.Logger;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.SBFEntryPoint;
import ru.sbsoft.client.components.*;
import ru.sbsoft.client.components.browser.Browser;
import ru.sbsoft.client.components.form.BaseForm;
import ru.sbsoft.client.components.grid.AggregateChangeEvent.AggregateChangeHandler;
import ru.sbsoft.client.components.grid.ChangeFiltersEvent.ChangeFiltersHandler;
import ru.sbsoft.client.components.grid.column.AddressColumnConfig;
import ru.sbsoft.client.components.grid.column.CurrencyColumnConfig;
import ru.sbsoft.client.components.grid.column.CustomColumnConfig;
import ru.sbsoft.client.components.grid.column.DateColumnConfig;
import ru.sbsoft.client.components.grid.column.DateTimeColumnConfig;
import ru.sbsoft.client.components.grid.column.IconColumnConfig;
import ru.sbsoft.client.components.grid.column.LongColumnConfig;
import ru.sbsoft.client.components.grid.column.LookupColumnConfig;
import ru.sbsoft.client.components.grid.column.StringColumnConfig;
import ru.sbsoft.client.components.grid.column.TimestampColumnConfig;
import ru.sbsoft.client.components.grid.column.cell.CustomCell;
import ru.sbsoft.client.components.browser.filter.FilterWindow;
import static ru.sbsoft.client.components.grid.GridConsts.AUTO_EXPAND_MAX_VALUE;
import static ru.sbsoft.client.components.grid.GridConsts.AUTO_EXPAND_MIN_VALUE;
import static ru.sbsoft.client.components.grid.GridConsts.ROW_CACHE_SIZE;
import ru.sbsoft.client.components.grid.aggregate.AggregateWindow;
import ru.sbsoft.client.components.grid.column.YearMonthDayColumnConfig;
import ru.sbsoft.client.components.grid.sort.SortWindow;
import ru.sbsoft.client.consts.SBFConfig;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.consts.SBFVariable;
import ru.sbsoft.client.schedule.*;
import ru.sbsoft.client.utils.CliUtil;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.client.utils.DefaultAsyncCallback;
import ru.sbsoft.client.utils.RoleCheker;
import ru.sbsoft.model.PageFilterInfo;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FormContext;
import ru.sbsoft.shared.GridContext;
import static ru.sbsoft.shared.consts.Formats.BOOL_DEFAULT;
import ru.sbsoft.shared.exceptions.FilterRequireException;
import ru.sbsoft.shared.filter.KeyFilterInfo;
import ru.sbsoft.shared.filter.StringFilterInfo;
import ru.sbsoft.shared.interfaces.GridType;
import ru.sbsoft.shared.meta.AggregatesWrapper;
import ru.sbsoft.shared.meta.BigDecimalWrapper;
import ru.sbsoft.shared.meta.Column;
import static ru.sbsoft.shared.meta.ColumnType.*;
import ru.sbsoft.shared.meta.ColumnType;
import ru.sbsoft.shared.meta.Columns;
import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.shared.meta.StringWrapper;
import ru.sbsoft.shared.meta.Wrapper;
import ru.sbsoft.shared.meta.config.ColumnCfg;
import ru.sbsoft.shared.meta.filter.FilterTemplate;
import ru.sbsoft.shared.meta.filter.FilterTemplateItem;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.model.LookupInfoModel;
import ru.sbsoft.shared.model.MarkModel;
import ru.sbsoft.shared.model.SortDirection;
import ru.sbsoft.shared.model.SortingInfo;
import ru.sbsoft.shared.renderer.Renderer;
import ru.sbsoft.shared.services.FetchResult;
import ru.sbsoft.shared.services.FetchParams;
import ru.sbsoft.shared.services.IConfigServiceAsync;
import ru.sbsoft.shared.services.IGridServiceAsync;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;
import ru.sbsoft.shared.api.i18n.consts.SBFExceptionStr;
import ru.sbsoft.shared.api.i18n.i18nUtils;
import ru.sbsoft.shared.meta.BigDecimalAggregatesWrapper;
import ru.sbsoft.shared.meta.ColumnWrapType;
import ru.sbsoft.shared.meta.filter.FilterBox;
import ru.sbsoft.shared.meta.filter.StoredFilterPath;
import ru.sbsoft.shared.model.CustomReportInfo;
import ru.sbsoft.shared.param.StringParamInfo;

/**
 * Наполняет функционалом {@link BaseGrid}
 *
 * @author balandin
 * @param <DataInfoModel>
 * @since Oct 3, 2013 6:10:18 PM
 */
public abstract class SystemGrid<DataInfoModel extends MarkModel>
        extends SimpleContainer
        implements GridConsts, IHasParentElement, LoaderHandler.HasLoadHandlers<FetchParams, FetchResult<MarkModel>>, HasParentFilters {

    private static Logger logger = Logger.getLogger(SystemGrid.class.getName());

    @BindingPropertySet.PropertyName("gxt.theme")
    public interface ActiveTheme extends BindingPropertySet {

        @PropertyValue(value = "gray", warn = false)
        boolean isGray();

        @PropertyValue(value = "blue", warn = false)
        boolean isBlue();

        @PropertyValue(value = "neptune", warn = false)
        boolean isNeptune();

        @PropertyValue(value = "triton", warn = false)
        boolean isTriton();
    }

    private class GridStoreHandlers implements StoreHandlers<DataInfoModel> {

        @Override
        public void onAdd(StoreAddEvent<DataInfoModel> event) {
        }

        @Override
        public void onRemove(StoreRemoveEvent<DataInfoModel> event) {
        }

        @Override
        public void onFilter(StoreFilterEvent<DataInfoModel> event) {
        }

        @Override
        public void onClear(StoreClearEvent<DataInfoModel> event) {
        }

        @Override
        public void onDataChange(StoreDataChangeEvent<DataInfoModel> event) {
        }

        // replace for GridSelectionModel.onUpdate(). This method is colled from onUpdate and onRecordChange only
        @Override
        public void onUpdate(StoreUpdateEvent<DataInfoModel> event) {
            stateUpdater.requestUpdate();
        }

        @Override
        public void onRecordChange(StoreRecordChangeEvent<DataInfoModel> event) {
            stateUpdater.requestUpdate();
        }

        @Override
        public void onSort(StoreSortEvent<DataInfoModel> event) {
        }

    }

    private static class GridEventInfo {

        private final GridEvent gridEvent;
        private final boolean stopPropagation;
        private final boolean preventDafault;

        public GridEventInfo(GridEvent gridEvent, boolean stopPropagation, boolean preventDafault) {
            this.gridEvent = gridEvent;
            this.stopPropagation = stopPropagation;
            this.preventDafault = preventDafault;
        }

        public GridEvent getGridEvent() {
            return gridEvent;
        }

        public boolean isStopPropagation() {
            return stopPropagation;
        }

        public boolean isPreventDafault() {
            return preventDafault;
        }
    }

    public static interface KeyEventListener {

        void onKeyPress(NativeEvent ev);
    }

    private KeyNav gridKeyNav = new KeyNav() {
        @Override
        public void onKeyPress(NativeEvent event) {
            int keyCode = event.getKeyCode();
            GridEventInfo inf = keyEvents.get(keyCode);
            exec(inf, event, keyCode);
//            for(KeyEventListener l : keyListeners){
//                l.onKeyPress(event);
//            }
        }

        private void exec(GridEventInfo inf, NativeEvent event, int keyCode) {
            if (inf != null) {
                fireGridEvent(inf.getGridEvent());
                if (inf.isStopPropagation()) {
                    event.stopPropagation();
                }
                if (inf.isPreventDafault()) {
                    event.preventDefault();
                }
            }
        }
    };
    private final Map<Integer, GridEventInfo> keyEvents = new HashMap<>();
//    private final List<KeyEventListener> keyListeners = new ArrayList<>();

    private static final boolean GRID_CONFIG = SBFConfig.readBool(SBFVariable.GRID_CONFIG);
    private static final int GRID_TYPE_MASK_MAIN = 0x1;
    private static final int GRID_TYPE_MASK_FROZEN = 0x2;
    private static final int GRID_TYPE_MASK_BOTH = GRID_TYPE_MASK_MAIN | GRID_TYPE_MASK_FROZEN;

    private GridInitialLoader initialLoader;
    private boolean initialized = false;
    private boolean needReload = true;
    private boolean readOnly = false;
    private boolean wasChanged = false;
    protected boolean forceFilter = false;
    //
    protected static final IConfigServiceAsync CONFIG_RPC = SBFConst.CONFIG_SERVICE;
    protected IGridServiceAsync RPC = SBFConst.GRID_SERVICE;
    //
    private AsyncCallback<MarkModel> loadRowCallback;
    private AsyncCallback<MarkModel> addRowCallback;
    private AsyncCallback<List<BigDecimal>> getOnlyIdsCallback;
    private AsyncCallback<List<BigDecimal>> findMarkCallback;
    //
    private List<GridReloadListener> reloadListeners = new ArrayList<>();
    //
    protected final List<FilterInfo> parentFilters = new ArrayList<FilterInfo>();
    
    protected StringFilterInfo tmpFilter;
    //
    private final BorderLayoutContainer gridContainer = new BorderLayoutContainer();
    //private final HorizontalLayoutContainer gridContainer = new HorizontalLayoutContainer();
    private final SBGrid<DataInfoModel> grid;
    //private final ListStore<DataInfoModel> gridStore = new SBListStore<DataInfoModel>(MARK_PROP.key());
    private final PagingLoader<FetchParams, FetchResult<MarkModel>> loader;
    //private final GridSelectionModel<DataInfoModel> selectionModel;
    private ColumnConfig markColumn;
    //
    private final MarkSet marker = new MarkSet(this);
    private final MarkScanBuffer scanBuffer = new MarkScanBuffer(marker);
    private ValueProvider<DataInfoModel, BigDecimal> keyProvider;
    //
    private boolean clonable = false;
    //
    private HandlerHolder loadHandlerHolder;
    //
    private Columns metaInfo;
    protected FilterBox filters;
    private boolean autoLoad = true;
    //
    private boolean dynamic = false;
    //
    private boolean autoFit = true;

    private boolean clearMarksOnDataLoad = true;

    private ISelectionModelAdapter<DataInfoModel> selectionModelAdapter = null;

    private final DefaultContainerHolder parentHolder = new DefaultContainerHolder();

    private final ActiveTheme theme = GWT.create(ActiveTheme.class);

    private final StateUpdater stateUpdater = new StateUpdater(this::onUpdateState);

    private final GridIntermediator<DataInfoModel> gridHelper;

    private ClientUtils.ErrorHandler errorLoadHandler = null;

    private final InitManager initManager = new InitManager();
    
    @Override
    public void setParentElement(IElementContainer parentElement) {
        parentHolder.setParentElement(parentElement);
    }

    @Override
    public IElementContainer getParentElement() {
        return parentHolder.getParentElement();
    }

    public SystemGrid() {
        loader = createLoader();
        grid = createGrid(loader);
        gridHelper = new GridIntermediator<>(grid);
        gridKeyNav.bind(grid);
        MarginData centerData = new MarginData();
        gridContainer.setCenterWidget(grid, centerData);
        setWidget(gridContainer);

        bindGridEventOnKey(32, GridEvent.SPACE_KEY_PRESS);
        bindGridEventOnKey(KeyCodes.KEY_ENTER, GridEvent.ENTER_KEY_PRESS);
        bindGridEventOnKey(45, GridEvent.INSERT_KEY_PRESS);
        bindGridEventOnKey(KeyCodes.KEY_DELETE, GridEvent.DELETE_KEY_PRESS);
    }

    public void setAutoFit(boolean autoFit) {
        this.autoFit = autoFit;

    }

    public boolean isAutoFit() {
        return autoFit;

    }

    protected void bindGridEventOnKey(int keyCode, final GridEvent gridEvent) {
        keyEvents.put(keyCode, new GridEventInfo(gridEvent, false, false));
    }

//    protected void addKeyListener(KeyEventListener l){
//        if(!keyListeners.contains(l)){
//            keyListeners.add(l);
//        }
//    }
    protected void addInitializable(Initializable ini) {
        initManager.addInitializable(ini);
    }

    protected void removeInitializable(Initializable ini) {
        initManager.removeInitializable(ini);
    }

    public boolean isMarked(DataInfoModel m) {
        return marker.isMarked(m);
    }

    public boolean isMultisortEnabled() {
        return true;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    public IGridServiceAsync getGridService() {
        return RPC;
    }

    public void setGridService(IGridServiceAsync gridService) {
        RPC = gridService;
    }

    @Override
    protected void onAfterFirstAttach() {
        final BaseForm form = ElementUtils.findForm((Widget) this);
        autoLoad = (form == null);
        super.onAfterFirstAttach();
    }

    public void setAutoLoad(boolean autoLoad) {
        this.autoLoad = autoLoad;
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        onUpdateState();
    }

    public Columns getMetaInfo() {
        return metaInfo;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public final ValueProvider<DataInfoModel, BigDecimal> getKeyProvider() {
        if (keyProvider == null) {
            keyProvider = createKeyProvider();
        }
        return keyProvider;
    }

    public void checkInitialized() {
        if ((initialLoader == null && !initialized) || needReload) {
            reload();
        }
        if (isAttached()) {
            gridHelper.positionLiveScroller();
            gridHelper.reshow();
        }
    }

    public void deleteRecord(MarkModel model) {
        deleteRecord(model, new DefaultAsyncCallback<BigDecimal>(SystemGrid.this) {
            @Override
            public void onResult(BigDecimal result) {
                deleteRow(result);
            }
        });

        /*   SBFConst.FORM_SERVICE.delRecord(getFormContext((DataInfoModel) model), model.getRECORD_ID(), new DefaultAsyncCallback<BigDecimal>(SystemGrid.this) {
            @Override
            public void onResult(BigDecimal result) {
                deleteRow(result);
            }
        });
         */
    }

    public void deleteRecord(MarkModel model, DefaultAsyncCallback<BigDecimal> callback) {
        SBFConst.FORM_SERVICE.delRecord(getFormContext((DataInfoModel) model), model.getRECORD_ID(), callback);
    }

    protected void deleteRow(final BigDecimal result) {
        wasChanged = true;
        gridHelper.deleteRow(result);
        marker.remove(result);

        onDelete(result);
        fireEvent(new DeleteEvent(result));
        updateSummary();
    }

    private AsyncCallback<MarkModel> createLoadRowCallback() {
        return new DefaultAsyncCallback<MarkModel>(SystemGrid.this) {
            @Override
            public void onResult(MarkModel result) {
                updateMetaInfo(result, metaInfo);
                gridHelper.updateRow((DataInfoModel) result);

                onUpdate(result.getRECORD_ID());
                fireEvent(new UpdateEvent(result.getRECORD_ID()));
                updateSummary();

            }
        };
    }

    private AsyncCallback<List<? extends MarkModel>> createLoadRowsCallback() {
        return new DefaultAsyncCallback<List<? extends MarkModel>>(SystemGrid.this) {
            @Override
            public void onResult(List<? extends MarkModel> result) {
                for (MarkModel row : result) {

                    updateMetaInfo(row, metaInfo);
                    gridHelper.updateRow((DataInfoModel) row);

                    onUpdate(row.getRECORD_ID());
                    fireEvent(new UpdateEvent(row.getRECORD_ID()));
                }

                updateSummary();

            }
        };
    }

    private ISelectionModelAdapter<DataInfoModel> getSelectionModel() {
        return selectionModelAdapter;
    }

    private AsyncCallback<MarkModel> createAddRowCallback() {
        return new DefaultAsyncCallback<MarkModel>(SystemGrid.this) {
            @Override
            public void onResult(MarkModel result) {
                updateMetaInfo(result, metaInfo);
                gridHelper.insertRow((DataInfoModel) result);
                //must to work automatically -- grid.getView().updateVScroll();
                getSelectionModel().setSelection((DataInfoModel) result);

                onInsert(result.getRECORD_ID());
                fireEvent(new InsertEvent(result.getRECORD_ID()));
                updateSummary();

            }
        };
    }

    public void showQuickAggregate() {
        new AggregateWindow((SystemGrid<Row>) this).show(this);
    }

    public boolean isQuickAggregateAllowed() {
        return isMarksAllowed();
    }

    public void showFilter(FilterBox filters) {
        new FilterWindow((BaseGrid) this).show(filters, ClientUtils.coalesce(GridUtils.findParentBrowser(this), this));
    }

    public void showFilter() {
        showFilter(filters);
    }

    public void clearFilter() {
        new FilterWindow((BaseGrid) this).quickClean();
    }

    public void showMultisort(List<? extends SortInfo> sort) {
        new SortWindow(this).show(sort, ClientUtils.coalesce(GridUtils.findParentBrowser(this), this));
    }

    public void showMultisort() {
        if (isRemoteSort()) {
            showMultisort(getGrid().getLoader().getSortInfo());
        } else {
            ClientUtils.showError(I18n.get(SBFBrowserStr.msgMultySortRemote));
        }
    }

    public void applySort(List<? extends SortInfo> sort) {
        if (isRemoteSort()) {
            ListLoader l = getGrid().getLoader();
            l.clearSortInfo();
            for (SortInfo si : sort) {
                l.addSortInfo(si);
            }
            l.load();
        } else {
            ClientUtils.showError(I18n.get(SBFBrowserStr.msgMultySortInfo));
        }
    }

    private AsyncCallback<List<BigDecimal>> createGetOnlyIdsCallback() {
        return new DefaultAsyncCallback<List<BigDecimal>>(SystemGrid.this) {
            @Override
            public void onResult(List<BigDecimal> result) {
                scanBuffer.inverse(result);
                gridHelper.reshow();
                onUpdateState();
            }
        };
    }

    public void setClearMarksOnDataLoad(boolean clearMarksOnDataLoad) {
        this.clearMarksOnDataLoad = clearMarksOnDataLoad;
    }

    public boolean isClearMarksOnDataLoad() {
        return clearMarksOnDataLoad;
    }

    protected ValueProvider<DataInfoModel, BigDecimal> createKeyProvider() {//Исправление инверсии отметок - выделения строк...
        ColumnConfig c = (ColumnConfig) metaInfo.getPrimaryKey().getData(Column.COLUMN_CONFIG_PREFIX);
        return c.getValueProvider();
    }

    private PagingLoader<FetchParams, FetchResult<MarkModel>> createLoader() {
        final RpcProxy<FetchParams, FetchResult<MarkModel>> proxy = new RpcProxy<FetchParams, FetchResult<MarkModel>>() {
            @Override
            public void load(final FetchParams loadConfig, final AsyncCallback<FetchResult<MarkModel>> callback) {
                loadData(loadConfig, callback);
            }
        };

        final PagingLoader<FetchParams, FetchResult<MarkModel>> pagingLoader = new PagingLoader<FetchParams, FetchResult<MarkModel>>(proxy) {
            @Override
            protected FetchParams newLoadConfig() {
                return new FetchParams();
            }
        };
        pagingLoader.setRemoteSort(true);
        pagingLoader.setReuseLoadConfig(true);
        pagingLoader.useLoadConfig(new FetchParams(0, 200));
        pagingLoader.addBeforeLoadHandler(new BeforeLoadEvent.BeforeLoadHandler<FetchParams>() {

            @Override
            public void onBeforeLoad(BeforeLoadEvent<FetchParams> event) {
                if (!canLoad()) {
                    event.setCancelled(true);
                }
            }

            private boolean canLoad() {
                if (!autoLoad) {
                    return false;
                }

                checkInit();

                return initialized;
            }
        });
        pagingLoader.addLoadExceptionHandler(new LoadExceptionEvent.LoadExceptionHandler<FetchParams>() {
            @Override
            public void onLoadException(LoadExceptionEvent<FetchParams> event) {
                onGridLoadException(event);
            }
        });
        pagingLoader.addLoadHandler(new LoadHandler<FetchParams, FetchResult<MarkModel>>() {
            @Override
            public void onLoad(LoadEvent<FetchParams, FetchResult<MarkModel>> event) {
                for (MarkModel row : event.getLoadResult().getData()) {
                    updateMetaInfo(row, metaInfo);
                }

                needReload = (tmpFilter != null);
                tmpFilter = null;

                setAggregates(event.getLoadResult().getAggs());
                onUpdateState();
                onSetFilters(event.getLoadResult().getActualFilter(), clearMarksOnDataLoad);
            }
        });
        return pagingLoader;
    }

    public void onGridLoadException(LoadExceptionEvent<FetchParams> event) {
        final Throwable e = event.getException();
        if (e instanceof StatusCodeException) {
            final StatusCodeException statusCodeException = (StatusCodeException) e;
            if (statusCodeException.getStatusCode() == 0) {
                return;
            }
        }

        final Throwable ex = event.getException();
        if (ex instanceof FilterRequireException) {
            gridHelper.clearData();
            showFilter();
            ClientUtils.alertException(ex);
            return;
        }

        ClientUtils.alertException(e);
    }

    private void updateMetaInfo(MarkModel markModel, Columns metaInfo) {
        if (markModel instanceof Row) {
            ((Row) markModel).setColumns(metaInfo);
        }
    }

    private void loadData(final FetchParams loadConfig, final AsyncCallback<FetchResult<MarkModel>> callback) {
        RPC.getModelForBrowser(getGridContext(), getFetchParams(loadConfig), callback);
    }

    private static class SystemGridView<M extends MarkModel> extends SBLiveGridView<M> {

        public boolean isLastPage() {
            return Math.min(totalCount, ds.size() + viewIndex) == totalCount;
        }

        public SystemGridView() {
            setViewConfig(new SysViewConfig<>(() -> ds, () -> cm, this::isLastPage));
            setCacheSize(ROW_CACHE_SIZE);
            setColumnLines(true);
            setTrackMouseOver(false);
        }
    }

    private SBGrid<DataInfoModel> createGrid(ListLoader<FetchParams, FetchResult<MarkModel>> loader) {
        final SBGrid<DataInfoModel> newGrid = new SBGrid<DataInfoModel>(new SystemGridView<>(), loader);
        newGrid.addGridSelectionModelChangeListener(new GridSelectionModelChangeListener<DataInfoModel>() {
            private HandlerRegistration reg = null;

            @Override
            public void onGridSelectionModelChanged(GridSelectionModel<DataInfoModel> newModel) {
                if (reg != null) {
                    reg.removeHandler();
                }
                if (selectionModelAdapter != null) {
                    selectionModelAdapter.free();
                }
                selectionModelAdapter = SelectionModelAdapterFactory.createSelectionAdapter(newGrid);
                if (newModel instanceof CellSelectionModel) {
                    reg = ((CellSelectionModel) newModel).addCellSelectionChangedHandler(new CellSelectionChangedEvent.CellSelectionChangedHandler<DataInfoModel>() {
                        @Override
                        public void onCellSelectionChanged(CellSelectionChangedEvent<DataInfoModel> event) {
                            stateUpdater.requestUpdate();
                        }
                    });
                } else {
                    reg = newModel.addSelectionChangedHandler(new SelectionChangedEvent.SelectionChangedHandler<DataInfoModel>() {
                        @Override
                        public void onSelectionChanged(SelectionChangedEvent<DataInfoModel> event) {
                            stateUpdater.requestUpdate();
                        }
                    });
                }
            }
        });
        newGrid.setSelectionModel(new SBGridSelectionModelNew<>());
        newGrid.getStore().addStoreHandlers(new GridStoreHandlers());

        newGrid.addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                final ColumnConfig<DataInfoModel, Object> column = newGrid.getColumnModel().getColumn(event.getCellIndex());
                if (column != null && column == markColumn) {
                    invertMark(false);
                }
            }
        });

        newGrid.addCellDoubleClickHandler(new CellDoubleClickHandler() {
            @Override
            public void onCellClick(CellDoubleClickEvent event) {
                fireGridEvent(GridEvent.DOUBLE_CLICK);
            }
        });

        return newGrid;
    }

    protected void setMetaInfo(Columns metaInfo) {
        this.metaInfo = metaInfo;

        setSortInfo(metaInfo.getSort());
        reconfigureColumns();

        String caption = metaInfo.getCaption();
        if (caption != null) {
            Browser browser = getData("browser");
            if (browser != null) {
                browser.setCaption(caption);
            }
        }
    }

    private void setSortInfo(List<SortingInfo> sorting) {
        List<SortInfo> sorts = convertSortingInfo(sorting);
        if (sorts != null && !sorts.isEmpty()) {
            loader.getLastLoadConfig().setSortInfo(new ArrayList<>(sorts));
            loader.clearSortInfo();
            sorts.forEach((sort) -> {
                loader.addSortInfo(sort);
            });
        }
    }

    public void reloadGrid() {
        metaInfo = null;
        initialized = false;
        //initialLoader = null;
        gridHelper.prepareReload();
        //tryReload();
        checkInitialized();
    }

    private void reconfigureColumns() {
        if (gridHelper.getColumnCount() > 0) {
            return;
        }
        if (metaInfo != null && gridHelper.isViewReady()) {

            gridHelper.pushLoadMask(false);

            boolean hasFrozen = metaInfo.hasFrozen();

            ColumnModel<DataInfoModel> cm = createColumnModel(metaInfo, true);
            gridHelper.reconfigureMain(cm, metaInfo.getAutoExpanColumn());

            int quickTipFlags = 0;
            int frozenWidth = 0;
            for (final Column meta : metaInfo.getColumns()) {
                if (meta.getType() == ColumnType.VCHAR && ColumnWrapType.QTIP.equals(meta.getWordWrap())) {
                    quickTipFlags |= meta.isFrozen() ? GRID_TYPE_MASK_FROZEN : GRID_TYPE_MASK_MAIN;
                    if ((quickTipFlags & GRID_TYPE_MASK_BOTH) == GRID_TYPE_MASK_BOTH) {
                        break;
                    }
                }
            }

            if (hasFrozen) {
                ColumnModel<DataInfoModel> frozenModel = createFrozenColumnModel(metaInfo);
                if (gridHelper.hasFrozenGrid()) {
                    gridHelper.reconfigureFrozen(frozenModel, metaInfo.getFrozenAutoExpanColumn());
                } else {
                    Grid<DataInfoModel> frozenGrid = gridHelper.createFrozenGrid(frozenModel);
                    final int margins = theme.isTriton() ? 10 : theme.isNeptune() ? 8 : 5;
                    BorderLayoutContainer.BorderLayoutData frozenLayoutData = new BorderLayoutContainer.BorderLayoutData(metaInfo.frozenWidth());
                    frozenLayoutData.setMinSize(100);
                    frozenLayoutData.setMaxSize(1000);
                    //frozenLayoutData.setMargins(new Margins(0, margins, 0, margins));
                    frozenLayoutData.setCollapsible(true);
                    frozenLayoutData.setCollapseHeaderVisible(true);
                    frozenLayoutData.setSplit(true);
                    frozenLayoutData.setCollapsed(false);
                    ContentPanel frozCp = new ContentPanel();
                    frozCp.setHeaderVisible(false);
                    frozCp.setWidget(frozenGrid);
                    gridContainer.setWestWidget(frozCp, frozenLayoutData);
                    setAutoexpandColumn(frozenGrid, metaInfo.getFrozenAutoExpanColumn());

                }
                gridContainer.forceLayout();
            } else {
                if (gridHelper.hasFrozenGrid()) {
                    gridHelper.removeFrozenGrid();
                    gridContainer.setWestWidget(null);
                    gridContainer.forceLayout();
                }
            }

            gridHelper.setQuickTip(quickTipFlags);
            gridHelper.popLoadMask();
            gridHelper.refreshView(true);

            if (isAutoFit() && gridHelper.hasFrozenGrid()) {
                //ColumnHeader<?> chf = gridHelper.frozenGrid.getView().getHeader();
                //ColumnHeader<?> ch = grid.getView().getHeader();

                // int h = ch.asWidget().getElement().getClientHeight();
                //int h = ch.getElement().getHeight(false);
                //int w = ch.getElement().getWidth(false);
//int w = ch.asWidget().getElement().getClientWidth();
                //  gridHelper.frozenGrid.
                //((FrozenColumnHeader) chf).adjustHeight(h);
                // ResizeEvent.fire(ch, w, h);
                grid.getView().updateAutoFit();
                gridHelper.frozenGrid.getView().doLayout();
            }

        }
    }

    private static void setAutoexpandColumn(Grid g, Column c) {
        if (c != null) {
            final ColumnConfig cfg = (ColumnConfig) c.getData(Column.COLUMN_CONFIG_PREFIX);
            if (cfg != null) {
                g.getView().setAutoExpandMax(AUTO_EXPAND_MAX_VALUE);
                g.getView().setAutoExpandMin(AUTO_EXPAND_MIN_VALUE);
                g.getView().setAutoExpandColumn(cfg);
            }
        } else {
            g.getView().setAutoExpandColumn(null);
        }
    }

    protected ColumnModel<DataInfoModel> createColumnModel(Columns columns__) {
        return new ColumnModelBuilder(gridHelper.getMainRowByIndexProvider()).mark().build(columns__);
    }

    protected ColumnModel<DataInfoModel> createColumnModel(Columns columns__, boolean isMark) {
        return new ColumnModelBuilder(gridHelper.getMainRowByIndexProvider()).mark(isMark).hide((c) -> c.getColumn().isFrozen()).build(columns__);
    }

    protected ColumnModel<DataInfoModel> createFrozenColumnModel(Columns columns__) {
        return new ColumnModelBuilder(gridHelper.getFrozenRowByIndexProvider()).hide((c) -> !c.getColumn().isFrozen()).build(columns__);
    }

    private interface ColCondition {

        boolean isValid(CustomColumnConfig c);
    }

    private interface CustomColTuner {

        void tune(CustomColumnConfig c);
    }

    private static class ColumnHider implements CustomColTuner {

        private final ColCondition cond;

        public ColumnHider(ColCondition cond) {
            this.cond = cond;
        }

        @Override
        public void tune(CustomColumnConfig c) {
            if (cond.isValid(c)) {
                c.setHidden(true);
                c.setHideable(false);
            }
        }
    }

    private class ColumnModelBuilder {

        private final IRowByIndexProvider grid;
        private boolean mark = false;
        private final List<CustomColTuner> tuners = new ArrayList<>();

        public ColumnModelBuilder(IRowByIndexProvider grid) {
            this.grid = grid;
        }

        public ColumnModelBuilder mark() {
            mark = true;
            return this;
        }

        public ColumnModelBuilder mark(boolean mark) {
            this.mark = mark;
            return this;
        }

        public ColumnModelBuilder hide(ColCondition cond) {
            tuners.add(new ColumnHider(cond));
            return this;
        }

        public ColumnModel<DataInfoModel> build(Columns columns__) {
            columns__.getColumnForAlias("");
            final List<Column> columns = rebuild(columns__);
            List<ColumnConfig> columnConfigList = new ArrayList<ColumnConfig>();
            if (mark) {
                columnConfigList.add(getMarkColumn());
            }
            StyleChecker checker = columns__.hasStyles() ? new StyleChecker(grid, columns__) : null;
            FormatPovider fmtProvider = columns__.hasExpCellFormat() ? new FormatPovider(grid) : null;
            for (final Column meta : columns) {
                if ((meta.getType() == KEY) || (meta.getType() == TEMPORAL_KEY) || (meta.getType() == IDENTIFIER)) {
                    meta.setVisible(false);
                }
                CustomColumnConfig column = createColumnConfig(meta, meta.getIndex());
                if (meta.getIconMap() != null) {
                    column = new IconColumnConfig(column);
                }
                final Cell cell = column.getCell();
                if (cell instanceof CustomCell) {
                    ((CustomCell) cell).setConfig(column);
                    ((CustomCell) cell).setStyleChecker(checker);
                    ((CustomCell) cell).setFormatProvider(fmtProvider);

                }
                meta.setData(Column.COLUMN_CONFIG_PREFIX, column);
                String description = I18n.get(meta.getDescription());
                description = description != null ? description : I18n.get(meta.getCaption());
                column.setHidden(!meta.isVisible() || meta.isHidden());
                if (meta.isHidden()) {
                    column.setHideable(false);
                }
                column.setToolTip(new SafeHtmlBuilder().appendEscaped(description).toSafeHtml());
                for (CustomColTuner t : tuners) {
                    t.tune(column);
                }
                columnConfigList.add(column);
            }

            final ColumnModel<DataInfoModel> result = (ColumnModel<DataInfoModel>) ColumnModelCreator.create(columnConfigList);

            if (GRID_CONFIG) {
                result.addColumnModelHandlers(new ColumnModelHandlers() {

                    @Override
                    public void onColumnWidthChange(ColumnWidthChangeEvent event) {
                        save();
                    }

                    @Override
                    public void onColumnMove(ColumnMoveEvent event) {
                        save();
                    }

                    @Override
                    public void onColumnHiddenChange(ColumnHiddenChangeEvent event) {
                        save();
                    }

                    @Override
                    public void onColumnHeaderChange(ColumnHeaderChangeEvent event) {
                    }

                    private void save() {

                        final ArrayList<ColumnCfg> columnConfigList = new ArrayList<ColumnCfg>();
                        for (Column column : metaInfo.getColumns()) {
                            columnConfigList.add(column.getConfig());
                        }
                        CONFIG_RPC.saveConfiguration(getGridContext(), columnConfigList, new DefaultAsyncCallback<Void>());
                    }
                });
            }
            return result;
        }

        private List<Column> rebuild(Columns metaInfo) {
            for (Column column : metaInfo.getColumns()) {
                final ColumnCfg config = column.getConfig();
                if (config != null) {
                    column.setConfig(config);
                }
            }
            return metaInfo.getColumns();
        }
    }

    private CustomColumnConfig createColumnConfig(Column meta, int index) {
        CustomColumnConfig c = null;

        if (meta.getValueSet() != null && !meta.getValueSet().isEmpty()) {
            return new LookupColumnConfig(meta, index, new KeyNameRenderer(meta.getValueSet()));
        } else if (meta.getFormat() != null) {
            final Renderer renderer = SBFEntryPoint.getRendererManager().getRenderer(meta.getFormat());
            if (renderer != null) {
                return new LookupColumnConfig(meta, index, renderer);
            }
        }

        switch (meta.getType()) {
            case KEY:
            case TEMPORAL_KEY:
                meta.setFormat("0");
                return new CurrencyColumnConfig(meta, index);
            case IDENTIFIER:
                return new LongColumnConfig(meta, index);
            case DATE:
                return new DateColumnConfig(meta, index);
            case DATE_TIME:
                return new DateTimeColumnConfig(meta, index);
            case TIMESTAMP:
                return new TimestampColumnConfig(meta, index);
            case VCHAR:
                return new StringColumnConfig(meta, index);
            case BOOL:
                return new LookupColumnConfig(meta, index, SBFEntryPoint.getRendererManager().getRenderer(BOOL_DEFAULT));
            case INTEGER:
                return new LongColumnConfig(meta, index);
            case CURRENCY:
                return new CurrencyColumnConfig(meta, index);
            case ADDRESS:
                return new AddressColumnConfig(meta, index);
            case YMDAY:
                return new YearMonthDayColumnConfig(meta, index);

        }
        return c;
    }

    public ColumnConfig getMarkColumn() {
        if (markColumn == null) {
            markColumn = new ColumnConfig<DataInfoModel, ImageResource>(new ValueProvider<DataInfoModel, ImageResource>() {
                @Override
                public ImageResource getValue(DataInfoModel object) {
                    return marker.isMarked(object) ? SBFResources.BROWSER_ICONS.Pin16() : null;
                }

                @Override
                public void setValue(DataInfoModel object, ImageResource value) {
                }

                @Override
                public String getPath() {
                    return "PIN_IMAGE";
                }
            }, 21, "*");

            markColumn.setHideable(false);
            markColumn.setHidden(true);
            markColumn.setSortable(false);
            markColumn.setFixed(true);
            markColumn.setMenuDisabled(true);
            markColumn.setCell(new ImageResourceCell());
        }
        return markColumn;
    }

    public void gotoPrevRecord() {
        sendKey(KeyCodes.KEY_UP);
    }

    public void gotoNextRecord() {
        sendKey(KeyCodes.KEY_DOWN);
    }

    public void gotoPrevPage() {
        sendKey(KeyCodes.KEY_PAGEUP);
    }

    public void gotoNextPage() {
        sendKey(KeyCodes.KEY_PAGEDOWN);
    }

    public void gotoFirstRecord() {
        sendKey(KeyCodes.KEY_HOME, true, false, false, false);
    }

    public void gotoLastRecord() {
        sendKey(KeyCodes.KEY_END, true, false, false, false);
    }

    private void sendKey(int keyCode) {
        sendKey(keyCode, false, false, false, false);
    }

    private void sendKey(int keyCode, boolean ctrlKey, boolean altKey, boolean shiftKey, boolean metaKey) {
        CliUtil.sendKey(getGrid(), keyCode, ctrlKey, altKey, shiftKey, metaKey);
    }

    public void invertMark(final boolean gotoNextRecord) {
        if (isMarksAllowed() && gridHelper.getMarkGrid().getStore().size() > 0) {
            DataInfoModel selectedModel = getSelectionModel().getSelectedItem();
            if (selectedModel != null) {
                mark(selectedModel, gotoNextRecord);
            }
        }
    }

    private void mark(final DataInfoModel model, final boolean gotoNextRecord) {
        marker.inverse(model);
        scanBuffer.addInterval(gridHelper.getMarkGrid().getView());
        if (gotoNextRecord) {
            gotoNextRecord();
        }
        gridHelper.getMarkGrid().getView().reshow();
        onUpdateState();
    }

    protected String createFormKeyFromModel(final DataInfoModel selectedModel) {
        return "BaseForm";
    }

    public AsyncCallback createFindMarkCallback() {
        return new DefaultAsyncCallback<List<BigDecimal>>(SystemGrid.this) {
            @Override
            public void onResult(List<BigDecimal> result) {
                scanBuffer.addInterval(scanBuffer.getScanOffset(), result);
                if (scanBuffer.find()) {
                    selectRecord(scanBuffer.getScanIndex());
                } else {
                    scanIDs();
                }
            }
        };
    }

    public void selectMarkedRecord(boolean forward, boolean fromCurrent) {
        if (marker.isEmpty()) {
            return;
        }

        scanBuffer.startCaching(gridHelper.getMarkGrid().getView().getTotalCount(), (List<SortInfo>) loader.getSortInfo());
        scanBuffer.addInterval(gridHelper.getMarkGrid().getView());

        DataInfoModel model = getCurrentRecord();
        if (model != null) {
            if (marker.isMarked(model) && marker.isSingleMark()) {
                return;
            }
        }

        int index = -1;
        if (fromCurrent) {
            index = gridHelper.getMarkGrid().getView().getViewIndex();
            if (model != null) {
                index += gridHelper.getMarkGrid().getStore().indexOf(model);
            }
        }

        if (scanBuffer.find(forward, index)) {
            selectRecord(scanBuffer.getScanIndex());
        } else {
            scanIDs();
        }
    }

    private void scanIDs() {
        FetchParams scanConfig = new FetchParams();
        scanConfig.setOffset(scanBuffer.getScanOffset());
        scanConfig.setLimit(MarkScanBuffer.SCAN_ROWS_LIMIT);
        scanConfig.setSortInfo(loader.getSortInfo());

        mask(I18n.get(SBFBrowserStr.msgSearch));
        RPC.getOnlyIdsForBrowser(getGridContext(), getFetchParams(scanConfig), getKeyProvider().getPath(), getGetRowsIDsCallback());
    }

    public AsyncCallback<List<BigDecimal>> getGetRowsIDsCallback() {
        if (findMarkCallback == null) {
            findMarkCallback = createFindMarkCallback();
        }
        return findMarkCallback;
    }

    private void selectRecord(int position) {
        SBGrid<DataInfoModel> grid = gridHelper.getMarkGrid();
        if (grid.getView().getViewIndex() <= position && position < grid.getView().getVisibleRowCount() + grid.getView().getViewIndex()) {
            getSelectionModel().setSelection(position - grid.getView().getViewIndex());
        } else {
            int index = Math.max(0, position - 5);
            grid.getView().getScroller().setScrollTop(index * grid.getView().getCalculatedRowHeight());
            Scheduler.get().scheduleFinally(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    getSelectionModel().setSelection(scanBuffer.getScanIndex() - grid.getView().getViewIndex());
                    final DataInfoModel selectedItem = getSelectionModel().getSelectedItem();
                    if (selectedItem == null || !marker.isMarked(selectedItem)) {
                        // рассинхронизация данных
                        scanBuffer.stopCaching(false);
                    }
                }
            });
        }
    }

    public ArrayList<BigDecimal> getMarkedRecords() {
        return marker.getMarkedRecords();
    }

    public int getTotalRecordsCount() {
        return gridHelper.getViewRowCount();
    }

    public int getTotalMarkedRecordsCount() {
        return marker.size();
    }

    public boolean hasMarkedRecords() {
        return marker.size() > 0;
    }

    public <M extends LookupInfoModel> void setMarkedLookup(final List<M> markItems) {
        List<BigDecimal> marks = new ArrayList<>(markItems.size());
        if (markItems != null && !markItems.isEmpty()) {
            boolean temporal = markItems.get(0).getSemanticID() != null;
            for (M m : markItems) {
                if (temporal && m.getSemanticID() == null) {
                    throw new IllegalStateException(I18n.get(SBFExceptionStr.differentItems, markItems.get(0).toString(), m.toString()));
                }
                marks.add(temporal ? m.getSemanticID() : m.getID());
            }
        }
        setMarkedRecords(marks);
    }

    public void setMarkedRecords(final List<BigDecimal> marks) {
        scanBuffer.stopCaching(false);
        marker.reload(marks);
        gridHelper.reshow();
        onUpdateState();
    }

    public DataInfoModel getCurrentRecord() {
        return getSelectionModel().getSelectedItem();
    }

    public List<DataInfoModel> getSelectedRecords() {
        return getSelectionModel().getSelectedItems();
    }

    public void deleteAllMarks() {
        scanBuffer.stopCaching(true);
        gridHelper.reshow();
        onUpdateState();
    }

    public void invertAllMarks() {
        mask(I18n.get(SBFBrowserStr.msgInversionMarks));
        RPC.getOnlyIdsForBrowser(getGridContext(), getFetchParams(new FetchParams(0, 0)),
                getKeyProvider().getPath(), getGetOnlyIdsCallback());
    }

    public AsyncCallback<List<BigDecimal>> getGetOnlyIdsCallback() {
        if (getOnlyIdsCallback == null) {
            getOnlyIdsCallback = createGetOnlyIdsCallback();
        }
        return getOnlyIdsCallback;
    }

    public PageFilterInfo getFetchParams(FetchParams bean) {
        bean.setParentFilters(getParentFilters());
        return convertLoadConfig(bean);
    }

    protected PageFilterInfo convertLoadConfig(FetchParams config) {
        final PageFilterInfo r = new PageFilterInfo();
        r.setOffset(config.getOffset());
        r.setLimit(config.getLimit());
        r.setParentFilters(config.getParentFilters());
        r.setTempFilter(tmpFilter);
        r.setSorts(convertSortInfo(config.getSortInfo()));
        return r;
    }

    protected static List<SortingInfo> convertSortInfo(List<? extends SortInfo> sortInfo) {
        List<SortingInfo> result = null;
        for (SortInfo item : sortInfo) {
            if (item != null) {
                final String sortField = item.getSortField();
                if (sortField != null) {
                    final SortingInfo sortingInfo = new SortingInfo();
                    sortingInfo.setAlias(sortField);
                    sortingInfo.setSortDirection(SortDirection.valueOf(item.getSortDir().name()));
                    if (result == null) {
                        result = new ArrayList<SortingInfo>();
                    }
                    result.add(sortingInfo);
                }
            }
        }
        return result;
    }

    protected static List<SortInfo> convertSortingInfo(List<SortingInfo> sortingInfos) {
        if (sortingInfos == null) {
            return null;
        }
        List<SortInfo> result = new ArrayList<SortInfo>();
        for (SortingInfo item : sortingInfos) {
            if (item != null) {
                final String sortField = item.getAlias();
                if (sortField != null) {
                    SortInfoBean sortInfo = new SortInfoBean();
                    sortInfo.setSortField(sortField);
                    sortInfo.setSortDir(SortDir.valueOf(item.getSortDirection().name()));
                    result.add(sortInfo);
                }
            }
        }
        return result;
    }

    public void tryReload() {
        if (GridUtils.isActiveGrid(this)) {
            reload();
        } else {
            needReload = true;
        }
    }

    public boolean isNeedReload() {
        return needReload;
    }

    public void setNeedReload(boolean needReload) {
        this.needReload = needReload;
    }

    /**
     * Вызывается контейнером браузеров, когда вкладка с браузером, содержащим
     * этот грид становится активной (выбранной).
     */
    public void onBrowserSelected() {
        gridHelper.reshow();
    }

    public void addReloadListener(GridReloadListener reloadListener) {
        reloadListeners.add(reloadListener);
    }

    public void reload() {
        checkInit();
        if (initialized) {
            loader.load();
            reloadListeners.forEach(listener -> listener.onReloadGrid(this));
        }
    }

    public void refreshRow(IFormModel model) {
        refreshRow(model.getId());
    }

    public void refreshRow(final BigDecimal ID) {
        loadSingleRow(ID, getRowLoadCallback());
    }

    protected AsyncCallback<MarkModel> getRowLoadCallback() {
        if (loadRowCallback == null) {
            loadRowCallback = createLoadRowCallback();
        }
        return loadRowCallback;
    }

    public void loadAddedRow(IFormModel model) {
        loadAddedRow(model.getId());
    }

    public void loadAddedRow(final BigDecimal ID) {
        loadSingleRow(ID, getAddRowCallback());
    }

    public AsyncCallback<MarkModel> getAddRowCallback() {
        if (addRowCallback == null) {
            addRowCallback = createAddRowCallback();
        }
        return addRowCallback;
    }

    public void loadSingleRow(BigDecimal ID, AsyncCallback<MarkModel> callback) {
        loadSingleRow(ID, null, callback);
    }
//new 

    public void refreshRows(List<BigDecimal> ids) {
        final PageFilterInfo fetchParams = getFetchParams(new FetchParams());
        fetchParams.setParentFilters(null);

       // GridUtils.loadRows(this, new HashSet<BigDecimal>(ids), createLoadRowsCallback());

        RPC.getModelRows(getGridContext(), fetchParams, new ArrayList<>(ids), createLoadRowsCallback());
        //RPC.getModelRow(getGridContext(), fetchParams, ID, new LoadRowCallbackAdapter(callback));
    }

    public void loadSingleRow(BigDecimal ID, List<FilterInfo> parentFilters, AsyncCallback<MarkModel> callback) {
        final PageFilterInfo fetchParams = getFetchParams(new FetchParams());
        fetchParams.setParentFilters(parentFilters);
        RPC.getModelRow(getGridContext(), fetchParams, ID, new LoadRowCallbackAdapter(callback));
    }
    
    public void loadCustomReports(List<StringParamInfo> filters, AsyncCallback<List<CustomReportInfo>> callback) {
        RPC.getCustomReports(getGridContext(), filters, callback);
    }

    public class LoadRowCallbackAdapter implements AsyncCallback<MarkModel> {

        private final AsyncCallback<MarkModel> callback;

        public LoadRowCallbackAdapter(AsyncCallback<MarkModel> callback) {
            this.callback = callback;
        }

        @Override
        public void onFailure(Throwable caught) {
            callback.onFailure(caught);
        }

        @Override
        public void onSuccess(MarkModel result) {
            updateMetaInfo(result, metaInfo);
            callback.onSuccess(result);

        }
    }

    public List<FilterInfo> getDefinedFilters() {
        return filters == null ? null : Collections.unmodifiableList(filters.getFilter().getSystemFilters());
    }

    public StoredFilterPath getFilterPath() {
        return filters != null ? filters.getPath() : null;
    }

    protected void onSetFilters(FilterBox filters, boolean clearMark) {
        scanBuffer.stopCaching(clearMark);
        this.filters = filters;

        fireEvent(new ChangeFiltersEvent(filters.getFilter()));

        Browser browser = GridUtils.findParentBrowser(this);
        if (browser != null) {
            browser.updateFilterStatus((BaseGrid) this, filters.getFilter());
        }
    }

    public void setOneTimeFilter(StringFilterInfo filter) {
        this.tmpFilter = filter;
        reload();
    }

    public <T extends FilterInfo> void setParentFilters(T... vals) {
        setParentFilters(FilterInfo.list(vals));
    }
    
    @Override
    public <T extends FilterInfo> void setParentFilters(final List<T> vals) {
        autoLoad = true;
        if (!ClientUtils.equals(parentFilters, vals)) {
            parentFilters.clear();
            if (vals != null && vals.size() > 0) {
                parentFilters.addAll(vals);
            }
            if (isInitialized() && dynamic) {
                reloadGrid();
            } else {
                tryReload();
            }
        }
    }

    public void setParentFilter(String columnName, IFormModel model) {
        setParentFilter(new KeyFilterInfo(columnName, model));
    }

    public void setParentFilter(FilterInfo val) {
        setParentFilters(Collections.singletonList(val));
    }

    public SBGrid<DataInfoModel> getGrid() {
        return grid;
    }

    public HandlerRegistration addLiveGridViewUpdateHandler(LiveGridViewUpdateHandler handler) {
        return grid.getView().addLiveGridViewUpdateHandler(handler);
    }
    
    public boolean isTableReadOnly() {
        return RoleCheker.getInstance().isTableReadOnly(getSecurityName());
    }

    public boolean isMarksAllowed() {
        return !getMarkColumn().isHidden();
    }

    protected void setMarksAllowed(boolean marksAllowed) {
        getMarkColumn().setHidden(!marksAllowed);
    }

    public boolean isClonable() {
        return clonable;
    }

    public void setClonable(boolean clonable) {
        this.clonable = clonable;
    }

    @Override
    public List<FilterInfo> getParentFilters() {
        return parentFilters;
    }

    public BigDecimal getClonableRecordID() {
        if (!clonable) {
            return null;
        }

        DataInfoModel rec = getCurrentRecord();
        if (rec == null) {
            rec = gridHelper.getFirstRow();
        }

        return rec == null ? null : rec.getRECORD_ID();
    }

    @Override
    public HandlerRegistration addLoadHandler(LoadHandler<FetchParams, FetchResult<MarkModel>> handler) {
        return loader.addLoadHandler(handler);
    }

    public void setLoadHandler(LoadHandler<FetchParams, FetchResult<MarkModel>> handler) {
        if (loadHandlerHolder == null) {
            loadHandlerHolder = new HandlerHolder();
        }
        if (loadHandlerHolder.getHandler() != handler) {
            loadHandlerHolder.removeHandler();
            if (handler != null) {
                loadHandlerHolder.hold(handler, loader.addLoadHandler(handler));
            }
        }
    }

    public boolean isRemoteSort() {
        ListLoader l = getGrid().getLoader();
        return l != null && l.isRemoteSort();
    }

    public boolean isReadOnly(boolean deep) {
        return deep ? GridUtils.isReadOnly(this) : isTableReadOnly() ? true : readOnly;
    }

    public boolean isReadOnly() {
        return isReadOnly(false);
    }

    public void setReadOnly(boolean readOnly) {
        if (isTableReadOnly()) {
            readOnly = true;
        }
        if (this.readOnly != readOnly) {
            this.readOnly = readOnly;
            onUpdateState();
        }
    }

    public boolean wasChanged() {
        boolean result = wasChanged;
        wasChanged = false;
        return result;
    }

    public void setWasChanged(boolean wasChanged) {
        this.wasChanged = wasChanged;
    }

    protected void onInsert(final BigDecimal result) {
    }

    protected void onUpdate(final BigDecimal result) {
    }

    protected void onDelete(final BigDecimal result) {
    }

    public HandlerRegistration addInsertHandler(InsertEvent.InsertHandler handler) {
        return addHandler(handler, InsertEvent.getType());
    }

    public HandlerRegistration addUpdateHandler(UpdateEvent.UpdateHandler handler) {
        return addHandler(handler, UpdateEvent.getType());
    }

    public HandlerRegistration addDeleteHandler(DeleteEvent.DeleteHandler handler) {
        return addHandler(handler, DeleteEvent.getType());
    }

    public GridContext __getGridContext() {
        return getGridContext();
    }

    public String __getTableName() {
        return getTableName();
    }

    private void updateSummary() {
        if (gridHelper.getAggregationSize() < 1) {
            return;
        }

        RPC.getModelForBrowser(getGridContext(), getFetchParams(new FetchParams(-1, -1)), new DefaultAsyncCallback<FetchResult<MarkModel>>() {

            @Override
            public void onResult(FetchResult<MarkModel> result) {
                setAggregates(result.getAggs());
            }
        });
    }

    public HandlerRegistration addAggregateChangeHandler(final AggregateChangeHandler handler) {
        return addHandler(handler, AggregateChangeEvent.getType());
    }

    private void setAggregates(final Map<String, Wrapper> aggs) {
        if (metaInfo == null || aggs == null) {
            return;
        }

        AggregationRowConfig<DataInfoModel> aggregationRow = null;
        boolean fire = false;
        int i = 0;
        for (String columnAlias : aggs.keySet()) {
            final Column column = metaInfo.getColumnForAlias(columnAlias);
            if (column == null) {
                continue;
            }
            final ColumnConfig columnConfig = (ColumnConfig) column.getData(Column.COLUMN_CONFIG_PREFIX);
            if (columnConfig != null) {
                if (aggregationRow == null) {
                    if (gridHelper.getAggregationSize() < 1) {
                        aggregationRow = new AggregationRowConfig<DataInfoModel>();
                        gridHelper.addAggregation(aggregationRow);
                    } else {
                        aggregationRow = gridHelper.getAggregation(0);
                    }
                }

                AggregationRenderer renderer = (AggregationRenderer) column.getData(Column.RENDERER_PREFIX);
                final Wrapper wrapper = aggs.get(columnAlias);
                if (wrapper instanceof BigDecimalAggregatesWrapper) {
                    NumberRenderer numberRenderer = (renderer instanceof NumberRenderer)
                            ? (NumberRenderer) renderer
                            : new NumberRenderer();

                    String format = null;
                    if (wrapper instanceof AggregatesWrapper) {
                        format = ((AggregatesWrapper) wrapper).getFormat();
                    }
                    if (format == null) {
                        if (column.getType() == ColumnType.CURRENCY) {
                            format = CurrencyColumnConfig.DEFAULT_FORMAT;
                        } else {
                            format = LongColumnConfig.DEFAULT_FORMAT;
                        }
                    }

                    BigDecimal val = ((BigDecimalAggregatesWrapper) wrapper).getValue();
                    numberRenderer.setValue(val != null ? val : BigDecimal.ZERO);
                    numberRenderer.setFormat(format);

                    renderer = numberRenderer;

                } else {
                    TextRenderer textRenderer = (renderer instanceof TextRenderer) ? (TextRenderer) renderer : new TextRenderer<>();
                    textRenderer.setValue(getWrapperText(wrapper));
                    renderer = textRenderer;
                }

                column.setData(Column.RENDERER_PREFIX, renderer);
                aggregationRow.setRenderer(columnConfig, renderer);

                fire = true;
            }
        }
        if (aggregationRow != null) {
            if (gridHelper.getAggregationSize() < 1) {
                gridHelper.addAggregation(aggregationRow);
            }
            gridHelper.refreshView(false);
        }
        if (fire) {
            fireEvent(new AggregateChangeEvent(this));
        }
    }

    public BigDecimal getSummaryValue(String alias) {
        final Column column = getMetaInfo().getColumnForAlias(alias);
        if (column != null) {
            AggregationRenderer renderer = (AggregationRenderer) column.getData(Column.RENDERER_PREFIX);
            if (renderer instanceof NumberRenderer) {
                return ((NumberRenderer) renderer).getValue();
            }
        }
        return null;
    }

    public <M extends LookupInfoModel> void lookup(final ILookupField<M> lookupField, List<BigDecimal> lookupKey, final Runnable callback) {
        RPC.lookup(getGridContext(), getFetchParams(new FetchParams()), lookupKey, new DefaultAsyncCallback<List<LookupInfoModel>>(SystemGrid.this) {
            @Override
            public void onResult(List<LookupInfoModel> result) {
                if (result == null || result.isEmpty()) {
                    lookupField.setValue(null, true);
                } else if (result.size() == 1) {
                    lookupField.setValue((M) result.get(0), true);
                } else {
                    lookupField.setValues((List<M>) result);
                }
                callback.run();
            }
        });
    }

    public void loadData(String columnName, final AsyncCallback<FetchResult<MarkModel>> callback) {
        final PageFilterInfo fetchParams = getFetchParams(new FetchParams());
        fetchParams.setColumnName(columnName);
        RPC.getModelForBrowser(getGridContext(), fetchParams, callback);
    }

    public void loadData(GridType gridType, final AsyncCallback<FetchResult<MarkModel>> callback) {
        final GridContext gridContext = new GridContext(gridType, null);

        RPC.getMeta(gridContext, new DefaultAsyncCallback<Columns>() {

            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onResult(final Columns meta) {
                final PageFilterInfo pageFilterInfo = new PageFilterInfo();
                pageFilterInfo.setOffset(0);
                pageFilterInfo.setLimit(50);
                RPC.getModelForBrowser(gridContext, pageFilterInfo, new DefaultAsyncCallback<FetchResult<MarkModel>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onResult(FetchResult<MarkModel> rows) {
                        for (MarkModel m : rows.getData()) {
                            if (m instanceof Row) {
                                ((Row) m).setColumns(meta);
                            }
                        }
                        callback.onSuccess(rows);
                    }
                });
            }
        });
    }

    private String getWrapperText(Wrapper wrapper) {

        if (wrapper instanceof StringWrapper) {
            String value = ((StringWrapper) wrapper).getValue();
            // проверка на содержание значения ILocalizedString;
            ILocalizedString localizedString = i18nUtils.stringToLocalizedString(value);
            if (localizedString != null) {
                return I18n.get(localizedString);
            }
            return value;
        }
        return String.valueOf(wrapper.getValue());
    }

    /**
     * @return имя таблицы, используется для проверки доступа пользователя к
     * операциям добавления, удаления
     */
    protected abstract String getTableName();

    protected abstract String getSecurityName();

    protected abstract void createEditForm(DataInfoModel model, AsyncCallback<BaseForm> callback);

    protected abstract FormContext getFormContext(DataInfoModel model);

    protected abstract GridContext getGridContext();

    protected abstract void fireGridEvent(GridEvent event);

    public abstract void onUpdateState();

    public boolean isMultiplyDeleteSupported() {
        return true;
    }

    public boolean isMasked() {
        return mask;
    }

    private void checkInit() {
        if (initialized || initialLoader != null) {
            return;
        }
        initialLoader = new GridInitialLoader();
        initialLoader.load();
    }

    private final List<Command> onInitCommands = new ArrayList<>();

    public void execOnInit(Command command) {
        if (initialized) {
            command.execute();
        } else {
            onInitCommands.add(command);
        }
    }

    public HandlerRegistration addChangeFiltersHandler(ChangeFiltersHandler handler) {
        return addHandler(handler, ChangeFiltersEvent.getType());
    }

    /**
     * Первоначальная загрузка данных Метаинформация по колонкам, конфигурация
     * колонок и фильтры
     */
    private class GridInitialLoader {

        private Columns meta;
        private FilterBox filter;

        public void load() {
            final List<SchedulerChainCommand> commandChain = new ArrayList<SchedulerChainCommand>();
//1- load meta            

            commandChain.add(new SchedulerChainCommand() {
                @Override
                public void execute() {
                    RPC.getMeta(getGridContext(), new AsyncCallback<Columns>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            error(caught);
                        }

                        @Override
                        public void onSuccess(Columns result) {
                            meta = result;
                            getChainManager().next();
                        }
                    });
                }
            });
//2- load filter                        
            commandChain.add(new SchedulerChainCommand() {
                @Override
                public void execute() {
                    CONFIG_RPC.loadCurrentFilter(getGridContext(), new AsyncCallback<FilterBox>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            error(caught);
                        }

                        @Override
                        public void onSuccess(FilterBox result) {
                            filter = result;
                            getChainManager().next();
                        }
                    });
                }
            });
//3-grid config
            if (GRID_CONFIG) {
                commandChain.add(new SchedulerChainCommand() {
                    @Override
                    public void execute() {
                        CONFIG_RPC.loadConfiguration(getGridContext(), new AsyncCallback<List<ColumnCfg>>() {

                            @Override
                            public void onFailure(Throwable caught) {
                                error(caught);
                            }

                            @Override
                            public void onSuccess(List<ColumnCfg> result) {
                                if (result != null) {
                                    for (ColumnCfg config : result) {
                                        meta.addColumnConfig(config);
                                    }
                                }
                                getChainManager().next();
                            }
                        });
                    }
                });
            }
//4 initialize tird party 
            commandChain.add(new SchedulerChainCommand() {
                @Override
                public void execute() {
                    initManager.doInit(new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            error(caught);
                        }

                        @Override
                        public void onSuccess(Void result) {
                            // simple all is ok. simple go next.
                            getChainManager().next();
                        }
                    });
                }
            });
//5 initialized
            commandChain.add(new SyncSchedulerChainCommand() {
                @Override
                protected void onCommand() {
                    setMetaInfo(meta);
                    onSetFilters(filter, false);
                    initialized = true;
                    for (Command comm : onInitCommands) {
                        comm.execute();
                    }
                    onInitCommands.clear();
                    if (forceFilter) {
                        forceFilter = false;
                        showFilter(filter);
                    } else {
                        FilterTemplate flt = meta.getFilterTemplate();
                        Map<String, FilterInfo> fltMap = filter.getFilter().getSysCache();
                        boolean hasEmptyReq = false;
                        for (FilterTemplateItem it : flt.getItems()) {
                            if (it.getRequired()) {
                                FilterInfo inf = fltMap.get(it.getAlias());
                                Object val = inf != null ? inf.getValue() : null;
                                if ((val instanceof String) && ((String) val).trim().isEmpty()) {
                                    val = null;
                                }
                                if (val == null) {
                                    hasEmptyReq = true;
                                    break;
                                }
                            }
                        }
                        if (hasEmptyReq) {
                            showFilter(filter);
                        } else {
                            reload();
                        }
                    }
                    SystemGrid.this.initialLoader = null;
                }
            });

            final SchedulerChainManager commandChainManager = new SchedulerChainManager(commandChain);
            commandChainManager.next();
        }

        private void error(Throwable ex) {
            SystemGrid.this.initialLoader = null;
            ClientUtils.alertException(ex, errorLoadHandler);
        }
    }

    public ClientUtils.ErrorHandler getErrorLoadHandler() {
        return errorLoadHandler;
    }

    public void setErrorLoadHandler(ClientUtils.ErrorHandler errorHandler) {
        this.errorLoadHandler = errorHandler;
    }

    private static class GridIntermediator<M extends MarkModel> {

        private final SBGrid<M> grid;
        private final GroupingHandlerRegistration frozenGridHandlers = new GroupingHandlerRegistration();
        private FrozenGrid<M> frozenGrid = null;

        public GridIntermediator(final SBGrid<M> grid) {
            Objects.requireNonNull(grid, "Grid in GridManager can't be null");
            this.grid = grid;
        }

        IRowByIndexProvider getMainRowByIndexProvider() {
            return (i) -> (Row) grid.getStore().get(i);
        }

        IRowByIndexProvider getFrozenRowByIndexProvider() {
            return (i) -> (Row) frozenGrid.getStore().get(i);
        }

        boolean hasFrozenGrid() {
            return frozenGrid != null;
        }

        SBGrid<M> getMarkGrid() {
            return grid;
        }

        void positionLiveScroller() {
            grid.getView().positionLiveScroller();
        }

        void reshow() {
            grid.getView().reshow();
            if (frozenGrid != null) {
                frozenGrid.getView().reshow();
            }
        }

        int getViewRowCount() {
            return grid.getView().getTotalCount();
        }

        int getAggregationSize() {
            return grid.getColumnModel().getAggregationRows().size();
        }

        void addAggregation(AggregationRowConfig<M> row) {
            grid.getColumnModel().addAggregationRow(row);
        }

        AggregationRowConfig<M> getAggregation(int index) {
            return grid.getColumnModel().getAggregationRow(index);
        }

        M getFirstRow() {
            return grid.getStore().size() > 0 ? grid.getStore().get(0) : null;
        }

        void deleteRow(final BigDecimal key) {
            if (key != null) {
                String keyStr = key.toString();
                grid.getView().deleteRowByKey(keyStr);
            }
        }

        void updateRow(final M model) {
            grid.getView().updateRow(model);
        }

        void insertRow(final M model) {
            M m = grid.getSelectionModel().getSelectedItem();
            grid.getView().insertRowAfter(model, m);
        }

        void clearData() {
            grid.getStore().clear();
            clearData(grid);
            if (frozenGrid != null) {
                clearData(frozenGrid);
            }
        }

        private static void clearData(Grid<?> g) {
            g.getView().setEmptyText(I18n.get(SBFBrowserStr.msgFilterNotSelected));
            g.getView().refresh(false);
        }

        void prepareReload() {
            grid.getStore().clear();
            prepareReload(grid);
            if (frozenGrid != null) {
                prepareReload(frozenGrid);
            }
        }

        private static <M extends MarkModel> void prepareReload(Grid<M> g) {
            g.getView().setAutoExpandColumn(null);
            g.reconfigure(g.getStore(), new ColumnModel<>(Collections.<ColumnConfig<M, ?>>emptyList()));
        }

        void setQuickTip(int flags) {
            new QTSetter(grid).setQuickTip(flags, GRID_TYPE_MASK_MAIN);
            if (frozenGrid != null) {
                new QTSetter(frozenGrid).setQuickTip(flags, GRID_TYPE_MASK_FROZEN);
            }
        }

        private class QTSetter {

            private final QuickTipAllowed grd;

            public QTSetter(QuickTipAllowed grd) {
                Objects.requireNonNull(grd, "Grid can't be null");
                this.grd = grd;
            }

            public void setQuickTip(int flags, int flag) {
                Scheduler.get().scheduleDeferred(() -> {
                    boolean b = ((flags & flag) == flag);
                    grd.setQuickTip(b);
                });

            }
        }

        int getColumnCount() {
            return grid.getColumnModel().getColumnCount();
        }

        boolean isViewReady() {
            return grid.isViewReady();
        }

        void pushLoadMask(boolean newFlag) {
            grid.pushLoadMask(newFlag);
            updateFrozenLoadMask();
        }

        void popLoadMask() {
            grid.popLoadMask();
            updateFrozenLoadMask();
        }

        private void updateFrozenLoadMask() {
            if (frozenGrid != null) {
                frozenGrid.setLoadMask(grid.isLoadMask());
            }
        }

        void refreshView(boolean headerToo) {
            grid.getView().refresh(headerToo);
            if (frozenGrid != null) {
                frozenGrid.getView().refresh(headerToo);
            }
        }

        void reconfigureMain(ColumnModel<M> cm, Column autoExpandColumn) {
            reconfigure(grid, cm, autoExpandColumn);
        }

        void reconfigureFrozen(ColumnModel<M> cm, Column autoExpandColumn) {
            Objects.requireNonNull(frozenGrid, "Frozen grid is not set");
            reconfigure(frozenGrid, cm, autoExpandColumn);
        }

        private static <M extends MarkModel> void reconfigure(Grid<M> g, ColumnModel<M> cm, Column autoExpandColumn) {
            g.reconfigure(g.getStore(), cm);
            setAutoexpandColumn(g, autoExpandColumn);
        }

        FrozenGrid<M> createFrozenGrid(ColumnModel<M> mod) {
            final FrozenView frozView = new FrozenView(((SystemGridView) grid.getView())::isLastPage);
            frozView.setForceFit(true);
            frozView.setColumnLines(true);
            frozView.setTrackMouseOver(false);

            final GridSelectionModel<M> frozSel = new FrozenSelectionModel<>();

            final FrozenGrid<M> frozGrid = new FrozenGrid(grid, mod, frozView);
            frozGrid.setSelectionModel(frozSel);
            frozGrid.setLoadMask(true);
            frozGrid.setColumnReordering(false);

            final FrozenColumnHeader<M> frozHead = new FrozenColumnHeader<>(frozGrid, mod);
            frozView.setColumnHeader(frozHead);

            frozenGridHandlers.add(grid.addBodyScrollHandler(new BodyScrollEvent.BodyScrollHandler() {
                @Override
                public void onBodyScroll(BodyScrollEvent event) {
                    frozView.getEditorParent().setScrollTop(event.getScrollTop());
                }
            }));
            frozenGridHandlers.add(grid.getView().getHeader().addResizeHandler(new ResizeHandler() {
                @Override
                public void onResize(ResizeEvent event) {
                    frozHead.adjustHeight(event.getHeight());
                }
            }));
            if (grid.getSelectionModel() instanceof CellSelectionModel) {
                frozenGridHandlers.add(((CellSelectionModel) grid.getSelectionModel()).addCellSelectionChangedHandler(new GridSelectionLinker<>(frozGrid)));
            } else {
                frozenGridHandlers.add(grid.getSelectionModel().addSelectionChangedHandler(new GridSelectionLinker<>(frozGrid)));
            }
            frozenGridHandlers.add(Event.addNativePreviewHandler((Event.NativePreviewEvent event) -> {
                final NativeEvent ne = event.getNativeEvent();
                final Element target = ne.getEventTarget().cast();
                final String neType = ne.getType();
                final boolean isMouseType = neType.contains("mouse") || neType.contains("Mouse");
                final boolean isExMouseType = isMouseType || neType.contains("click");
                final boolean isKeyType = neType.contains("key");

                if (frozGrid.getElement().isOrHasChild(target) && (isExMouseType || isKeyType) && !(neType.equals("mouseover") || neType.equals("mouseout"))) {
                    event.cancel();
                    event.getNativeEvent().preventDefault();
                    event.getNativeEvent().stopPropagation();
                    if (GXTLogConfiguration.loggingIsEnabled() && isKeyType) {
                        logger.finest("PreviewNativeEvent CANCELLED type: " + neType);
                    }
                } else {
                    if (GXTLogConfiguration.loggingIsEnabled() && isKeyType) {
                        logger.finest("PreviewNativeEvent type: " + neType);
                    }
                }
            }));
            frozenGrid = frozGrid;
            updateFrozenLoadMask();
            return frozGrid;
        }

        void removeFrozenGrid() {
            frozenGridHandlers.removeHandler();
            frozenGrid = null;
        }
    }

    private static class FrozenColumnHeader<M> extends ColumnHeader<M> {

        private CellHeightAdjuster adjuster = new CellHeightAdjuster();

        public FrozenColumnHeader(Grid<M> container, ColumnModel<M> cm) {
            super(container, cm);
        }

        public void adjustHeight(int toHeight) {
            setHeight(toHeight);
            adjuster.doAdjust(toHeight);
        }

        private class CellHeightAdjuster {

            private Integer h = null;
            private boolean sheduled = false;

            public void doAdjust(int height) {
                if (table.getRowCount() > 0 && table.getCellCount(0) > 0) {
                    h = null;
                    table.getFlexCellFormatter().getElement(0, 0).getStyle().setHeight(height - 2, com.google.gwt.dom.client.Style.Unit.PX);

                } else {
                    h = height;
                    sched();
                }
            }

            private void sched() {
                if (!sheduled) {
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            sheduled = false;
                            if (h != null) {
                                doAdjust(h);
                            }
                        }
                    });
                    sheduled = true;
                }
            }
        }
    }

    private static class FrozenGrid<M extends MarkModel> extends Grid<M> implements QuickTipAllowed {

        private QuickTip quickTip = null;

        public FrozenGrid(final Grid<M> mainGrid, ColumnModel<M> cm, FrozenView<M> view) {
            super(mainGrid.getStore(), cm, view);
            mainGrid.addResizeHandler(new ResizeHandler() {
                @Override
                public void onResize(ResizeEvent event) {
                    Scheduler.get().scheduleFinally(() -> {
                        int gh = mainGrid.getView().getEditorParent().getClientHeight();
                        Style sty = getView().getEditorParent().getStyle();
                        sty.setProperty("maxHeight", gh, Style.Unit.PX);
                        sty.setProperty("height", gh, Style.Unit.PX);
                    });
                }
            });
        }

        @Override
        public void setQuickTip(boolean on) {
            if (quickTip == null && on) {
                quickTip = new QuickTip(this);
            } else if (quickTip != null && !on) {
                quickTip.initTarget(null);
                quickTip = null;
            }
        }

        @Override
        public FrozenView<M> getView() {
            return (FrozenView<M>) super.getView();
        }

        @Override
        protected Size adjustSize(Size size) {
            return new Size(size.getWidth() + XDOM.getScrollBarWidth() + 1, size.getHeight());
        }
    }

    private interface IPage {

        boolean isLast();
    }

    private static class FrozenView<M extends MarkModel> extends GridView<M> {

        public FrozenView(IPage page) {
            setViewConfig(new SysViewConfig<>(() -> ds, () -> cm, page::isLast));
        }

        public void doLayout() {

            lastViewWidth = -1;
            layout(true);

        }

        @Override
        protected void calculateVBar(boolean force) {
            if (force) {
                resize();
            }
            if (force || !this.vbar) {
                this.vbar = true;
                lastViewWidth = -1;
                layout(true);
            }
        }

        @Override
        protected void renderUI() {
            super.renderUI();
            scroller.getStyle().setOverflowY(Style.Overflow.HIDDEN);
        }

        @Override
        public void scrollToTop(boolean resetHorizontal) {
            super.scrollToTop(resetHorizontal);
        }

        public void reshow() {
            boolean p = preventScrollToTopOnRefresh;
            preventScrollToTopOnRefresh = true;
            refresh(false);
            preventScrollToTopOnRefresh = p;
        }
    }

    private static class FrozenSelectionModel<M> extends GridSelectionModel<M> {

        @Override
        public void bindGrid(Grid<M> grid) {
            super.bindGrid(grid);
            if (handlerRegistration != null) {
                handlerRegistration.removeHandler();
            }
            keyNav.bind(null);
        }

    }

    private static class GridSelectionLinker<M> implements SelectionChangedEvent.SelectionChangedHandler<M>, CellSelectionChangedEvent.CellSelectionChangedHandler<M> {

        private final ISelectionModelAdapter<M> linkedAdapter;

        public GridSelectionLinker(Grid<M> grid) {
            this.linkedAdapter = SelectionModelAdapterFactory.createSelectionAdapter(grid);
        }

        @Override
        public void onSelectionChanged(SelectionChangedEvent<M> event) {
            doSync(event.getSource());
        }

        @Override
        public void onCellSelectionChanged(CellSelectionChangedEvent<M> event) {
            doSync(event.getSource());
        }

        private void doSync(StoreSelectionModel<M> m) {
            linkedAdapter.setSelection(m.getSelectedItem());
        }
    }

    public interface IRowByIndexProvider {

        Row getRow(int index);
    }

    private static class SysViewConfig<M> extends ColViewConfig<M> {

        private final Supplier<ListStore<M>> dsGetter;
        private final Supplier<ColumnModel<M>> cmGetter;
        private final BooleanSupplier isLastPage;

        public SysViewConfig(Supplier<ListStore<M>> dsGetter, Supplier<ColumnModel<M>> cmGetter, BooleanSupplier isLastPage) {
            super(Styles.INST);
            this.dsGetter = dsGetter;
            this.cmGetter = cmGetter;
            this.isLastPage = isLastPage;
        }

        @Override
        public String getColStyle(Object model, ValueProvider valueProvider, int rowIndex, int colIndex) {
            final ListStore<M> ds = dsGetter.get();
            final ColumnModel<M> cm = cmGetter.get();
            StringBuilder style = new StringBuilder();
            if (rowIndex == ds.size() - 1 && isLastPage.getAsBoolean()) {
                style.append(Styles.INST.sbCellLast());
            } else {
                style.append(Styles.INST.sbCell());
            }
            final Cell cell = cm.getColumn(colIndex).getCell();
            if (cell instanceof ImageResourceCell) {
                style.append(' ').append(Styles.INST.gridImageResourceCell());
            }
            return style.toString();
        }
    }
}
