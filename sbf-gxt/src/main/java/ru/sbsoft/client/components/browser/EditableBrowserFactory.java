package ru.sbsoft.client.components.browser;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.client.components.grid.editable.EditableFieldFactory;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.GxtYearMonthDayConverter;
import com.sencha.gxt.core.client.GXTLogConfiguration;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.ValueBaseField;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.selection.CellSelection;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.actions.Action;
import ru.sbsoft.client.components.browser.actions.GridAction;
import ru.sbsoft.client.components.browser.filter.ComponentProvider;
import ru.sbsoft.client.components.browser.filter.DictionaryLoader;
import ru.sbsoft.client.components.grid.ContextGrid;
import ru.sbsoft.client.components.grid.GridEvent;
import ru.sbsoft.client.components.grid.GridMode;
import ru.sbsoft.client.components.grid.SBMultiCellSelectionModel;
import ru.sbsoft.client.components.grid.column.CustomColumnConfig;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.client.utils.DefaultAsyncCallback;
import ru.sbsoft.sbf.app.model.FormModel;
import ru.sbsoft.sbf.app.model.YearMonthDay;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FormContext;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.interfaces.NamedGridType;
import ru.sbsoft.shared.kladr.AddressModel;
import ru.sbsoft.shared.meta.AddressWrapper;
import ru.sbsoft.shared.meta.BigDecimalWrapper;
import ru.sbsoft.shared.meta.BooleanWrapper;
import ru.sbsoft.shared.meta.Column;
import ru.sbsoft.shared.meta.Columns;
import ru.sbsoft.shared.meta.RowsInfo;
import ru.sbsoft.shared.meta.DateWrapper;
import ru.sbsoft.shared.meta.LongWrapper;
import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.shared.meta.StringWrapper;
import ru.sbsoft.shared.meta.UpdateInfo;
import ru.sbsoft.shared.meta.Wrapper;
import ru.sbsoft.shared.meta.YearMonthDayWrapper;

public class EditableBrowserFactory extends BrowserFactory {

    private static Logger logger = Logger.getLogger(EditableBrowserFactory.class.getName());

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
    protected EditableCommonGrid createGridInstance() {
        return new EditableCommonGrid();
    }

    protected class EditableCommonGrid extends CommonGrid {

        protected GridInlineEditing<Row> gridEditing;
        private int firstEditableCol = 1;
        private Action directUpdateAction;
        private HandlerRegistration editKeyHandlerReg = null;

        private boolean autoedit = true;

        public EditableCommonGrid() {

            getGrid().setSelectionModel(new SBMultiCellSelectionModel((cell) -> ((SBGridInlineEditing) gridEditing).isCellEditable(cell)));

            addClipboardEngine();

        }

        public void setAutoedit(boolean autoedit) {
            this.autoedit = autoedit;
            updateAutoeditEngine(false);

        }

        public Action getDirectUpdateAction() {
            return directUpdateAction;
        }

        @Override
        public void initEventMap() {
            super.initEventMap();
            bindGridEventOnKey(/*113*/KeyCodes.KEY_F2, GridEvent.F2_KEY_PRESS);
            bindEvent(GridEvent.F2_KEY_PRESS, getDirectUpdateAction());
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
            super.initGrid(flags);
            updateAutoeditEngine(true);
        }

        private void initEditing() {

            if (gridEditing != null) {
                gridEditing.setEditableGrid(null);

            }
            gridEditing = new SBGridInlineEditing(this);

        }

        private void addClipboardEngine() {

            //  getGrid().addHandler(new MouseDownHandler())
            getGrid().addDomHandler(new MouseDownHandler() {

                public void onMouseDown(MouseDownEvent event) {

                    if (event.getNativeButton() == com.google.gwt.dom.client.NativeEvent.BUTTON_LEFT && gridEditing.isEditing() && event.isShiftKeyDown()) {
                        if (GXTLogConfiguration.loggingIsEnabled()) {
                            logger.finest("NativeEvent " + event.getNativeEvent().getType());
                        }

                        event.getNativeEvent().preventDefault();
                        event.getNativeEvent().stopPropagation();

                        Grid.GridCell activeCell = gridEditing.getActiveCell();

                        final ColumnModel<Row> columnModel = getGrid().getColumnModel();
                        final List<ColumnConfig<Row, ?>> columnConfigs = columnModel.getColumns();
                        final ColumnConfig<Row, ?> columnConfig = columnConfigs.get(activeCell.getCol());

                        final CustomColumnConfig customColumnConfig = (CustomColumnConfig) columnConfig;
                        final Column column = customColumnConfig.getColumn();
                        final ListStore<Row> store = getGrid().getStore();

                        int max_rec_cnt = getTotalRecordsCount() - activeCell.getRow();
                        int max_col_cnt = columnConfigs.size() - activeCell.getCol();

                        ClipboardForm form = new ClipboardForm(max_rec_cnt, max_col_cnt);

                        form.getClipboard((BigDecimal[][] dump) -> {

                            final Map<BigDecimal, Map<String, Wrapper>> rows = new HashMap<>();

                            for (int r = 0; r < dump.length; r++) {

                                Map row = new HashMap<>();
                                rows.put(store.getRecord(store.get(r + activeCell.getRow())).getModel().getPrimaryKeyValue(), row);

                                for (int c = 0; c < dump[r].length; c++) {
                                    if (!((SBGridInlineEditing) gridEditing).isCellEditable(new Grid.GridCell(r + activeCell.getRow(),
                                            c + activeCell.getCol()))) {
                                        continue;
                                    }

                                    Column cc = ((CustomColumnConfig) columnConfigs.get(c + activeCell.getCol())).getColumn();

                                    if (cc.getUpdateInfo() != null) {
                                        row.put(cc.getUpdateInfo().getColumn(), createWrapper(cc,
                                                dump[r][c]));
                                    }

                                }

                            }
                            getGrid().mask(I18n.get(SBFGeneralStr.loadData));
                            SBFConst.EDITABLE_GRID_UPDATE_SERVICE.updateRows(
                                    rows, column.getUpdateInfo().getTable(),
                                    new AsyncCallback<List<BigDecimal>>() {

                                @Override
                                public void onFailure(Throwable caught) {
                                    getGrid().unmask();

                                    ClientUtils.alertException(caught, null);
                                }

                                @Override
                                public void onSuccess(List<BigDecimal> result) {
                                    if (null != result) {

                                        refreshRows(result);

                                    }
                                    ((SBMultiCellSelectionModel) getGrid().getSelectionModel()).clearSelections();
                                    getGrid().unmask();
                                }
                            }
                            );

                        });

                    }

                }

            }, MouseDownEvent.getType());

        }

        @Override
        protected void setMetaInfo(Columns metaInfo) {
            super.setMetaInfo(metaInfo);
            initEditing();

            final ColumnModel<Row> columnModel = getGrid().getColumnModel();
            boolean isFirst = true;
            for (int i = 0; i < columnModel.getColumns().size(); i++) {
                final ColumnConfig<Row, ?> columnConfig = columnModel.getColumns().get(i);
                if (columnConfig instanceof CustomColumnConfig) {
                    final CustomColumnConfig column = (CustomColumnConfig) columnConfig;
                    final Column metaColumn = column.getColumn();
                    final UpdateInfo updateInfo = metaColumn.getUpdateInfo();
                    if (null == updateInfo) {
                        continue;
                    }
                    if (isFirst) {
                        firstEditableCol = i;
                        isFirst = false;
                    }
                    final EditableFieldFactory fieldFactory = new EditableFieldFactory(metaColumn,
                            new DictionaryLoader(new ComponentProvider() {

                                @Override
                                public Component getComponent() {
                                    return getGrid();
                                }
                            }, this));
                    final Field field = fieldFactory.createField();
                    final Converter converter = fieldFactory.createConverter();
                    if (null != field) {
                        gridEditing.addEditor(column, converter, field);
                    }
                }
            }

            getGrid().getView().setShowDirtyCells(false);

            gridEditing.addCompleteEditHandler(new CompleteEditEvent.CompleteEditHandler<Row>() {

                @Override
                public void onCompleteEdit(CompleteEditEvent<Row> event) {
                    final Grid.GridCell activeCell = event.getEditCell();
                    final ColumnModel<Row> columnModel = getGrid().getColumnModel();
                    final List<ColumnConfig<Row, ?>> columnConfigs = columnModel.getColumns();
                    final ColumnConfig<Row, ?> columnConfig = columnConfigs.get(activeCell.getCol());
                    if (columnConfig instanceof CustomColumnConfig) {
                        final CustomColumnConfig customColumnConfig = (CustomColumnConfig) columnConfig;
                        final Column column = customColumnConfig.getColumn();

                        final ValueProvider<? super Row, ?> valueProvider
                                = customColumnConfig.getValueProvider();

                        final ListStore<Row> store = getGrid().getStore();
                        final ListStore<Row>.Record record
                                = store.getRecord(store.get(activeCell.getRow()));
                        final Row row = record.getModel();
                        final Store.Change<Row, ?> change = record.getChange(valueProvider);

                        if (change != null) {

                            if (GXTLogConfiguration.loggingIsEnabled()) {
                                logger.finest("Changed :" + change.getValue());
                                logger.finest("Changed is dirty:" + record.isDirty());

                            }
                            Integer cellPrecision = null;
                            if (column.getExpCellFormat() != null) {
                                cellPrecision = column.getExpCellFormat().getPrecision(row);
                            }

                            if (getGrid().getSelectionModel() instanceof SBMultiCellSelectionModel) {

                                List<CellSelection<Row>> selCells = ((SBMultiCellSelectionModel) getGrid().getSelectionModel()).getSelectionsForCell(activeCell);

                                if (selCells != null) {
                                    final Set<Column> columns = new HashSet<>();

                                    for (CellSelection<Row> sel : selCells) {

                                        Column c = ((CustomColumnConfig) columnConfigs.get(sel.getCell())).getColumn();

                                        if (!columns.contains(c)) {

                                            c.setCustomInfo(new RowsInfo());
                                            columns.add(c);
                                        }

                                        ((RowsInfo) (c.getCustomInfo())).addRow(sel.getModel().getRECORD_ID());

                                    }
                                    getGrid().mask(I18n.get(SBFGeneralStr.loadData));
                                    gridEditing.cancelEditing();
                                    SBFConst.EDITABLE_GRID_UPDATE_SERVICE.updateColumns(
                                            new ArrayList<>(columns), cellPrecision,
                                            createWrapper(column, change.getValue()),
                                            new AsyncCallback<List<BigDecimal>>() {

                                        @Override
                                        public void onFailure(Throwable caught) {
                                            getGrid().unmask();
                                            record.revert();
                                            ClientUtils.alertException(caught, null);
                                        }

                                        @Override
                                        public void onSuccess(List<BigDecimal> result) {
                                            if (null != result) {

                                                refreshRows(result);

                                            }
                                            ((SBMultiCellSelectionModel) getGrid().getSelectionModel()).clearSelections();
                                            getGrid().unmask();
                                        }
                                    }
                                    );

                                    return;
                                }

                            }

                            SBFConst.EDITABLE_GRID_UPDATE_SERVICE.updateColumn(
                                    row.getPrimaryKeyValue(), column, cellPrecision,
                                    createWrapper(column, change.getValue()),
                                    new AsyncCallback<BigDecimal>() {

                                @Override
                                public void onFailure(Throwable caught) {
                                    unmask();
                                    record.revert();
                                    ClientUtils.alertException(caught, null);
                                }

                                @Override
                                public void onSuccess(BigDecimal result) {
                                    if (null != result) {

                                        refreshRow(result);

                                    }
                                }
                            }
                            );
                        }
                    }

                }

            }
            );
        }

        private Wrapper createWrapper(Column column, Object value) {
            switch (column.getType()) {
                case ADDRESS:
                    return new AddressWrapper((AddressModel) value);
                case BOOL:
                    return new BooleanWrapper((Boolean) value);
                case CURRENCY:
                case KEY:
                case TEMPORAL_KEY:
                    return new BigDecimalWrapper((BigDecimal) value);
                case DATE:
                    final YearMonthDay date
                            = GxtYearMonthDayConverter.convert((Date) value);
                    return new YearMonthDayWrapper(date);
                case DATE_TIME:
                case TIMESTAMP:
                    return new DateWrapper((Date) value);
                case IDENTIFIER:
                case INTEGER:
                    return new LongWrapper((Long) value);
                default:
                    return new StringWrapper(null == value ? null : value.toString());
            }
        }

        private boolean isAutoedit(KeyPressEvent event, IsField f) {
            if (f != null && event != null) {
                char pressChar = event.getCharCode();
                if (pressChar > 0) {
                    if (f instanceof NumberField) {
                        NumberField nf = (NumberField) f;
                        return (nf.isAllowNegative() && pressChar == '-') || (nf.isAllowDecimals() && nf.getDecimalSeparator().equals(String.valueOf(pressChar))) || Character.isDigit(pressChar);
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
                                    if (s != null && !s.isEmpty()) {
                                        ht.setText(s);
                                        if (w instanceof ValueBaseField) {
                                            final ValueBaseField vf = (ValueBaseField) w;
                                            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                                                @Override
                                                public void execute() {
                                                    vf.setCursorPos(s.length());
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        }
                    }
                }, KeyPressEvent.getType());
            }
        }

        private IsField<?> getEditor(Grid.GridCell forCell) {
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

//        @Override
//        public boolean isTableReadOnly() {
//            return RoleCheker.getInstance().isTableDirectReadOnly(getTableName());
//        }
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
                        return new Grid.GridCell(selRowIndex, firstEditableCol);
                    }
                }
                return null;
            }
        }

        private int getCurrentCellNum() {
            Grid.GridCell c = getCurrentCell();
            return c != null ? c.getCol() : firstEditableCol;
        }

        private void insertRow() {
            final FormContext formContext = getFormContext(null);
            final Map<String, FilterInfo> mix = new HashMap<String, FilterInfo>();
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
                for (FilterInfo f : filterList) {
                    filterMap.put(f.getColumnName(), f);
                }
            }
        }
    }
}
