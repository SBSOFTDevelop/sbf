/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.cell.core.client.form.FieldCell.FieldAppearanceOptions;
import ru.sbsoft.svc.cell.core.client.form.TwinTriggerFieldCell.TwinTriggerFieldAppearance;
import ru.sbsoft.svc.core.client.SVC;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.themebuilder.base.client.config.FieldDetails;

/**
 *
 */
public class Css3TwinTriggerFieldAppearance extends Css3TriggerFieldAppearance implements TwinTriggerFieldAppearance {
  public interface Css3TwinTriggerFieldResources extends Css3TriggerFieldResources {
    @Override
    @Source({"Css3ValueBaseField.gss", "Css3TextField.gss", "Css3TriggerField.gss", "Css3TwinTriggerField.gss"})
    Css3TwinTriggerFieldStyle style();
  }

  public interface Css3TwinTriggerFieldStyle extends Css3TriggerFieldStyle {
    String triggerClick();

    String triggerOver();

    String triggerWrap();

    String twinTrigger();

    String twinTriggerClick();

    String twinTriggerOver();
  }

  private final Css3TwinTriggerFieldStyle style;

  public Css3TwinTriggerFieldAppearance() {
    this(GWT.<Css3TwinTriggerFieldResources>create(Css3TwinTriggerFieldResources.class));
  }

  public Css3TwinTriggerFieldAppearance(Css3TwinTriggerFieldResources resources) {
    super(resources);
    this.style = resources.style();
  }

  @Override
  public void onTriggerClick(XElement parent, boolean click) {
    parent.setClassName(style.triggerClick(), click);
  }

  @Override
  public void onTriggerOver(XElement parent, boolean over) {
    parent.setClassName(style.triggerOver(), over);
  }

  @Override
  public void onTwinTriggerClick(XElement parent, boolean click) {
    parent.setClassName(style.twinTriggerClick(), click);
  }

  @Override
  public void onTwinTriggerOver(XElement parent, boolean over) {
    parent.setClassName(style.twinTriggerOver(), over);
  }

  @Override
  public boolean twinTriggerIsOrHasChild(XElement parent, Element target) {
    return parent.isOrHasChild(target) && target.<XElement>cast().is("." + style.twinTrigger());
  }

  @Override
  public void render(SafeHtmlBuilder sb, String value, FieldAppearanceOptions options) {
    int width = options.getWidth();
    boolean hideTrigger = options.isHideTrigger();

    if (width == -1) {
      width = 150;
    }

    SafeStylesBuilder inputStylesBuilder = new SafeStylesBuilder();
    inputStylesBuilder.appendTrustedString("width:100%;");

    sb.appendHtmlConstant("<div style='width:" + width + "px;'>");


    if (hideTrigger) {
      sb.appendHtmlConstant("<div class='" + style.wrap() + "'>");
      renderInput(sb, value, inputStylesBuilder.toSafeStyles(), options);
    } else {
      FieldDetails fieldDetails = getResources().theme().field();

      int rightPadding = fieldDetails.padding().right();
      sb.appendHtmlConstant("<div class='" + style.wrap() + "' style='padding-right:" + (getResources().triggerArrow().getWidth() + rightPadding) + "px;'>");
      renderInput(sb, value, inputStylesBuilder.toSafeStyles(), options);

      if (!SVC.isTouch()) {
        int triggerWrapTopMargin = -(getTriggerWrapHeight() / 2);
        sb.appendHtmlConstant("<div class='" + getStyle().triggerWrap() + "' style='margin-top:" + triggerWrapTopMargin + "px;'>");
        sb.appendHtmlConstant("<div class='" + getStyle().trigger() + "'></div>");
        sb.appendHtmlConstant("<div class='" + getStyle().twinTrigger() + "'></div>");
        sb.appendHtmlConstant("</div>");
      } else {
        // for touch devices, we want the trigger's to cover the whole vertical area - each one should take up 50%.
        int triggerHeight = fieldDetails.height() / 2;
        // override top here, otherwise triggers will be pushed off the bottom of the input
        sb.appendHtmlConstant("<div class='" + getStyle().triggerWrap() + "' style='top:0%;'>");
        sb.appendHtmlConstant("<div class='" + getStyle().trigger() + "' style='height:" + triggerHeight + "px;'></div>");
        sb.appendHtmlConstant("<div class='" + getStyle().twinTrigger() + "' style='height:" + triggerHeight + "px;'></div>");
        sb.appendHtmlConstant("</div>");
      }
    }

    sb.appendHtmlConstant("</div></div>");
  }

  @Override
  protected Css3TwinTriggerFieldStyle getStyle() {
    return style;
  }

  protected int getTriggerWrapHeight() {
    return getResources().triggerArrow().getHeight();
  }
}
