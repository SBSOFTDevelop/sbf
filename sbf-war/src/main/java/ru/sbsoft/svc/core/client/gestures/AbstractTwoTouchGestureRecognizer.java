/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.core.client.gestures;

import java.util.List;

public abstract class AbstractTwoTouchGestureRecognizer extends AbstractGestureRecognizer {
  protected TouchData first, second;

  public AbstractTwoTouchGestureRecognizer() {
    this(ExtraTouchBehavior.IGNORE);
  }

  public AbstractTwoTouchGestureRecognizer(ExtraTouchBehavior extraTouchBehavior) {
    super(extraTouchBehavior, 2);
  }

  @Override
  protected boolean onStart(TouchData startedTouch) {
    setInterest(startedTouch, true);
    if (first == null) {
      first = startedTouch;
    } else {
      second = startedTouch;
      setCaptured(first, true);
      setCaptured(second, true);
      bothTouchStart(first, second);
    }
    return false;
  }


  @Override
  protected void onMove(List<TouchData> touches) {
    assert second != null : "onMove should not be called if either touch is null, bug in onStart/onEnd/onCancel";
    bothTouchUpdate(first, second);
  }


  @Override
  protected void onEnd(List<TouchData> touches) {
    boolean fireEndEvent = endOneOrBothTouches(touches);

    if (fireEndEvent) {
      fireEndEvent(touches);
    }
  }

  private boolean endOneOrBothTouches(List<TouchData> touches) {
    boolean fireEvent = false;
    if (second != null) {
      fireEvent = true;//TODO consider minimum move amount?

      if (touches.contains(second)) {
        setCaptured(first, false);//must release first, cannot be null since second is not null
//      setCaptured(second, false);//not necessary, will happen automatically

        second = null;
      }
    }

    if (first != null && touches.contains(first)) {
//    setCaptured(first, false);//not necessary, will happen automatically
      if (second != null) {//don't want to release a null
        setCaptured(second, false);//must release second, since it is useless to us now
      }

      first = second;
      second = null;
    }
    return fireEvent;
  }
  protected abstract void bothTouchStart(TouchData first, TouchData second);
  protected abstract void bothTouchUpdate(TouchData first, TouchData second);

  protected abstract void fireEndEvent(List<TouchData> touches);
  protected abstract void fireCancelEvent(List<TouchData> touches);

  @Override
  protected void onCancel(List<TouchData> touches) {
    boolean fireCancelEvent = endOneOrBothTouches(touches);

    if (fireCancelEvent) {
      fireCancelEvent(touches);
    }
  }
}
