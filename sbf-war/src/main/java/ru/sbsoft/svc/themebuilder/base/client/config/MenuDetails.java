/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.themebuilder.base.client.config;

public interface MenuDetails {

  @TypeDetails(sampleValue = "#ffffff", comment = "background color for the menu list")
  String backgroundColor();
  @TypeDetails(sampleValue = "util.border('solid', '#000000', 1)", comment = "border around the menu popup")
  BorderDetails border();
  @TypeDetails(sampleValue = "util.padding(0)", comment = "spacing between the border and the list of contents")
  EdgeDetails padding();
  @TypeDetails(sampleValue = "util.solidGradientString('#ffffff')", comment = "background gradient (left to right) for the menu list")
  String gradient();
  @TypeDetails(sampleValue = "#ffffff", comment = "helper for sliced browsers to have a background color after the sliced image runs out for wide menus")
  String lastGradientColor();

  @TypeDetails(sampleValue = "util.fontStyle('sans-serif','medium')", comment = "item text styling")
  FontDetails itemText();
  @TypeDetails(sampleValue = "'normal'", comment = "item line height")
  String itemLineHeight();
  @TypeDetails(sampleValue = "util.padding(1,8)", comment = "padding around each individual menu item")
  EdgeDetails itemPadding();

  @TypeDetails(sampleValue = "#cccccc", comment = "background color for active items for browsers that dont support gradients")
  String activeItemBackgroundColor();
  @TypeDetails(sampleValue = "util.border('solid', '#333333', 1)", comment = "border for active items")
  BorderDetails activeItemBorder();
  @TypeDetails(sampleValue = "util.solidGradientString('#cccccc')", comment = "gradient for active items (top to bottom)")
  String activeItemGradient();
  @TypeDetails(sampleValue = "util.fontStyle('sans-serif','medium')", comment = "item text styling")
  FontDetails activeItemText();

  @TypeDetails(sampleValue = "16", comment = "height of the bottom/top scroller items")
  int scrollerHeight();

  MenuBarDetails bar();

  public interface MenuBarDetails {

    @TypeDetails(sampleValue = "#ffffff", comment = "background color of the menu bar for browsers that dont support gradients")
    String backgroundColor();
    @TypeDetails(sampleValue = "util.border('none')", comment = "menu bar border")
    BorderDetails border();
    @TypeDetails(sampleValue = "util.solidGradientString('#ffffff')", comment = "background gradient of the menubar")
    String gradient();
    @TypeDetails(sampleValue = "util.padding(1, 8)", comment = "spacing between the border and the items")
    EdgeDetails padding();

    @TypeDetails(sampleValue = "util.fontStyle('sans-serif','medium')", comment = "item text styling")
    FontDetails itemText();
    @TypeDetails(sampleValue = "'normal'", comment = "height of each menubar item")
    String itemLineHeight();
    @TypeDetails(sampleValue = "util.padding(0)", comment = "menu bar item text padding")
    EdgeDetails itemPadding();

    @TypeDetails(sampleValue = "#ffffff", comment = "hovered menu bar item background color for browsers that dont support gradients")
    String hoverItemBackgroundColor();
    @TypeDetails(sampleValue = "util.border('none')", comment = "hovered menu bar item border")
    BorderDetails hoverItemBorder();
    @TypeDetails(sampleValue = "util.solidGradientString('#ffffff')", comment = "hovered menu bar item background gradient")
    String hoverItemGradient();
    @TypeDetails(sampleValue = "util.fontStyle('sans-serif','medium')", comment = "hovered item text styling")
    FontDetails hoverItemText();

    @TypeDetails(sampleValue = "#ffffff", comment = "active menu bar item background color for browsers that dont support gradients")
    String activeItemBackgroundColor();
    @TypeDetails(sampleValue = "util.border('none')", comment = "active menu bar item horder")
    BorderDetails activeItemBorder();
    @TypeDetails(sampleValue = "util.solidGradientString('#ffffff')", comment = "active menu bar item background gradient")
    String activeItemGradient();
    @TypeDetails(sampleValue = "util.fontStyle('sans-serif','medium')", comment = "active item text styling")
    FontDetails activeItemText();
  }

  MenuSeparatorDetails separator();

  public interface MenuSeparatorDetails {
    @TypeDetails(sampleValue = "1", comment = "separator height in px")
    int height();
    @TypeDetails(sampleValue = "util.margin(2,3)", comment = "separator margins")
    EdgeDetails margin();
    @TypeDetails(sampleValue = "#cccccc", comment = "separator line color")
    String color();
  }

  HeaderItemDetails header();

  public interface HeaderItemDetails {
    @TypeDetails(sampleValue = "util.border('solid', '#333333', 0, 0, 1)", comment = "border around the header text")
    BorderDetails border();
    @TypeDetails(sampleValue = "#dddddd", comment = "background color for header text")
    String backgroundColor();

    @TypeDetails(sampleValue = "util.fontStyle('sans-serif','medium')", comment = "header text styling")
    FontDetails itemText();
    @TypeDetails(sampleValue = "'normal'", comment = "")
    String itemLineHeight();
    @TypeDetails(sampleValue = "util.padding(3)", comment = "padding between header text and border")
    EdgeDetails itemPadding();
  }

}
