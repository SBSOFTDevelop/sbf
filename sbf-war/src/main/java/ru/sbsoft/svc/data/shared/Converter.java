/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.shared;

/**
 * Converts back and forth between model and field values
 * 
 * @param <N> the model value type
 * @param <O> the field value type
 */
public interface Converter<N, O> {

  /**
   * Converts the value in the field to a value that can be stored in the model
   * 
   * @param object the value displayed in the field/cell
   * @return a value to be placed in the model
   */
  N convertFieldValue(O object);

  /**
   * Converts the value from the model to something that can be displayed and
   * edited by the user
   * 
   * @param object the value in the model object
   * @return a value to be rendered or edited in a cell or field
   */
  O convertModelValue(N object);
}