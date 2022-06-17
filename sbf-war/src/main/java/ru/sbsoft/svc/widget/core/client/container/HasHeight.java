/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.container;

/**
 * A class that implements this interface has a height specification. This
 * interface provides access to the height specification without compromising
 * the ability to provide a mock container instance in JRE unit tests.
 * 
 * @see HorizontalLayoutContainer
 * @see VerticalLayoutContainer
 */
public interface HasHeight {
  /**
   * Returns the height specification. Values greater than 1 represent height in
   * pixels. Values between 0 and 1 (inclusive) represent a percent of the
   * height of the container. A value of -1 represents the default height of the
   * associated widget. Values less than -1 represent the height of the
   * container minus the absolute value of the widget height.
   * 
   * @return the height specification
   */
  double getHeight();

  /**
   * Sets the height specification. Values greater than 1 represent height in
   * pixels. Values between 0 and 1 (inclusive) represent a percent of the
   * height of the container. A value of -1 represents the default height of the
   * associated widget. Values less than -1 represent the height of the
   * container minus the absolute value of the widget height.
   * 
   * @param height the height specification
   */
  void setHeight(double height);
}
