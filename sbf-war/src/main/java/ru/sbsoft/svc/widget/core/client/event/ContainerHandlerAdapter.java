/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;


/**
 * Abstract adapter class for <code>ContainerHandler</code>. The methods in the
 * class are empty.
 */
public abstract class ContainerHandlerAdapter implements ContainerHandler {

  @Override
  public void onAdd(AddEvent event) {
  }

  @Override
  public void onBeforeAdd(BeforeAddEvent event) {
  }

  @Override
  public void onBeforeRemove(BeforeRemoveEvent event) {
  }

  @Override
  public void onRemove(RemoveEvent event) {
  }

}
