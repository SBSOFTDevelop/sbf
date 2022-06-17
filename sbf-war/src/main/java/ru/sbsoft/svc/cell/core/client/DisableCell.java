/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.cell.core.client;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.dom.client.Element;
import ru.sbsoft.svc.widget.core.client.cell.CellComponent;

/**
 * Interface for cells that can be "disabled".
 * <p/>
 * When used with {@link CellComponent} the
 * {@link #disable(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element)} and
 * {@link #enable(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element)} will be called when the
 * component is enabled and disabled.
 */
public interface DisableCell {

  void disable(Context context, Element parent);

  void enable(Context context, Element parent);

}
