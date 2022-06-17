/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.selection;

public class CellSelection<M> {
  private int cell;
  private M model;
  private int row;

  public CellSelection(M model, int row, int cell) {
    this.model = model;
    this.row = row;
    this.cell = cell;
  }

  public int getCell() {
    return cell;
  }

  public M getModel() {
    return model;
  }

  public int getRow() {
    return row;
  }
  
  
}
