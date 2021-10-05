package ru.sbsoft.client.components.browser;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.sencha.gxt.core.client.GXTLogConfiguration;
import com.sencha.gxt.core.client.util.KeyNav;
import com.sencha.gxt.widget.core.client.event.BeforeStartEditEvent;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import java.util.logging.Logger;
import ru.sbsoft.client.components.grid.SBGridSelectionBehavior;
import ru.sbsoft.client.components.grid.SBMultiCellSelectionModel;
import ru.sbsoft.client.components.grid.SystemGrid;
import ru.sbsoft.client.components.grid.column.CustomColumnConfig;
import ru.sbsoft.shared.grid.condition.IGridCondition;
import ru.sbsoft.shared.grid.format.IExpCellFormat;
import ru.sbsoft.shared.meta.Row;

/**
 *
 * @author Kiselev
 */
public class SBGridInlineEditing extends GridInlineEditing<Row> {

    private static Logger logger = Logger.getLogger(SBGridInlineEditing.class.getName());

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
        callback = new Grid.Callback() {
            @Override
            public boolean isSelectable(Grid.GridCell cell) {
                return parentWalkCallback.isSelectable(cell) && isCellEditable(cell);
            }
        };
        addBeforeStartEditHandler(new BeforeStartEditEvent.BeforeStartEditHandler<Row>() {
            @Override
            public void onBeforeStartEdit(BeforeStartEditEvent<Row> event) {
                if (!isCellEditable(event.getEditCell())) {
                    event.setCancelled(true);
                } else {
                    setNumberFormatCell(event.getEditCell());
                }
            }
        });

    }

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
                }
            }
        }
    }

    public boolean isCellEditable(Grid.GridCell gridCell) {
        ColumnConfig<Row, ?> gridCol = getEditableGrid().getColumnModel().getColumn(gridCell.getCol());
        if (gridCol instanceof CustomColumnConfig) {
            IGridCondition rowCon = systemGrid.getMetaInfo().getEditCondition();
            IGridCondition con = ((CustomColumnConfig<?>) gridCol).getColumn().getEditCondition();
            Row row = getEditableGrid().getStore().get(gridCell.getRow());
            if (systemGrid.isReadOnly() || (rowCon != null && !rowCon.isMatch(row)) || (con != null && !con.isMatch(row))) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    protected <N, O> void doStartEditing(Grid.GridCell cell) {
        super.doStartEditing(cell);
        lastActiveCell = activeCell;
    }

    @Override
    public void cancelEditing() {
        super.cancelEditing();
        lastActiveCell = activeCell;
    }

    @Override
    protected void onEnter(NativeEvent evt) {
        if (GXTLogConfiguration.loggingIsEnabled()) {
            logger.finest("onEnter");
        }
        if (isOnEdit()) {
            moveEdit(false, evt);
        } else {
            super.onEnter(evt);
        }
    }

    @Override
    protected void onMouseDown(MouseDownEvent event) {

        if (event.isShiftKeyDown()) {
            //cancelEditing();
            //systemGrid.getGrid().getSelectionModel().
//activeEdit = false;
            //rowUpdated = false;

        } else {
            super.onMouseDown(event); //To change body of generated methods, choose Tools | Templates.
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
        if (GXTLogConfiguration.loggingIsEnabled()) {
            logger.finest("moveEdit => " + (horizontally ? "horizontally" : "vertically") + "; " + (forward ? "forward" : "backward"));
        }

        // keep active cell since we manually fire blur (finishEditing) which will
        // call cancel edit
        // clearing active cell
        final Grid.GridCell active = activeCell != null ? activeCell : lastActiveCell;

        if (GXTLogConfiguration.loggingIsEnabled()) {
            logger.finest("moveEdit activeCell is " + (activeCell == null ? "null" : "not null"));
            logger.finest("moveEdit lastActiveCell is " + (lastActiveCell == null ? "null" : "not null"));
        }
        if (activeCell != null) {
            ColumnConfig<Row, ?> c = columnModel.getColumn(activeCell.getCol());

            IsField<?> field = getEditor(c);

            // since we are preventingDefault on tab key, the field will not blur on
            // its
            // own, which means the value change event will not fire so we manually
            // blur
            // the field, so we call finishEditing
            if (GXTLogConfiguration.loggingIsEnabled()) {
                logger.finest("moveEdit calling field.finishEditing()");
            }
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
                                if (GXTLogConfiguration.loggingIsEnabled()) {
                                    logger.finest("moveEdit scheduleFinally startEditing");
                                }
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

}
