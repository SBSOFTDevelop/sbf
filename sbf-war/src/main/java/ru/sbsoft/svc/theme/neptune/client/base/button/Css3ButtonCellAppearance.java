/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.button;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import ru.sbsoft.svc.cell.core.client.ButtonCell;
import ru.sbsoft.svc.cell.core.client.ButtonCell.ButtonArrowAlign;
import ru.sbsoft.svc.cell.core.client.ButtonCell.ButtonCellAppearance;
import ru.sbsoft.svc.cell.core.client.ButtonCell.ButtonScale;
import ru.sbsoft.svc.cell.core.client.ButtonCell.IconAlign;
import ru.sbsoft.svc.cell.core.client.SplitButtonCell;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.themebuilder.base.client.config.ButtonDetails;
import ru.sbsoft.svc.themebuilder.base.client.config.EdgeDetails;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;

public class Css3ButtonCellAppearance<M> implements ButtonCellAppearance<M> {

  public interface Css3ButtonResources extends ClientBundle {

    @ImageOptions(repeatStyle = RepeatStyle.None)
    ImageResource arrow();

    @ImageOptions(repeatStyle = RepeatStyle.None)
    ImageResource arrowBottom();

    @ImageOptions(repeatStyle = RepeatStyle.None)
    ImageResource split();

    @ImageOptions(repeatStyle = RepeatStyle.None)
    ImageResource splitBottom();

    @Source({"Css3ButtonCell.gss", "Css3ButtonCellToolBar.gss"})
    Css3ButtonStyle style();

    ThemeDetails theme();

    @ImageOptions(repeatStyle = RepeatStyle.None)
    @Source("toolBarArrow.png")
    ImageResource toolBarArrow();

    @ImageOptions(repeatStyle = RepeatStyle.None)
    @Source("toolBarArrowBottom.png")
    ImageResource toolBarArrowBottom();

    @ImageOptions(repeatStyle = RepeatStyle.None)
    @Source("toolBarButtonSplit.png")
    ImageResource toolBarSplit();

    @ImageOptions(repeatStyle = RepeatStyle.None)
    @Source("toolBarButtonSplitBottom.png")
    ImageResource toolBarSplitBottom();
  }

  public interface Css3ButtonStyle extends CssResource {
    String iconRight();

    String iconTop();

    String iconLeft();

    String iconBottom();

    String noText();

    String split();

    String button();

    String buttonInner();

    String over();

    String pressed();

    String focused();

    String small();

    String medium();

    String large();

    String arrow();

    String arrowBottom();

    String splitBottom();

  }

  public interface Css3ButtonTemplates extends XTemplates {
    @XTemplate(source = "Css3ButtonCell.html")
    SafeHtml render(String value, Css3ButtonStyle style);
  }

  private final Css3ButtonResources resources;
  private final Css3ButtonStyle style;

  public Css3ButtonCellAppearance() {
    this(GWT.<Css3ButtonResources>create(Css3ButtonResources.class));
  }

  public Css3ButtonCellAppearance(Css3ButtonResources resources) {
    this.resources = resources;
    style = resources.style();

    StyleInjectorHelper.ensureInjected(resources.style(), true);
  }

  @Override
  public XElement getButtonElement(XElement parent) {
    return parent.getFirstChildElement().cast();
  }

  @Override
  public XElement getFocusElement(XElement parent) {
    return parent.getFirstChildElement().cast();
  }

  @Override
  public void onFocus(XElement parent, boolean focused) {
    parent.getFirstChildElement().<XElement> cast().setClassName(style.focused(), focused);
  }

  @Override
  public void onOver(XElement parent, boolean over) {
    parent.getFirstChildElement().<XElement> cast().setClassName(style.over(), over);
  }

  @Override
  public void onPress(XElement parent, boolean pressed) {
    parent.getFirstChildElement().<XElement> cast().setClassName(style.pressed(), pressed);
  }

  @Override
  public void onToggle(XElement parent, boolean pressed) {
    parent.getFirstChildElement().<XElement> cast().setClassName(style.pressed(), pressed);
  }

  @Override
  public void render(ButtonCell<M> cell, Context context, M value, SafeHtmlBuilder sb) {
    String constantHtml = cell.getHTML();
    boolean hasConstantHtml = constantHtml != null && constantHtml.length() != 0;
    boolean isBoolean = value != null && value instanceof Boolean;
    // is a boolean always a toggle button?
    String text = hasConstantHtml ? cell.getText() : (value != null && !isBoolean)
        ? SafeHtmlUtils.htmlEscape(value.toString()) : "";

    ImageResource icon = cell.getIcon();
    IconAlign iconAlign = cell.getIconAlign();

    String arrowClass = "";
    String scaleClass = "";
    String iconClass = "";

    int width = cell.getWidth();
    int height = cell.getHeight();
    boolean hasIcon = cell.getIcon() != null;
    boolean isSplitButton = cell instanceof SplitButtonCell;
    boolean hasMenu = cell.getMenu() != null;

    boolean hasWidth = width != -1;

    if (hasMenu) {
      if (cell instanceof SplitButtonCell) {
        switch (cell.getArrowAlign()) {
          case RIGHT:
            arrowClass = style.split();
            break;
          case BOTTOM:
            arrowClass = style.splitBottom();
            break;
        }
      } else {
        if (applyMenuArrowClass()) {
          switch (cell.getArrowAlign()) {
            case RIGHT:
              arrowClass = style.arrow();
              break;
            case BOTTOM:
              arrowClass = style.arrowBottom();
              break;
          }
        }
      }
    }

    ButtonScale scale = cell.getScale();

    switch (scale) {
      case SMALL:
        scaleClass = style.small();
        break;

      case MEDIUM:
        scaleClass = style.medium();
        break;

      case LARGE:
        scaleClass = style.large();
        break;
    }

    if (icon != null) {
      switch (iconAlign) {
        case TOP:
          iconClass = style.iconTop();
          break;
        case BOTTOM:
          iconClass = style.iconBottom();
          break;
        case LEFT:
          iconClass = style.iconLeft();
          break;
        case RIGHT:
          iconClass = style.iconRight();
          break;
      }
    }

    String buttonClass = style.button();
    boolean hasText = text != null && !text.equals("");
    if (!hasText) {
      buttonClass += " " + style.noText();
    }

    // toggle button
    if (value == Boolean.TRUE) {
      buttonClass += " " + style.pressed();
    }

    String innerClass = style.buttonInner() + " " + iconClass;

    innerClass += " " + arrowClass;
    innerClass += " " + scaleClass;

    SafeHtmlBuilder builder = new SafeHtmlBuilder();

    SafeStylesBuilder ss = new SafeStylesBuilder();

    if (height != -1) {
      ButtonDetails bd = resources.theme().button();
      EdgeDetails padding = bd.padding();
      EdgeDetails paddingInner = bd.radiusMinusBorderWidth();
      int ah = height;
      ah -= padding.top();
      ah -= padding.bottom();
      ah -= paddingInner.top();
      ah -= paddingInner.bottom();

      ss.appendTrustedString("line-height: " + ah + "px;");
    }

    builder.appendHtmlConstant("<div class='" + buttonClass + "'>");

    // get iconbuilder ready
    SafeHtmlBuilder iconBuilder = new SafeHtmlBuilder();
    if (icon != null) {
      int iconWidth = icon != null ? icon.getWidth() : 0;
      int iconHeight = icon != null ? icon.getHeight() : 0;
      String styles = "width: " + iconWidth + "px; height: " + iconHeight + "px;";

      iconBuilder.appendHtmlConstant("<div class='" + iconClass + "' style='" + styles + "'>");
      iconBuilder.append(AbstractImagePrototype.create(icon).getSafeHtml());
      iconBuilder.appendHtmlConstant("</div>");
    }

    // for left / right aligned icons with a fixed button width we render the icon outside of the inner div
    if (hasWidth && hasIcon && iconAlign == IconAlign.LEFT) {
      builder.append(iconBuilder.toSafeHtml());
    }
    
    if (hasWidth && hasIcon && (iconAlign == IconAlign.LEFT)) {
      int tw = width - (resources.theme().button().borderRadius() * 2);
      if (isSplitButton && cell.getArrowAlign() == ButtonArrowAlign.RIGHT) {
        tw -= resources.split().getWidth() + 10;
      }

      if (!isSplitButton && hasMenu && cell.getArrowAlign() == ButtonArrowAlign.RIGHT && applyMenuArrowClass()) {
        tw -= resources.arrow().getWidth() + 10;
      }
      
      if (hasIcon) {
        tw -= icon.getWidth();
      } 
      
      ss.appendTrustedString("width: " + tw + "px;");
    }

    builder.appendHtmlConstant("<div class='" + innerClass + "' style='" + ss.toSafeStyles().asString() + "'>");

    if (icon != null) {
      if ((!hasWidth && iconAlign == IconAlign.LEFT) || iconAlign == IconAlign.TOP) {
        builder.append(iconBuilder.toSafeHtml());
      }
      builder.appendHtmlConstant(text);
      if (iconAlign == IconAlign.RIGHT || iconAlign == IconAlign.BOTTOM) {
        builder.append(iconBuilder.toSafeHtml());
      }
    } else {
      builder.appendHtmlConstant(text);
    }

    builder.appendHtmlConstant("</div></div>");
    sb.append(builder.toSafeHtml());
  }

  /**
   * Determines whether the Button Cell should show an arrow if the button contains a menu.
   * <p>
   *   Note: this does not apply to Split Buttons.
   * </p>
   * @return true if the arrow should show, false if not
   */
  protected boolean applyMenuArrowClass() {
    return true;
  }
}
