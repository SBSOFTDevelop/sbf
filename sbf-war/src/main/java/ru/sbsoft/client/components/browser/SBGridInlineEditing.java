package ru.sbsoft.client.components.browser;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.core.client.SVCLogConfiguration;
import ru.sbsoft.svc.core.client.dom.XDOM;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.util.KeyNav;
import ru.sbsoft.svc.widget.core.client.form.CheckBox;
import ru.sbsoft.svc.widget.core.client.form.ComboBox;
import ru.sbsoft.svc.widget.core.client.form.IsField;
import ru.sbsoft.svc.widget.core.client.form.NumberField;
import ru.sbsoft.svc.widget.core.client.grid.*;
import ru.sbsoft.svc.widget.core.client.grid.editing.GridInlineEditing;
import java.util.Objects;
import java.util.logging.Logger;
import ru.sbsoft.client.components.grid.SBGridSelectionBehavior;
import ru.sbsoft.client.components.grid.SBLiveGridView;
import ru.sbsoft.client.components.grid.SystemGrid;
import ru.sbsoft.client.components.grid.column.CustomColumnConfig;
import ru.sbsoft.shared.grid.condition.IGridCondition;
import ru.sbsoft.shared.grid.format.IExpCellFormat;
import ru.sbsoft.shared.meta.IColumn;
import ru.sbsoft.shared.meta.Row;

/**
 * @author Kiselev
 */
public class SBGridInlineEditing extends GridInlineEditing<Row> {

    private static Logger logger = Logger.getLogger(SBGridInlineEditing.class.getName());

    private static void logGWT(String msg) {

        if (SVCLogConfiguration.loggingIsEnabled()) {
            logger.finest(msg);
        }

    }

    protected class SBGridEditingKeyNav extends GridEditingKeyNav {

        @Override
        public void onUp(NativeEvent evt) {
            SBGridInlineEditing.this.onUp(evt);
        }

        @Override
        public void onDown(NativeEvent evt) {
            SBGridInlineEditing.this.onDown(evt);
        }

    }

    private final SystemGrid<Row> systemGrid;
    private Grid.GridCell lastActiveCell = null;
    private SBGridSelectionBehavior<Row> selectionBehavior = new SBGridSelectionBehavior<>();

    public SBGridInlineEditing(SystemGrid<Row> systemGrid) {
        super(systemGrid.getGrid());
        this.systemGrid = systemGrid;
        selectionBehavior.bindGrid(systemGrid.getGrid());

        final Grid.Callback parentWalkCallback = callback;
        callback = cell -> parentWalkCallback.isSelectable(cell) && isCellEditable(cell);
        addBeforeStartEditHandler(event -> {
            if (!isCellEditable(event.getEditCell())) {
                event.setCancelled(true);
            } else {
                setNumberFormatCell(event.getEditCell());
                systemGrid.getGrid().getSelectionModel().fireEvent(event);

            }
        });

    }

    /*
    @Override
    protected void onClick(ClickEvent event) {
        if (event.isAltKeyDown() && event.getNativeButton() == NativeEvent.BUTTON_LEFT && systemGrid.getGrid().getSelectionModel() instanceof SBMultiCellSelectionModel) {

            if (isEditing()) {
                cancelEditing();
            }

            return;
        }

        super.onClick(event);
    }
     */
    private void setNumberFormatCell(Grid.GridCell gridCell) {
        ColumnConfig<Row, ?> gridCol = getEditableGrid().getColumnModel().getColumn(gridCell.getCol());
        if (gridCol instanceof CustomColumnConfig) {
            CustomColumnConfig col = (CustomColumnConfig) gridCol;
            IExpCellFormat expCellFormat = col.getColumn().getExpCellFormat();
            if (expCellFormat != null) {
                IsField field = editorMap.get(gridCol);
                if (field instanceof NumberField) {
                    Row row = getEditableGrid().getStore().get(gridCell.getRow());
                    NumberField numberField = (NumberField) field;
                    String format = expCellFormat.getFormat(row);
                    numberField.setFormat(NumberFormat.getFormat(format));
                    numberField.getPropertyEditor().setFormat(NumberFormat.getFormat(format));
                    numberField.setAllowNegative(!isSignPositive(gridCell));
                }
            }
        }
    }

    private boolean isSignPositive(Grid.GridCell gridCell) {
        final ColumnModel<Row> cm = getEditableGrid().getColumnModel();
        ColumnConfig<Row, ?> gridCol = cm.getColumn(gridCell.getCol());
        if (gridCol instanceof CustomColumnConfig) {
            final IColumn column = ((CustomColumnConfig<?>) gridCol).getColumn();
            if(column.getSignPositiveCondition() != null) {
                Row row = getEditableGrid().getStore().get(gridCell.getRow());
                if (column.getSignPositiveCondition().isMatch(row)) {
                    return true;
                }
            }
            if (column.getSignAllCondition() != null) {
                Row row = getEditableGrid().getStore().get(gridCell.getRow());
                if (column.getSignAllCondition().isMatch(row)) {
                    return false;
                } 
            }
        }
        return systemGrid.getMetaInfo().isSignPositive();
    }

    public boolean isCellEditable(Grid.GridCell gridCell) {

        return isCellEditable(gridCell, false);
    }

    public boolean isCellEditable(Grid.GridCell gridCell, boolean inCache) {

        final ColumnModel<Row> cm = getEditableGrid().getColumnModel();
        ColumnConfig<Row, ?> gridCol = cm.getColumn(gridCell.getCol());
        if (gridCol instanceof CustomColumnConfig) {
            IGridCondition rowCon = systemGrid.getMetaInfo().getEditCondition();
            final IColumn column = ((CustomColumnConfig<?>) gridCol).getColumn();

            IGridCondition con = column.getEditCondition();

            Row row = inCache ? ((SBLiveGridView<Row>) getEditableGrid().getView()).getCacheStore().get(gridCell.getRow()) : getEditableGrid().getStore().get(gridCell.getRow());

            if (row == null) {
                return false;
            }

            if (systemGrid.isReadOnly() || cm.isHidden(gridCell.getCol()) || (rowCon != null && !rowCon.isMatch(row)) || (con != null && !con.isMatch(row))) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean isLastVisibleCol(ColumnConfig c) {

        for (int i = columnModel.getColumnCount() - 1; i >= 0; i--) {
            if (columnModel.getColumn(i).isHidden()) {
                continue;
            }
            return (Objects.equals(columnModel.getColumn(i), c));

        }
        return false;
    }

    @Override
    protected <N, O> void doStartEditing(Grid.GridCell cell) {
        super.doStartEditing(cell);
        lastActiveCell = activeCell;

        adjustComboWidth(cell);

        if (getEditableGrid() != null && getEditableGrid().isAttached() && cell != null) {
            Row value = getEditableGrid().getStore().get(cell.getRow());
            ColumnConfig<Row, N> c = columnModel.getColumn(cell.getCol());
            if (c != null && value != null) {
                final IsField<O> ed = getEditor(c);
                final Element row = getEditableGrid().getView().getRow(cell.getRow());
                if (ed != null && row != null) {
                    final Widget edw = ed.asWidget();
                    final XElement edEl = edw.getElement().<XElement>cast();
                    if (ed instanceof CheckBox) {
                        CheckBox cb = (CheckBox) ed;
                        cb.setBorders(false);
                        final int msz = Math.min(row.getClientHeight(), c.getWidth());
                        final int of = msz / 20;
                        final int sz = msz - of * 2;
                        edw.setPixelSize(sz, sz);
                        XElement inEl = cb.getCell().getAppearance().getInputElement(cb.getElement());
                        inEl.setSize(sz, sz);
                        final int top = edEl.getTop() + (row.getClientHeight() - sz) / 2;
                        final int left;
                        if (HasHorizontalAlignment.ALIGN_LEFT == c.getHorizontalAlignment()) {
                            left = edEl.getLeft();
                        } else if (HasHorizontalAlignment.ALIGN_RIGHT == c.getHorizontalAlignment()) {
                            left = edEl.getLeft() + c.getWidth() - sz;
                        } else {
                            left = edEl.getLeft() + (c.getWidth() - sz) / 2;
                        }
                        edEl.setLeftTop(left, top);
                    }
                }
            }
        }
    }

    private void adjustComboWidth(Grid.GridCell cell) {
        logGWT("doStartEditing adjust 0");

        if (cell == null || getEditableGrid() == null || !getEditableGrid().isAttached()
                || !(getEditableGrid().getView() instanceof SBLiveGridView) || !((SBLiveGridView) getEditableGrid().getView()).isAutoFit()) {
            return;
        }
        logGWT("doStartEditing adjust 1");

        ColumnConfig c = columnModel.getColumn(cell.getCol());
        // columnModel.getColumn(1).isHidden()
        if (c != null && isLastVisibleCol(c)) {

            logGWT("doStartEditing adjust 2");

            final IsField<?> field = getEditor(c);

            if (field instanceof ComboBox) {

                Widget w = field.asWidget();

                int scrollAdj = Math.max(XDOM.getScrollBarWidth(), 14);
                logGWT("doStartEditing adjust combo width with " + scrollAdj);
                w.setPixelSize(c.getWidth() - scrollAdj, -2147483648);

            }
        }
    }

    @Override
    public void cancelEditing() {
        super.cancelEditing();
        lastActiveCell = activeCell;
    }

    @Override
    protected void onEnter(NativeEvent evt) {

        logGWT("onEnter");

        if (isOnEdit()) {
            moveEdit(false, evt);
        } else {
            super.onEnter(evt);
        }
    }

    @Override
    protected void onTab(NativeEvent evt) {
        moveEdit(true, evt);
    }

    protected void onUp(NativeEvent evt) {
        moveEdit(false, evt, false);
    }

    protected void onDown(NativeEvent evt) {
        moveEdit(false, evt, true);
    }

    protected void moveEdit(boolean horizontally, NativeEvent evt) {
        moveEdit(horizontally, evt, true);
    }

    protected void moveEdit(boolean horizontally, NativeEvent evt, boolean forward) {
        if (isOnEdit()) {
            // we handle navigation programatically
            evt.preventDefault();
            moveEdit(horizontally, forward ^ evt.getShiftKey());
        }
    }

    private boolean isOnEdit() {
        return activeCell != null || lastActiveCell != null;
    }

    protected void moveEdit(boolean horizontally, boolean forward) {

        logGWT("moveEdit => " + (horizontally ? "horizontally" : "vertically") + "; " + (forward ? "forward" : "backward"));

        // keep active cell since we manually fire blur (finishEditing) which will
        // call cancel edit
        // clearing active cell
        final Grid.GridCell active = activeCell != null ? activeCell : lastActiveCell;

        logGWT("moveEdit activeCell is " + (activeCell == null ? "null" : "not null"));
        logGWT("moveEdit lastActiveCell is " + (lastActiveCell == null ? "null" : "not null"));

        if (activeCell != null) {
            ColumnConfig<Row, ?> c = columnModel.getColumn(activeCell.getCol());

            IsField<?> field = getEditor(c);

            // since we are preventingDefault on tab key, the field will not blur on
            // its
            // own, which means the value change event will not fire so we manually
            // blur
            // the field, so we call finishEditing
            logGWT("moveEdit calling field.finishEditing()");

            field.finishEditing();
        }
        if (active != null) {
            Grid.Callback nextEditCallback = callback;
            if (!horizontally) {
                nextEditCallback = new Grid.Callback() {
                    @Override
                    public boolean isSelectable(Grid.GridCell cell) {
                        return cell.getCol() == active.getCol() && callback.isSelectable(cell);
                    }
                };
            }

            if (isEditing()) {
                completeEditing();
            }

            int step = forward ? 1 : -1;
            selectionBehavior.selectNextCell(active.getRow(), active.getCol() + step, step, nextEditCallback, new SBGridSelectionBehavior.CellResult() {
                @Override
                public void onResult(Grid.GridCell cell) {
                    if (cell != null) {
                        final Grid.GridCell nextCell = cell;
                        Scheduler.get().scheduleFinally(new Scheduler.ScheduledCommand() {
                            @Override
                            public void execute() {

                                logGWT("moveEdit scheduleFinally startEditing");

                                startEditing(nextCell);
                            }
                        });
                    } else {
                        Grid.Callback simpleCallback = new GridSelectionModel.SelectionModelCallback(editableGrid.getSelectionModel());
                        Grid.Callback selectCallback = horizontally ? simpleCallback : new Grid.Callback() {
                            @Override
                            public boolean isSelectable(Grid.GridCell cell) {
                                return cell.getCol() == active.getCol() && simpleCallback.isSelectable(cell);
                            }
                        };
                        selectionBehavior.selectNextCell(editableGrid.getStore().size() - 1, editableGrid.getColumnModel().getColumnCount() - 1, -1, selectCallback);
                    }
                }
            });
        }
    }

    @Override
    protected KeyNav ensureInternalKeyNav() {
        if (keyNav == null) {
            keyNav = new SBGridEditingKeyNav();
        }
        return keyNav;
    }

    public Grid.GridCell getCell(final NativeEvent event) {
        return findCell(event.getEventTarget().<Element>cast());

    }

}
