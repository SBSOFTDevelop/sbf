/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.grid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.widget.core.client.grid.ColumnHeader.ColumnHeaderAppearance;
import ru.sbsoft.svc.widget.core.client.grid.ColumnHeader.ColumnHeaderStyles;

public class ColumnHeaderDefaultAppearance implements ColumnHeaderAppearance {

  public interface ColumnHeaderResources extends ClientBundle {

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource columnHeader();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource columnHeaderOver();

    /** Menu icon */
    ImageResource columnsIcon();

    /** grid header marker */
    ImageResource sortAsc();

    /** grid header marker */
    ImageResource sortDesc();

    /** Menu icon */
    ImageResource sortAscendingIcon();

    /** Menu icon */
    ImageResource sortDescendingIcon();

    /** Column dnd indicator 1 */
    ImageResource columnMoveTop();

    /** Column dnd indicator 2 */
    ImageResource columnMoveBottom();

    @Source("ColumnHeader.gss")
    DefaultColumnHeaderStyles style();

  }
  public interface DefaultColumnHeaderStyles extends ColumnHeaderStyles {

  }

  public interface ColumnHeaderTemplate extends XTemplates {
    @XTemplate(source = "ColumnHeader.html")
    SafeHtml render(ColumnHeaderStyles style);
  }

  private final ColumnHeaderResources resources;
  private final ColumnHeaderStyles style;
  private ColumnHeaderTemplate templates = GWT.create(ColumnHeaderTemplate.class);

  public ColumnHeaderDefaultAppearance() {
    this(GWT.<ColumnHeaderResources> create(ColumnHeaderResources.class));
  }

  public ColumnHeaderDefaultAppearance(ColumnHeaderResources resources) {
    this.resources = resources;
    this.style = this.resources.style();

    StyleInjectorHelper.ensureInjected(style, true);
  }

  @Override
  public ImageResource columnsIcon() {
    return resources.columnsIcon();
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(templates.render(style));
  }

  @Override
  public ImageResource sortAscendingIcon() {
    return resources.sortAscendingIcon();
  }

  @Override
  public ImageResource sortDescendingIcon() {
    return resources.sortDescendingIcon();
  }

  @Override
  public ColumnHeaderStyles styles() {
    return style;
  }

  @Override
  public String columnsWrapSelector() {
    return "." + style.headerInner();
  }

  @Override
  public int getColumnMenuWidth() {
    return 14;
  }
}
