/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.fx.client.animation;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Timer;
import ru.sbsoft.svc.core.client.dom.XElement;

public class Blink extends BaseEffect {

  private int interval;
  private Timer t;
  private boolean visible;

  public Blink(final XElement el, int interval) {
    super(el);
    this.interval = interval;
    t = new Timer() {
      @Override
      public void run() {
        el.setVisibility(visible);
        visible = !visible;
      }
    };
  }

  public int getInterval() {
    return interval;
  }

  @Override
  public void onCancel() {
    super.onCancel();
    t.cancel();
  }

  @Override
  public void onComplete() {
    t.cancel();
    // ensure timer is done executing
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
      @Override
      public void execute() {
        element.setVisibility(true);
      }
    });
  }

  @Override
  public void onStart() {
    t.scheduleRepeating(interval);
  }

  @Override
  public void onUpdate(double progress) {

  }

  public void setInterval(int interval) {
    this.interval = interval;
  }

}
