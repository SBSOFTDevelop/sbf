/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client;

import java.util.List;

import ru.sbsoft.svc.widget.core.client.event.CheckChangedEvent.HasCheckChangedHandlers;

/**
 * Interface for objects that provide check state.
 * 
 * @param <M> the model type
 */
public interface CheckProvider<M> extends HasCheckChangedHandlers<M> {

  /**
   * Returns the current checked selection.
   * 
   * @return the checked selection
   */
  public List<M> getCheckedSelection();

  /**
   * Returns true if the model is checked.
   * 
   * @param model the model
   * @return the check state
   */
  public boolean isChecked(M model);

  /**
   * Sets the current checked selection.
   * 
   * @param selection the checked selection
   */
  public void setCheckedSelection(List<M> selection);

}
