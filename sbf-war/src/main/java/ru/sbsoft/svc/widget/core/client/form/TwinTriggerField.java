/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form;

import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.cell.core.client.form.TwinTriggerFieldCell;
import ru.sbsoft.svc.widget.core.client.event.TwinTriggerClickEvent;
import ru.sbsoft.svc.widget.core.client.event.TwinTriggerClickEvent.HasTwinTriggerClickHandlers;
import ru.sbsoft.svc.widget.core.client.event.TwinTriggerClickEvent.TwinTriggerClickHandler;

/**
 * An abstract base class for an input field and two clickable triggers. The
 * purpose of the triggers is defined by the derived class (e.g. modifying the
 * value of the input field).
 * 
 * @param <T> the field type
 */
public abstract class TwinTriggerField<T> extends TriggerField<T> implements HasTwinTriggerClickHandlers {

  /**
   * Creates a trigger field with the specified cell and property editor.
   * 
   * @param cell renders the trigger field
   * @param propertyEditor performs string / value conversions and other
   *          operations
   */
  public TwinTriggerField(TwinTriggerFieldCell<T> cell, PropertyEditor<T> propertyEditor) {
    super(cell);
    setPropertyEditor(propertyEditor);
  }

  @Override
  public HandlerRegistration addTwinTriggerClickHandler(TwinTriggerClickHandler handler) {
    return addHandler(handler, TwinTriggerClickEvent.getType());
  }

}
