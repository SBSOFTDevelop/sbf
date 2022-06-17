/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form.error;

public interface HasErrorHandler {
  /**
   * Returns the field's error support instance.
   * 
   * @return the error support
   */
  public ErrorHandler getErrorSupport();

  /**
   * Sets the field's error support instance.
   * 
   * <p />
   * See the following concrete implementations:
   * <ul>
   * <li>{@link SideErrorHandler}</li>
   * <li>{@link ToolTipErrorHandler}</li>
   * <li>{@link TitleErrorHandler}</li>
   * <li>{@link ElementErrorHandler}</li>
   * </ul>
   * 
   * @param error the error support
   */
  void setErrorSupport(ErrorHandler error);
}
