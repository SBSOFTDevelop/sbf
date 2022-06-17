/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.core.client.gestures;

import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.core.client.util.Point;

public class PinchAndRotateGestureRecognizer extends AbstractTwoTouchGestureRecognizer {
  private double initialDistance;
  private double initialAngle;
  private Point initialAvgPosition;


  @Override
  protected void bothTouchStart(TouchData first, TouchData second) {
    initialDistance = Math.sqrt(Math.pow(first.getStartPosition().getX() - second.getStartPosition().getX(), 2) +
                                Math.pow(first.getStartPosition().getY() - second.getStartPosition().getY(), 2));

    initialAngle = Math.atan2(first.getStartPosition().getY() - second.getStartPosition().getY(),
                              first.getStartPosition().getX() - second.getStartPosition().getX());

    initialAvgPosition = new Point((first.getStartPosition().getX() + second.getStartPosition().getX()) / 2,
            (first.getStartPosition().getY() + second.getStartPosition().getY()) / 2);
  }

  @Override
  protected void bothTouchUpdate(TouchData first, TouchData second) {
    // measure change, fire update event(s?)
    double newDistance = Math.sqrt(Math.pow(first.getLastPosition().getX() - second.getLastPosition().getX(), 2) +
            Math.pow(first.getLastPosition().getY() - second.getLastPosition().getY(), 2));
    double newAngle = Math.atan2(first.getLastPosition().getY() - second.getLastPosition().getY(),
            first.getLastPosition().getX() - second.getLastPosition().getX());

    Point newAvgPosition = new Point((first.getLastPosition().getX() + second.getLastPosition().getX()) / 2,
            (first.getLastPosition().getY() + second.getLastPosition().getY()) / 2);


    fireEvent(new RotateGestureMoveEvent(this,
            newAngle, initialAngle,
            newAvgPosition, initialAvgPosition,
            newDistance, initialDistance));
  }


  @Override
  protected void fireEndEvent(List<TouchData> touches) {
    //TODO fire end event, finished
  }

  @Override
  protected void fireCancelEvent(List<TouchData> touches) {
    //TODO fire cancel event, stopped early
  }


  //TODO just one event for both rotate/scale? also include position? or three events?
  public static class RotateGestureMoveEvent extends AbstractGestureEvent<RotateGestureMoveEvent.RotateGestureMoveHandler> {
    private static final Type<RotateGestureMoveHandler> TYPE = new Type<RotateGestureMoveHandler>();
    private final PinchAndRotateGestureRecognizer gesture;
    private final double newAngle;
    private final double initialAngle;
    private final Point newAvgPosition;
    private final Point initialAvgPosition;
    private final double newDistance;
    private final double initialDistance;

    public RotateGestureMoveEvent(PinchAndRotateGestureRecognizer gesture, double newAngle, double initialAngle, Point newAvgPosition, Point initialAvgPosition, double newDistance, double initialDistance) {
      super(gesture);
      this.gesture = gesture;
      this.newAngle = newAngle;
      this.initialAngle = initialAngle;
      this.newAvgPosition = newAvgPosition;
      this.initialAvgPosition = initialAvgPosition;
      this.newDistance = newDistance;
      this.initialDistance = initialDistance;
    }

    public PinchAndRotateGestureRecognizer getGesture() {
      return gesture;
    }

    public double getNewAngle() {
      return newAngle;
    }

    public double getInitialAngle() {
      return initialAngle;
    }

    public Point getNewAvgPosition() {
      return newAvgPosition;
    }

    public Point getInitialAvgPosition() {
      return initialAvgPosition;
    }

    public double getNewDistance() {
      return newDistance;
    }

    public double getInitialDistance() {
      return initialDistance;
    }

    public static Type<RotateGestureMoveHandler> getType() {
      return TYPE;
    }

    @Override
    public Type<RotateGestureMoveHandler> getAssociatedType() {
      return TYPE;
    }

    @Override
    public void dispatch(RotateGestureMoveHandler handler) {
      handler.onRotateGesture(this);
    }

    public interface RotateGestureMoveHandler extends EventHandler {
      void onRotateGesture(RotateGestureMoveEvent event);
    }

    public interface HasRotateGestureMoveHandlers {
      HandlerRegistration addRotateGestureHandler(RotateGestureMoveHandler handler);
    }
  }
}
