/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.grid;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;
import ru.sbsoft.svc.widget.core.client.grid.editing.GridRowEditing.RowEditorAppearance;

public class Css3RowEditorAppearance implements RowEditorAppearance {

  public interface Css3RowEditorResources extends ClientBundle {
    @Source("Css3RowEditor.gss")
    Css3RowEditorStyle style();
    
    ThemeDetails theme();
  }

  public interface Css3RowEditorStyle extends CssResource {
    String editor();

    String editorInner();

    String buttons();

    String buttonsInner();

    String label();
  }

  public interface Template extends XTemplates {
    @XTemplate(source = "Css3RowEditor.html")
    SafeHtml render(Css3RowEditorStyle style);
  }

  private final Css3RowEditorResources resources;
  private final Css3RowEditorStyle style;
  private final Template template;

  public Css3RowEditorAppearance() {
    this(GWT.<Css3RowEditorResources> create(Css3RowEditorResources.class));
  }

  public Css3RowEditorAppearance(Css3RowEditorResources resources) {
    this.resources = resources;
    this.style = this.resources.style();

    this.template = GWT.create(Template.class);

    StyleInjectorHelper.ensureInjected(style, false);
  }

  @Override
  public XElement getButtonWrap(XElement parent) {
    return parent.selectNode("." + style.buttonsInner());
  }

  @Override
  public XElement getContentWrap(XElement parent) {
    return parent.selectNode("." + style.editorInner());
  }

  @Override
  public String labelClass() {
    return style.label();
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(template.render(style));
  }

  @Override
  public void onResize(final XElement parent, final int width, final int height) {
    // button offset width not correct unless we run deferred and allow buttons to 
    // be rendered
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
      
      @Override
      public void execute() {
        Element buttons = getButtonWrap(parent).getParentElement();
        int buttonsWidth = buttons.getOffsetWidth();
        System.out.println("bw: " + buttonsWidth);
        int left = (width - buttonsWidth) / 2;
        int top = parent.getOffsetHeight() - 2;

        buttons.getStyle().setTop(top, Unit.PX);
        buttons.getStyle().setLeft(left, Unit.PX);
      }
    });

  }

}
