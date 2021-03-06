/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.grid.editing;

import ru.sbsoft.svc.data.shared.Converter;
import ru.sbsoft.svc.widget.core.client.event.BeforeStartEditEvent.HasBeforeStartEditHandlers;
import ru.sbsoft.svc.widget.core.client.event.CancelEditEvent.HasCancelEditHandlers;
import ru.sbsoft.svc.widget.core.client.event.CompleteEditEvent.HasCompleteEditHandlers;
import ru.sbsoft.svc.widget.core.client.event.StartEditEvent.HasStartEditHandlers;
import ru.sbsoft.svc.widget.core.client.form.IsField;
import ru.sbsoft.svc.widget.core.client.grid.ColumnConfig;
import ru.sbsoft.svc.widget.core.client.grid.Grid;
import ru.sbsoft.svc.widget.core.client.grid.Grid.GridCell;

/**
 * Defines the interface for classes that support grid editing.
 * 
 * @see GridInlineEditing
 * @see GridRowEditing
 * 
 * @param <M> the model type
 */
public interface GridEditing<M> extends HasBeforeStartEditHandlers<M>, HasStartEditHandlers<M>,
    HasCompleteEditHandlers<M>, HasCancelEditHandlers<M> {

  /**
   * Adds an editor for the given column.
   * 
   * @param columnConfig the column config
   * @param converter the converter
   * @param field the field
   */
  <N, O> void addEditor(ColumnConfig<M, N> columnConfig, Converter<N, O> converter, IsField<O> field);

  /**
   * Adds an editor for the given column.
   * 
   * @param columnConfig the column config
   * @param field the field
   */
  <N> void addEditor(ColumnConfig<M, N> columnConfig, IsField<N> field);

  /**
   * Cancels an active edit.
   */
  void cancelEditing();

  /**
   * Completes the active edit.
   */
  void completeEditing();

  /**
   * Returns the converter for the given column.
   * 
   * @param columnConfig the column config
   * @return the converter
   */
  <N, O> Converter<N, O> getConverter(ColumnConfig<M, N> columnConfig);

  /**
   * Returns the target grid.
   * 
   * @return the target grid
   */
  Grid<M> getEditableGrid();

  /**
   * Returns the editor for the given column.
   * 
   * @param columnConfig the column config
   * @return the editor
   */
  <O> IsField<O> getEditor(ColumnConfig<M, ?> columnConfig);

  /**
   * Returns true if editing is active.
   * 
   * @return true if editing
   */
  boolean isEditing();

  /**
   * Removes the editor for the given column.
   * 
   * @param columnConfig the column config
   */
  void removeEditor(ColumnConfig<M, ?> columnConfig);

  /**
   * Sets the target grid to be edited.
   * 
   * @param editableGrid the editable grid
   */
  void setEditableGrid(Grid<M> editableGrid);

  /**
   * Starts editing for the given cell.
   * 
   * @param cell the cell
   */
  void startEditing(GridCell cell);
}
