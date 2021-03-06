/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.themebuilder.base.client;

/**
 * This class is for operations that can be done in the css file and don't belong in the theme config. All methods
 * here should be able to compile to static content.
 */
public class CssResourceThemeUtils {
  /**
   * Takes an opacity value 0.0-1.0 and converts it to a ie alpha filter function string. Given a constant value,
   * this should compile out to a constant string.
   */
  public static String opacityToIe8Filter(double opacity) {
    //-ms-filter:"progid:DXImageTransform.Microsoft.Alpha(Opacity=50)"
    return "alpha(opacity=" + ((int) (opacity * 100)) + ")";
  }
  /**
   * Takes two int values and returns the max of the two, with 'px' appended to the end. Given two constant values,
   * this should compile out to a constant string in the css.
   */
  public static String maxPxSize(int a, int b) {
    return Math.max(a, b) + "px";
  }

  /**
   * Calculates the padding size needed to make sliced/css3 look the same based on borderRadius and border size
   */
  public static String framePaddingCalc(int radius, int border) {
    return (Math.max(radius, border) - border) + "px";
  }

}
