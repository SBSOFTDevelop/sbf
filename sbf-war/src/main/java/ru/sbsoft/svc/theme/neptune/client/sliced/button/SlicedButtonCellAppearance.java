/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.sliced.button;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import ru.sbsoft.svc.cell.core.client.ButtonCell;
import ru.sbsoft.svc.cell.core.client.ButtonCell.ButtonScale;
import ru.sbsoft.svc.cell.core.client.ButtonCell.IconAlign;
import ru.sbsoft.svc.cell.core.client.SplitButtonCell;
import ru.sbsoft.svc.core.client.resources.CommonStyles;
import ru.sbsoft.svc.core.client.util.TextMetrics;
import ru.sbsoft.svc.theme.base.client.button.ButtonCellDefaultAppearance;
import ru.sbsoft.svc.theme.base.client.frame.Frame;
import ru.sbsoft.svc.theme.base.client.frame.TableFrame;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;

public class SlicedButtonCellAppearance<C> extends ButtonCellDefaultAppearance<C> {

  public interface SlicedButtonCellResources extends ButtonCellResources {
    @Override
    @Source({"ru/sbsoft/svc/theme/base/client/button/ButtonCell.gss", "SlicedButtonCell.gss", "SlicedToolBarButtonCell.gss"})
    SlicedButtonCellStyle style();

    @ImageOptions(repeatStyle = RepeatStyle.None)
    @Source("ru/sbsoft/svc/theme/neptune/client/base/button/arrow.png")
    ImageResource arrow();

    @ImageOptions(repeatStyle = RepeatStyle.None)
    @Source("ru/sbsoft/svc/theme/neptune/client/base/button/arrowBottom.png")
    ImageResource arrowBottom();

    @ImageOptions(repeatStyle = RepeatStyle.None)
    @Source("ru/sbsoft/svc/theme/neptune/client/base/button/split.png")
    ImageResource split();

    @ImageOptions(repeatStyle = RepeatStyle.None)
    @Source("ru/sbsoft/svc/theme/neptune/client/base/button/splitBottom.png")
    ImageResource splitBottom();


    @ImageOptions(repeatStyle = RepeatStyle.None)
    @Source("ru/sbsoft/svc/theme/neptune/client/base/button/toolBarArrow.png")
    ImageResource toolBarArrow();

    @ImageOptions(repeatStyle = RepeatStyle.None)
    @Source("ru/sbsoft/svc/theme/neptune/client/base/button/toolBarArrowBottom.png")
    ImageResource toolBarArrowBottom();

    @ImageOptions(repeatStyle = RepeatStyle.None)
    @Source("ru/sbsoft/svc/theme/neptune/client/base/button/toolBarButtonSplit.png")
    ImageResource toolBarSplit();

    @ImageOptions(repeatStyle = RepeatStyle.None)
    @Source("ru/sbsoft/svc/theme/neptune/client/base/button/toolBarButtonSplitBottom.png")
    ImageResource toolBarSplitBottom();


    ThemeDetails theme();
  }

  public interface SlicedButtonCellStyle extends ButtonCellStyle {

  }

  public SlicedButtonCellAppearance() {
    this(GWT.<SlicedButtonCellResources>create(SlicedButtonCellResources.class));
  }

  public SlicedButtonCellAppearance(SlicedButtonCellResources resources) {
    this(resources, GWT.<ButtonCellTemplates>create(ButtonCellTemplates.class));
  }

  public SlicedButtonCellAppearance(SlicedButtonCellResources resources, ButtonCellTemplates templates) {
    this(resources, templates, new TableFrame(
        GWT.<SlicedButtonCellTableFrameResources>create(SlicedButtonCellTableFrameResources.class)));
  }

  public SlicedButtonCellAppearance(SlicedButtonCellResources resources, ButtonCellTemplates templates, Frame frame) {
    super(resources, templates, frame);
  }

  @Override
  public void render(final ButtonCell<C> cell, Context context, C value, SafeHtmlBuilder sb) {
    String constantHtml = cell.getHTML();
    boolean hasConstantHtml = constantHtml != null && constantHtml.length() != 0;
    boolean isBoolean = value != null && value instanceof Boolean;
    // is a boolean always a toggle button?
    SafeHtml valueHtml = SafeHtmlUtils.fromTrustedString(hasConstantHtml ? cell.getText() : (value != null && !isBoolean)
        ? SafeHtmlUtils.htmlEscape(value.toString()) : "");

    ImageResource icon = cell.getIcon();
    IconAlign iconAlign = cell.getIconAlign();

    String cls = style.button();
    String arrowCls = "";
    if (cell.getMenu() != null) {

      if (cell instanceof SplitButtonCell) {
        switch (cell.getArrowAlign()) {
          case RIGHT:
            arrowCls = style.split();
            break;
          case BOTTOM:
            arrowCls = style.splitBottom();
            break;
          default:
            // empty
        }

      } else {
        switch (cell.getArrowAlign()) {
          case RIGHT:
            arrowCls = style.arrow();
            break;
          case BOTTOM:
            arrowCls = style.arrowBottom();
            break;
        }
      }

    }

    ButtonScale scale = cell.getScale();

    switch (scale) {
      case SMALL:
        cls += " " + style.small();
        break;
      case MEDIUM:
        cls += " " + style.medium();
        break;
      case LARGE:
        cls += " " + style.large();
        break;
      default:
        // empty
    }

    SafeStylesBuilder stylesBuilder = new SafeStylesBuilder();

    int width = -1;

    if (cell.getWidth() != -1) {
      int w = cell.getWidth();
      if (w < cell.getMinWidth()) {
        w = cell.getMinWidth();
      }
      stylesBuilder.appendTrustedString("width:" + w + "px;");
      cls += " " + style.hasWidth() + " x-has-width";
      width = w;
    } else {

      if (cell.getMinWidth() != -1) {
        TextMetrics.get().bind(style.text());
        int length = TextMetrics.get().getWidth(valueHtml);
        length += 6; // frames

        if (icon != null) {
          switch (iconAlign) {
            case LEFT:
            case RIGHT:
              length += icon.getWidth();
              break;
            default:
              // empty
          }
        }
      }
    }

    final int height = cell.getHeight();
    if (height != -1) {
      stylesBuilder.appendTrustedString("height:" + height + "px;");
    }

    if (icon != null) {
      switch (iconAlign) {
        case TOP:
          arrowCls += " " + style.iconTop();
          break;
        case BOTTOM:
          arrowCls += " " + style.iconBottom();
          break;
        case LEFT:
          arrowCls += " " + style.iconLeft();
          break;
        case RIGHT:
          arrowCls += " " + style.iconRight();
          break;
      }

    } else {
      arrowCls += " " + style.noIcon();
    }

    // toggle button
    if (value == Boolean.TRUE) {
      cls += " " + frame.pressedClass();
    }

    sb.append(templates.outer(cls, new SafeStylesBuilder().toSafeStyles()));

    SafeHtmlBuilder inside = new SafeHtmlBuilder();

    String innerWrap = arrowCls;

    inside.appendHtmlConstant("<div class='" + innerWrap + "'>");
    inside.appendHtmlConstant("<table cellpadding=0 cellspacing=0 class='" + style.mainTable() + "'>");

    boolean hasText = valueHtml != null && !valueHtml.equals("");

    if (icon != null) {
      switch (iconAlign) {
        case LEFT:
          inside.appendHtmlConstant("<tr>");
          writeIcon(inside, icon, height);
          if (hasText) {
            int w = width - (icon != null ? icon.getWidth() : 0) - 4;
            writeValue(inside, valueHtml, w, height);
          }
          inside.appendHtmlConstant("</tr>");
          break;
        case RIGHT:
          inside.appendHtmlConstant("<tr>");
          if (hasText) {
            int w = width - (icon != null ? icon.getWidth() : 0) - 4;
            writeValue(inside, valueHtml, w, height);
          }
          writeIcon(inside, icon, height);
          inside.appendHtmlConstant("</tr>");
          break;
        case TOP:
          inside.appendHtmlConstant("<tr>");
          writeIcon(inside, icon, height);
          inside.appendHtmlConstant("</tr>");
          if (hasText) {
            inside.appendHtmlConstant("<tr>");
            writeValue(inside, valueHtml, width, height);
            inside.appendHtmlConstant("</tr>");
          }
          break;
        case BOTTOM:
          if (hasText) {
            inside.appendHtmlConstant("<tr>");
            writeValue(inside, valueHtml, width, height);
            inside.appendHtmlConstant("</tr>");
          }
          inside.appendHtmlConstant("<tr>");
          writeIcon(inside, icon, height);
          inside.appendHtmlConstant("</tr>");
          break;
      }

    } else {
      inside.appendHtmlConstant("<tr>");
      if (valueHtml != null) {
        writeValue(inside, valueHtml, width, height);
      }
      inside.appendHtmlConstant("</tr>");
    }
    inside.appendHtmlConstant("</table>");
    inside.appendHtmlConstant("</div>");

    frame.render(sb, new Frame.FrameOptions(0, CommonStyles.get().noFocusOutline(), stylesBuilder.toSafeStyles()),
        inside.toSafeHtml());

    sb.appendHtmlConstant("</div>");

  }
}
