/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.core.client.gestures;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 * Groups gestures together, in order of which should have first crack an an incoming event. Does not support
 * {@link #start(java.util.List)} or {@link #getTouches()}.
 */
public class GestureChain implements GestureRecognizer {
  private final List<GestureRecognizer> gestures;

  public GestureChain(GestureRecognizer... gestures) {
    this(Arrays.asList(gestures));
  }

  public GestureChain(List<GestureRecognizer> gestures) {
    this.gestures = gestures;
  }

  @Override
  public void cancel() {
    for (int i = 0; i < gestures.size(); i++) {
      gestures.get(i).cancel();
    }
  }

  @Override
  public List<TouchData> getTouches() {
    throw new UnsupportedOperationException("Can't return specific touches from a chain of gestures");
  }

  @Override
  public boolean handle(NativeEvent event) {
    Iterator<GestureRecognizer> iter = gestures.iterator();
    while (iter.hasNext()) {
      if (!iter.next().handle(event)) {
        //that handler took the event, don't give it to a later handler, and return false overall
        return false;
      }
    }
    //no one asked that the event be stopped from propagating, so return true
    return true;
  }

  @Override
  public void start(List<TouchData> touches) {
    throw new UnsupportedOperationException("Can't start a chain of gestures");
  }

  @Override
  public void setDelegate(HasHandlers eventDelegate) {
    for (int i = 0; i < gestures.size(); i++) {
      gestures.get(i).setDelegate(eventDelegate);
    }
  }
}
