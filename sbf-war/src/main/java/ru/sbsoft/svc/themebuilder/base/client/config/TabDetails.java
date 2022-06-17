/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.themebuilder.base.client.config;

/**
 *
 */
public interface TabDetails {
  @TypeDetails(sampleValue = "3", comment = "border radius of tabs")
  int borderRadius();

  @TypeDetails(sampleValue = "#ffffff", comment = "background color of tab body")
  String bodyBackgroundColor();

  @TypeDetails(sampleValue = "#037ecf", comment = "border color or tab panel")
  String borderColor();

  @TypeDetails(sampleValue = "#add2ed", comment = "active tab background color")
  String activeTabItemBackgroundColor();

  @TypeDetails(sampleValue = "#4b9cd7", comment = "inactive tab background")
  String inactiveTabItemBackgroundColor();

  @TypeDetails(sampleValue = "#5fa7db", comment = "hover tab background color")
  String hoverTabItemBackgroundColor();

  @TypeDetails(sampleValue = "util.solidGradientString('#add2ed')", comment = "tab gradient string (top to bottom)")
  String gradient();

  @TypeDetails(sampleValue = "util.solidGradientString('#4b9cd7')", comment = "inactive tab gradient string (top to bottom)")
  String inactiveGradient();

  @TypeDetails(sampleValue = "util.solidGradientString('#5fa7db')", comment = "hover tab gradient string (top to bottom)")
  String hoverGradient();

  @TypeDetails(sampleValue = "#add2ed", comment = "last color in gradient")
  String lastStopColor();

  @TypeDetails(sampleValue = "#4b9cd7", comment = "last color in inactive gradient")
  String inactiveLastStopColor();

  @TypeDetails(sampleValue = "util.fontStyle('sans-serif','medium','#000000','normal')", comment = "tab heading text style")
  FontDetails headingText();

  @TypeDetails(sampleValue = "util.fontStyle('sans-serif','medium','#000000','normal')", comment = "active tab heading text style")
  FontDetails activeHeadingText();

  @TypeDetails(sampleValue = "util.fontStyle('sans-serif','medium','#000000','normal')", comment = "hovered tab heading text style")
  FontDetails hoverHeadingText();

  @TypeDetails(sampleValue = "util.fontStyle('sans-serif','medium','#000000','normal')", comment = "plain tab heading text style")
  FontDetails plainHeadingText();

  @TypeDetails(sampleValue = "util.fontStyle('sans-serif','medium','#000000','normal')", comment = "active plain tab heading text style")
  FontDetails plainActiveHeadingText();

  @TypeDetails(sampleValue = "util.fontStyle('sans-serif','medium','#000000','normal')", comment = "hovered plain tab heading text style")
  FontDetails plainHoverHeadingText();

  @TypeDetails(sampleValue = "1", comment = "top border for plain tab bar")
  String plainTabBarBorderTop();

  @TypeDetails(sampleValue = "1", comment =  "bottom border for plain tab bar")
  String plainTabBarBorderBottom();

  @TypeDetails(sampleValue = "2", comment = "space between tab and plain tab body")
  String plainTabStripSpacerHeight();

  @TypeDetails(sampleValue = "1", comment = "border size of tab bar strip spacer")
  String plainTabStripSpacerBorder();

  @TypeDetails(sampleValue = "util.solidGradientString('#037ecf')", comment = "tab strip background gradient string (top to bottom)")
  String tabStripGradient();

  @TypeDetails(sampleValue =  "'none'", comment = "tab item bottom border")
  String tabItemBorderBottom();

  @TypeDetails(sampleValue = "'none'", comment = "tab item left border")
  String tabItemBorderLeft();

  @TypeDetails(sampleValue = "'none'", comment = "tab item top border")
  String tabItemBorderTop();

  @TypeDetails(sampleValue = "'none'", comment = "tab item right border")
  String tabItemBorderRight();

  @TypeDetails(sampleValue = "1px", comment = "active tab item bottom border")
  String activeTabItemBorderBottom();

  @TypeDetails(sampleValue = "1px", comment = "active tab item border top")
  String activeTabItemBorderTop();

  @Optional(defaultValue = "0px", comment = "active tab item border bottom (defaults to 0px)")
  String activeTabItemMarginBottom();

  @TypeDetails(sampleValue = "1px", comment = "over (hovered) tab item border bottom")
  String overTabItemBorderBottom();

  @TypeDetails(sampleValue = "1px", comment = "over (hovered) tab item border top")
  String overTabItemBorderTop();

  @TypeDetails(sampleValue = "'none'", comment = "tab bar border")
  String tabBarBorder();

  @TypeDetails(sampleValue = "util.padding(0)", comment = "padding")
  EdgeDetails padding();

  @TypeDetails(sampleValue = "util.padding(0)", comment = "padding")
  EdgeDetails paddingWithIcon();

  @TypeDetails(sampleValue = "6", comment = "left offset for tab icon")
  int iconLeftOffset();

  @TypeDetails(sampleValue = "5", comment = "top offset for tab icon")
  int iconTopOffset();

  @TypeDetails(sampleValue = "util.padding(0)", comment = "padding")
  EdgeDetails paddingWithClosable();

  @TypeDetails(sampleValue = "#037ecf", comment = "background color of the tab scrollers")
  String scrollerBackgroundColor();

  @TypeDetails(sampleValue = "0.7", comment = "left scroller opacity when hovered")
  double scrollerLeftOverOpacity();

  @TypeDetails(sampleValue = "0.5", comment = "left scroller opacity when disabled")
  double scrollerLeftDisabledOpacity();

  @TypeDetails(sampleValue = "0.7", comment = "right scroller opacity when hovered")
  double scrollerRightOverOpacity();

  @TypeDetails(sampleValue = "1", comment = "right scroller opacity when disabled")
  double scrollerRightDisabledOpacity();

  @TypeDetails(sampleValue = "18", comment = "width of scroller")
  int scrollerWidth();

  @TypeDetails(sampleValue = "31", comment = "height of tabs")
  int tabHeight();

  @TypeDetails(sampleValue = "1", comment = "spacing between tabs")
  int tabSpacing();

  @TypeDetails(sampleValue = "util.padding(0)", comment = "padding")
  EdgeDetails tabStripPadding();

  @TypeDetails(sampleValue = "4", comment = "Spacing between tab bar and body")
  int tabBarBottomHeight();

  @TypeDetails(sampleValue = "#ffffff", comment = "tab strip background color")
  String tabStripBackgroundColor();

  @TypeDetails(sampleValue = "'none'", comment = "tab strip bottom border")
  String tabStripBottomBorder();

  @TypeDetails(sampleValue = "'none'", comment = "tab strip top border")
  String tabStripTopBorder();

  @TypeDetails(sampleValue = "'none'", comment = "border of tab panel body")
  String tabBodyBorder();

  @TypeDetails(sampleValue = "util.padding(0)", comment = "padding")
  EdgeDetails tabTextPadding();

  @TypeDetails(sampleValue = "0.8", comment = "")
  double activeTabCloseIconOpacity();

  @TypeDetails(sampleValue = "1", comment = "")
  double activeTabCloseOverIconOpacity();

  @TypeDetails(sampleValue = "0.6", comment = "")
  double tabCloseIconOpacity();

  @TypeDetails(sampleValue = "1", comment = "")
  double tabCloseOverIconOpacity();
}
