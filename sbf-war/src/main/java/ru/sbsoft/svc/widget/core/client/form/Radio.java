/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import ru.sbsoft.svc.cell.core.client.form.RadioCell;
import ru.sbsoft.svc.core.client.util.ToggleGroup;

/**
 * Single radio field.
 * <p/>
 * {@link ValueChangeEvent}s are fired when the checkbox state is changed by the user, instead of waiting for a
 * {@link BlurEvent}.
 * <p/>
 * Group radios together using the {@link ToggleGroup}.
 */
public class Radio extends CheckBox {

  /**
   * Creates a new radio field.
   */
  public Radio() {
    this(new RadioCell());
  }

  /**
   * Creates a new radio field.
   *
   * @param cell the radio cell
   */
  public Radio(RadioCell cell) {
    super(cell);
  }

  /**
   * Sets the group name of the radios.
   * <ul>
   * <li>When grouping radios, also use {@link ToggleGroup} to group them.</li>
   * <li>Setting the name is not required in a {@link ToggleGroup}</li>
   * </ul>
   *
   * @param name is the group name of the radios.
   */
  @Override
  public void setName(String name) {
    super.setName(name);
  }

}
