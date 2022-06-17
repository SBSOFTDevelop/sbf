package ru.sbsoft.client.components.browser;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.cell.core.client.form.SvcYearMonthDayConverter;
import ru.sbsoft.svc.core.client.SVCLogConfiguration;
import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.svc.data.shared.Converter;
import ru.sbsoft.svc.data.shared.ListStore;
import ru.sbsoft.svc.data.shared.Store;
import ru.sbsoft.svc.widget.core.client.Component;
import ru.sbsoft.svc.widget.core.client.event.CompleteEditEvent;
import ru.sbsoft.svc.widget.core.client.form.IsField;
import ru.sbsoft.svc.widget.core.client.form.NumberField;
import ru.sbsoft.svc.widget.core.client.form.ValueBaseField;
import ru.sbsoft.svc.widget.core.client.grid.CellSelectionModel;
import ru.sbsoft.svc.widget.core.client.grid.ColumnConfig;
import ru.sbsoft.svc.widget.core.client.grid.ColumnModel;
import ru.sbsoft.svc.widget.core.client.grid.Grid;
import ru.sbsoft.svc.widget.core.client.selection.CellSelection;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.actions.Action;
import ru.sbsoft.client.components.actions.event.ChangedStateEvent;
import ru.sbsoft.client.components.actions.event.ChangedStateHandler;
import ru.sbsoft.client.components.actions.event.StdActKey;
import ru.sbsoft.client.components.browser.actions.GridAction;
import ru.sbsoft.client.components.browser.filter.DictionaryLoader;
import ru.sbsoft.client.components.form.IEditorComponent;
import ru.sbsoft.client.components.grid.*;
import ru.sbsoft.client.components.grid.column.CustomColumnConfig;
import ru.sbsoft.client.components.grid.editable.EditableFieldFactory;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.DefaultAsyncCallback;
import ru.sbsoft.sbf.app.model.FormModel;
import ru.sbsoft.sbf.app.model.YearMonthDay;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FormContext;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.grid.condition.GridEditVal;
import ru.sbsoft.shared.interfaces.NamedGridType;
import ru.sbsoft.shared.kladr.AddressModel;
import ru.sbsoft.shared.meta.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import ru.sbsoft.shared.util.IdNameLong;

public class EditableBrowserFactory extends BrowserFactory {

    private static final Logger logger = Logger.getLogger(EditableBrowserFactory.class.getName());
    private boolean multiSelectionMode = true;

    public EditableBrowserFactory(NamedGridType gridType) {
        super(gridType);
        addBrowserFlags(GridMode.HIDE_UPDATE);
    }

    @Override
    protected BaseBrowser createBrowserInstance(String idBrowser, String titleBrowser, ContextGrid grid, GridMode... flags) {
        BaseBrowser browser = super.createBrowserInstance(idBrowser, titleBrowser, grid, flags);
        grid.setDefferedContextMenu(null);

        return browser;
    }

    @Override
    public BrowserFactory setMultiselect(boolean b) {
        multiSelectionMode = b;
        return super.setMultiselect(b);

    }

    @Override
    protected EditableCommonGrid createGridInstance() {

        return new EditableCommonGrid();
    }

    public interface GridSavedListener {

        void onSavedGrid(EditableCommonGrid grid);
    }

    public class EditableCommonGrid extends CommonGrid implements ICloseController, IEditorComponent {

        protected SBGridInlineEditing gridEditing;
        private static final int FIRST_COL = 0;
        private final List<GridSavedListener> savedListeners = new ArrayList<>();
        private Action directUpdateAction;
        private Action pasteClipboardAction;
        private Action deleteMultiSellAction;

        private HandlerRegistration editKeyHandlerReg = null;

        private final CellSelectionModel selModel;

        private boolean autoedit = true;
        private final EditCache editCache;

        public EditableCommonGrid() {

            selModel = multiSelectionMode
                    ? new SBMultiCellSelectionModel((cell, inCache) -> ((SBGridInlineEditing) gridEditing).isCellEditable(cell, inCache))
                    : new SBCellSelectionModel();

            getGrid().setSelectionModel(selModel);

            switchOffDefaultFrozenGridMouseHandling();
            editCache = new EditCache();
            getGrid().addReconfigureHandler(ev -> {
                if (!editCache.isEmpty()) {
                    requireUnchanged(new DefaultAsyncCallback<>());
                    //ClientUtils.showError("Changes is not saved before grid reconfigure and will be lost");
                    //editCache.clear();
                }
            });
            final ListStore<Row> store = getGrid().getStore();
            store.addStoreAddHandler(ev -> {
                applyChanges(ev.getSource(), ev.getItems());
            });
            store.addStoreDataChangeHandler(ev -> {
                applyChanges(ev.getSource(), ev.getSource().getAll());
            });
            store.setAutoCommit(false);
        }

        private void applyChanges(final Store<Row> store, final List<Row> rows) {
            if (!editCache.isEmpty()) {
                Scheduler.get().scheduleDeferred(() -> {
                    Map<String, ? extends ColumnConfig<Row, ?>> cols = getColumnsByAlias();
                    rows.forEach(row -> {
                        Map<String, GridEditVal> editRow = editCache.getRow(row);
                        Store.Record rec = store.getRecord(row);
                        if (editRow != null && rec != null) {
                            editRow.values().forEach(v -> {
                                ColumnConfig<Row, ?> cfg = cols.get(v.getCol().getAlias());
                                if (cfg != null) {
                                    rec.addChange(cfg.getValueProvider(), v.getVal().getValue());
                                }
                            });
                        }
                    });
                });
            }
        }

        private Map<String, ? extends ColumnConfig<Row, ?>> getColumnsByAlias() {
            return getGrid().getColumnModel().getColumns().stream()
                    .map(cfg -> (CustomColumnConfig) ((cfg instanceof CustomColumnConfig) ? cfg : null))
                    .filter(cfg -> cfg != null)
                    .collect(Collectors.toMap(cfg -> cfg.getColumn().getAlias(), cfg -> cfg));
        }

        @Override
        protected Action createSaveAction() {
            return new SaveAction();
        }

        public void setAutoedit(boolean autoedit) {
            this.autoedit = autoedit;
            updateAutoeditEngine(false);
        }

        @Override
        public void initEventMap() {
            super.initEventMap();
            bindGridEventOnKey(KeyCodes.KEY_F2, GridEvent.DIRECT_EDIT);
            bindEvent(GridEvent.DIRECT_EDIT, directUpdateAction);
            bindGridEventOnKey(StdActKey.PASTE, GridEvent.CLIPBOARD_PASTE);
            bindEvent(GridEvent.CLIPBOARD_PASTE, pasteClipboardAction);
            bindGridEventOnKey(KeyCodes.KEY_DELETE, GridEvent.DELETE, false, false);
            bindGridEventOnKey(KeyCodes.KEY_BACKSPACE, GridEvent.DELETE, false, false);

            //bindGridEventOnKey(KeyCodes.KEY_DELETE, GridEvent.DELETE);
            //bindGridEventOnKey(KeyCodes.KEY_BACKSPACE, GridEvent.DELETE);
            bindEvent(GridEvent.DELETE, deleteMultiSellAction);
        }

        @Override
        public void initGrid(GridMode[] flags) {

            directUpdateAction = new GridAction(this) {
                @Override
                protected void onExecute() {
                    if (gridEditing != null && !gridEditing.isEditing()) {
                        getGrid().edit();
                    }
                }
            };

            pasteClipboardAction = new GridAction(this) {
                @Override
                protected void onExecute() {
                    if (gridEditing != null && gridEditing.isEditing()) {

                        doPaste();
                    }
                }
            };

            deleteMultiSellAction = new GridAction(this) {
                @Override
                protected void onExecute() {
                    if (!multiSelectionMode) {
                        return;
                    }

                    CellSelection<Row> sel = selModel.getSelectCell();

                    if (!gridEditing.isEditing() && sel != null) {
                        doUpdateCells(new Grid.GridCell(sel.getRow(), sel.getCell()), null);

                    }
                }
            };

            super.initGrid(flags);
            updateAutoeditEngine(true);
        }

        public void addSavedListener(GridSavedListener listener) {
            savedListeners.add(listener);
        }

        private void initEditing() {

            if (gridEditing != null) {
                gridEditing.setEditableGrid(null);

            }
            gridEditing = new SBGridInlineEditing(this);
            addClipboardEngine();
        }

        private void addClipboardEngine() {

            final MouseDownHandler pasteClipboardHandler = (MouseDownEvent event) -> {

                if (!event.isControlKeyDown() || !gridEditing.isEditing()) {
                    return;
                }
                event.getNativeEvent().preventDefault();
                event.getNativeEvent().stopPropagation();
                doPaste();

            };

            getGrid().addDomHandler(pasteClipboardHandler, MouseDownEvent.getType());

        }

        private CustomColumnConfig<?> getNextColumn(List<ColumnConfig<Row, ?>> columnConfigs, int index) {
            for (int i = index; i < columnConfigs.size(); i++) {
                if (!columnConfigs.get(i).isHidden() && columnConfigs.get(i) instanceof CustomColumnConfig) {
                    return (CustomColumnConfig) columnConfigs.get(i);
                }
            }
            return null;
        }

        private void doPaste() {

            final Grid.GridCell activeCell = gridEditing.getActiveCell();

            final ColumnModel<Row> colModel = getGrid().getColumnModel();
            final List<ColumnConfig<Row, ?>> columnConfigs = colModel.getColumns();

            final ListStore<Row> store = getGrid().getStore();

            int max_rec_cnt = getTotalRecordsCount() - activeCell.getRow();
            int max_col_cnt = columnConfigs.size() - activeCell.getCol();

            ClipboardForm form = new ClipboardForm(max_rec_cnt, max_col_cnt);

            form.getClipboard((BigDecimal[][] dump) -> {

                for (int r = 0; r < dump.length; r++) {

                    Row row_ = store.get(r + activeCell.getRow());
                    if (row_ == null) {
                        continue;
                    }

                    for (int c = 0; c < dump[r].length; c++) {

                        CustomColumnConfig<?> colConf = getNextColumn(columnConfigs, c + activeCell.getCol());

                        if (colConf == null) {
                            break;
                        }
                        if (SVCLogConfiguration.loggingIsEnabled()) {
                            IColumn cc = colConf.getColumn();
                            logger.finest("Columns alias=" + cc.getAlias()
                                    + ", Hidden=" + cc.isHidden()
                                    + ", Visible=" + cc.isVisible()
                                    + ", cc.idForUpdate= " + cc.isForUpdate()
                                    + ", cc.getEditCondition().isMatch=" + (cc.getEditCondition() == null || cc.getEditCondition().isMatch(row_))
                            );

                        }
                        if (!gridEditing.isCellEditable(new Grid.GridCell(r + activeCell.getRow(), colModel.indexOf(colConf)))) {
                            continue;
                        }
                        setVal(row_, colConf, dump[r][c]);
                    }
                }
            });
        }

        private <V> void doUpdateCells(Grid.GridCell cell, V value) {
            if (selModel instanceof SBMultiCellSelectionModel) {
                final ColumnModel<Row> cm = getGrid().getColumnModel();
                Set<CellSelection<Row>> selCells = ((SBMultiCellSelectionModel) selModel).getSelectionsForCell(cell);

                if (selCells != null && selCells.size() > 1) {
                    for (CellSelection<Row> sel : selCells) {
                        CustomColumnConfig colCfg = (CustomColumnConfig) cm.getColumn(sel.getCell());
                        IColumn c = colCfg.getColumn();
                        setVal(sel.getModel(), colCfg, value);
                    }
                    gridEditing.cancelEditing();
                }
            }
        }

        @Override
        protected void setMetaInfo(IColumns metaInfo) {
            super.setMetaInfo(metaInfo);
            initEditing();

            final ColumnModel<Row> cm = getGrid().getColumnModel();

            for (int i = 0; i < cm.getColumns().size(); i++) {
                final ColumnConfig<Row, ?> columnConfig = cm.getColumns().get(i);
                if (columnConfig instanceof CustomColumnConfig) {
                    final CustomColumnConfig column = (CustomColumnConfig) columnConfig;
                    final IColumn metaColumn = column.getColumn();
                    if (!metaColumn.isForUpdate()) {
                        continue;
                    }

                    final EditableFieldFactory fieldFactory = new EditableFieldFactory(metaColumn,
                            new DictionaryLoader(() -> getGrid(), this));
                    final IsField field = fieldFactory.createField();
                    final Converter converter = fieldFactory.createConverter();
                    if (null != field) {
                        gridEditing.addEditor(column, converter, field);
                    }

                }
            }

            getGrid().getView().setShowDirtyCells(true);

            gridEditing.addCompleteEditHandler((CompleteEditEvent<Row> event) -> {
                final Grid.GridCell activeCell = event.getEditCell();
                final ColumnConfig<Row, ?> columnConfig = cm.getColumn(activeCell.getCol());

                if (columnConfig instanceof CustomColumnConfig) {
                    final ValueProvider<? super Row, ?> valueProvider = columnConfig.getValueProvider();
                    final ListStore<Row> store = getGrid().getStore();
                    final ListStore<Row>.Record record = store.getRecord(store.get(activeCell.getRow()));
                    final Store.Change<Row, ?> change = record.getChange(valueProvider);

                    if (change != null) {
                        if (SVCLogConfiguration.loggingIsEnabled()) {
                            logger.finest("Changed :" + change.getValue());
                            logger.finest("Changed is dirty:" + record.isDirty());
                        }
                        final Object val = change.getValue();
                        boolean hasMultiSel = false;
                        if (selModel instanceof SBMultiCellSelectionModel) {
                            Set<CellSelection<Row>> selCells = ((SBMultiCellSelectionModel) selModel).getSelectionsForCell(activeCell);
                            hasMultiSel = selCells != null && selCells.size() > 1;
                        }
                        if (hasMultiSel) {
                            doUpdateCells(activeCell, val);
                        } else {
                            setVal(record.getModel(), columnConfig, val);
                        }
                    }
                }
            });
        }

        private boolean sizeGreateThan(Collection<?> collection, int i) {
            return collection != null && collection.size() > i;
        }

        private Wrapper createWrapper(IColumn column, Object value) {
            switch (column.getType()) {
                case ADDRESS:
                    return new AddressWrapper((AddressModel) value);
                case ID_NAME:
                    return new IdNameLongWrapper((IdNameLong) value);
                case BOOL:
                    return new BooleanWrapper((Boolean) value);
                case CURRENCY:
                case KEY:
                case TEMPORAL_KEY:
                    return new BigDecimalWrapper((BigDecimal) value);
                case DATE:
                case YMDAY:
                    final YearMonthDay date
                            = value instanceof YearMonthDay ? (YearMonthDay) value : SvcYearMonthDayConverter.convert((Date) value);
                    return new YearMonthDayWrapper(date);
                case DATE_TIME:
                case TIMESTAMP:
                    return new DateWrapper((Date) value);
                case IDENTIFIER:
                case INTEGER:
                    return new LongWrapper((Long) value);
                case VCHAR:
                    return new StringWrapper((String) value);
                default:
                    throw new IllegalArgumentException("Unknown ColumnType: " + column.getType());
            }
        }

        private Object normalizeValue(Row row, IColumn col, Object val) {
            if (val instanceof BigDecimal) {
                Integer cellPrecision = null;
                if (col.getExpCellFormat() != null) {
                    cellPrecision = col.getExpCellFormat().getPrecision(row);
                }
                if (cellPrecision != null) {
                    return ((BigDecimal) val).setScale(cellPrecision, RoundingMode.HALF_UP);
                }
            }
            return val;
        }

        private boolean isAutoedit(KeyPressEvent event, IsField f) {
            if (f != null && event != null) {
                char pressChar = event.getCharCode();
                if (pressChar > 0) {
                    if (f instanceof NumberField) {
                        NumberField nf = (NumberField) f;
                        return (nf.isAllowNegative() && pressChar == '-')
                                || (nf.isAllowDecimals()
                                && nf.getDecimalSeparator().equals(String.valueOf(pressChar)))
                                || Character.isDigit(pressChar);
                    }
                }
            }
            return false;
        }

        private void updateAutoeditEngine(boolean renew) {
            if ((!autoedit || renew) && editKeyHandlerReg != null) {
                editKeyHandlerReg.removeHandler();
                editKeyHandlerReg = null;
            }
            if (editKeyHandlerReg == null && autoedit) {
                editKeyHandlerReg = getGrid().addDomHandler(new KeyPressHandler() {
                    @Override
                    public void onKeyPress(KeyPressEvent event) {
                        if (gridEditing != null && !gridEditing.isEditing() && isAutoedit(event, getEditor(getCurrentCell()))) {
                            edit();
                            IsField f = getEditor(gridEditing.getActiveCell());
                            if (f != null) {
                                Widget w = f.asWidget();
                                if (w instanceof HasText) {
                                    HasText ht = (HasText) w;
                                    String s = Character.toString(event.getCharCode());
                                    if (!s.isEmpty()) {
                                        ht.setText(s);
                                        if (w instanceof ValueBaseField) {
                                            final ValueBaseField vf = (ValueBaseField) w;
                                            Scheduler.get().scheduleDeferred(() -> vf.setCursorPos(s.length()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }, KeyPressEvent.getType());
            }
        }

        protected IsField<?> getEditor(Grid.GridCell forCell) {
            if (forCell != null) {
                ColumnConfig<Row, ?> c = EditableCommonGrid.this.getGrid().getColumnModel().getColumn(forCell.getCol());
                return gridEditing.getEditor(c);
            }
            return null;
        }

        @Override
        protected void onUpdate(BigDecimal result) {
            super.onUpdate(result);
            if (!gridEditing.isEditing() && (getGrid().getSelectionModel() instanceof CellSelectionModel)) {
                CellSelectionModel cellSel = (CellSelectionModel) getGrid().getSelectionModel();
                CellSelection<Row> sel = cellSel.getSelectCell();
                if (sel != null) {
                    cellSel.selectCell(sel.getRow(), sel.getCell());
                }
            }
        }

        @Override
        protected boolean tryShowForm(Row model) {
            if (null == model) {
                insertRow();
            } else {
                if (!gridEditing.isEditing()) {
                    gridEditing.startEditing(new Grid.GridCell(getGrid().getStore().indexOf(model), getCurrentCellNum()));
                }
                return true;
            }
            return true;
        }

        private Grid.GridCell getCurrentCell() {
            if (getGrid().getSelectionModel() instanceof CellSelectionModel) {
                CellSelectionModel<Row> mod = (CellSelectionModel<Row>) getGrid().getSelectionModel();
                if (mod.getSelectCell() != null) {
                    CellSelection<Row> sel = mod.getSelectCell();
                    return new Grid.GridCell(sel.getRow(), sel.getCell());
                } else {
                    return null;
                }
            } else {
                Row selRow = getGrid().getSelectionModel().getSelectedItem();
                if (selRow != null) {
                    int selRowIndex = getGrid().getStore().indexOf(selRow);
                    if (selRowIndex >= 0) {
                        return new Grid.GridCell(selRowIndex, FIRST_COL);
                    }
                }
                return null;
            }
        }

        private int getCurrentCellNum() {
            Grid.GridCell c = getCurrentCell();
            return c != null ? c.getCol() : FIRST_COL;
        }

        private void insertRow() {
            final FormContext formContext = getFormContext(null);
            final Map<String, FilterInfo> mix = new HashMap<>();
            add(mix, getParentFilters());
            add(mix, getDefinedFilters());
            SBFConst.EDITABLE_GRID_UPDATE_SERVICE.insertRow(formContext, parentFilters, new DefaultAsyncCallback<FormModel>() {
                @Override
                public void onResult(FormModel result) {
                    loadAddedRow(result);
                }
            });
        }

        @Override
        protected void onInsert(final BigDecimal result) {
            final Row selected = getGrid().getSelectionModel().getSelectedItem();
            if (null != selected) {
                tryShowForm(selected);
            }
        }

        private void add(Map<String, FilterInfo> filterMap, List<FilterInfo> filterList) {
            if (filterList != null) {
                filterList.forEach(f -> {
                    filterMap.put(f.getColumnName(), f);
                });
            }
        }

        @Override
        public boolean isCloseSafe() {
            return !isChanged();
        }

        @Override
        public void confirmClose(AsyncCallback<Boolean> answer) {
            confirmSave(answer);
        }

        private void save() {
            save(new DefaultAsyncCallback<>());
        }

        public void save(AsyncCallback<Void> callback) {
            Objects.requireNonNull(callback, "Callback object must be provided for save in " + this.getClass().getName());
            if (isChanged()) {
                String tableName = this.getMetaInfo().getUpdateTableName();
                if (tableName == null) {
                    callback.onFailure(new IllegalStateException("System config error: db table for save is not set"));
                }
                final Browser br = GridUtils.findParentBrowser(EditableCommonGrid.this);
                Component cmp = br != null ? br : EditableCommonGrid.this;
                cmp.mask(I18n.get(SBFGeneralStr.saveData));
                updateRows(tableName, editCache.getVals(), new AsyncCallback<List<BigDecimal>>() {
                    @Override
                    public void onSuccess(List<BigDecimal> result) {
                        cmp.unmask();
                        getGrid().getStore().commitChanges();
                        commitChangesToLiveGridViewCacheStore();
                        clearChanges();
                        callback.onSuccess(null);
                        savedListeners.forEach(l -> l.onSavedGrid(EditableCommonGrid.this));
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        cmp.unmask();
                        callback.onFailure(caught);
                    }
                });
            } else {
                callback.onSuccess(null);
            }
        }

        protected void updateRows(String tableName, Map<BigDecimal, Map<String, GridEditVal>> rows, AsyncCallback<List<BigDecimal>> callback) {
            SBFConst.EDITABLE_GRID_UPDATE_SERVICE.updateRows(tableName, rows, callback);
        }

        private void commitChangesToLiveGridViewCacheStore() {
            ListStore<Row> cacheStore = getGrid().getView().getCacheStore();
            List<Row> updatedRows = new ArrayList<>();
            cacheStore.getAll().forEach(row -> {
                List vals = row.getValues();
                IColumns columns = row.getColumns();
                Map<String, GridEditVal> editRow = editCache.getRow(row);
                final Wrapper<Boolean> updated = new Wrapper<>(false);
                if (editRow != null && columns != null) {
                    editRow.forEach((alias, edVal) -> {
                        final IColumn column = columns.getColumnForAlias(alias);
                        if (column != null && edVal != null) {
                            vals.set(column.getIndex(), edVal.getVal().getValue());
                            updated.setValue(true);
                        }
                    });
                }
                if (updated.getValue()) {
                    row.setValues(vals);
                    updatedRows.add(row);
                }
            });
            updatedRows.forEach(r -> cacheStore.update(r));
        }

        @Override
        public HandlerRegistration addChangedStateHandler(ChangedStateHandler handler) {
            return addHandler(handler, ChangedStateEvent.getType());
        }

        @Override
        public boolean isChanged() {
            return !editCache.isEmpty();
        }

        @Override
        public void clearChanges() {
            finishEditing();
            editCache.clear();
            getGrid().getStore().rejectChanges();
            fireEvent(new ChangedStateEvent(false));
        }

        @Override
        public void finishEditing() {
            if (gridEditing != null && gridEditing.isEditing()) {
                gridEditing.completeEditing();
            }
        }

        protected Object getVal(Row row, String alias) {
            if (editCache.isEmpty()) {
                return row.getValue(alias);
            }
            Map<String, GridEditVal> entryVal = editCache.getRow(row);
            if (entryVal == null) {
                return row.getValue(alias);
            }
            GridEditVal gridEditVal = entryVal.get(alias);
            if (gridEditVal == null) {
                return row.getValue(alias);
            }
            return gridEditVal.getVal().getValue();

        }

        private void setVal(Row row, ColumnConfig<Row, ?> columnConfig, Object val) {
            if (columnConfig instanceof CustomColumnConfig) {
                final IColumn col = ((CustomColumnConfig) columnConfig).getColumn();
                val = normalizeValue(row, col, val);
                editCache.setValue(row, col, val);
                final ListStore<Row>.Record record = getGrid().getStore().getRecord(row);
                if (record != null) {
                    final ValueProvider property = columnConfig.getValueProvider();
                    record.addChange(property, val);
                }
                fireEvent(new ChangedStateEvent(true));
            } else {
                throw new IllegalArgumentException("Only " + CustomColumnConfig.class.getName() + " is supported for value set");
            }
        }

        private class EditCache {

            private Map<BigDecimal, Map<String, GridEditVal>> rows = new HashMap<>();

            public void setValue(Row r, IColumn c, Object v) {
                Map<String, GridEditVal> row = rows.computeIfAbsent(r.getPrimaryKeyValue(), key -> new HashMap<>());
                row.put(c.getAlias(), new GridEditVal(c, createWrapper(c, v)));
            }

            public Map<BigDecimal, Map<String, GridEditVal>> getVals() {
                return new HashMap(rows);
            }

            public void clear() {
                rows.clear();
            }

            public boolean isEmpty() {
                return rows.isEmpty();
            }

            public Map<String, GridEditVal> getRow(Row r) {
                return rows.get(r.getPrimaryKeyValue());
            }
        }

        private class SaveAction extends GridAction {

            public SaveAction() {
                super(EditableCommonGrid.this);
                setCaption(SBFGeneralStr.labelSave);
                setToolTip(SBFGeneralStr.hintSave);
                setIcon16(SBFResources.GENERAL_ICONS.Save16());
                setIcon24(SBFResources.GENERAL_ICONS.Save());
            }

            @Override
            protected void onExecute() {
                finishEditing();
                EditableCommonGrid.this.save();
            }

            @Override
            public boolean checkEnabled() {
                return super.checkEnabled() && !editCache.isEmpty();
            }
        }
    }
}
