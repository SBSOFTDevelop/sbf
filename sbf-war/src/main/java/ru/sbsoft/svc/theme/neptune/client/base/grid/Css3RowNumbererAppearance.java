/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.grid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.Import;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;
import ru.sbsoft.svc.widget.core.client.grid.GridView.GridStateStyles;
import ru.sbsoft.svc.widget.core.client.grid.RowNumberer.RowNumbererAppearance;

public class Css3RowNumbererAppearance implements RowNumbererAppearance {

  public interface RowNumbererResources extends ClientBundle {
    @Import(GridStateStyles.class)
    @Source("Css3RowNumberer.gss")
    RowNumbererStyles styles();

    //to be placed in sliced impl after sliced job is created
//    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
//    ImageResource specialColumn();
//
//    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
//    ImageResource specialColumnSelected();

    ThemeDetails theme();
  }
  public interface RowNumbererStyles extends CssResource{
    String numberer();
    String cell();
  }

  private final RowNumbererResources resources;

  public Css3RowNumbererAppearance() {
    this(GWT.<RowNumbererResources>create(RowNumbererResources.class));
  }

  public Css3RowNumbererAppearance(RowNumbererResources resources) {
    this.resources = resources;
    StyleInjectorHelper.ensureInjected(resources.styles(), true);
  }

  @Override
  public String getCellClassName() {
    return resources.styles().cell();
  }

  @Override
  public void renderCell(int rowNumber, SafeHtmlBuilder sb) {
    sb.appendHtmlConstant("<div class='"+resources.styles().numberer()+"'>").append(rowNumber).appendHtmlConstant("</div>");
  }

  @Override
  public SafeHtml renderHeader() {
    return SafeHtmlUtils.EMPTY_SAFE_HTML;
  }
}