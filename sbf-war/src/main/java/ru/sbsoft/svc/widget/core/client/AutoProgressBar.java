/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client;

import ru.sbsoft.svc.cell.core.client.AutoProgressBarCell;

/**
 * An auto mode progress bar widget.
 * 
 * <p />
 * You simply call {@link #auto} and let the progress bar run indefinitely, only
 * clearing it once the operation is complete. You can optionally have the
 * progress bar wait for a specific amount of time and then clear itself.
 * Automatic mode is most appropriate for timed operations or asynchronous
 * operations in which you have no need for indicating intermediate progress.
 */
public class AutoProgressBar extends ProgressBar {

  /**
   * Creates a new progress bar with the default automatic progress bar cell.
   */
  public AutoProgressBar() {
    this(new AutoProgressBarCell());
  }

  /**
   * Creates a new progress bar with the specified automatic progress bar cell.
   * 
   * @param cell the automatic progress bar cell
   */
  public AutoProgressBar(AutoProgressBarCell cell) {
    super(cell);
  }

  /**
   * Initiates an auto-updating progress bar using the current duration,
   * increment, and interval.
   */
  public void auto() {
    getCell().auto(createContext(), getElement());
  }

  @Override
  public AutoProgressBarCell getCell() {
    return (AutoProgressBarCell) super.getCell();
  }

  /**
   * Returns the duration.
   * 
   * @return the duration
   */
  public int getDuration() {
    return getCell().getDuration();
  }

  /**
   * Returns the bar's interval value.
   * 
   * @return the interval in milliseconds
   */
  public int getInterval() {
    return getCell().getInterval();
  }

  /**
   * Returns true if the progress bar is currently in a {@link #auto} operation.
   * 
   * @return true if waiting, else false
   */
  public boolean isRunning() {
    return getCell().isRunning();
  }

  /**
   * The length of time in milliseconds that the progress bar should run before
   * resetting itself (defaults to DEFAULT, in which case it will run
   * indefinitely until reset is called).
   * 
   * @param duration the duration in milliseconds
   */
  public void setDuration(int duration) {
    getCell().setDuration(duration);
  }

  /**
   * Sets the length of time in milliseconds between each progress update
   * (defaults to 300 ms).
   * 
   * @param interval the interval to set
   */
  public void setInterval(int interval) {
    getCell().setInterval(interval);
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    if (isRunning()) {
      reset();
    }
  }

}
